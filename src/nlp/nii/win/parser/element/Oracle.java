/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import java.util.ArrayList;
import java.util.List;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.corpus.Binarizer;
import nlp.nii.win.corpus.Corpuses;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.corpus.stanford.util.Pair;
import nlp.nii.win.parser.RuleManager;
import nlp.nii.win.util.CustomizeStack;

/**
 *
 * @author lelightwin
 */
public class Oracle {

    private Tree<Integer> tree;
    private List<DPState> goldenStates = new ArrayList<>();
    private Sentence sentence;

    public Oracle(Tree<Integer> tree) {
        this.tree = tree;
        createStates();
        createFeatures();
    }

    /**
     * generate the golden states from the input tree
     */
    private void createStates() {
        CustomizeStack<Tree<Integer>> stack = new CustomizeStack<>();
        List<Tree<Integer>> words = tree.getPreTerminals();
        sentence = new Sentence(words);
        int queueSize = 0; // size of current queue
        DPState finishState = new DPState(sentence, stack, queueSize); // finish state
        finishState.setAction(DataInfo.instance().getNumericAction("FIN"));
        goldenStates.add(0, finishState);
        stack.push(tree); // init stack with only root node
        while (!stack.isEmpty()) {
            DPState state = new DPState(sentence, stack, queueSize); // this is current state
            Tree<Integer> t = stack.pop();
            int p = t.getLabel();
            int action = -1;

            if (t.isBinary()) { //if t is a binary node, this is a binary action
                Pair<Tree<Integer>, Tree<Integer>> tlr = t.getBinaryChild();
                int lc = tlr.getFirst().getLabel();
                int rc = tlr.getSecond().getLabel();
                action = RuleManager.instance().getBinaryRule(lc, rc, p).action(); // get action value
                stack.push(tlr.getFirst()); // add left child to stack
                stack.push(tlr.getSecond()); // add right child to stack
            } else { // if t is a unary node, there are three cases
                if (t.isPreTerminal()) { // t is a preterminal, this is shift action
                    queueSize += 1;
                    action = DataInfo.instance().getNumericAction("S"); // current action
                } else {
                    Tree<Integer> tu = t.getUnaryChild(); // check t's unary child: tu
                    int tuc = tu.getLabel();
                    action = RuleManager.instance().getUnaryRule(tuc, p).action(); // get action value

                    if (tu.isPreTerminal()) { // if tu is preTerminal, this is shift/unary action
                        queueSize += 1;
                    } else { // if tu is non-terminal, this is binary/unary action (there is no unary chain) => tu must be binary node
                        Pair<Tree<Integer>, Tree<Integer>> tulr = tu.getBinaryChild();
                        stack.push(tulr.getFirst()); // add left child to stack
                        stack.push(tulr.getSecond()); // add right child to stack
                    }
                }
            }

            state.setAction(action); // set action to be the previous action to current state (the action must take to get to state)
            goldenStates.add(0, state);
        }

        DPState initState = new DPState(sentence, stack, queueSize); // initial state has no previous action
        goldenStates.add(0, initState);
    }

    public ArrayList<DPState> getStates(int step) {
        ArrayList<DPState> states = new ArrayList<>();
        for (int i = 0; i <= step; i++) {
            states.add(goldenStates.get(i));
        }
        return states;
    }

    /**
     * create features for each golden state and the connection between them
     * (state[i-1] = previous state of state[i])
     */
    private void createFeatures() {
        for (int i = 0; i < goldenStates.size(); i++) {
            DPState si = goldenStates.get(i);
            if (i > 0) {
                DPState si_1 = goldenStates.get(i - 1);
                si.setPreState(si_1);
            }
        }
    }

    public int getNextAction(int step) { // return the next action at the step
        return getGoldenStates().get(step + 1).getAction();
    }

    public DPState state(int step) {
        return goldenStates.get(step);
    }

    public DPState start() {
        return goldenStates.get(0);
    }

    public int finalStep() {
        return goldenStates.size() - 1;
    }

    /**
     * @return the tree
     */
    public Tree<Integer> getTree() {
        return tree;
    }

    public List<DPState> getGoldenStates() {
        return goldenStates;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void displayStates() {

        for (DPState ds : getGoldenStates()) {
            System.out.println(ds);
        }
    }

    public static void main(String[] args) {
        WinTreeBankReader reader = new WinTreeBankReader();
//
//        Oracle oracle = new Oracle(t);
//        oracle.displayStates();
//        oracle.displayFeatures();
        ArrayList<Oracle> oracles = new ArrayList<>();
        System.out.println("Loading corpus!!!");
        for (int i = 2; i <= 21; i++) { // section 2 to 21
            for (Tree<String> t : reader.readSection(i)) {
                Binarizer.binarizing(t);
                oracles.add(new Oracle(Corpuses.numericTransform(t)));
            }
            System.out.println("Section " + i + " done!!! (oracles size: " + oracles.size() + ")");
        }
        System.out.println("Complete!!!!!");
        System.out.println("Number of training instances: " + oracles.size());
    }
}
