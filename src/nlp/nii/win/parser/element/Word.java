/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public class Word {

    private int pos;
    private int value;
    private int index;
    private Tree<Integer> tree;

    public Word(Tree<Integer> w) {
        this.pos = w.getLabel();
        this.value = w.getChild(0).getLabel();
        this.tree = w;
    }

    /**
     * @return the pos
     */
    public int t() {
        return pos;
    }

    /**
     * @return the value
     */
    public int w() {
        return value;
    }

    /**
     * @return the tree
     */
    public Tree<Integer> getTree() {
        return tree;
    }

    @Override
    public String toString() {
        return DataInfo.instance().label(this.pos) + "/" + this.value;
    }

    /**
     * @return the index
     */
    public int i() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
