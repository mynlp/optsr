/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.test;

import java.util.Random;

/**
 *
 * @author lelightwin
 */
public class TestMultiThread {

    static final int len = 100000000;
    static final int threadNumber = 100;
    static int[] a = new int[len];
    static String[] b = new String[len];
    static String[] c = new String[len];

    public static void main(String[] args) throws InterruptedException {

        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(100);
        }
        int np = Runtime.getRuntime().availableProcessors();
        ThreadGroup tg = new ThreadGroup("main");

        Thread[] threads = new Thread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            int start = i * (len / threadNumber);
            int end = (i + 1) * (len / threadNumber) - 1;
            threads[i] = new Square(start, end, "s/e:" + start + "/" + end, tg);
        }

        long p1 = System.currentTimeMillis();
        int i = 0;
        while (i < threads.length) {
            if (tg.activeCount() < np) {
                threads[i].start();
                i++;
            } else {
                try {
                    Thread.sleep(10);
                } /*wait 0.1 second before checking again*/ catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
         while (tg.activeCount() > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long p2 = System.currentTimeMillis();

//        for (int k = 0; k < threadNumber; k++) {
//            int start = k * (len / threadNumber);
//            int end = (k + 1) * (len / threadNumber) - 1;
//            partition(start, end);
//        }
//        long p3 = System.currentTimeMillis();

        System.out.println((p2 - p1) / 1000f);
//        System.out.println((p3 - p2) / 1000f);
    }

    public static void partition(int i, int j) {
        for (int t = i; t < j; t++) {
            b[t] = "" + a[t] + " " + a[t] + " " + a[t] + " " + a[t];
        }
        System.out.println(i+" "+j);
    }

    static class Square extends Thread {

        int i, j;

        public Square(int i, int j, String name, ThreadGroup tg) {
            super(tg, name);
            this.i = i;
            this.j = j;
        }

        @Override
        public void run() {
            partition(i, j);
        }

    }
}
