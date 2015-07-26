/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.parser.metrics.LabeledConstituentEval;

/**
 *
 * @author lelightwin
 */
public class EpicParserEvaluation {

    public static void main(String[] args) throws IOException {
        WinTreeBankReader reader = new WinTreeBankReader();
        LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton(""), DataInfo.instance().punctuation());

//        List<Tree<String>> goldenTrees = reader.readTreesFrom(new File("section23.gold.mrg"));
        List<Tree<String>> goldenTrees = reader.readSection(23);

        List<Tree<String>> predictTrees = reader.readTreesFrom(new File("section23.epic.mrg"));
//        // create raw file
        BufferedWriter rawBFW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("section23.raw"), "utf-8"));
        BufferedWriter goldBFW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("section23.gold.mrg"), "utf-8"));
        BufferedWriter predictBFW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("section23.predict.mrg"), "utf-8"));
        for (Tree<String> tree : goldenTrees) {
            tree.setLabel("TOP");
            goldBFW.write(tree.toString());
            goldBFW.newLine();
            String sentence = "";

            for (Tree<String> tokens : tree.getPreTerminals()) {
                if (tokens.getLabel().equals("-LRB-")) {
                    sentence += "( ";
                } else if (tokens.getLabel().equals("-RRB-")) {
                    sentence += ") ";
                } else {
                    sentence += tokens.getChild(0) + " ";
                }
            }
            rawBFW.write(sentence);
            rawBFW.newLine();

        }

        for (Tree<String> tree : predictTrees) {
            tree.setLabel("TOP");
            predictBFW.write(tree.toString());
            predictBFW.newLine();
        }
        rawBFW.close();
        goldBFW.close();
        predictBFW.close();
    }
}
