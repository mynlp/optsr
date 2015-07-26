/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.test;

import java.util.HashMap;
import java.util.Random;
import nlp.nii.win.parser.element.Feature;

/**
 *
 * @author lelightwin
 */
public class IntHasher {

    public static int hash(int v1, int v2, int v3) {
        return v1 + 100 * v2 + 10000 * v3;
    }

    public static boolean check(int[] arr, int i) {
        for (int j = 0; j < arr.length; j++) {
            if ((arr[i] == arr[j]) && (i != j)) {
                System.out.println(i + " " + j);
                System.out.println(arr[i] + " " + arr[j]);
                return false;
            }
        }
        return true;
    }

    public static boolean check(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (!check(arr, i)) {
                System.out.println("false");
            }
        }
        return true;
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map1 = new HashMap();
        HashMap<Feature, Integer> map2 = new HashMap();
        Random r = new Random();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            int v1 = r.nextInt(97);
            int v2 = r.nextInt(97);
            int v3 = r.nextInt(43633);
            int v = hash(v1, v2, v3);
            map1.put(v, map1.size());
//            map2.put(new Feature(v1, v2, v3), map2.size());
        }
        long end = System.currentTimeMillis();

        System.out.println("time: " + (end - start) / 1000f);
        System.out.println(map1.size());
        System.out.println(map2.size());
    }
}