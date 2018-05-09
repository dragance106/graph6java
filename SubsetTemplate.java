/**
 * Template for selecting a subset of graphs that satisfy a given condition.
 *
 * How to use the template:
 * In your OS:
 * - make a copy of the whole framework folder in a new location
 * - copy (or generate) required sets of graphs to the same folder
 * 
 * In BlueJ:
 * - modify the template according to your needs
 * - right-click on the template, select 'New SubsetTemplate' and press Enter
 * - at the bottom of the window, right-click a newly created instance of the template
 *        and select 'run(String inputFileName)'
 * - in a dialog that appears, enter the filename of the graph set 
 *        within quotation marks "" (inputFileName), and
 *        0 if you do NOT want to create Graphviz .dot files,
 *        1 if you do want to create Graphviz .dot files for further visualisation
 * - study your results!
 */
import java.io.*;

public class SubsetTemplate {
    // Variables needed to run the template
    private String g6code;          // g6code of a graph
    private Graph g;                // graph
    
    // Other required variables are declared here:
    // For integral graphs example:
    private double[] eigs;          // eigenvalues
    
    // Files
    private BufferedReader in;      // input file with graphs
    private PrintWriter outResults; // output file for selected graphs and other data

    // graph counter, useful for occassionally printing the number of graphs processed so far
    private static int counter;
    
    public SubsetTemplate() {
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
        outResults = new PrintWriter(new BufferedWriter(new FileWriter(inputFileName + ".results.tex")));
        
        // Strings, arrays and other objects need to be created with "new" keyword.
        // For arrays, one has to specify type and dimensions as well.
        g6code = new String();
        
        while ((g6code = in.readLine())!=null) {  // Loading g6 codes until an empty line is found
            g = new Graph(g6code);                // Create a graph out of its g6 code
            
            // Calculate necessary invariants here:
            eigs = g.Aspectrum();                 // For integral graphs example
            
            // Write a criterion to select a graph into the subset here:
            int integral = 1;                     // Instead of just: if (g.Aintegral())
            for (int i=0; i<g.n(); i++)           //                      integral=0;
                 if (!DoubleUtil.equals(eigs[i], Math.round(eigs[i]))) {
                     integral = 0;
                     break;
                 }
                
            // Output selected graphs and other data to the output file here:
            if (integral==1) {                    // For integral graphs example
                outResults.println(g6code);       // output g6code and eigenvalues
                outResults.println("Eigenvalues: ");
                for (int i=0; i<g.n(); i++)
                     outResults.printf("%.6f ", eigs[i]);
                outResults.println();
                
                // export graph in Graphviz format for later visualisation
                if (createDotFiles!=0) {
                    StringBuilder data = new StringBuilder("eigenvalues=[");
                    for (int i=0; i<g.n(); i++) {   // create string representation of spectrum
                        data.append(eigs[i]);
                        if (i<g.n()-1)
                            data.append(", ");
                        else
                            data.append("]");
                    }
                    
                    g.saveDotFormat("integral-n-" + g.n() + "-g6code-" + g6code + ".dot", 
                                    data.toString());
                }
            }
            
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
        new SubsetTemplate().run(args[0], Integer.decode(args[1]));
    }
}