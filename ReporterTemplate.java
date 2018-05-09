/**
 * Template for reporting values of invariants for a set of graphs.
 *
 * How to use the template:
 * In your OS:
 * - make a copy of the whole framework folder in a new location
 * - copy (or generate) required sets of graphs to the same folder
 * 
 * In BlueJ:
 * - modify the template according to your needs
 * - right-click on the template, select 'New ReporterTemplate' and press Enter
 * - at the bottom of the window, right-click a newly created instance of the template
 *        and select 'run(String inputFileName)'
 * - in a dialog that appears, enter the filename of the graph set 
 *        within quotation marks "" (inputFileName), and
 *        0 if you do NOT want to create Graphviz .dot files,
 *        1 if you do want to create Graphviz .dot files for further visualisation
 * - study your results!
 */
import java.io.*;

public class ReporterTemplate {
    // Variables needed to run the template
    private String g6code;          // g6code of a graph
    private Graph g;                // graph
    
    // Other required variables are declared here:
    // For energy and nullity example:
    private double energy;
    private double[] eigs;
    private int nullity;
    
    // Files
    private BufferedReader in;      // input file with graphs
    private PrintWriter outResults; // output file for selected graphs and other data

    // graph counter, useful for occassionally printing the number of graphs processed so far
    private static int counter;
    
    public ReporterTemplate() {
    }
        
    /** 
     * The main method whose argument inputFileName
     * points to a file containing graphs in g6 format,
     * while createDotFiles instructs whether to write Graphviz .dot files for g6codes
     */
    public void run(String inputFileName, int createDotFiles) throws IOException {
        long startTime = System.currentTimeMillis();               // Take a note of starting time
        counter = 0;                                               // Initialise counter
        
        in = new BufferedReader(new FileReader(inputFileName));    // Open input and output files
        outResults = new PrintWriter(new BufferedWriter(new FileWriter(inputFileName + ".results.csv")));
        outResults.println("g6code, energy, nullity");             // Header line of .csv file

        // Strings, arrays and other objects need to be created with "new" keyword.
        // For arrays, one has to specify type and dimensions as well.
        g6code = new String();
        
        while ((g6code = in.readLine())!=null) {  // Loading g6 codes until an empty line is found
            g = new Graph(g6code);                // Create a graph out of its g6 code
            
            // Calculate necessary invariants here:
            energy = g.energy();                  // energy is provided in Graph class

            eigs = g.Aspectrum();                 // eigenvalues of adjacency matrix
            nullity = 0;
            for (int i=0; i<g.n(); i++)           // how many eigenvalues are approximately 0?
                if (DoubleUtil.equals(eigs[i], 0.0))
                    nullity++;
                            
            // Output g6code and invariant values here:
            outResults.println(g6code + ", " + energy + ", " + nullity);

            // export graph in Graphviz format for later visualisation
            if (createDotFiles!=0)
                g.saveDotFormat("energy-nullity-n-" + g.n() + "-g6code-" + g6code + ".dot", 
                                "energy="+energy+", nullity="+nullity);

            counter++;                            // Update counter and report progress
            if (counter % 10000 == 0)
                System.out.println("" + counter + " graphs processed so far");
        }
        
        in.close();                              // Testing done, close the files
        outResults.close();
        
        long totalTime = System.currentTimeMillis() - startTime;    // Report elapsed time
        System.out.println("Time elapsed: " + 
            (totalTime / 60000) + " min, " + ((double) (totalTime % 60000) / 1000) + " sec");
    }
    
    // This function may be used to run the template from out of BlueJ
    public static void main(String[] args) throws IOException, NumberFormatException {
        new ReporterTemplate().run(args[0], Integer.decode(args[1]));
    }
}
