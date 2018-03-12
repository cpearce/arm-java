package nz.org.pearce.arm;

import java.util.HashMap;

public class ItemCounter {
  public void increment(int item) {
    counter.put(item, count(item) + 1);
  }

  public int count(int item) {
    Integer count = counter.get(item);
    return (count == null) ? 0 : count;
  }

  private HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();
}