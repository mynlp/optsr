/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.parser.element.Rule;
import nlp.nii.win.util.CustomizeHashMap;
import nlp.nii.win.ConstantResource.DataInfo;

/**
 *
 * @author lelightwin
 */
public class RuleManager {

    private Set<Rule<Integer>> rules = new HashSet();
    private List<Rule<Integer>>[][] binaryMapper;
    private List<Rule<Integer>>[] unaryMapper;

    private static RuleManager instance = null;

    public static RuleManager instance() {
        if (instance == null) {
            instance = new RuleManager();
        }
        return instance;
    }

    public RuleManager() {
        int size = DataInfo.instance().labels().size();
        binaryMapper = new List[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                binaryMapper[i][j] = new ArrayList();
            }
        }
        unaryMapper = new List[size];
        for (int i = 0; i < size; i++) {
            unaryMapper[i] = new ArrayList();
        }
        // load rules from file

    }

    private void loadInfo() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(Global.byteGrammarRulesFile), "utf-8"));
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] datas = line.split(" ");
                boolean binary = (datas.length == 5);

                int con = Integer.parseInt(datas[0]);
                int child1 = Integer.parseInt(datas[1]);
                Rule<Integer> r;
                if (binary) {
                    int child2 = Integer.parseInt(datas[2]);
                    int action = Integer.parseInt(datas[3]);
                    int head = Integer.parseInt(datas[4]);
                    r = new Rule<>(con, child1, child2, head);
                    r.setAction(action);
                } else {
                    int action = Integer.parseInt(datas[2]);
                    int head = Integer.parseInt(datas[3]);
                    r = new Rule<>(con, child1, head);
                    r.setAction(action);
                }

                addRule(r);
            }
            bfr.close();
        } catch (IOException ex) {
            Logger.getLogger(RuleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveInfo() {
        try {
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Global.byteGrammarRulesFile), "utf-8"));
            for (Rule<Integer> r : rules) {
                bfw.write("" + r.cons());
                bfw.write(" " + r.child1());
                if (r.isBinary()) {
                    bfw.write(" " + r.child2());
                }
                bfw.write(" " + r.action());
                bfw.write(" " + r.head());
                bfw.newLine();
            }
            bfw.close();
        } catch (IOException ex) {
            Logger.getLogger(RuleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addRule(Rule<Integer> r) {
        if (!rules.contains(r)) {
            rules.add(r);
            if (r.isBinary()) {
                int action = DataInfo.instance().getUpdateNumericAction("B_" + DataInfo.instance().label(r.cons())); // reduce action
                r.setAction(action);
                binaryMapper[r.child1()][r.child2()].add(r);
            } else {
                int unary = r.child1();
                int action = -1;
                if (DataInfo.instance().isPreTerminal(unary)) { // unary child is a preterminal
                    action = DataInfo.instance().getUpdateNumericAction("S_U_" + DataInfo.instance().label(r.cons())); // shu action
                } else { // unary child is a constituent
                    action = DataInfo.instance().getUpdateNumericAction("B_" + DataInfo.instance().label(unary) + "U_" + DataInfo.instance().label(r.cons())); // shu action
                }
                r.setAction(action);
                unaryMapper[r.child1()].add(r);
            }
        }
    }

    /**
     *
     * @param left
     * @param right
     * @param parent
     * @return
     */
    public Rule<Integer> getBinaryRule(int left, int right, int parent) {
        List<Rule<Integer>> rArr = RuleManager.this.get(left, right);
        for (int i = 0; i < rArr.size(); i++) {
            Rule<Integer> r = rArr.get(i);
            if ((r.cons() == parent)) {
                return r;
            }
        }
        return null;
    }

    /**
     *
     * @param unary
     * @param parent
     * @return
     */
    public Rule<Integer> getUnaryRule(int unary, int parent) {
        List<Rule<Integer>> rArr = get(unary);
        for (Rule<Integer> r : rArr) {
            if (parent == r.cons()) {
                return r;
            }
        }
        return null;
    }

    /**
     *
     * @param left
     * @param right
     * @return list of binary rules with i and j is the right hand side
     */
    public List<Rule<Integer>> get(int left, int right) {
        return binaryMapper[left][right];
    }

    /**
     *
     * @param unary
     * @return list of unary rules with i is the right hand side
     */
    public List<Rule<Integer>> get(int unary) {
        return unaryMapper[unary];
    }

    public static void main(String[] args) throws IOException {
//        ArrayList<Rule<Integer>> rs = Global.ruleManager.getUnary(Labels.byteValue("VP"));
//
//        for (Rule<Integer> r : rs) {
//            System.out.println(Labels.strValue(r.getCons()) + " " + r.getAction());
//        }

    }
}
