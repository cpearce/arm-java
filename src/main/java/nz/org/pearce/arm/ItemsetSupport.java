package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ItemsetSupport
{
  private HashMap<Integer, Double> map = new HashMap<Integer, Double>();

  public static ItemsetSupport make(ArrayList<FrequentPattern> patterns,
                                    int numTransactions)
  {
    ItemsetSupport supports = new ItemsetSupport();
    for (FrequentPattern pattern : patterns) {
      supports.map.put(new Integer(Arrays.hashCode(pattern.itemset)),
                       (double)pattern.count / (double)numTransactions);
    }
    return supports;
  }

  public double support(int[] items) { return map.get(Arrays.hashCode(items)); }
}