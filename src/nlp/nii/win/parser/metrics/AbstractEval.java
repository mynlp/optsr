/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.metrics;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nlp.nii.win.corpus.stanford.syntax.Tree;

/**
 *
 * @author lelightwin
 */
public abstract class AbstractEval<L> {

    protected String str = "";

    private int exact = 0;
    private int total = 0;

    private int correctEvents = 0;
    private int guessedEvents = 0;
    private int goldEvents = 0;
    
    private double precision;
    private double recall;

    abstract Set<Object> makeObjects(Tree<L> tree);

    public double evaluate(Tree<L> guess, Tree<L> gold) {
        return evaluate(guess, gold, null);
    }

    /*
     * evaluates precision and recall by calling makeObjects() to make a set
     * of structures for guess Tree and gold Tree, and compares them with
     * each other.
     */
    public double evaluate(Tree<L> guess, Tree<L> gold, PrintWriter pw) {
        Set<Object> guessedSet = makeObjects(guess);
        Set<Object> goldSet = makeObjects(gold);
        Set<Object> correctSet = new HashSet<>();
        correctSet.addAll(goldSet);
        correctSet.retainAll(guessedSet);

        correctEvents += correctSet.size();
        guessedEvents += guessedSet.size();
        goldEvents += goldSet.size();

        int currentExact = 0;
        if (correctSet.size() == guessedSet.size()
                && correctSet.size() == goldSet.size()) {
            exact++;
            currentExact = 1;
        }
        total++;

        // guess.pennPrint(pw);
        // gold.pennPrint(pw);
        double f1 = displayPRF(str + " [Current] ", correctSet.size(),
                guessedSet.size(), goldSet.size(), currentExact, 1, pw);
        return f1;

    }

    public double evaluateMultiple(List<Tree<L>> guesses,
            List<Tree<L>> golds, PrintWriter pw) {
        assert (guesses.size() == golds.size());
        int correctCount = 0;
        int guessedCount = 0;
        int goldCount = 0;
        for (int i = 0; i < guesses.size(); i++) {
            Tree<L> guess = guesses.get(i);
            Tree<L> gold = golds.get(i);
            Set<Object> guessedSet = makeObjects(guess);
            Set<Object> goldSet = makeObjects(gold);
            Set<Object> correctSet = new HashSet<>();
            correctSet.addAll(goldSet);
            correctSet.retainAll(guessedSet);
            correctCount += correctSet.size();
            guessedCount += guessedSet.size();
            goldCount += goldSet.size();
        }

        correctEvents += correctCount;
        guessedEvents += guessedCount;
        goldEvents += goldCount;

        int currentExact = 0;
        if (correctCount == guessedCount && correctCount == goldCount) {
            exact++;
            currentExact = 1;
        }
        total++;

        // guess.pennPrint(pw);
        // gold.pennPrint(pw);
        double f1 = displayPRF(str + " [Current] ", correctCount,
                guessedCount, goldCount, currentExact, 1, pw);
        return f1;

    }

    public double[] massEvaluate(Tree<L> guess, Tree<L>[] goldTrees) {
        Set<Object> guessedSet = makeObjects(guess);
        double cEvents;
        double guEvents;
        double goEvents;
        double exactM = 0, precision = 0, recall = 0, f1 = 0;

        for (int treeI = 0; treeI < goldTrees.length; treeI++) {
            Tree<L> gold = goldTrees[treeI];
            Set<Object> goldSet = makeObjects(gold);
            Set<Object> correctSet = new HashSet<>();
            correctSet.addAll(goldSet);
            correctSet.retainAll(guessedSet);
            cEvents = correctSet.size();
            guEvents = guessedSet.size();
            goEvents = goldSet.size();

            double p = cEvents / guEvents;
            double r = cEvents / goEvents;
            double f = (p > 0.0 && r > 0.0 ? 2.0 / (1.0 / p + 1.0 / r)
                    : 0.0);

            precision += p;
            recall += r;
            f1 += f;

            if (cEvents == guEvents && cEvents == goEvents) {
                exactM++;
            }
        }
        double ex = exactM / goldTrees.length;
        double[] results = {precision, recall, f1, ex};

        return results;

    }

    private double displayPRF(String prefixStr, int correct, int guessed,
            int gold, int exact, int total, PrintWriter pw) {
        double precision = (guessed > 0 ? correct / (double) guessed : 1.0);
        double recall = (gold > 0 ? correct / (double) gold : 1.0);
        double f1 = (precision > 0.0 && recall > 0.0 ? 2.0 / (1.0 / precision + 1.0 / recall)
                : 0.0);

        double exactMatch = exact / (double) total;

        String displayStr = " P: " + ((int) (precision * 10000)) / 100.0
                + " R: " + ((int) (recall * 10000)) / 100.0 + " F1: "
                + ((int) (f1 * 10000)) / 100.0 + " EX: "
                + ((int) (exactMatch * 10000)) / 100.0;

        if (pw != null) {
            pw.println(prefixStr + displayStr);
        }
        this.precision  = precision;
        this.recall = recall;
        return f1;
    }

    public double display(boolean verbose) {
        return display(verbose, new PrintWriter(System.out, true));
    }

    public double display(boolean verbose, PrintWriter pw) {
        return displayPRF(str + " [Average] ", correctEvents,
                guessedEvents, goldEvents, exact, total, pw);
    }

    /**
     * @return the precision
     */
    public double getPrecision() {
        return precision;
    }

    /**
     * @return the recall
     */
    public double getRecall() {
        return recall;
    }
}
