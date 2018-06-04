/**
 * Template for finding groups of graphs 
 * with (approximately) the same value of invariant expression in a set of graphs.
 *
 * How to use the template:
 * In your OS:
 * - make a copy of the whole framework folder in a new location
 * - copy (or generate) required sets of graphs to the same folder
 * 
 * In BlueJ:
 * - modify the template according to your needs
 * - right-click on the template, select 'New EquiTemplate' and press Enter
 * - at the bottom of the window, right-click a newly created instance of the template
 *        and select 'run(String inputFileName)'
 * - in a dialog that appears, enter the filename of the graph set 
 *        within quotation marks "" (inputFileName)
 * - study your results!
 */
import java.util.Iterator;
import java.util.Vector;
import java.util.Set;
import java.util.NavigableSet;

import java.io.*;

public class EquiTemplate {
    // Basic variables needed for the template
    private String g6code;          // g6code of a graph
    private Graph g;                // graph    
    private Double key;             // key is value of invariant expression
    private DoubleMap map;          // map containing graphs with their keys 
        
    // Files
    private BufferedReader in;      // input file with graphs
    private PrintWriter outResults; // output file for selected graphs and other data

    // graph counter, useful for occassionally printing the number of graphs processed so far
    private static int counter;
    
    public EquiTemplate() {
    }
        
    /**
     *  The main method whose argument inputFileName
     *  points to a file containing graphs in g6 format
     */
    public void run(String inputFileName) throws IOException {
        long startTime = System.currentTimeMillis();               // Take a note of starting time
        counter = 0;                                               // Initialise counter

        in = new BufferedReader(new FileReader(inputFileName));    // Open input and output files
        outResults = new PrintWriter(new BufferedWriter(new FileWriter(inputFileName + ".results.tex")));
        
        // Strings, arrays and other objects need to be created here with "new" keyword.
        // For arrays, one has to specify type and dimensions as well.
        g6code = new String();
        map = new DoubleMap();

        while ((g6code = in.readLine())!=null) {   // Loading g6 codes until an empty line is found
            g = new Graph(g6code);                 // Create a graph out of its g6 code

            // Calculate necessary invariant here and make it the key:
            key = new Double(g.energy());          // for equienergetic example

            map.put(key, g6code);                  // put graph's key and g6code into the map
                                                   // map contains g6codes only to save memory
            counter++;                             // Update counter and report progress
            if (counter % 10000 == 0)
                System.out.println("" + counter + " graphs processed so far");
        }
        
        // Report groups of graphs with (approximately) equal key values
        Iterator it = map.navigableKeySet().iterator();        

        while (it.hasNext()) {                     // Browse through the whole map    
            Double key = (Double) it.next();       // get key of the current group
            Vector<String> codes = map.get(key);   // get g6codes of the current group

            if (codes.size() >= 2) {               // interesting groups have >=2 graphs
                // Report the value of the key
                outResults.println("Energy " + key + " held by graphs:");
    
                Iterator codesIt = codes.iterator(); 
                int localcounter = 1;
                
                while (codesIt.hasNext()) {             // Browse through g6 codes
                    g6code = (String) codesIt.next();
                    outResults.println(g6code);         // Report the g6 code
                    
                    // export graph in Graphviz format for later visualisation
                    g = new Graph(g6code);
                    g.saveDotFormat("equi-energy-n-" + g.n() + "-energy-" + key + "-count-" + localcounter + ".dot", 
                                    "energy="+key);                  
                    localcounter++;                     // Update for the next graph in group
                }
            }
        }
        
        in.close();                             // Testing done, close the files
        outResults.close();
        
        long totalTime = System.currentTimeMillis() - startTime;     // Report elapsed time
        System.out.println("Time elapsed: " + 
            (totalTime / 60000) + " min, " + ((double) (totalTime % 60000) / 1000) + " sec");
    }
    
    // This function may be used to run the template from out of BlueJ
    public static void main(String[] args) throws IOException, NumberFormatException {
        new EquiTemplate().run(args[0]);
    }
}