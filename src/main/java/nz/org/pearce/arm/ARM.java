package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;

public class ARM {


  public static void main(String[] args) {
    System.out.println("Association Rule Mining with Java");
    Arguments arguments = Arguments.parseOrDie(args);
    try {
      (new ARM()).run(arguments);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run(Arguments arguments) throws IOException {
    System.out.println("Input: " + arguments.inputPath);
    System.out.println("Output: " + arguments.outputPath);
    System.out.println("Minimum Support: " + arguments.minimumSupport);
    System.out.println("Minimum Confidence: " + arguments.minimumConfidence);
    System.out.println("Minimum Lift: " + arguments.minimumLift);
    System.out.println("");

    System.out.println("Counting item frequencies");
    Itemizer itemizer = new Itemizer();
    DataSetReader reader = DataSetReader.open(arguments.inputPath, itemizer);
    ItemCounter itemCounter = new ItemCounter();
    int[] itemset = null;
    int numTransactions = 0;
    while ((itemset = reader.next()) != null) {
      for (int item : itemset) {
        itemCounter.increment(item);
      }
      numTransactions++;
    }

    System.out.println("" + numTransactions + " transactions");
    System.out.println("Building initial FPTree");
    final int minCount = (int) (numTransactions * arguments.minimumSupport);
    reader.reset();
    while ((itemset = reader.nextAboveMinCount(minCount, itemCounter)) != null) {
      // TODO: Insert into FPTree.
    }

  }
}
