/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.util;

import java.util.Stack;

/**
 *
 * @author lelightwin
 */
public class CustomizeStack<L> extends Stack<L> {

    public L peek(int k) {
        if (k <= size() - 1) {
            return elementAt(size() - 1 - k);
        }
        return null;
    }
    
    public static void main(String[] args) {
    }
}
