package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class ConsequentTree {
  // Child nodes, keyed by item in the consequent.
  HashMap<Integer, ConsequentTree> children =
      new HashMap<Integer, ConsequentTree>();

  // List of rules stored at this node. The path down the tree to
  ArrayList<Rule> rules = new ArrayList<Rule>();

  public void insert(Rule rule) {
    ConsequentTree tree = this;
    for (int item : rule.consequent) {
      ConsequentTree child = tree.children.get(item);
      if (child == null) {
        child = new ConsequentTree();
        tree.children.put(item, child);
      }
      tree = child;
    }
    tree.rules.add(rule);
  }

  public void generate(HashSet<Rule> rules, double minimumConfidence,
                       double minimumLift, ItemsetSupport supports) {
    // Find children of this node that store rules. The rules in these
    // child nodes will have consequents of length n with n-1 items
    // in common with each other.
    ArrayList<ConsequentTree> leafChildren = new ArrayList<ConsequentTree>();
    for (ConsequentTree child : children.values()) {
      if (child.rules.size() > 0) {
        leafChildren.add(child);
      }
    }

    // Try to merge these rules stored in all combinations of the
    // children/nodes.
    for (int i = 0; i < leafChildren.size(); i++) {
      ConsequentTree a = leafChildren.get(i);
      for (int j = i + 1; j < leafChildren.size(); j++) {
        ConsequentTree b = leafChildren.get(j);
        for (int k = 0; k < a.rules.size(); k++) {
          Rule r1 = a.rules.get(k);
          for (int l = 0; l < b.rules.size(); l++) {
            Rule r2 = b.rules.get(l);
            Rule rule =
                Rule.merge(r1, r2, minimumConfidence, minimumLift, supports);
            if (rule != null) {
              rules.add(rule);
            }
          }
        }
      }
    }

    // Recurse down, to catch other permutations.
    for (ConsequentTree child : children.values()) {
      child.generate(rules, minimumConfidence, minimumLift, supports);
    }
  }
}

/**
 * Generates association rules from frequent itemsets.
 */
public class RuleGenerator {

  private HashSet<Rule> generate(HashSet<Rule> candidates,
                                 double minimumConfidence, double minimumLift,
                                 ItemsetSupport supports) {
    ConsequentTree tree = new ConsequentTree();
    for (Rule rule : candidates) {
      tree.insert(rule);
    }
    HashSet<Rule> rules = new HashSet<Rule>();
    tree.generate(rules, minimumConfidence, minimumLift, supports);
    return rules;
  }

  /**
   * For each frequent itemset of size >= 2, generate all possible
   * antecedent => consequent pairs that satisfy minimum support,
   * confidence and lift constraints.
  */
  public HashSet<Rule> generate(ArrayList<FrequentPattern> itemsets,
                                int numTransactions, double minimumConfidence,
                                double minimumLift) {
    // Create a lookup table of itemset support for fast access.
    ItemsetSupport supports = ItemsetSupport.make(itemsets, numTransactions);

    HashSet<Rule> rules = new HashSet<Rule>();
    for (FrequentPattern itemset : itemsets) {
      if (itemset.itemset.length < 2) {
        continue;
      }

      // Seed generation with rules with consequents of size 1.
      HashSet<Rule> candidates = new HashSet<Rule>();
      for (int item : itemset.itemset) {
        int[] antecedent = IntSets.without(itemset.itemset, item);
        int[] consequent = {item};
        Rule rule = Rule.make(antecedent, consequent, supports,
                              minimumConfidence, minimumLift);
        if (rule != null) {
          rules.add(rule);
          candidates.add(rule);
        }
      }

      // Recursively combine rules with consequents that have overlapping
      // consequents.
      while (candidates.size() > 0) {
        HashSet<Rule> nextGeneration =
            generate(candidates, minimumConfidence, minimumLift, supports);
        rules.addAll(nextGeneration);
        candidates = nextGeneration;
      }
    }
    return rules;
  }
}