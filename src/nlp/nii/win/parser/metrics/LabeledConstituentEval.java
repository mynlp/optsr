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
public class LabeledConstituentEval<L> extends AbstractEval<L> {

    Set<L> labelsToIgnore;
    Set<L> punctuationTags;

    static <L> Tree<L> stripLeaves(Tree<L> tree) {
        if (tree.isLeaf()) {
            return null;
        }
        if (tree.isPreTerminal()) {
            return new Tree<L>(tree.getLabel());
        }
        List<Tree<L>> children = new ArrayList<Tree<L>>();
        for (Tree<L> child : tree.getChildren()) {
            children.add(stripLeaves(child));
        }
        return new Tree<L>(tree.getLabel(), children);
    }

    @Override
    Set<Object> makeObjects(Tree<L> tree) {
        Tree<L> noLeafTree = stripLeaves(tree);
        Set<Object> set = new HashSet<Object>();
        addConstituents(noLeafTree, set, 0);
        return set;
    }

    private int addConstituents(Tree<L> tree, Set<Object> set, int start) {
        if (tree == null) {
            return 0;
        }
        if (tree.isLeaf()) {
            if (punctuationTags.contains(tree.getLabel())) {
                return 0;
            } else {
                return 1;
            }
        }
        int end = start;
        for (Tree<L> child : tree.getChildren()) {
            int childSpan = addConstituents(child, set, end);
            end += childSpan;
        }
        L label = tree.getLabel();
        if (!labelsToIgnore.contains(label)) {
            set.add(new LabeledConstituent<L>(label, start, end));
        }
        return end - start;
    }

    public LabeledConstituentEval(Set<L> labelsToIgnore,
            Set<L> punctuationTags) {
        this.labelsToIgnore = labelsToIgnore;
        this.punctuationTags = punctuationTags;
    }

    public int getHammingDistance(Tree<L> guess, Tree<L> gold) {
        Set<Object> guessedSet = makeObjects(guess);
        Set<Object> goldSet = makeObjects(gold);
        Set<Object> correctSet = new HashSet<Object>();
        correctSet.addAll(goldSet);
        correctSet.retainAll(guessedSet);
        return (guessedSet.size() - correctSet.size())
                + (goldSet.size() - correctSet.size());
    }

}
