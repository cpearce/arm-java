package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class FPNode {
  FPNode(int item, FPNode parent) {
    this.item = item;
    this.parent = parent;
  }

  FPNode child(int item) {
    for (FPNode child : children) {
      if (child.item == item) {
        return child;
      }
    }
    return null;
  }

  boolean isRoot() {
    return this.item == 0;
  }

  int item = 0;
  int count = 0;
  FPNode parent;
  ArrayList<FPNode> children = new ArrayList<FPNode>();
}

public class FPTree {
  FPNode root = new FPNode(0, null);
  ItemCounter itemCounter = new ItemCounter();
  HashMap<Integer, ArrayList<FPNode>> itemLists =
      new HashMap<Integer, ArrayList<FPNode>>();

  public void insert(int[] itemset, int count) {
    FPNode node = root;
    for (int item : itemset) {
      itemCounter.increment(item, count);
      FPNode child = node.child(item);
      if (child == null) {
        child = new FPNode(item, node);
        node.children.add(child);
        addToItemList(item, child);
      }
      child.count += count;
      node = child;
    }
  }

  private void addToItemList(int item, FPNode child) {
    ArrayList<FPNode> itemList = itemLists.get(item);
    if (itemList == null) {
      itemList = new ArrayList<FPNode>();
      itemLists.put(item, itemList);
    }
    itemList.add(child);
  }

  public ArrayList<FrequentPattern> growFrequentPatterns(int minCount, int numTransactions) {
    return growFrequentPatterns(minCount, new ArrayList<Integer>(), numTransactions);
  }

  private ArrayList<FrequentPattern> growFrequentPatterns(int minCount, ArrayList<Integer> path, int pathCount) {
    ArrayList<FrequentPattern> frequentPatterns = new ArrayList<FrequentPattern>();
    ArrayList<Integer> items = itemCounter.itemsWithCountAtLeast(minCount);
    for (Integer item : items) {
      path.add(item);
      FPTree conditionalTree = constructConditionalTree(item);
      int newPathCount = Math.min(pathCount, itemCounter.count(item));
      frequentPatterns.addAll(conditionalTree.growFrequentPatterns(minCount, path, newPathCount));
      frequentPatterns.add(new FrequentPattern(path, newPathCount));
      path.remove(path.size() - 1);
    }
    return frequentPatterns;
  }

  private FPTree constructConditionalTree(int item) {
    FPTree tree = new FPTree();
    ArrayList<FPNode> itemList = itemLists.get(item);
    for (FPNode node : itemList) {
      int[] path = pathFromRootExcluding(node);
      tree.insert(path, node.count);
    }
    return tree;
  }

  private int[] pathFromRootExcluding(FPNode node) {
    ArrayList<Integer> path = new ArrayList<Integer>();
    while (true) {
      node = node.parent;
      if (node.isRoot()) {
        break;
      }
      path.add(node.item);
    }
    Collections.reverse(path);
    return path.stream().mapToInt(i -> i).toArray();
  }

}