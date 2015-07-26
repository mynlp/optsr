/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import nlp.nii.win.util.CustomizeHashMap;

/**
 *
 * @author lelightwin
 */
public class Feature {

    private int value1 = -1;
    private int value2 = -1;
    private int value3 = -1;
//    private int index = -1;

    public Feature(int... values) {
        if (values.length >= 1) {
            this.value1 = values[0];
        }
        if (values.length >= 2) {
            this.value2 = values[1];
        }
        if (values.length >= 3) {
            this.value3 = values[2];
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.value1;
        hash = 83 * hash + this.value2;
        hash = 83 * hash + this.value3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Feature other = (Feature) obj;
        if (this.value1 != other.value1) {
            return false;
        }
        if (this.value2 != other.value2) {
            return false;
        }
        if (this.value3 != other.value3) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return toFeatValue();
    }

    public String toFeatValue() {
        String result = "";
        if (value1 >= 0) {
            result += value1 + " ";
        }
        if (value2 >= 0) {
            result += value2 + " ";
        }
        if (value3 >= 0) {
            result += value3 + " ";
        }
        return result.trim();
    }

    public static void main(String[] args) {
        CustomizeHashMap<Feature> map = new CustomizeHashMap();
        Feature f3 = Features.from(10, -1);
        map.updateIfNotExist(f3);

        Feature f6 = Features.from(10, -1);
        System.out.println(map.get(f6));
    }
}
