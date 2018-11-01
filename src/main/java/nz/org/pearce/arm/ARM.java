package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

class Timer
{
  private long time = System.currentTimeMillis();
  void reset() { time = System.currentTimeMillis(); }
  long elapsed() { return System.currentTimeMillis() - time; }
}

public class ARM
{

  public static void main(String[] args)
  {
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

  public void run(Arguments arguments) throws IOException
  {
    Timer total = new Timer();
    Timer timer = new Timer();
    System.out.println("Counting item frequencies");
    DataSetReader reader = DataSetReader.open(arguments.inputPath);
    ItemCounter itemCounter = reader.countItemFrequencies();
    int numTransactions = reader.getNumTransactions();
    System.out.println("Counted item frequencies in " + timer.elapsed() +
                       " ms");
    System.out.println("" + numTransactions + " transactions");
    final int minCount = (int)(Math.ceil(numTransactions * arguments.minimumSupport));
    System.out.println("MinCount=" + minCount);

    System.out.println("Building initial FPTree");
    timer.reset();
    reader.reset();
    FPTree tree = new FPTree();
    int[] itemset = null;
    while ((itemset = reader.nextAboveMinCount(minCount, itemCounter)) !=
           null) {
      tree.insert(itemset, 1);
    }
    System.out.println("Built initial FPTree in " + timer.elapsed() + " ms");

    System.out.println("Generating frequent patterns with FPGrowth...");
    timer.reset();
    ArrayList<FrequentPattern> itemsets =
      tree.growFrequentPatterns(minCount, numTransactions);
    System.out.println("Generated " + itemsets.size() +
                       " frequent patterns in " + timer.elapsed() + " ms");
    // for (FrequentPattern pattern : itemsets) {
    //   System.out.println(pattern);
    // }

    System.out.println("Generating rules...");
    timer.reset();
    RuleGenerator ruleGenerator = new RuleGenerator();
    ArrayList<Rule> rules = ruleGenerator.generate(itemsets,
                                                   numTransactions,
                                                   arguments.minimumConfidence,
                                                   arguments.minimumLift);
    System.out.println("Generated " + rules.size() + " rules in " +
                       timer.elapsed() + " ms");

    System.out.println("Writing rules to file");
    timer.reset();
    long size = writeRules(rules, arguments.outputPath, reader.getItemizer());
    long elapsed = timer.elapsed();
    System.out.println("Wrote rules to file in " + elapsed + " ms, " +
                       MBPS(size, elapsed) + " MB/s");

    System.out.println("Total runtime " + total.elapsed() + " ms");
  }

  private String MBPS(long size, long ms)
  {
    return String.format("%.2f", (double)size / ((double)ms / 1000) / 1000000);
  }

  private long writeRules(ArrayList<Rule> rules,
                          String outputPath,
                          Itemizer itemizer) throws IOException
  {
    PrintWriter writer =
      new PrintWriter(new BufferedWriter(new FileWriter(outputPath)));
    writer.println("Antecedent => Consequent, Confidence, Lift, Support");
    for (Rule rule : rules) {
      writeItems(writer, rule.antecedent, itemizer);
      writer.print(" => ");
      writeItems(writer, rule.consequent, itemizer);
      writer.print(",");
      writer.print(rule.confidence);
      writer.print(",");
      writer.print(rule.lift);
      writer.print(",");
      writer.println(rule.support);
    }
    writer.close();
    return (new File(outputPath)).length();
  }

  private void writeItems(PrintWriter writer, int[] items, Itemizer itemizer)
    throws IOException
  {
    String[] names = itemizer.namesOf(items);
    boolean first = true;
    for (String name : names) {
      if (!first) {
        writer.print(" ");
      } else {
        first = false;
      }
      writer.print(name);
    }
  }
}
