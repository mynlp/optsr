/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.metrics;

/**
 *
 * @author lelightwin
 */
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.corpus.stanford.syntax.Trees;

/**
 * Evaluates precision and recall for English Penn Treebank parse trees. NOTE:
 * Unlike the standard evaluation, multiplicity over each span is ignored. Also,
 * punction is NOT currently deleted properly (approximate hack), and other
 * normalizations (like AVDP ~ PRT) are NOT done.
 *
 * @author Dan Klein
 */
public class EnglishPennTreebankParseEvaluator<L> {

    public static void main(String[] args) throws Throwable {
        Tree<String> goldTree = (new Trees.PennTreeReader(new StringReader(
                "(ROOT (S (NP (DT the) (NN can)) (-NONE- *T*) (VP (VBD fell))))"))).next();
        Tree<String> guessedTree = (new Trees.PennTreeReader(new StringReader(
                "(ROOT (S (NP (DT the)) (VP (MB can) (VP (VBD fell)))))"))).next();


        LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton("ROOT"), new HashSet());
        System.out.println(Trees.PennTreeRenderer.render(goldTree));
        System.out.println(Trees.PennTreeRenderer.render(guessedTree));
        double d = eval.evaluate(guessedTree, goldTree);
        System.out.println(eval.getPrecision() + " " + eval.getRecall());
    }
}
