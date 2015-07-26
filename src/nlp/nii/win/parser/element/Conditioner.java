/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import nlp.nii.win.ConstantResource.DataInfo;

/**
 * this is the class for checking the restriction for each actions
 *
 * @author lelightwin
 */
public class Conditioner {

    public static boolean canShift(DPState state) {
        /* the shift action can only be performed when the 
         queue of incoming words is not empty; (done)*/
        if (state.getQueueSize() == 0) {
            return false;
        }

        /* when the node on top of the stack is temporary 
         and its head word is from the right child, no shift 
         action can be performed; (done)*/
        DPPoint top = state.s0();
        if (state.s0() != null) {
            if (DataInfo.instance().isIncomplete(top.c())) {
                DPPoint toprc = state.s0r();
                if (top.h() == toprc.h()) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * first node on stack = s0 = right.s0
     * second node on stack = s1 = right.s1 = left.s0
     * third node on stack = s2 = right.s2 = left.s1
     */
    public static boolean canBReduce(DPState left, DPState right) {
        /* there must be at least two nodes on the top of stack */
        if (left.s0() == null) {
            return false;
        }
        /* the binary reduce actions can only be performed when 
         the stack contains at least two nodes, with at least one 
         of the two nodes on top of stack being non-temporary; (done)*/
//        int l = left.s0().c();
//        int r = right.s0().c();
//        return !(Labels.isIncomplete(l) && Labels.isIncomplete(r));
        return true;
    }

    public static boolean canBReduceWithRule(DPState left, DPState right, Rule<Integer> rule) {
        boolean queueIsEmpty = (right.getQueueSize() == 0);
        boolean stackHasOnlyTwoNodes = ((left.s1() == null && left.s0() != null));
        boolean stackHasMoreThanTwoNodes = (left.s1() != null);
        boolean ruleIsTemporary = DataInfo.instance().isIncomplete(rule.cons());
        boolean leftIsHead = (rule.head() == 0);
        boolean thirdIsTemporary = stackHasMoreThanTwoNodes && (DataInfo.instance().isIncomplete(left.s1().c()));

        /* when the incoming queue is empty and the stack contains 
         only two nodes, binary reduce can be applied only if the 
         resulting node is non-temporary;(done)*/
        if (queueIsEmpty) { // current queue is empty
            if (stackHasOnlyTwoNodes) { // stack has only two nodes
                if (ruleIsTemporary) {
                    return false; // then the resulting node must be complete (or non-temporary)
                }
            }
        }
        /* when the stack contains only two nodes, temporary resulting 
         nodes from binary reduce must be left-headed;(done)*/
        if (stackHasOnlyTwoNodes) { // stack has only two nodes
            if (ruleIsTemporary) { // resulting node is temporary
                if (!leftIsHead) {
                    return false; // then the resulting node must be left-headed
                }
            }
        }
        /* when the queue is empty and the stack contains more than 
         two nodes, with the third node from the top being temporary, 
         binary reduce can be applied only if the resulting node is non-temporary;*/
        if (queueIsEmpty) { // current queue is empty
            if (thirdIsTemporary) { // third node from the top in stack is temporary
                if (ruleIsTemporary) {
                    return false; // then the resulting node must be complete
                }
            }
        }
        /* when the stack contains more than two nodes, with the 
         third node from the top being temporary, temporary resulting 
         nodes from binary reduce must be left-headed;*/
        if (thirdIsTemporary) { // third node from the top in stack is temporary
            if (ruleIsTemporary) { //resulting node is temporary
                if (!leftIsHead) {
                    return false; // then the resulting node must be left-headed
                }
            }
        }
        return true;
    }

    public static boolean canUReduce(DPState unary) {
        /* the unary reduce actions can be performed only when 
         the stack is not empty; (done) */
        return unary.s0() != null;
    }

    public static boolean canFinish(DPState state) {
        /* the terminate action can be performed when the queue 
         is empty, and the stack size is one. (done) */
        if (state.getQueueSize() > 0) {
            return false; // current queue must be empty
        }
        if (state.s0() == null) {
            return false; // stack still has element
        }
        return state.s1() == null; // stack must have only one element
    }
}
