/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.corpus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nlp.nii.win.corpus.stanford.ling.CollinsHeadFinder;
import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public class Binarizer {

    private static CollinsHeadFinder headFinder = new CollinsHeadFinder();

    public static void headFinding(Tree<String> tree) {
        if (tree.isPreTerminal()) {
            return;
        }
        Tree<String> head = headFinder.determineHead(tree);
        head.setHeadToken(true);

        for (Tree<String> child : tree.getChildren()) {
            headFinding(child);
        }
    }

    public static void binarizing(Tree<String> t) {
        headFinding(t);
        binarizingByHead(t);
    }

    public static void binarizingByHead(Tree<String> t) {
        Tree<String> root = t;
        List<Tree<String>> children = t.getChildren();
        List<Tree<String>> childrenToModify = new ArrayList(children);
        String intermediateLabel = t.getLabel() + "*";

        while (childrenToModify.size() > 2) {
            boolean left = true;
            int idx = 0; // first element
            if (childrenToModify.get(idx).isHeadToken()) { // if first element is head
                idx = childrenToModify.size() - 1; // last element
                left = false;
            }

            Tree<String> side = childrenToModify.remove(idx); // remove side from children

            Tree<String> interNode = new Tree(intermediateLabel, childrenToModify); //create intermediate node
            interNode.setHeadToken(true);

            List<Tree<String>> newChildren = new ArrayList();
            if (left) {
                newChildren.add(side);
                newChildren.add(interNode);
            } else {
                newChildren.add(interNode);
                newChildren.add(side);
            }
            root.setChildren(newChildren);
            root = interNode;
        }
        for (Tree<String> c : children) {
            binarizingByHead(c);
        }
    }

    public static void debinarizing(Tree<String> t) {
        String label = t.getLabel();
        List<Tree<String>> newChildren = realchildrenFrom(t);
        t.setChildren(newChildren);
        for (Tree<String> nc : newChildren) {
            debinarizing(nc);
        }
    }

    private static List<Tree<String>> realchildrenFrom(Tree<String> t) {
        List<Tree<String>> realChildren = new ArrayList();
        for (Tree<String> c : t.getChildren()) {
            if (c.isLeaf() || c.isPreTerminal()) {
                realChildren.add(c);
            } else {
                if (!c.getLabel().endsWith("*")) {
                    realChildren.add(c);
                } else {
                    realChildren.addAll(realchildrenFrom(c));
                }
            }
        }
        return realChildren;
    }

    public static void main(String[] args) throws IOException {
        WinTreeBankReader reader = new WinTreeBankReader();
        int count = 0;
        Set<Integer> sizes = new HashSet<>();
        for (int i = 22; i <= 22; i++) {
            for (Tree<String> t : reader.readSection(i)) {
                System.out.println(t.getPreTerminals().size());
            }
        }

        Tree<String> t = reader.readSection(0).get(2);
//        System.out.println(t);
//        System.out.println("count = " + count);
    }
}
