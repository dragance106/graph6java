import java.io.*;
public class Broom extends Graph
{
    public Broom(int a, int b) {
        int[][] A = new int[a+b][a+b];
        
        for (int i=0; i<a-1; i++) {
            A[i][i+1]=1;
            A[i+1][i]=1;
        }
        
        for (int i=a; i<a+b; i++) {
            A[a-1][i]=1;
            A[i][a-1]=1;
        }
        
        initializeGraph(A);
    }
    
    public static void testBrooms(int n) throws IOException
    {
        PrintWriter outResults = new PrintWriter(new BufferedWriter(new FileWriter("brooms-" + n + "-fiedler.tex")));
        outResults.println("a, b, Fiedler vector");

        for (int a=3; a<=n-2; a++) {
            int b=n-a;
            Broom g = new Broom(a,b);
            double[] fv = g.fiedlerVector();
            
            for (int i=0; i<fv.length; i++)
                if (DoubleUtil.equals(fv[i],0))
                    System.out.println("A zero component with a=" + a + ", b=" + b + " and i=" + i);

            outResults.println("" + a + ", " + b + ", " + Graph.printVector(fv));
        }
        
        outResults.close();
    }

    public static void testBroomsForZero()
    {
        int zeroFound=0;
        
        for (int n=10; n<=100; n++) {
            for (int a=3; a<=n-2; a++) {
                int b=n-a;
                Broom g = new Broom(a,b);
                double[] fv = g.fiedlerVector();
            
                for (int i=0; i<fv.length; i++)
                    if (DoubleUtil.equals(fv[i],0)) {
                        System.out.println("A zero component with a=" + a + ", b=" + b + " and i=" + i);
                        zeroFound=1;
                    }
            }
        }
        
        if (zeroFound==0)
            System.out.println("No zero component found.");
    }
    
}
