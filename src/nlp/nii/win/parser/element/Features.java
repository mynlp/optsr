/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

/**
 *
 * @author lelightwin
 */
public class Features {

    public static Feature from(int... values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 0) {
                return null;
            }
        }
        return new Feature(values);
    }

    public static Feature from(String s) {
        int v1, v2, v3;
        String[] datas = s.split(" ");
        if (datas.length == 1) {
            v1 = Integer.parseInt(datas[0]);
            return new Feature(v1);
        } else if (datas.length == 2) {
            v1 = Integer.parseInt(datas[0]);
            v2 = Integer.parseInt(datas[1]);
            return new Feature(v1, v2);
        } else if (datas.length == 3) {
            v1 = Integer.parseInt(datas[0]);
            v2 = Integer.parseInt(datas[1]);
            v3 = Integer.parseInt(datas[2]);
            return new Feature(v1, v2, v3);
        }
        return null;
    }
}
