package arm;

import java.util.Iterator;
import java.io.*;

public class DataSetReader {
 
  public static DataSetReader open(String path, Itemizer itemizer) throws IOException {
    return new DataSetReader(new BufferedReader(new FileReader(path)), itemizer);
  }

  private DataSetReader(BufferedReader reader, Itemizer itemizer) {
    this.reader = reader;
    this.itemizer = itemizer;
  }

  public int[] next() {
      String line = null;
      try {
        line = reader.readLine();
      } catch (IOException exception) {
        return null;
      }
      String[] strings = line.split(",");
      int[] items = new int[strings.length];
      for (int i = 0; i < strings.length; i++) {
        items[i] = itemizer.idOf(strings[i].trim());
      }
      return items;
  }

  private BufferedReader reader;
  private Itemizer itemizer;
}