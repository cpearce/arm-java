package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemCounter {
  public void increment(int item) {
    increment(item, 1);
  }

  public void increment(int item, int count) {
    counter.put(item, count(item) + count);
  }

  public int count(int item) {
    Integer count = counter.get(item);
    return (count == null) ? 0 : count;
  }

  public ArrayList<Integer> itemsWithCountAtLeast(int minCount) {
    ArrayList<Integer> items = new ArrayList<Integer>();
    counter.forEach((item, count)->{
      if (count >= minCount) {
        items.add(item);
      }
    });
    return items;
  }

  private HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();
}