// Copyright 2018 Chris Pearce
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package nz.org.pearce.arm;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DataSetReader {

  private int numTransactions = 0;

  public ItemCounter countItemFrequencies() throws IOException {
    ItemCounter itemCounter = new ItemCounter();
    HashSet<Integer> itemset = null;
    while ((itemset = nextAsSet()) != null) {
      for (int item : itemset) {
        itemCounter.increment(item);
      }
    }
    return itemizer.sort(itemCounter);
  }

  public int getNumTransactions() {
    return numTransactions;
  }

  public static DataSetReader open(String path) throws IOException {
    RandomAccessFile file = new RandomAccessFile(new File(path), "r");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getFD())));
    return new DataSetReader(file, reader);
  }

  private DataSetReader(RandomAccessFile file, BufferedReader reader) {
    this.file = file;
    this.reader = reader;
    this.itemizer = new Itemizer();
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
    numTransactions++;
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

  void reset() throws IOException{
    file.seek(0);
    numTransactions = 0;
    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getFD())));
  }

  public Itemizer getItemizer() {
    return itemizer;
  }

  private RandomAccessFile file;
  private BufferedReader reader;
  private Itemizer itemizer;
}