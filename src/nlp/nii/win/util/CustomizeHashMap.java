/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.util;

import java.util.HashMap;

/**
 *
 * @author lelightwin
 */
public class CustomizeHashMap<T> extends HashMap<T, Integer> {

    /**
     *
     * @param key
     * @return
     */
    public int updateIfNotExist(T key) {
        Integer value = this.get(key);
        if (value == null) {
            value = this.size();
            this.put(key, size());
        }
        return value;

    }

    /**
     *
     * @param key
     * @param v
     * @return
     */
    public int updateIfNotExist(T key, int v) {
        Integer value = this.get(key);
        if (value == null) {
            value = v;
            this.put(key, v);
        }
        return value;
    }

    public void updateWithOffset(T key, int offset) {
        Integer value = this.get(key);
        if (value == null) {
            value = 0;
        }
        this.put(key, value + offset);
    }

    public static void main(String[] args) {
        CustomizeHashMap<String> map = new CustomizeHashMap();
        map.updateIfNotExist("thang");
        map.updateIfNotExist("quang");
        map.updateIfNotExist("noi");
        map.updateWithOffset("quang", 3);
        map.updateWithOffset("tong", 5);

        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
        }
    }
}
