package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;

public class ARM {

  public static void main(String[] args) {
    System.out.println("Association Rule Mining with Java");
    Arguments arguments = Arguments.parseOrDie(args);
    System.out.println("Input: " + arguments.inputPath);
    System.out.println("Output: " + arguments.outputPath);
    System.out.println("Minimum Support: " + arguments.minimumSupport);
    System.out.println("Minimum Confidence: " + arguments.minimumConfidence);
    System.out.println("Minimum Lift: " + arguments.minimumLift);
    System.out.println("");
    try {
      (new ARM()).run(arguments);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run(Arguments arguments) throws IOException {

    System.out.println("Counting item frequencies");
    Itemizer itemizer = new Itemizer();
    DataSetReader reader = DataSetReader.open(arguments.inputPath, itemizer);
    ItemCounter itemCounter = reader.countItemFrequencies();
    int numTransactions = reader.getNumTransactions();

    System.out.println("" + numTransactions + " transactions");
    System.out.println("Building initial FPTree");
    final int minCount = (int) (numTransactions * arguments.minimumSupport);
    System.out.println("MinCount=" + minCount);
    reader.reset();
    FPTree tree = new FPTree();
    int[] itemset = null;
    while ((itemset = reader.nextAboveMinCount(minCount, itemCounter)) != null) {
      tree.insert(itemset, 1);
    }

    System.out.println("Generating frequent patterns with FPGrowth");
    ArrayList<FrequentPattern> itemsets = tree.growFrequentPatterns(minCount, numTransactions);
    System.out.println("Generated " + itemsets.size() + " frequent patterns");
    for (FrequentPattern pattern : itemsets) {
      System.out.println(pattern);
    }
  }
}
