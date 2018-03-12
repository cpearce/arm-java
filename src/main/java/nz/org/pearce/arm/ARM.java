package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;

public class ARM {
  public static void main(String[] args) {

    final double minimumSupport = 0.05;

    DataSetReader reader;
    Itemizer itemizer = new Itemizer();
    try {
      reader = DataSetReader.open(args[0], itemizer);
    } catch (IOException e) {
      System.err.println("Can't open " + args[0] + " for input");
      System.exit(1);
      return;
    }

    // Make one pass to count the item frequencies.
    ItemCounter itemCounter = new ItemCounter();
    int[] itemset = null;
    int numTransactions = 0;
    while ((itemset = reader.next()) != null) {
      for (int item : itemset) {
        itemCounter.increment(item);
      }
      numTransactions++;
    }

    System.out.println("Read " + numTransactions);

    // Make another pass to build the FPTree.
    final int minCount = (int) (numTransactions * minimumSupport);
    try {
      reader.reset();
    } catch (IOException e) {
      System.err.println("Can't reset " + args[0] + " for input");
      System.exit(1);
      return;
    }

    while ((itemset = reader.nextAboveMinCount(minCount, itemCounter)) != null) {
      // TODO: Insert into FPTree.
    }

  }
}
