/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.test;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 *
 * @author lelightwin
 */

class Token implements HasWord{

    @Override
    public String word() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setWord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

public class TestTagger {

    public static void main(String[] args) {
        MaxentTagger tagger = new MaxentTagger("taggerModels/english-left3words-distsim.tagger");
        String input = "This software is a Java implementation of the log - linear part - of - speech ( something ) taggers described in : ";
        System.out.println(tagger.tagString(input));
    }
}
