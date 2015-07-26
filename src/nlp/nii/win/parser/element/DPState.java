/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import java.util.ArrayList;
import java.util.List;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.corpus.stanford.util.Pair;
import nlp.nii.win.parser.ParserPerceptronCommunicator;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.parser.RuleManager;
import nlp.nii.win.util.CustomizeStack;

/**
 *
 * @author lelightwin
 */
public class DPState implements Comparable<DPState> {

    private List<DPState> backPointer = new ArrayList<>(); // back pointer for recreating the output tree

    private List<DPState> leftStates = new ArrayList<>(); // left states of this state

    private DPState preState = null; // previous state of this current state (in best path)

    private boolean gold = false; // true if this state is a golden state

    private boolean finish = false; // true if this state is a finish state

    private int action = -1; // the previous action to this state

    private float cost = 0.0f; // real cost

    private float insideCost = 0.0f; // inside cost (within span)

    private float shiftCost = 0.0f; // cost for shift into another state

    private int queueSize = -1; // current size of queue

    private Sentence sentence;

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="basic elements of a DPState">
    private DPPoint s0 = null;
    private DPPoint s0l = null;
    private DPPoint s0r = null;
    private DPPoint s0u = null;
    private DPPoint s1 = null;
    private DPPoint s1l = null;
    private DPPoint s1r = null;
    private DPPoint s1u = null;
    private DPPoint s2 = null;
    private DPPoint s3 = null;

    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="constructor">
    public DPState(Sentence sentence, int queueSize) {
        this.sentence = sentence;
        this.queueSize = queueSize;
    }

    public DPState(Sentence sentence, CustomizeStack<Tree<Integer>> stack, int queueSize) {
        this.sentence = sentence;
        this.queueSize = queueSize;
        // create state from stack
        Tree<Integer> st0 = stack.peek(0);
        Tree<Integer> st1 = stack.peek(1);
        Tree<Integer> st2 = stack.peek(2);
        Tree<Integer> st3 = stack.peek(3);

        if (st0 != null) {
            this.s0 = new DPPoint(st0, this.sentence);
            Tree<Integer> st0u = st0.getUnaryChild();
            Pair<Tree<Integer>, Tree<Integer>> st0lr = st0.getBinaryChild();
            if ((st0u != null) && (!st0u.isLeaf())) {
                this.s0u = new DPPoint(st0u, this.sentence);
            }
            if (st0lr != null) {
                this.s0l = new DPPoint(st0lr.getFirst(), this.sentence);
                this.s0r = new DPPoint(st0lr.getSecond(), this.sentence);
            }

            if (st1 != null) {
                this.s1 = new DPPoint(st1, this.sentence);
                Tree<Integer> st1u = st1.getUnaryChild();
                Pair<Tree<Integer>, Tree<Integer>> st1lr = st1.getBinaryChild();
                if ((st1u != null) && ((!st1u.isLeaf()))) {
                    this.s1u = new DPPoint(st1u, this.sentence);
                }
                if (st1lr != null) {
                    this.s1l = new DPPoint(st1lr.getFirst(), this.sentence);
                    this.s1r = new DPPoint(st1lr.getSecond(), this.sentence);
                }

                if (st2 != null) {
                    this.s2 = new DPPoint(st2, this.sentence);
                    if (st3 != null) {
                        this.s3 = new DPPoint(st3, this.sentence);
                    }
                }
            }
        }
    }

    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="methods for basic elements">
    //<editor-fold defaultstate="collapsed" desc="checking methods">
    public boolean hasS0() {
        return s0 != null;
    }

    public boolean hasS1() {
        return s1 != null;
    }

    public boolean hasS2() {
        return s2 != null;
    }

    public boolean hasS3() {
        return s3 != null;
    }

    public boolean hasS0l() {
        return s0l != null;
    }

    public boolean hasS0r() {
        return s0r != null;
    }

    public boolean hasS0u() {
        return s0u != null;
    }

    public boolean hasS1l() {
        return s1l != null;
    }

    public boolean hasS1r() {
        return s1r != null;
    }

    public boolean hasS1u() {
        return s1u != null;
    }

    public boolean hasq0() {
        return queueSize > 0;
    }

    public boolean hasq1() {
        return queueSize > 1;
    }

    public boolean hasq2() {
        return queueSize > 2;
    }

    public boolean hasq3() {
        return queueSize > 3;
    }

    public boolean qIsEmpty() {
        return queueSize == 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="return methods for basic elements">
    /**
     * @return the s0
     */
    public DPPoint s0() {
        return s0;
    }

    /**
     * @return the s0l
     */
    public DPPoint s0l() {
        return s0l;
    }

    /**
     * @return the s0r
     */
    public DPPoint s0r() {
        return s0r;
    }

    /**
     * @return the s0u
     */
    public DPPoint s0u() {
        return s0u;
    }

    /**
     * @return the s1
     */
    public DPPoint s1() {
        return s1;
    }

    /**
     * @return the s1l
     */
    public DPPoint s1l() {
        return s1l;
    }

    /**
     * @return the s1r
     */
    public DPPoint s1r() {
        return s1r;
    }

    /**
     * @return the s1u
     */
    public DPPoint s1u() {
        return s1u;
    }

    /**
     * @return the s2
     */
    public DPPoint s2() {
        return s2;
    }

    /**
     * @return the s3
     */
    public DPPoint s3() {
        return s3;
    }

    public Word q0() {
        return sentence.queueWord(0, queueSize);
    }

    public Word q1() {
        return sentence.queueWord(1, queueSize);
    }

    public Word q2() {
        return sentence.queueWord(2, queueSize);
    }

    public Word q3() {
        return sentence.queueWord(3, queueSize);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="return methods for basic features">
    public int s0c() {
        if (this.hasS0()) {
            return s0.c();
        }
        return -1;
    }

    public int s0t() {
        if (this.hasS0()) {
            return sentence.word(s0.h()).t();
        }
        return -1;
    }

    public int s0w() {
        if (this.hasS0()) {
            return sentence.word(s0.h()).w();
        }
        return -1;
    }

    public int s1c() {
        if (this.hasS1()) {
            return s1.c();
        }
        return -1;
    }

    public int s1t() {
        if (this.hasS1()) {
            return sentence.word(s1.h()).t();
        }
        return -1;
    }

    public int s1w() {
        if (this.hasS1()) {
            return sentence.word(s1.h()).w();
        }
        return -1;
    }

    public int s2c() {
        if (this.hasS2()) {
            return s2.c();
        }
        return -1;
    }

    public int s2t() {
        if (this.hasS2()) {
            return sentence.word(s2.h()).t();
        }
        return -1;
    }

    public int s2w() {
        if (this.hasS2()) {
            return sentence.word(s2.h()).w();
        }
        return -1;
    }

    public int s3c() {
        if (this.hasS3()) {
            return s3.c();
        }
        return -1;
    }

    public int s3t() {
        if (this.hasS3()) {
            return sentence.word(s3.h()).t();
        }
        return -1;
    }

    public int s3w() {
        if (this.hasS3()) {
            return sentence.word(s3.h()).w();
        }
        return -1;
    }

    public int q0t() {
        if (this.hasq0()) {
            return q0().t();
        }
        return -1;
    }

    public int q0w() {
        if (this.hasq0()) {
            return q0().w();
        }
        return -1;
    }

    public int q1t() {
        if (this.hasq1()) {
            return q1().t();
        }
        return -1;
    }

    public int q1w() {
        if (this.hasq1()) {
            return q1().w();
        }
        return -1;
    }

    public int q2t() {
        if (this.hasq2()) {
            return q2().t();
        }
        return -1;
    }

    public int q2w() {
        if (this.hasq2()) {
            return q2().w();
        }
        return -1;
    }

    public int q3t() {
        if (this.hasq3()) {
            return q3().t();
        }
        return -1;
    }

    public int q3w() {
        if (this.hasq3()) {
            return q3().w();
        }
        return -1;
    }

    public int s0lc() {
        if (this.hasS0l()) {
            return s0l.c();
        }
        return -1;
    }

    public int s0lw() {
        if (this.hasS0l()) {
            return sentence.word(s0l.h()).w();
        }
        return -1;
    }

    public int s0rc() {
        if (this.hasS0r()) {
            return s0r.c();
        }
        return -1;
    }

    public int s0rw() {
        if (this.hasS0r()) {
            return sentence.word(s0r.h()).w();
        }
        return -1;
    }

    public int s0uc() {
        if (this.hasS0u()) {
            return s0u.c();
        }
        return -1;
    }

    public int s0uw() {
        if (this.hasS0u()) {
            return sentence.word(s0u.h()).w();
        }
        return -1;
    }

    public int s1lc() {
        if (this.hasS1l()) {
            return s1l.c();
        }
        return -1;
    }

    public int s1lw() {
        if (this.hasS1l()) {
            return sentence.word(s1l.h()).w();
        }
        return -1;
    }

    public int s1rc() {
        if (this.hasS1r()) {
            return s1r.c();
        }
        return -1;
    }

    public int s1rw() {
        if (this.hasS1r()) {
            return sentence.word(s1r.h()).w();
        }
        return -1;
    }

    public int s1uc() {
        if (this.hasS1u()) {
            return s1u.c();
        }
        return -1;
    }

    public int s1uw() {
        if (this.hasS1u()) {
            return sentence.word(s1u.h()).w();
        }
        return -1;
    }
    //</editor-fold>
    //</editor-fold>

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="methods for fundamental shift-reduce actions">
    /**
     *
     * @return state after taking shift action from this state
     */
    public DPState doShift() {
        if (Conditioner.canShift(this)) { // check if shift action could be performed
            Word newWord = sentence.queueWord(0, queueSize); // get new word from queue of incoming words
            DPState newState = new DPState(sentence, queueSize - 1); // create new state with same input sentence and substract the queueSize by 1
            newState.s0 = new DPPoint(newWord);
            newState.s1 = this.s0;
            newState.s2 = this.s1;
            newState.s3 = this.s2;
            newState.s1l = this.s0l;
            newState.s1r = this.s0r;
            newState.s1u = this.s0u;
            newState.action = DataInfo.instance().getNumericAction("S");
            return newState;
        }
        return null;
    }

    /**
     *
     * @param leftState
     * @return list of states after taking binary reduce action between this
     * state and one of its left state
     */
    public ArrayList<DPState> doBReduceWith(DPState leftState) {
        ArrayList<DPState> newStates = new ArrayList<>(); // list of newly formed states

        if (Conditioner.canBReduce(leftState, this)) { // check if b-reduce action could be performed
            DPPoint left = leftState.s0(); // left child of b-reduce action
            DPPoint right = this.s0(); // right child of b-reduce action

            for (Rule<Integer> rule : RuleManager.instance().get(left.c(), right.c())) { // process list of rules which have lhs = l and r
                if (Conditioner.canBReduceWithRule(leftState, this, rule)) { // check if b-reduce action could be performed with this resulting node (indicated by rule)
                    int c = rule.cons();
                    DPPoint newPoint; // create newly formed point
                    DPState newState = new DPState(sentence, queueSize); // create newly formed state

                    if (rule.head() == 0) { // head is left
                        newPoint = new DPPoint(c, left.h());
                    } else { // head is right
                        newPoint = new DPPoint(c, right.h());
                    }

                    newState.s0 = newPoint;
                    newState.s1 = leftState.s1; // this.s2
                    newState.s2 = leftState.s2; // this.s3
                    newState.s3 = leftState.s3;
                    newState.s0l = leftState.s0; // this.s1
                    newState.s0r = this.s0;
                    newState.s1l = leftState.s1l;
                    newState.s1r = leftState.s1r;
                    newState.s1u = leftState.s1u;
                    newState.action = rule.action();
                    newStates.add(newState);
                }
            }
        }
        return newStates;
    }

    /**
     *
     * @return list of states after taking unary reduce action from this state
     */
    public ArrayList<DPState> doUReduce() {
        ArrayList<DPState> newStates = new ArrayList<>();

        if (Conditioner.canUReduce(this)) { // check if u-reduce action could be performed
            DPPoint unary = this.s0(); // unary child of u-reduce action
            for (Rule<Integer> rule : RuleManager.instance().get(unary.c())) { // process list of rules which have lhs = u
                DPPoint newPoint = new DPPoint(rule.cons(), unary.h());
                DPState newState = new DPState(sentence, queueSize);
                newState.s0 = newPoint;
                newState.s1 = this.s1;
                newState.s2 = this.s2;
                newState.s3 = this.s3;
                newState.s0u = this.s0;
                newState.s0l = this.s0l; // use for binary/unary reduce action, still right in the case of shift/unary
                newState.s0r = this.s0r; // use for binary/unary reduce action, still right in the case of shift/unary
                newState.s1l = this.s1l;
                newState.s1r = this.s1r;
                newState.s1u = this.s1u;
                newState.action = rule.action();
                newStates.add(newState);
            }
        }
        return newStates;
    }

    /**
     *
     * @return return finish state from this state
     */
    public DPState doFinish() {
        if (Conditioner.canFinish(this)) { // check if finish action could be performed
            DPState newState = new DPState(sentence, 0);
            newState.action = DataInfo.instance().getNumericAction("FIN");
            newState.finish = true;
            return newState;
        }
        return null;
    }
    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/

    //<editor-fold defaultstate="collapsed" desc="functions for taking our unary-merging actions">
    /**
     *
     * @param actionGold is the correct action (only use for training)
     * @return list of states after taking shift/unary actions
     */
    public ArrayList<DPState> takeShift(int actionGold) {
        ArrayList<DPState> newStates = new ArrayList();
        DPState shiftState = this.doShift();
        if (shiftState != null) {
            //<editor-fold defaultstate="collapsed" desc="this is for regular shift action">
            float shCost = ParserPerceptronCommunicator.score(shiftState.getAction(), this);
            this.shiftCost = shCost;
            shiftState.cost = this.cost + shCost;
            shiftState.leftStates.add(this);
            shiftState.preState = this;
            shiftState.gold = this.gold && (actionGold == shiftState.getAction());
            newStates.add(shiftState);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="this is for shift merging with unary action">
            for (DPState shUState : shiftState.doUReduce()) {
                float shUCost = ParserPerceptronCommunicator.score(shUState.getAction(), this);
                this.shiftCost = shUCost;
                shUState.cost = this.cost + shUCost;
                shUState.backPointer.add(shiftState);
                shUState.leftStates.add(this);
                shUState.preState = this;
                shUState.gold = this.gold && (actionGold == shUState.getAction());
                newStates.add(shUState);
            }
            //</editor-fold>
        }
        return newStates;
    }

    /**
     *
     * @param actionGold is the correct action (only use for training)
     * @return list of states after taking binary/unary actions
     */
    public ArrayList<DPState> takeBReduce(int actionGold) {
        ArrayList<DPState> newStates = new ArrayList();
        for (DPState leftState : this.leftStates) {
            float c1 = leftState.shiftCost + this.insideCost;
            float c2 = leftState.insideCost + c1;
            float c3 = leftState.cost + c1;
            for (DPState binState : this.doBReduceWith(leftState)) {
                //<editor-fold defaultstate="collapsed" desc="this is for regular binary action">
                float binCost = ParserPerceptronCommunicator.score(binState.getAction(), this);
                binState.insideCost = c2 + binCost;
                binState.cost = c3 + binCost;
                binState.gold = leftState.gold && this.gold && (actionGold == binState.getAction());
                binState.leftStates = leftState.leftStates;
                binState.preState = this;
                binState.backPointer.add(leftState);
                binState.backPointer.add(this);
                newStates.add(binState);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="this is for binary/unary action">
                for (DPState binUState : binState.doUReduce()) {
                    float binUCost = ParserPerceptronCommunicator.score(binUState.getAction(), this);
                    binUState.insideCost = c2 + binUCost;
                    binUState.cost = c3 + binUCost;
                    binUState.gold = leftState.gold && this.gold && (actionGold == binUState.getAction());
                    binUState.leftStates = leftState.leftStates;
                    binUState.preState = this;
                    binUState.backPointer.add(binState);

                    newStates.add(binUState);
                }
                //</editor-fold>
            }
        }
        return newStates;
    }

    /**
     *
     * @return list of states after taking binary/unary actions
     */
    public DPState takeFinish() {
        DPState finState = this.doFinish();
        if (finState != null) {
            float finCost = ParserPerceptronCommunicator.score(finState.getAction(), this);
            finState.cost = this.cost + finCost;
            finState.preState = this;
            finState.backPointer.add(this);
            finState.gold = this.gold;
        }
        return finState;
    }
    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="some output of DPState">

    public ArrayList<DPState> listStates() {
        ArrayList<DPState> states = new ArrayList<>();
        DPState cur = this;
        while (cur != null) {
            states.add(0, cur);
            cur = cur.preState;
        }
        return states;
    }

    public Tree<Integer> makeTree() {
        Tree<Integer> t;
        if (this.action == DataInfo.instance().getNumericAction("FIN")) {
            return backPointer.get(0).makeTree();
        }
        if (backPointer.isEmpty()) { // this is a leaf state
            t = sentence.word(s0.h()).getTree();
        } else { // this is not a leaf state
            t = new Tree(s0.c());
            for (DPState s : backPointer) {
                t.getChildren().add(s.makeTree());
            }
        }
        return t;
    }
    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/

    //<editor-fold defaultstate="collapsed" desc="common methods (getter, setter, toString, hashcode and equals)">
    public void displayStatesPath() {
        if (preState != null) {
            preState.displayStatesPath();
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        String result = "DPState{";
        if (s0 != null) {

            if (s1 != null) {
                if (s2 != null) {
                    if (s3 != null) {
                        result += ", s3=" + s3;
                    }
                    result += ", s2=" + s2;
                }
                result += ", s1=" + s1;
                if (s1u != null) {
                    result += ",s1u=" + s1u;
                }
            }
            result += ", s0=" + s0;
            if (s0u != null) {
                result += ",s0u=" + s0u;
            }
        }
        result += ", queueSize=" + queueSize;
        result += ", action=" + DataInfo.instance().action(action) + '}';
        return result;
    }

    public DPState getPreState() {
        return preState;
    }

    public void setPreState(DPState preState) {
        this.preState = preState;
    }

    /**
     * @return the backPointer
     */
    public List<DPState> getBackPointer() {
        return backPointer;
    }

    /**
     * @return the leftStates
     */
    public List<DPState> getLeftStates() {
        return leftStates;
    }

    /**
     * @return the gold
     */
    public boolean isGold() {
        return gold;
    }

    /**
     *
     * @param gold to set
     */
    public void setGold(boolean gold) {
        this.gold = gold;
    }

    /**
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * @return the cost
     */
    public float getCost() {
        return cost;
    }

    /**
     * @return the insideCost
     */
    public float getInsideCost() {
        return insideCost;
    }

    /**
     * @return the shiftCost
     */
    public float getShiftCost() {
        return shiftCost;
    }

    /**
     * @return the queueSize
     */
    public int getQueueSize() {
        return queueSize;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    /**
     * @return the sentence
     */
    public Sentence getSentence() {
        return sentence;
    }

    /**
     * @return the finish
     */
    public boolean isFinish() {
        return finish;
    }
    //</editor-fold>

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="comparable interface">
    private int compare(float value1, float value2) {
        if (value1 == value2) {
            return 0;
        }
        if (value1 > value2) {
            return 1;
        }
        return -1;
    }

    @Override
    public int compareTo(DPState o) {
        int c = compare(this.getCost(), o.getCost());
        if (c == 0) { // their cost is equal
            return compare(this.getInsideCost(), o.getInsideCost());
        } else {
            return c;
        }
    }

    //</editor-fold>
}
