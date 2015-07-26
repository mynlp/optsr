/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.decoder;

import java.util.ArrayList;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.parser.Agenda;
import nlp.nii.win.parser.ParserPerceptronCommunicator;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;

/**
 *
 * @author lelightwin
 */
public class DeterministicDecoder extends Decoder {

    @Override
    public void trainDecoding(Oracle oracle) {
        Agenda agenda = new Agenda();
        predict = oracle.start();
        predict.setGold(true);
        step = 0;
        predictIsRight = true;
        while (step < oracle.finalStep()) {
            if (!predict.isGold()) {
                ArrayList<DPState> predictChain = predict.listStates();
                ArrayList<DPState> goldenChain = gold.listStates();
                ParserPerceptronCommunicator.update(goldenChain, predictChain);
                predict = gold;
            }

            agenda.clear();
            int actionGold = oracle.getNextAction(step);
            agenda.addAll(predict.takeShift(actionGold));
            agenda.addAll(predict.takeBReduce(actionGold));
            agenda.addExceptNull(predict.takeFinish());
            predict = max(agenda);
            gold = goldExist(agenda);
            predictIsRight = predictIsRight && predict.isGold();
            step += 1;
        }
    }

    @Override
    public void performDecoding(DPState state) {
        Agenda agenda = new Agenda();
        predict = state;
        while (!predict.isFinish()) {
            agenda.clear();
            agenda.addAll(predict.takeShift(-1));
            agenda.addAll(predict.takeBReduce(-1));
            agenda.addExceptNull(predict.takeFinish());
            if (!agenda.isEmpty()) {
                predict = max(agenda);
            } else {
                return;
            }
        }
    }

    public static void main(String[] args) {
    }
}
