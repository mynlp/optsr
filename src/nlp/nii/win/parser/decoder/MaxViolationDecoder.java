/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.decoder;

import java.util.ArrayList;
import nlp.nii.win.parser.Agenda;
import nlp.nii.win.parser.ParserPerceptronCommunicator;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;

/**
 *
 * @author lelightwin
 */
public class MaxViolationDecoder extends Decoder {

    private int beam;
    private boolean earlyUpdate;

    public MaxViolationDecoder(int beam, boolean earlyUpdate) {
        this.beam = beam;
        this.earlyUpdate = earlyUpdate;
    }

    @Override
    public void trainDecoding(Oracle oracle) {
        Agenda agenda = new Agenda();
        predict = null;
        ArrayList<DPState> beamStates = new ArrayList<>();
        step = 0;
        DPState startState = oracle.start();
        startState.setGold(true);
        beamStates.add(startState); // init beamStates with initial state of oracle
        predictIsRight = true;

        while (step < oracle.finalStep()) {
            // perform early update
            if (earlyUpdate) {
                if (goldExist(beamStates) == null) {
                    return;
                }
            }

            agenda.clear();
            int actionGold = oracle.getNextAction(step);
            for (DPState state : beamStates) { // perform the shift-reduce actions from all the states in beam
                agenda.addAll(state.takeShift(actionGold));
                agenda.addAll(state.takeBReduce(actionGold));
                agenda.addExceptNull(state.takeFinish());
            }

            // after then, choose the new beam-best states based on their cost
            beamStates.clear();
            if (beam >= agenda.size()) {
                beamStates.addAll(agenda);
            } else {
                DPState[] stateArr = agenda.toStateArray();
                int pivot = ParserPerceptronCommunicator.orderer.kthLargest(stateArr, beam);
                for (int j = pivot; j < stateArr.length; j++) {
                    beamStates.add(stateArr[j]);
                }
            }

            // for non-early update case
            if (!beamStates.isEmpty()) {
                predict = max(beamStates);
                predictIsRight = predict.isGold();
                gold = goldExist(agenda);
                step += 1;
            } else {
                return;
            }
        }
    }

    /**
     * @param earlyUpdate the earlyUpdate to set
     */
    public void setEarlyUpdate(boolean earlyUpdate) {
        this.earlyUpdate = earlyUpdate;
    }

    @Override
    public void performDecoding(DPState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
