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
import nlp.nii.win.parser.decoder.Decoder;
import nlp.nii.win.parser.decoder.BeamSearchDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.MachineLearning.Perceptron;
import nlp.nii.win.corpus.Binarizer;
import nlp.nii.win.corpus.Corpuses;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;
import nlp.nii.win.parser.metrics.LabeledConstituentEval;
import static nlp.nii.win.parser.ParserPerceptronCommunicator.*;

/**
 *
 * @author lelightwin
 */
public class Parser {

    private Decoder decoder;
    private final LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton("ROOT"), DataInfo.instance().punctuation());
//    private final LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton("ROOT"), new HashSet());

    public Parser(Decoder decoder) {
        this.decoder = decoder;
    }

    public void train(List<Oracle> oracles, int numberOfIterations, String model) {
        learner = new Perceptron(maximumModelSize, DataInfo.instance().actions().size(), BaselineFeature.quantity, true);
        System.out.println("Start training...");
        int iterationIdx;
        int oracleIdx;
        boolean learn = true;
        float errorRate;
        int error;

        for (int i = 0; i < numberOfIterations; i++) {
            iterationIdx = i + 1;
            error = 0;
            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Iteration " + iterationIdx + "th:");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            if (learn) {
                long start = System.currentTimeMillis();
                long decodingTime = 0;
                long updatingTime = 0;
                for (int j = 0; j < oracles.size(); j++) {
                    oracleIdx = j + 1;
                    Oracle oracle = oracles.get(j);
                    // decoding and update parameters
                    long start1 = System.currentTimeMillis();
                    decoder.trainDecoding(oracle);
                    long end1 = System.currentTimeMillis();
                    decodingTime += (end1 - start1);

                    long start2 = System.currentTimeMillis();
                    DPState predict = decoder.getPredict();
                    DPState gold = decoder.getGold();
                    if (predict != null) {
                        if (!decoder.isPredictIsRight()) {
                            error += 1;
                            //update parameters
                            ArrayList<DPState> predictChain = predict.listStates();
                            ArrayList<DPState> goldenChain = gold.listStates();
                            update(goldenChain, predictChain);
                        }
                    }
                    long end2 = System.currentTimeMillis();
                    updatingTime += (end2 - start2);
                }
                long end = System.currentTimeMillis();
                if (error == 0) {
                    learn = false;
                }
                System.out.println("");
                System.out.println("ITERATION COMPLETE------------------------------------------------------------");
                errorRate = error * 100f / oracles.size();
                System.out.println("full sentence accuracy: " + (100 - errorRate) + "%");
                System.out.println("number of error sentence: " + error);
                System.out.println("decoding speed(sentences/second): " + oracles.size() / ((end - start) / 1000f));
                System.out.println("model size: " + learner.modelSize());
                System.out.println("Total time: " + (end - start) / 1000f);
                System.out.println("Total decoding time: " + decodingTime / 1000f);
                System.out.println("Total updating time: " + updatingTime / 1000f);

                if (!(new File(Global.modelDir + model).exists())) {
                    new File(Global.modelDir + model).mkdir();
                }
                learner.saveModels(Global.modelDir + model + "/matrix." + iterationIdx, Global.modelDir + model + "/weights." + iterationIdx);
            }

        }
        System.out.println("Learning complete!!!!");
    }

    public Tree<String> parse(String input) {
        DPState start = Tagger.tag(input);
        decoder.performDecoding(start);
        DPState predict = decoder.getPredict();
        if (predict.isFinish()) {
            Tree<String> t = Corpuses.textInstance(predict.makeTree());
            Binarizer.debinarizing(t);
            return t;
        }

        return null;
    }

    public void evaluate(List<Oracle> oracles, String model, int iterationIdx) throws IOException {
        learner = new Perceptron(maximumModelSize, DataInfo.instance().actions().size(), BaselineFeature.quantity, false);
        System.out.println("Loading model...");
        learner.loadModels(Global.modelDir + model + "/matrix." + iterationIdx, Global.modelDir + model + "/weights." + iterationIdx);
        System.out.println("Done!!!");
        System.out.println("model size :" + learner.modelSize());

        System.out.println("Start evaluation...");
        long start = System.currentTimeMillis();
        double precision = 0.0;
        double recall = 0.0;
        double fscore = 0.0;
        int fail = 0;

//        BufferedWriter predictBFW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(model + "." + iterationIdx + ".predict"), "utf-8"));
//        BufferedWriter goldenBFW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(model + "." + iterationIdx + ".golden"), "utf-8"));
        for (int i = 0; i < oracles.size(); i++) {
            Oracle oracle = oracles.get(i);
//            System.out.print("sentence " + i + "... ");

            long subStart = System.nanoTime();
            decoder.performDecoding(oracle.start());
            DPState predict = decoder.getPredict();
            long subEnd = System.nanoTime();
            if (predict.isFinish()) {
                Tree<String> t1 = Corpuses.textInstance(predict.makeTree());
                Tree<String> t2 = Corpuses.textInstance(oracle.getTree());
                Binarizer.debinarizing(t1);
                Binarizer.debinarizing(t2);

                fscore += eval.evaluate(t1, t2);
                precision += eval.getPrecision();
                recall += eval.getRecall();
            } else {
                fail++;
            }
        }
//        predictBFW.close();
//        goldenBFW.close();

        long end = System.currentTimeMillis();
        float totalTime = (end - start) / 1000f;
        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.printf("P:%.3f - R:%.3f - F:%.3f \n", 100 * precision / oracles.size(), 100 * recall / oracles.size(), 100 * fscore / oracles.size());
        System.out.println("time: " + totalTime + " seconds");
        System.out.println("parsing speed(sentences/second): " + (oracles.size() / totalTime));
        System.out.println("Percentage of failed parsing sentence: " + (100.0 * fail / oracles.size()) + "%");
    }

    public static void main(String[] args) throws IOException {
        Decoder beamDecoder = new BeamSearchDecoder(16, true);
//        Decoder bfsDecoder = new BestFirstSearchDecoder();

        Parser parser = new Parser(beamDecoder);
        WinTreeBankReader reader = new WinTreeBankReader();

        List<Oracle> oracles = new ArrayList<>();
        List<Tree<Integer>> corpus = new ArrayList<>();
        System.out.println("begin loading corpus...");
//        for (int i = 2; i <= 21; i++) {
        int i = 23;
            for (Tree<Integer> t : reader.readNumericSection(i)) {
                corpus.add(t);
            }
//        }
        Corpuses.extractRulesFrom(corpus);
//        DataInfo.instance().saveInfo();
//        RuleManager.instance().saveInfo();

        for (Tree<Integer> tree : corpus) {
            oracles.add(new Oracle(tree));
        }

        for (int j = 1; j <= 50; j++) {
            System.out.println("");
            System.out.println("Iteration " + j);
            parser.evaluate(oracles, "beam16.baseline", j);
        }

//        parser.train(oracles, 50, "bfs.baseline");
    }
}
