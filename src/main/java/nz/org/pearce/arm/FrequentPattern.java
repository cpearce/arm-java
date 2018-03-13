package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.Arrays;

class FrequentPattern {

  FrequentPattern(ArrayList<Integer> path, int count) {
    this.itemset = path.stream().mapToInt(i -> i).toArray();
    Arrays.sort(this.itemset);
    this.count = count;
  }

  public int[] itemset;
  public int count;

  public String toString() {
    StringBuffer s = new StringBuffer();
    boolean first = true;
    s.append("[");
    for (int item : itemset) {
      if (!first) {
        s.append(",");
        first = false;
      }
      s.append(item);
    }
    s.append("] count=" + count);
    return s.toString();
  }
}