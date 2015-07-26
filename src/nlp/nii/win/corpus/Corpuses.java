/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.corpus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.parser.RuleManager;
import nlp.nii.win.parser.element.Rule;

/**
 *
 * @author lelightwin
 */
public class Corpuses {

    public static Tree<String> textInstance(Tree<Integer> tree) {
        String label = null;
        Integer idx = tree.getLabel();
        if (idx != null) {
            if (!tree.isLeaf()) { // constituent + pos
                label = DataInfo.instance().label(idx);
            } else { // word
                label = DataInfo.instance().word(idx);
            }
        }
        Tree<String> textTree = new Tree(label);
        textTree.setHeadToken(tree.isHeadToken());

        for (Tree<Integer> child : tree.getChildren()) {
            textTree.getChildren().add(textInstance(child));
        }
        return textTree;
    }

    public static Tree<Integer> numericTransform(Tree<String> tree) {
        Tree<Integer> intTree = numericInstance(tree);
        extractHeadIdx(intTree);
        return intTree;
    }

    private static Tree<Integer> numericInstance(Tree<String> tree) {
        int intLabel = -1;
        String label = tree.getLabel();
        if (label != null) {
            if (!tree.isLeaf()) { // constituent + pos
                intLabel = DataInfo.instance().getUpdateNumericLabel(label);
                if (tree.isPreTerminal()) {
                    DataInfo.instance().updatePreTerminal(intLabel, label);
                } else { // this is a constituent node
                    DataInfo.instance().updateConstituent(intLabel, label);
                    if (tree.getLabel().endsWith("*")) { // intermediate node
                        DataInfo.instance().updateIncomplete(intLabel, label);
                    }
                }
            } else { // word
                intLabel = DataInfo.instance().getUpdateNumericWord(label);
            }
        }
        Tree<Integer> intTree = new Tree(intLabel);
        intTree.setHeadToken(tree.isHeadToken());

        for (Tree<String> child : tree.getChildren()) {
            intTree.getChildren().add(numericInstance(child));
        }
        return intTree;
    }

    private static void extractHeadIdx(Tree<Integer> tree) {
        List<Tree<Integer>> words = tree.getPreTerminals();
        for (int i = 0; i < words.size(); i++) {
            words.get(i).setHeadIdx(i);
        }
        tree.extractHeadIdx();
    }

    public static void extractRulesFrom(List<Tree<Integer>> corpus) {
        for (Tree<Integer> tree : corpus) {
            for (Rule<Integer> r : rulesFrom(tree)) {
                RuleManager.instance().addRule(r);
            }
        }
    }

    private static Set<Rule<Integer>> rulesFrom(Tree<Integer> t) {
        Set<Rule<Integer>> rules = new HashSet();
        if (!t.isPreTerminal()) {
            int head = -1;

            for (int i = 0; i < t.getChildren().size(); i++) {
                Tree<Integer> c = t.getChild(i);
                rules.addAll(rulesFrom(c));
                if (c.isHeadToken()) {
                    head = i;
                }
            }

            Rule<Integer> newRule = null;
            if (t.getChildren().size() == 2) { // binary tree
                newRule = new Rule<>(
                        t.getLabel(),
                        t.getChild(0).getLabel(),
                        t.getChild(1).getLabel(),
                        head);
            } else if (t.getChildren().size() == 1) { // unary tree
                newRule = new Rule<>(
                        t.getLabel(),
                        t.getChild(0).getLabel(),
                        head);
            }

            if (newRule != null) {
                rules.add(newRule);
            }
        }
        return rules;
    }

    public static void main(String[] args) {
        WinTreeBankReader reader = new WinTreeBankReader();

        List<Tree<Integer>> corpus = new ArrayList();
        Map<Integer, Integer> map = new HashMap();
        for (int i = 22; i <= 22; i++) {
            for (Tree<Integer> tree : reader.readNumericSection(i)) {
                corpus.add(tree);
                int size = tree.getPreTerminals().size();

                if (!map.containsKey(size)) {
                    map.put(size, 1);
                }
                map.put(size, map.get(size) + 1);
            }
        }

        for (Entry<Integer, Integer> e : map.entrySet()) {
//            System.out.println(e.getKey()+" "+e.getValue());
        }
    }
}
