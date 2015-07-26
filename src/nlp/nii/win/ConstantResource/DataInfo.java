/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.ConstantResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelightwin
 */
public class DataInfo {

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="constant resources">
    private final Map<Integer, String> consMap = new HashMap<>();
    private final Map<Integer, String> interConsMap = new HashMap<>();
    private final Map<Integer, String> preTerminalMap = new HashMap<>();

    private final Map<String, Integer> actionsMap = new HashMap<>();
    private final Map<String, Integer> labelsMap = new HashMap<>();
    private final Map<String, Integer> wordsMap = new HashMap<>();

    private final List<String> labelsList = new ArrayList<>();
    private final List<String> wordsList = new ArrayList<>();
    private final List<String> actionsList = new ArrayList<>();

    private final Set<String> punctuation = new HashSet<>();
    private static DataInfo instance = null;

    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="init block">
    public static DataInfo instance() {
        if (instance == null) {
            instance = new DataInfo();
        }
        return instance;
    }

    public DataInfo() {
        getUpdateNumericAction("S");
        getUpdateNumericAction("FIN");

        loadInfo();
        punctuation.add("''");
        punctuation.add(",");
        punctuation.add(".");
        punctuation.add(":");
        punctuation.add("``");
        punctuation.add("-NONE-");
    }

    private void loadInfo() {
        loadMapAndArray(wordsMap, wordsList, Global.wordsFile);
        loadMapAndArray(labelsMap, labelsList, Global.labelsFile);
        loadMapAndArray(actionsMap, actionsList, Global.actionsFile);

        loadMap(consMap, Global.constituentsFile);
        loadMap(interConsMap, Global.interConstituentsFile);
        loadMap(preTerminalMap, Global.preTerminalFile);
    }

    public void saveInfo() {
        saveMap1(wordsMap, Global.wordsFile);
        saveMap1(labelsMap, Global.labelsFile);
        saveMap1(actionsMap, Global.actionsFile);

        saveMap2(consMap, Global.constituentsFile);
        saveMap2(interConsMap, Global.interConstituentsFile);
        saveMap2(preTerminalMap, Global.preTerminalFile);
    }

    private void saveMap1(Map<String, Integer> map, String file) {
        try {
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            for (Entry<String, Integer> e : map.entrySet()) {
                bfw.write(e.getKey() + " " + e.getValue());
                bfw.newLine();
            }
            bfw.close();
        } catch (IOException ex) {
            Logger.getLogger(DataInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveMap2(Map<Integer, String> map, String file) {
        try {
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            for (Entry<Integer, String> e : map.entrySet()) {
                bfw.write(e.getValue() + " " + e.getKey());
                bfw.newLine();
            }
            bfw.close();
        } catch (IOException ex) {
            Logger.getLogger(DataInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMapAndArray(Map<String, Integer> map, List<String> array, String fileName) {
        try {
            // load data
            BufferedReader bfr1 = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            String data1 = "";
            while ((data1 = bfr1.readLine()) != null) { // init labels map
                String[] datas = data1.split(" ");
                map.put(datas[0], Integer.parseInt(datas[1]));
            }

            String[] a1 = new String[map.size()];
            for (String k : map.keySet()) {
                int v = map.get(k);
                a1[v] = k;
            }
            for (String str : a1) {
                array.add(str);
            }
            bfr1.close();
        } catch (IOException ex) {
        }
    }

    private void loadMap(Map<Integer, String> map, String fileName) {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            String data = "";
            while ((data = bfr.readLine()) != null) {
                String[] datas = data.split(" ");
                map.put(Integer.parseInt(datas[1]), datas[0]);
            }
            bfr.close();
        } catch (IOException ex) {
        }
    }
    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //<editor-fold defaultstate="collapsed" desc="function for checking type of constituent tag">
    public boolean isConstituent(int label) {
        return consMap.containsKey(label);
    }

    public boolean isIncomplete(int label) {
        return interConsMap.containsKey(label);
    }

    public boolean isPreTerminal(int label) {
        return preTerminalMap.containsKey(label);
    }

    //</editor-fold>
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc="functions for converting value (string <-> int)">
    public void updateConstituent(int idx, String label) {
        consMap.put(idx, label);
    }

    public void updatePreTerminal(int idx, String label) {
        preTerminalMap.put(idx, label);
    }

    public void updateIncomplete(int idx, String label) {
        interConsMap.put(idx, label);
    }

    public Integer getNumericWord(String word) {
        return wordsMap.get(word);
    }

    public Integer getUpdateNumericWord(String word) {
        Integer value = wordsMap.get(word);
        if (value == null) {
            value = wordsMap.size();
            wordsMap.put(word, wordsMap.size());
            wordsList.add(word);
        }
        return value;
    }

    public Integer getNumericLabel(String label) {
        return labelsMap.get(label);
    }

    public Integer getUpdateNumericLabel(String label) {
        Integer value = labelsMap.get(label);
        if (value == null) {
            value = labelsMap.size();
            labelsMap.put(label, labelsMap.size());
            labelsList.add(label);
        }
        return value;
    }

    public Integer getNumericAction(String action) {
        return actionsMap.get(action);
    }

    public Integer getUpdateNumericAction(String action) {
        Integer value = actionsMap.get(action);
        if (value == null) {
            value = actionsMap.size();
            actionsMap.put(action, actionsMap.size());
            actionsList.add(action);
        }
        return value;
    }

    public String label(int idx) {
        return labelsList.get(idx);
    }

    public String word(int idx) {
        return wordsList.get(idx);
    }

    public String action(int idx) {
        return actionsList.get(idx);
    }

    public List<String> labels() {
        return labelsList;
    }

    public List<String> actions() {
        return actionsList;
    }

    public List<String> words() {
        return wordsList;
    }

    public Set<String> punctuation() {
        return punctuation;
    }
    //</editor-fold>

    public static void main(String[] args) {
        System.out.println(DataInfo.instance().getNumericLabel("NP"));
    }

}
