package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsetSupport
{
  private HashMap<ArrayList<Integer>, Double> map =
    new HashMap<ArrayList<Integer>, Double>();

  public static ItemsetSupport make(ArrayList<FrequentPattern> patterns,
                                    int numTransactions)
  {
    ItemsetSupport supports = new ItemsetSupport();
    for (FrequentPattern pattern : patterns) {
      supports.map.put(toArrayList(pattern.itemset),
                       (double)pattern.count / (double)numTransactions);
    }
    return supports;
  }

  public double support(int[] items) { return map.get(toArrayList(items)); }

  private static ArrayList<Integer> toArrayList(int[] items)
  {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i : items) {
      list.add(i);
    }
    return list;
  }
}