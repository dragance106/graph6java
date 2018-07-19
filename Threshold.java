import java.io.*;
public class Threshold extends Graph
{
    public Threshold(String s) {
        int[] bits = new int[s.length()];
        
        for (int i=0; i<s.length(); i++)
            if (s.charAt(i)=='0')
                bits[i]=0;
            else
                bits[i]=1;
                
        initializeFromBits(bits);
    }
    
    public Threshold(int[] bits) {
        initializeFromBits(bits);
    }
    
    // Assumes that bits[] contains only zeros and ones
    public void initializeFromBits(int[] bits) {
        int n = bits.length + 1;
        int[][] A = new int[n][n];
        
        for (int i=0; i<n; i++)
            A[i][i]=0;
        
        for (int i=1; i<n; i++)
            for (int j=0; j<i; j++) {
                A[i][j] = bits[i-1];
                A[j][i] = bits[i-1];
            }
            
        initializeGraph(A);
    }
    
    public static void run() throws IOException {
        // Variables needed to run the template
        Threshold g;                // graph
    
        // Files
        PrintWriter outResults; // output file for selected graphs and other data
        long startTime = System.currentTimeMillis();               // Take a note of starting time
        
        outResults = new PrintWriter(new BufferedWriter(new FileWriter("threshold10.csv")));
        outResults.println("bit sequence, spectral radius, principal eigenvector");             // Header line of .csv file

        int[] bits = new int[9];
        for (bits[0]=0; bits[0]<=1; bits[0]++)
        for (bits[1]=0; bits[1]<=1; bits[1]++)
        for (bits[2]=0; bits[2]<=1; bits[2]++)
        for (bits[3]=0; bits[3]<=1; bits[3]++)
        for (bits[4]=0; bits[4]<=1; bits[4]++)
        for (bits[5]=0; bits[5]<=1; bits[5]++)
        for (bits[6]=0; bits[6]<=1; bits[6]++)
        for (bits[7]=0; bits[7]<=1; bits[7]++)
        for (bits[8]=0; bits[8]<=1; bits[8]++) {
            g = new Threshold(bits);
            double sprad = g.Aspectrum()[9];
            double[][] eigenvectors = g.Aeigenvectors();
            
            // bit sequence
            for (int i=0; i<9; i++)
                outResults.print(bits[i]);
            outResults.print(", " + sprad + ", [");
            for (int i=0; i<10; i++)
                outResults.printf("%.5f ", eigenvectors[i][9]);
            outResults.println("]");
        }

        outResults.close();
        
        long totalTime = System.currentTimeMillis() - startTime;    // Report elapsed time
        System.out.println("Time elapsed: " + 
            (totalTime / 60000) + " min, " + ((double) (totalTime % 60000) / 1000) + " sec");
    }
}
