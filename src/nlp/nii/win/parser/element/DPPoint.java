/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public class DPPoint {

    private int label = -1; // integer value of label
    private int head = -1;
    private int rule = -1;

    public DPPoint(int label, int head) {
        this.label = label;
        this.head = head;
    }

    public DPPoint(Word word) {
        this.label = word.t();
        this.head = word.i();
    }

    public DPPoint(Tree<Integer> t, Sentence sentence) {
        this.label = t.getLabel();
        this.head = t.getHeadIdx();
    }

    /**
     * @return the label
     */
    public int c() {
        return label;
    }

    /**
     * @return the headIdx
     */
    public int h() {
        return head;
    }

    public static void main(String[] args) {
        
    }
}
