/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.ConstantResource;

/**
 *
 * @author lelightwin
 */
public class Global {
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //<editor-fold defaultstate="collapsed" desc="grammar files">
    public static final String wsjDirectory = System.getProperty("user.dir") + "/data/WSJ/";
    public static final String byteGrammarRulesFile = System.getProperty("user.dir") + "/data/byteRules.gr";
    //</editor-fold>

    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="labels files">
    public static final String labelsFile = System.getProperty("user.dir") + "/data/LabelsMap.dat";
    public static final String wordsFile = System.getProperty("user.dir") + "/data/wordsMap.dat";
    public static final String specialsFile = System.getProperty("user.dir") + "/data/Specials.dat";
    public static final String constituentsFile = System.getProperty("user.dir") + "/data/consMap.dat";
    public static final String interConstituentsFile = System.getProperty("user.dir") + "/data/incompleteConsMap.dat";
    public static final String preTerminalFile = System.getProperty("user.dir") + "/data/preTerminalMap.dat";
    public static final String actionsFile = System.getProperty("user.dir") + "/data/actionsMap.dat";
    //</editor-fold>

    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="model files">
    public static final String modelDir = System.getProperty("user.dir") + "/data/Learning Parameter/";
    //</editor-fold>

    public static final float offset = 0.0f;
    public static final float alpha = -1.0f;
}
