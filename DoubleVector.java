/**
 * Class enabling the use of double[] arrays (vectors) as keys in a map,
 * necessary for finding cospectral graphs, for example.
 *
 * @author Dragan Stevanovic
 * @version July 19, 2018
 */
import java.util.Vector;

public class DoubleVector extends Vector<Double> implements Comparable<DoubleVector>
{
    public DoubleVector(DoubleVector c) {
        super(c);
    }
    
    public DoubleVector(double[] key) {
        for (int i=0; i<key.length; i++)
            this.add(new Double(key[i]));
    }
    
    /* Adds off to each entry of the vector. */
    public void offset(double off)
    {
        for (int i=0; i<size(); i++) {
            Double e = get(i);
            Double enew = new Double(e.doubleValue() + off);
            set(i, enew);
        }
    }
        
    public int compareTo(DoubleVector o) 
    {
        int c1=this.size(), c2=o.size(), c=Math.min(c1,c2);
        
        for (int i=0; i<c; i++) {
            int res = DoubleUtil.compareTo(this.get(i).doubleValue(), o.get(i).doubleValue());
            if (res==0)
                continue;
            else
                return res;
        }

        // no more common coordinates
        if (c1==c2) 
            return 0;
        else if (c1<c2)
            return -1;
        else 
            return 1;
    }
    
    public String toString(String delims) {
        StringBuffer buf = new StringBuffer("");
        
        buf.append(delims.charAt(0));
        for (int i=0; i<size(); i++) {
            buf.append("" + this.get(i).doubleValue());
            if (i!=size()-1)    // was it the last entry?
                buf.append(delims.charAt(1));
        }
        buf.append(delims.charAt(2));
        
        return buf.toString();
    }

    public String toString() {
        return toString("[,]");
    }
}
