/**
 * DoubleMap.java
 * 
 * This class enables forming a collection of graphs that are indexed by double valued keys.
 * These keys are actually values of the invariant for which we are interested to find 
 * extremal graphs or pairs of graphs with (approximately) the same value of the invariant.
 * This class relies on DoubleUtil class to properly handle the keys that are approximately equal.
 * 
 * Since different graphs often come up with the same key(=invariant) values,
 * entries of the large collection are actually pairs of a key and an additional collection
 * that carries all graphs with that given key value.
 * 
 * Since this map will be used to keep all graphs from a file within memory,
 * values kept in the map are g6codes only (String) and not Graph objects.
 * Reasoning is that g6codes take only a fraction of the memory compared to Graph objects,
 * and Graph object can easily be reconstructed from its g6code.
 * 
 * @author Vladimir Brankov (original version), Dragan Stevanovic (updated Vector version)
 */

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class DoubleMap extends TreeMap<Double, Vector<String>> {

    /**
     * Retrieves a vector collection of g6codes whose keys differ from the provided key
     * by less than {@see DoubleUtil.DOUBLE_EQUALITY_THRESHOLD}.
     * 
     * @param key The provided key.
     * @return Vector collection of g6codes whose keys are approximately equal to the provided key.
     */
    public Vector<String> getEqualGraphs(Double key) {
        Vector<String> graphs = new Vector<String>();
        for(Iterator iter = getEqualKeys(key).iterator(); iter.hasNext(); )
            graphs.addAll(super.get(iter.next()));
        return graphs;
    }
    
    /**
     * Responds whether the collection already contains keys 
     * that are approximately equal to the provided key.
     * 
     * @param key Double value that we are checking for existence.
     * @return boolean True if the collection already contains an approximately equal key.
     */
    public boolean containsEqualKeys(Double key) {
        return getEqualKeys(key).size() > 0;
    }
    
    /**
     * Retrieves the set of keys from the collection, 
     * which differ from the provided key less than {@see DoubleUtil.DOUBLE_EQUALITY_THRESHOLD}.
     * 
     * @param key The provided key.
     * @return Set The set of keys in the collection that are approximately equal to the provided one.
     */
    public Set getEqualKeys(Double key) {
        Double startKey = new Double(key.doubleValue() - DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
        Double  endKey  = new Double(key.doubleValue() + DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
        return subMap(startKey, true, endKey, true).keySet();
    }
    
    /**
     * Puts a g6code and its corresponding key into the map.
     * Keys are organised so that they are approximately equal in the sense that
     * if a map already contains approximately equal key, 
     * then a new value is not put under its key, but under the approximate key found in the map.
     * 
     * @see java.util.Map#put(Object, Object)
     */
    public Vector<String> put(Double key, String value) {
        if (containsEqualKeys(key)) {
            Double startKey = new Double(key.doubleValue() - DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
            Double  endKey  = new Double(key.doubleValue() + DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
            
            Double approxKey = subMap(startKey, true, endKey, true).firstKey();
            Vector vec = get(approxKey);
            vec.add(value);
            return vec;
        }
        else {
            Vector vec = new Vector();
            vec.add(value);
            super.put(key, vec);
            return vec;
        }
    }
}