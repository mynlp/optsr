/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.test;

/**
 *
 * @author lelightwin
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadSimulation extends Thread {

    protected double result;			/*result*/

    protected double error;				/*error in percent*/

    protected int nk;
    Random rnd;

    double getResult() {
        return result;
    }

    double getError() {
        return error;
    }

    ThreadSimulation(int nk, String name, ThreadGroup tg) {
        super(tg, name);
        this.nk = nk;
        rnd = new java.util.Random();
    }

    public void run() {
        computePI();
    }

    /*CalculatesPI by taking nk*1000 sample points*/
    void computePI() {
        double x, y;
        double r;
        int i, j = 0;
        int count = 0;

        for (i = 0; i < nk; i++) {
            for (j = 0; j < 1000; j++) {
                /*select random point*/
                x = rnd.nextDouble();
                y = rnd.nextDouble();

                r = Math.sqrt(x * x + y * y);
                if (r <= 1.0) {
                    count++;
                }
            }
        }

        result = 4 * count / (double) (nk * j);
        error = 100 * Math.abs(result - Math.PI) / Math.PI;
        System.out.printf(" nk = %d:\t pi = %g,\t error = %.2g%%\n", nk, result, error);
    }

    public static void main(String[] args) {
        ThreadGroup tg = new ThreadGroup("main");
        int np = Runtime.getRuntime().availableProcessors();
        int i, ns = 24;

        List<ThreadSimulation> sims = new ArrayList<>();

        long start = System.nanoTime();

        for (i = 0; i < ns; i++) {
            sims.add(new ThreadSimulation(50000, "PI" + i, tg));
        }

        i = 0;
        while (i < sims.size()) {
            /*do we have available CPUs?*/
            if (tg.activeCount() < np) {
                ThreadSimulation sim = sims.get(i);
                sim.start();
                i++;
            } else {
                try {
                    Thread.sleep(100);
                } /*wait 0.1 second before checking again*/ catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /*wait for threads to finish*/
        while (tg.activeCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*sum up errors*/
        double sum = 0;
        for (i = 0; i < sims.size(); i++) {
            ThreadSimulation sim = sims.get(i);
            sum += sim.getError();
        }

        long end = System.nanoTime();

        System.out.printf("Average error is %g\n", sum / sims.size());
        System.out.printf("Simulation took %.2g seconds\n", (double) (end - start) / 1e9);
    }
}
