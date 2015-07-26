/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.util;

/**
 *
 * @author lelightwin This is a two-dimension matrix of CustomizeHashMap
 */
public class HashMatrix<T> {

    private CustomizeHashMap<T>[][] mapper;

    public HashMatrix(int n1, int n2) {
        mapper = new CustomizeHashMap[n1][n2];
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                mapper[i][j] = new CustomizeHashMap<>();
            }
        }
    }
    
    public CustomizeHashMap<T> getMap(int i, int j){
        return mapper[i][j];
    }

    public int update(int i, int j, T key, int value) {
        return mapper[i][j].updateIfNotExist(key, value);
    }

    public Integer get(int i, int j, T key) {
        return mapper[i][j].get(key);
    }
    
    public void put(int i, int j, T key, int value){
        mapper[i][j].put(key, value);
    }

    public static void main(String[] args) {
    }

    /**
     * @return the mapper
     */
    public CustomizeHashMap<T>[][] getMapper() {
        return mapper;
    }
}
