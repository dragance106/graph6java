/**
 * Template for finding a predefined number of extremal graphs 
 * for a user-defined invariant expression in a set of graphs.
 * 
 * How to use the template:
 * In your OS:
 * - make a copy of the whole framework folder in a new location
 * - copy (or generate) required sets of graphs to the same folder
 * 
 * In BlueJ:
 * - modify the template in the source below according to your needs
 * - right-click on the template, select 'New ExtremalTemplate' and press Enter
 * - at the bottom of the window, right-click a newly created instance of the template
 *        and select 'run(String inputFileName, int extnum)'
 * - in a dialog that appears, enter the filename of graph set 
 *        within quotation marks "" (inputFileName)
 *        and the number of extremal values to be found (extnum)
 * - study your results!
 */
import java.util.Iterator;
import java.util.Vector;
import java.util.Set;
import java.util.NavigableSet;

import java.io.*;

public class ExtremalTemplate {
    // Variables needed for the template
    private String g6code;          // g6code of a graph
    private Graph g;                // graph    
    private DoubleMap map;          // map containing graphs with extremal keys 
    private Double key;             // key is value of invariant expression
    
    // Files
    private BufferedReader in;      // input file with graphs
    private PrintWriter outResults; // output file for selected graphs and other data

    // graph counter, useful for occassionally printing the number of graphs processed so far
    private static int counter;
    
    public ExtremalTemplate() {
    }
        
    /**
     *  The main method whose argument inputFileName
     *  points to a file containing graphs in g6 format,
     *  extnum is the number of extremal values of the invariant to be found (1 or more),
     *  and lookformax specifies whether the extremal values are maximal (>=0) or minimal (<0).
     */
    public void run(String inputFileName, int extnum, int lookformax) throws IOException {
        long startTime = System.currentTimeMillis();               // Take a note of starting time
        counter = 0;                                               // Initialise counter

        in = new BufferedReader(new FileReader(inputFileName));    // Open input and output files
        outResults = new PrintWriter(new BufferedWriter(new FileWriter(inputFileName + ".results.tex")));
        
        // Strings, arrays and other objects need to be created here with "new" keyword.
        // For arrays, one has to specify type and dimensions as well.
        g6code = new String();
        map = new DoubleMap();
        
        while ((g6code = in.readLine())!=null) {     // loading g6 codes until an empty line is found
            g = new Graph(g6code);                   // create a graph out of its g6 code
            
            // Calculate necessary invariant here and make it the key:
            key = new Double(g.dshi());              // for distance-sum heterogeneity example
            
            // Graphs are put into map if they are more "extremal" than those currently in it
            if (map.size()<extnum)             // If we still haven't seen at least extnum keys
                map.put(key, g6code);          // then insert graph's g6code and its key into map
            else {
                if (map.containsEqualKeys(key)) {  // If the key is already in the map
                    map.put(key, g6code);          // then you should definitely insert this g6code
                }
                else {
                    // Otherwise, insert it if it's better than the worst key in the map.
                    // Since the present key represents a truly new key in the map, 
                    // delete the previously worst key from the map (to ensure extnum keys).
                    
                    if (lookformax<0) {   // we're looking for minimal values
                        Double lastKey = map.lastKey();      // largest key in the map
                        if (key.doubleValue() < lastKey.doubleValue()) {
                           map.remove(lastKey);
                           map.put(key, g6code);
                        }
                    }
                    else {    // we're looking for maximal values
                        Double firstKey = map.firstKey();       // smallest key in the map
                        if (key.doubleValue() > firstKey.doubleValue()) {
                            map.remove(firstKey);
                            map.put(key, g6code);
                        }
                    }
                }
            }

            counter++;                    // Update counter and report progress
            if (counter % 10000 == 0)
                System.out.println("" + counter + " graphs processed so far");
        }
        
        // Report on the number of extremal keys found (may be occasionally less than extnum)
        outResults.println("" + map.size() + " extremal values are achieved for:");

        for (Iterator it = map.navigableKeySet().iterator(); it.hasNext(); ) {
             key = (Double) it.next();                              // Browse the map of keys
             outResults.println("Following graphs have key=" + key);     // Report the key
             
             // Note that the map entry corresponding to a given key is
             // actually a collection (vector) of g6codes with that key
             // (even if there is only one graph corresponding to that key).
             // Also pay attention that keys in the map are listed in increasing order,
             // hence if you are looking for the maximum value, 
             // look at the end of results file!
             Vector<String> vec = map.get(key);     // Collection of graphs with this key
             int localcounter=1;
             
             for (Iterator vecIt = vec.iterator(); vecIt.hasNext(); ) {
                 g6code = (String) vecIt.next();    // Browse the collection
                 outResults.println(g6code);        // Output g6codes below the key
                 
                 // export graph in Graphviz format for later visualisation
                 g = new Graph(g6code);
                 g.saveDotFormat("maxdshi-n-" + g.n() + "-dshi-" + key + "-count-" + localcounter + ".dot", "dshi="+key);

                 localcounter++;
                 counter++;
             }
        }
        
        in.close();                             // Testing done, close the files
        outResults.close();
        
        long totalTime = System.currentTimeMillis() - startTime;        // Report elapsed time
        System.out.println("Time elapsed: " + 
            (totalTime / 60000) + " min, " + ((double) (totalTime % 60000) / 1000) + " sec");
    }
    
    // This function may be used to run the template from out of BlueJ
    public static void main(String[] args) throws IOException, NumberFormatException {
        new ExtremalTemplate().run(args[0], Integer.decode(args[1]), Integer.decode(args[2]));
    }
    
    public static void automateMe() throws IOException, NumberFormatException {
        ExtremalTemplate E = new ExtremalTemplate();
        for (int m=9; m<=45; m++)
            E.run("graph10cm" + m + ".g6", 1, 1);
    }
}