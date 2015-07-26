/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.util;

/**
 *
 * @author lelightwin
 */
public class BloomFilter {

    boolean[] bloom1;
    boolean[] bloom2;
    boolean[] bloom3;

    public BloomFilter(int v1, int v2) {
        bloom1 = new boolean[v1];
        bloom2 = new boolean[v2];
        for (int i = 0; i < bloom1.length; i++) {
            bloom1[i] = false;
        }
        for (int i = 0; i < bloom2.length; i++) {
            bloom2[i] = false;
        }
    }

    public BloomFilter(int v1, int v2, int v3) {
        this(v1, v2);
        bloom3 = new boolean[v3];
        for (int i = 0; i < bloom3.length; i++) {
            bloom3[i] = false;
        }
    }

    public void turnOn(int v1, int v2) {
        bloom1[v1] = true;
        bloom2[v2] = true;
    }

    public void turnOn(int v1, int v2, int v3) {
        turnOn(v1, v2);
        if (v3 >= 0) {
            bloom3[v3] = true;
        }
    }

    public boolean contains(int v1, int v2) {
        return bloom1[v1] && bloom2[v2];
    }

    public boolean contains(int v1, int v2, int v3) {
        if (v3 < 0) {
            return contains(v1, v2);
        }
        return contains(v1, v2) && bloom3[v3];
    }

    public static void main(String[] args) {
        boolean[] bArr = new boolean[1000000000];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = true;
        }
    }
}
