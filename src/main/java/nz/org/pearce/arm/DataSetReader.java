package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class DataSetReader {

  public static DataSetReader open(String path, Itemizer itemizer) throws IOException {
    return new DataSetReader(new BufferedReader(new FileReader(path)), itemizer);
  }

  private DataSetReader(BufferedReader reader, Itemizer itemizer) {
    this.reader = reader;
    this.itemizer = itemizer;
  }

  private HashSet<Integer> nextAsSet() {
    String line = null;
    try {
      line = reader.readLine();
    } catch (IOException exception) {
      return null;
    }
    if (line == null) {
      return null;
    }
    String[] strings = line.split(",");
    HashSet<Integer> itemset = new HashSet<Integer>();
    for (int i = 0; i < strings.length; i++) {
      itemset.add(itemizer.idOf(strings[i].trim()));
    }
    return itemset;
  }

  public int[] next() {
    HashSet<Integer> itemset = nextAsSet();
    if (itemset == null) {
      return null;
    }
    int[] items = itemset.stream().mapToInt(i -> i).toArray();
    return items;
  }

  public int[] nextAboveMinCount(int minCount, ItemCounter itemCounter) {
    HashSet<Integer> itemset = nextAsSet();
    if (itemset == null) {
      return null;
    }
    ArrayList<Integer> frequentItems = new ArrayList<Integer>();
    for (int item : itemset) {
      if (itemCounter.count(item) >= minCount) {
        frequentItems.add(item);
      }
    }
    // Sort by decreasing order of frequency.
    frequentItems.sort(((a, b) -> itemCounter.count(a) - itemCounter.count(b)));
    return frequentItems.stream().mapToInt(i -> i).toArray();
  }

  private BufferedReader reader;
  private Itemizer itemizer;
}