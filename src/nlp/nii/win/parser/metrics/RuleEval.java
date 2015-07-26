/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public class RuleEval<L> extends AbstractEval<L> {

    Set<L> labelsToIgnore;
    Set<L> punctuationTags;

    static <L> Tree<L> stripLeaves(Tree<L> tree) {
        if (tree.isLeaf()) {
            return null;
        }
        if (tree.isPreTerminal()) {
            return new Tree<>(tree.getLabel());
        }
        List<Tree<L>> children = new ArrayList<>();
        for (Tree<L> child : tree.getChildren()) {
            children.add(stripLeaves(child));
        }
        return new Tree<>(tree.getLabel(), children);
    }

    @Override
    Set<Object> makeObjects(Tree<L> tree) {
        Tree<L> noLeafTree = stripLeaves(tree);
        Set<Object> set = new HashSet<>();
        addConstituents(noLeafTree, set, 0);
        return set;
    }

    private int addConstituents(Tree<L> tree, Set<Object> set, int start) {
        if (tree == null) {
            return 0;
        }
        if (tree.isLeaf()) {
            /*
             * if (punctuationTags.contains(tree.getLabel())) return 0; else
             */
            return 1;
        }
        int end = start, i = 0;
        L lC = null, rC = null;
        for (Tree<L> child : tree.getChildren()) {
            int childSpan = addConstituents(child, set, end);
            if (i == 0) {
                lC = child.getLabel();
            } else /* i==1 */ {
                rC = child.getLabel();
            }
            i++;
            end += childSpan;
        }
        L label = tree.getLabel();
        if (!labelsToIgnore.contains(label)) {
            set.add(new RuleConstituent<>(label, lC, rC, start, end));
        }
        return end - start;
    }

    public RuleEval(Set<L> labelsToIgnore, Set<L> punctuationTags) {
        this.labelsToIgnore = labelsToIgnore;
        this.punctuationTags = punctuationTags;
    }

}
