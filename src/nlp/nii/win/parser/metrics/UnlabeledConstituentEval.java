/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.metrics;

import java.util.HashSet;
import java.util.Set;
import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public class UnlabeledConstituentEval<L> extends AbstractEval<L> {

    public UnlabeledConstituentEval() {

    }

    @Override
    Set<Object> makeObjects(Tree<L> tree) {
        Tree<L> noLeafTree = LabeledConstituentEval.stripLeaves(tree);
        Set<Object> set = new HashSet<Object>();
        addConstituents(noLeafTree, set, 0);
        return set;
    }

    private int addConstituents(Tree<L> tree, Set<Object> set, int start) {
        if (tree == null) {
            return 0;
        }
        if (tree.getYield().size() == 1) {

            return 1;
        }
        int end = start;
        for (Tree<L> child : tree.getChildren()) {
            int childSpan = addConstituents(child, set, end);
            end += childSpan;
        }

        set.add(new UnlabeledConstituent<L>(start, end));

        return end - start;
    }

}