package nz.org.pearce.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Generates association rules from frequent itemsets.
 */
public class RuleGenerator
{

  private HashSet<Rule> generate(HashSet<Rule> candidates,
                                 double minimumConfidence,
                                 double minimumLift,
                                 ItemsetSupport supports)
  {
    Rule[] array = new Rule[candidates.size()];
    array = candidates.toArray(array);
    HashSet<Rule> rules = new HashSet<Rule>();
    for (int i = 0; i < array.length; i++) {
      Rule rule1 = array[i];
      for (int k = i + 1; k < array.length; k++) {
        Rule rule2 = array[k];
        if (rule1.consequent.length != rule2.consequent.length) {
          continue;
        }
        int size = rule1.consequent.length;
        int overlap = IntSets.intersectionSize(rule1.consequent, rule2.consequent);
        if (overlap != size - 1) {
          continue;
        }
        Rule rule = Rule.merge(rule1, rule2, minimumConfidence, minimumLift, supports);
        if (rule != null) {
          rules.add(rule);
        }
      }
    }
    return rules;
  }

  /**
   * For each frequent itemset of size >= 2, generate all possible
   * antecedent => consequent pairs that satisfy minimum support,
   * confidence and lift constraints.
   */
  public HashSet<Rule> generate(ArrayList<FrequentPattern> itemsets,
                                int numTransactions,
                                double minimumConfidence,
                                double minimumLift)
  {
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
        int[] consequent = { item };
        Rule rule = Rule.make(
          antecedent, consequent, supports, minimumConfidence, minimumLift);
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