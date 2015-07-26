/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import java.util.ArrayList;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.MachineLearning.Perceptron;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Feature;
import nlp.nii.win.util.OrderStatistic;

/**
 *
 * @author lelightwin
 */
public class ParserPerceptronCommunicator {

    public static final int maximumModelSize = 30000000;
    public static Perceptron learner; // leaner from system
    public static final OrderStatistic<DPState> orderer = new OrderStatistic<>();

    //<editor-fold defaultstate="collapsed" desc="update methods">
    public static void update(ArrayList<DPState> golds, ArrayList<DPState> predicts) {
        update(golds, Global.alpha);
        update(predicts, -Global.alpha);
        learner.accumulate(); // increase C (for average perceptron)!
    }

    private static void update(ArrayList<DPState> states, float offset) {
        for (int i = 1; i < states.size(); i++) {
            int action = states.get(i).getAction();
            update(action, states.get(i - 1), offset);
        }
    }

    public static void update(int action, DPState p, float offset) {
        Feature[] feats = FeatureExtractor.baselineFeaturesFrom(p);
        for (int i = 0; i < feats.length; i++) {
            learner.updateWeight(action, i, feats[i], offset);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="scoring methods">
    public static float score(int action, DPState p) {
        float sc = 0.0f;
        Feature[] feats = FeatureExtractor.baselineFeaturesFrom(p);

        for (int i = 0; i < feats.length; i++) {
            sc += learner.W(action, i, feats[i]);
        }
        return sc;
    }
    //</editor-fold>

}
