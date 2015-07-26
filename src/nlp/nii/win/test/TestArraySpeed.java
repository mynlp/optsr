/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.test;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lelightwin
 */
public class TestArraySpeed {

    public static void main(String[] args) {

        int size = 100000000;
        float[] floatArr = new float[size];
        ArrayList<Float> floatList = new ArrayList<>();

        Random r = new Random();
        System.out.println("creating data");
        for (int i = 0; i < floatArr.length; i++) {
            float c = r.nextFloat();
            floatArr[i] = c;
//            floatList.add(c);
        }
        System.out.println("done");

        long start = System.currentTimeMillis();
        for (int t = 0; t < 10000; t++) {
            for (int i = 0; i < floatArr.length; i++) {
                float g = floatArr[i];
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("array time: " + (end - start) / 1000f);

        start = System.currentTimeMillis();
//        for (int t = 0; t < 10000; t++) {
            for (int i = 0; i < floatList.size(); i++) {
                Float f = floatList.get(i);
            }
//        }
        end = System.currentTimeMillis();
        System.out.println("array list time: " + (end - start) / 1000f);
    }
}
