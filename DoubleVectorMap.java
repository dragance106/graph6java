/**
 * DoubleArrayMap.java
 * 
 * This class extends functionality of DoubleMap by allowing keys to be double arrays,
 * so that one could use EquiTemplate to search for pairs of cospectral graphs,
 * for example.
 * 
 * For the remaining description, see DoubleMap.java.
 * 
 * @author Dragan Stevanovic
 */

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class DoubleVectorMap extends TreeMap<DoubleVector, Vector<String>> {

    /**
     * Retrieves a vector collection of g6codes whose keys differ from the provided key
     * by less than {@see DoubleUtil.DOUBLE_EQUALITY_THRESHOLD}.
     * 
     * @param key The provided key.
     * @return Vector collection of g6codes whose keys are approximately equal to the provided key.
     */
    public Vector<String> getEqualGraphs(DoubleVector key) {
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
    public boolean containsEqualKeys(DoubleVector key) {
        return getEqualKeys(key).size() > 0;
    }
    
    /**
     * Retrieves the set of keys from the collection, 
     * which differ from the provided key less than {@see DoubleUtil.DOUBLE_EQUALITY_THRESHOLD}.
     * 
     * @param key The provided key.
     * @return Set The set of keys in the collection that are approximately equal to the provided one.
     */
    public Set getEqualKeys(DoubleVector key) {
        DoubleVector startKey = new DoubleVector(key);
        startKey.offset(-DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
        DoubleVector endKey = new DoubleVector(key);
        endKey.offset(+DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
                
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
    public Vector<String> put(DoubleVector key, String value) {
        if (containsEqualKeys(key)) {
            DoubleVector startKey = new DoubleVector(key);
            startKey.offset(-DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
            DoubleVector endKey = new DoubleVector(key);
            endKey.offset(+DoubleUtil.DOUBLE_EQUALITY_THRESHOLD);
        
            DoubleVector approxKey = subMap(startKey, true, endKey, true).firstKey();
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