/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import java.util.ArrayList;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Sentence;

/**
 *
 * @author lelightwin
 */
public class Agenda extends ArrayList<DPState> {

    /**
     * this is function for dynamic programming
     *
     * @param state to add or merge with its equivalent state in agenda
     */
    public void addOrMerge(DPState state) {

    }

    public void addExceptNull(DPState state) {
        if (state != null) {
            this.add(state);
        }
    }
    
    public DPState[] toStateArray(){
        DPState[] stateArr = new DPState[this.size()];
        Object[] oArr = this.toArray();
        for (int i = 0; i < oArr.length; i++) {
            stateArr[i] = (DPState)oArr[i];
        }
        return stateArr;
    }

    public ArrayList<DPState> prune(int k) {
        ArrayList<DPState> result = new ArrayList();
        if (k >= size()) {
            k = size();
        }
        for (int i = 0; i < k; i++) {
            result.add(this.get(i));
        }
        return result;
    }

    public static void main(String[] args) {
        Agenda states = new Agenda();
        states.add(new DPState(new Sentence(10), 0));
        states.prune(1);
    }
}
