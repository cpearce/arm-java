package nz.org.pearce.arm;

import java.io.*;

public class ARM {
  public static void main(String[] args) {
    DataSetReader reader;
    Itemizer itemizer = new Itemizer();
    try {
      reader = DataSetReader.open(args[0], itemizer);
    } catch (IOException e) {
      System.err.println("Can't open " + args[0] + " for input");
      System.exit(1);
      return;
    }
    int[] itemset = null;
    while ((itemset = reader.next()) != null) {
      StringBuilder builder = new StringBuilder();
      for (int i : itemset) {
        if (builder.length() > 0) {
          builder.append(",");
        }
        builder.append(i);
      }
      System.out.println(builder.toString());
    }
  }
}
