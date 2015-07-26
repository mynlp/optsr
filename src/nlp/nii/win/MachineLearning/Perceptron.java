/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nlp.nii.win.ConstantResource.DataInfo;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.parser.BaselineFeature;
import nlp.nii.win.parser.element.Feature;
import nlp.nii.win.parser.element.Features;
import nlp.nii.win.util.CustomizeHashMap;
import nlp.nii.win.util.HashMatrix;

/**
 * This is a class for weight vector, it will convert from feature to its weight
 *
 * @author lelightwin
 */
public class Perceptron {

    private float C = 1.0f;
    private int size = 0;
    private int maximumSize;
    private HashMatrix<Feature> indexer;
    private float[] W;
    private float[] sumW;
    private boolean train;

    public Perceptron(int maximumSize, int actionsNum, int featureTypesNum, boolean train) {
        this.indexer = new HashMatrix(actionsNum, featureTypesNum);
        this.maximumSize = maximumSize;
        this.W = new float[maximumSize];
        for (int i = 0; i < W.length; i++) {
            W[i] = 0.0f;
        }
        this.train = train;
        if (this.train) {
            this.sumW = new float[maximumSize];
            for (int i = 0; i < sumW.length; i++) {
                sumW[i] = 0.0f;
            }
        }
    }

    /*-------------------------------------------------------------------------------*/
    /*updateIfNotExist weights with an offset*/
    public void updateWeight(int action, int featType, Feature value, float offset) {
        if (value != null) {
            int idx = indexer.update(action, featType, value, size);
            if (idx == size) {
                size += 1;
            }

            W[idx] += offset;
            if (this.train) {
                sumW[idx] += C * offset;
            }
        }
    }

    /*--------------------------------------------------------------------------------*/
    /**
     *
     * @param action
     * @param featType
     * @param value
     * @return the weight value from feature
     */
    public float W(int action, int featType, Feature value) {
        if (value != null) {
            Integer idx = indexer.get(action, featType, value);
            if (idx != null) {
                return W[idx];
            }
        }
        return 0.0f;
    }

    /**
     *
     * @return the current average weights
     */
    public float[] takeAverage() {
        float[] avgW = new float[size];
        for (int i = 0; i < size; i++) {
            avgW[i] = W[i] - sumW[i] / C;
        }
        return avgW;
    }

    public void accumulate() {
        C += 1;
    }

    public HashMatrix<Feature> getMatrix() {
        return indexer;
    }

    public float[] getWeights() {
        return W;
    }

    public int modelSize() {
        return size;
    }

    /*----------------------------------------------------------------------------------------*/
    /**
     *
     * @param hashMatrixFile to save indexer from weight vector
     * @param weightsFile to save weight array from weight vector
     */
    public void saveModels(String hashMatrixFile, String weightsFile) {
        try {

            BufferedWriter bfw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hashMatrixFile), "utf-8"));
            BufferedWriter bfw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(weightsFile), "utf-8"));
            float[] averageWeights = takeAverage(); // get average weights
            for (int i = 0; i < DataInfo.instance().actions().size(); i++) {
                bfw1.write("$a$: " + i);
                bfw1.newLine();
                for (int j = 0; j < BaselineFeature.quantity; j++) {
                    CustomizeHashMap<Feature> map = indexer.getMap(i, j);
                    if (!map.isEmpty()) {
                        bfw1.write("$f$: " + j);
                        bfw1.newLine();
                        Iterator<Map.Entry<Feature, Integer>> entries = map.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry entry = entries.next();
//                            System.out.println(entry);
                            bfw1.write(entry.getKey().toString());
                            bfw1.write(">>");
                            bfw1.write(entry.getValue() + "");
                            bfw1.newLine();
                        }
                    }
                }
            }
            for (int i = 0; i < averageWeights.length; i++) {
                float w = averageWeights[i];
                bfw2.write(w + " ");
            }
            bfw2.close();
            bfw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Perceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param hashMatrixFile to load indexer from weight vector
     * @param weightsFile to load weight array from weight vector
     */
    public void loadModels(String hashMatrixFile, String weightsFile) {
        String error = "";

        try {
            BufferedReader bfr1 = new BufferedReader(new InputStreamReader(new FileInputStream(hashMatrixFile), "utf-8"));
            BufferedReader bfr2 = new BufferedReader(new InputStreamReader(new FileInputStream(weightsFile), "utf-8"));
            String data1 = "";
            int x = -1;
            int y = -1;
            while ((data1 = bfr1.readLine()) != null) {
                if (data1.startsWith("$a$: ")) {
                    x = Integer.parseInt(data1.substring(5));
                } else if (data1.startsWith("$f$: ")) {
                    y = Integer.parseInt(data1.substring(5));
                } else {
                    String[] datas = data1.split(">>");
                    error = data1;
                    Feature f = Features.from(datas[0]);
                    int idx = Integer.parseInt(datas[1]);
                    indexer.put(x, y, f, idx);
                }
            }

            String[] weightsStr = bfr2.readLine().split(" ");
            for (int i = 0; i < weightsStr.length; i++) {
                W[i] = Float.parseFloat(weightsStr[i]);
            }
            size = weightsStr.length;
            bfr2.close();
            bfr1.close();
        } catch (IOException ex) {
            Logger.getLogger(Perceptron.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            System.out.println("number format error:"+error);
        }
    }
}
