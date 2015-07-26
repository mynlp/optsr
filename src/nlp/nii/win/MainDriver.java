/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win;

import java.util.ArrayList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.parser.Parser;
import nlp.nii.win.parser.decoder.BeamSearchDecoder;
import nlp.nii.win.parser.decoder.Decoder;
import nlp.nii.win.parser.decoder.DeterministicDecoder;
import nlp.nii.win.parser.element.Oracle;

/**
 *
 * @author lelightwin
 */
public class MainDriver {

    public static void main(String[] args) {
        Decoder d = null;
        Parser parser;

        OptionParser optParser = new OptionParser();
        optParser.accepts("action").withOptionalArg();
        optParser.accepts("decoder").withOptionalArg().defaultsTo("beam");
        optParser.accepts("beamWidth").withOptionalArg().defaultsTo("16");
        optParser.accepts("trainSection").withOptionalArg().defaultsTo("2-21");
        optParser.accepts("evaluateSection").withOptionalArg().defaultsTo("22");
        optParser.accepts("numIter").withOptionalArg().defaultsTo("20");
        optParser.accepts("model").withOptionalArg().defaultsTo("");

        OptionSet options = optParser.parse(args);
        if (options.has("action")) {
            String action = options.valueOf("action").toString();
            String decoder = options.valueOf("decoder").toString();
            switch (decoder) {
                case "beam":
                    int beam = Integer.parseInt(options.valueOf("beamWidth").toString());
                    d = new BeamSearchDecoder(beam, true);
                    break;
                case "deterministic":
                    d = new DeterministicDecoder();
                    break;
                case "BFS":
                    // not support yet
                    break;
                case "AStar":
                    // not support yet
                    break;
                default:
                    System.out.println("There is no action " + action + ". Please input [\"beam\",\"deterministic\",\"BFS\",\"AStar\"]");
                    System.exit(0);
            }

            parser = new Parser(d);
            WinTreeBankReader reader = new WinTreeBankReader();
            ArrayList<Oracle> oracles = new ArrayList<>();
            String model = options.valueOf("model").toString();

            switch (action) {
                case "train":
                    String[] sections = options.valueOf("trainSection").toString().split("-");
                    if (sections.length != 2) {
                        System.out.println("Please input the start and end index of section you want to train on (0-24) in form \"a-b\"");
                        System.exit(0);
                    } else {
                        try {
                            int start = Integer.parseInt(sections[0]);
                            int end = Integer.parseInt(sections[1]);
                            if (start > end) {
                                System.out.println("Invalid section range!");
                                System.exit(0);
                            }
                            for (int i = start; i <= end; i++) {
//                                for (Tree<String> t : reader.readBinarizeSection(i, 5000)) {
//                                    oracles.add(new Oracle(t));
//                                }
                            }
                            int numberOfIterations = Integer.parseInt(options.valueOf("numIter").toString());
                            parser.train(oracles, numberOfIterations, model);
                        } catch (NumberFormatException e) {
                            System.out.println("Number format error");
                            System.exit(0);
                        }
                    }
                    break;
                case "evaluate":
                    try {
                        int evalIdx = Integer.parseInt(options.valueOf("evaluateSection").toString());
//                        for (Tree<String> t : reader.readBinarizeSection(evalIdx, 5000)) {
//                            oracles.add(new Oracle(t));
//                        }
                        parser.evaluate(oracles, model);
                    } catch (NumberFormatException ex) {
                        System.out.println("Number format error. Please type again the indexes of evaluate section");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("There is no action " + action + ". Please input \"train\" or \"evaluate\"");
                    System.exit(0);
            }
        }
    }
}
