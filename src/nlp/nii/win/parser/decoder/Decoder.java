/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.decoder;

import java.util.ArrayList;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;

/**
 *
 * @author lelightwin
 */
public abstract class Decoder {

    protected DPState predict = null;
    protected DPState gold = null;
    protected int step = -1;
    protected boolean predictIsRight = true;

    public abstract void trainDecoding(Oracle oracle);

    public abstract void performDecoding(DPState state);

    public DPState getPredict() {
        return predict;
    }

    public int getStep() {
        return step;
    }

    public DPState getGold() {
        return gold;
    }

    /**
     *
     * @param states
     * @return the if of state with maximum score in states
     */
    protected int maxId(ArrayList<DPState> states) {
        int max = 0;
        for (int i = 1; i < states.size(); i++) {
            if (states.get(i).compareTo(states.get(max)) > 0) {
                max = i;
            }
        }
        return max;
    }
    
    /**
     *
     * @param states
     * @return the state with maximum score in states
     */
    protected DPState max(ArrayList<DPState> states) {
        int max = 0;
        for (int i = 1; i < states.size(); i++) {
            if (states.get(i).compareTo(states.get(max)) > 0) {
                max = i;
            }
        }
        return states.get(max);
    }
    
    /**
     *
     * @param states
     * @return the id of state with minimum score in states
     */
    protected int minId(ArrayList<DPState> states) {
        int min = 0;
        for (int i = 1; i < states.size(); i++) {
            if (states.get(i).compareTo(states.get(min)) < 0) {
                min = i;
            }
        }
        return min;
    }
    
    /**
     *
     * @param states
     * @return the state with minimum score in states
     */
    protected DPState min(ArrayList<DPState> states) {
        int min = 0;
        for (int i = 1; i < states.size(); i++) {
            if (states.get(i).compareTo(states.get(min)) < 0) {
                min = i;
            }
        }
        return states.get(min);
    }

    /**
     *
     * @param states
     * @return true if @states contain golden states
     */
    protected DPState goldExist(ArrayList<DPState> states) {
        for (DPState st : states) {
            if (st.isGold()) {
                return st;
            }
        }
        return null;
    }

    /**
     * @return the predictIsRight
     */
    public boolean isPredictIsRight() {
        return predictIsRight;
    }
}
