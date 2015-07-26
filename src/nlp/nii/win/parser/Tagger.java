/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.ArrayList;
import java.util.List;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Sentence;

/**
 *
 * @author lelightwin
 */
public class Tagger {

    private static MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");

    public static DPState tag(String inputSentence) {
        List<Tree<Integer>> taggedWords = new ArrayList();
        for (String input : tagger.tagString(inputSentence).split(" ")) {

        }

        Sentence sentence = new Sentence(taggedWords);
        return new DPState(sentence, sentence.length());
    }

    public static void main(String[] args) {

    }
}
