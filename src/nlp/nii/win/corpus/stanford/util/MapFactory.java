package nlp.nii.win.corpus.stanford.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 * The MapFactory is a mechanism for specifying what kind of map is to be used
 * by some object. For example, if you want a Counter which is backed by an
 * IdentityHashMap instead of the defaul HashMap, you can pass in an
 * IdentityHashMapFactory.
 *
 * @author Dan Klein
 */
public abstract class MapFactory<K, V> implements Serializable {

    public static class HashMapFactory<K, V> extends MapFactory<K, V> {

        private static final long serialVersionUID = 1L;

        @Override
        public Map<K, V> buildMap() {
            return new HashMap<K, V>();
        }
    }

    public static class IdentityHashMapFactory<K, V> extends MapFactory<K, V> {

        private static final long serialVersionUID = 1L;

        @Override
        public Map<K, V> buildMap() {
            return new IdentityHashMap<K, V>();
        }
    }

    public static class TreeMapFactory<K, V> extends MapFactory<K, V> {

        private static final long serialVersionUID = 1L;

        @Override
        public Map<K, V> buildMap() {
            return new TreeMap<K, V>();
        }
    }

    public static class WeakHashMapFactory<K, V> extends MapFactory<K, V> {

        private static final long serialVersionUID = 1L;

        @Override
        public Map<K, V> buildMap() {
            return new WeakHashMap<K, V>();
        }
    }

    public abstract Map<K, V> buildMap();
}
