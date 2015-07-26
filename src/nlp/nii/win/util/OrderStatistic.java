/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.util;

import java.util.Random;

/**
 *
 * @author lelightwin
 * @param <T>
 */
public class OrderStatistic<T extends Comparable<T>> {

    /**
     * swap ith and jth elements of array
     *
     * @param array
     * @param i
     * @param j
     */
    private void swap(T[] array, int i, int j) {
        T median = array[i];
        array[i] = array[j];
        array[j] = median;
    }

    public int partition(T[] array, int first, int last) {
        T pivot = array[first];
        int pivotPosition = first++;
        while (first <= last) {
            // scan for values less than the pivot
            while ((first <= last) && (array[first].compareTo(pivot)) < 0) {
                first++;
            }

            // scan for values greater than the pivot
            while ((last >= first) && (array[last].compareTo(pivot) >= 0)) {
                last--;
            }

            if (first > last) {
                // swap the last uncoformed 
                // element with the pivot
                swap(array, pivotPosition, last);
            } else {
                // swap unconformed elements:
                // first that was not lesser than the pivot 
                // and last that was not larger than the pivot
                swap(array, first, last);
            }
        }
        return last;
    }

    private int orderStatistic(T[] array, int k, int first, int last) {

        int pivotPosition = partition(array, first, last);

        while (pivotPosition != k - 1) {
            if (k - 1 < pivotPosition) {
                last = pivotPosition - 1;
            } else {
                first = pivotPosition + 1;
            }

            pivotPosition = partition(array, first, last);
        }
        return k - 1;
    }

    public int kthSmallest(T[] array, int k) {
        return orderStatistic(array, k, 0, array.length - 1);
    }

    public int kthLargest(T[] array, int k) {
        return orderStatistic(array, array.length - k + 1, 0, array.length - 1);
    }

    public static void main(String[] args) {
        Random r = new Random();

        Integer[] a = new Integer[20];
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(80);
        }

        OrderStatistic<Integer> orderer = new OrderStatistic<>();
        long start = System.currentTimeMillis();

        int pivot = orderer.kthLargest(a, 1);
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+" ");
        }
        System.out.println("");
        for (int j = pivot; j < a.length; j++) {
            System.out.print(a[j]+" ");
        }
        System.out.println("");

        long end = System.currentTimeMillis();

        System.out.println("time: " + (end - start) / 1000f);
    }
}
