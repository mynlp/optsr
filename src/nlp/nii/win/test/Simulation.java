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
public class Simulation {

    protected double result;			/*result*/

    protected double error;				/*error in percent*/

    protected int nk;

    double getResult() {
        return result;
    }

    double getError() {
        return error;
    }

    Simulation(int nk) {
        this.nk = nk;
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
                x = Math.random();
                y = Math.random();

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
        double sum = 0;
        int ns = 24;   /*number of computations*/

        /*sample 50,000 x 1,000 points*/
        Simulation sim = new Simulation(50000);

        for (int i = 0; i < ns; i++) {
            sim.computePI();
            sum += sim.getError();
        }

        System.out.printf("Average error is %g\n", sum / ns);
    }
}
