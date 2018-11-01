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

import java.util.ArrayList;

/**
 * Generates association rules from frequent itemsets.
 */
public class RuleGenerator
{

  private int prefixMatchLength(int[] a, int[] b) {
    assert a.length == b.length;
    for (int i = 0; i < a.length; i++) {
      if (a[i] != b[i]) {
        return i;
      }
    }
    return a.length;
  }

  private void generateRulesFor(int[] itemset,
                                double support,
                                ArrayList<Rule> rules,
                                ItemsetSupport supports,
                                double minimumConfidence,
                                double minimumLift)
  {
      // Seed generation with rules with consequents of size 1.
      ArrayList<int[]> candidates = new ArrayList<int[]>();
      for (int item : itemset) {
        int[] consequent = new int[]{ item };
        int[] antecedent = IntSets.subtract(itemset, consequent);

        double aSupport = supports.support(antecedent);
        double confidence = support / aSupport;
        double cSupport = supports.support(consequent);
        double lift = support / (aSupport * cSupport);

        if (confidence < minimumConfidence) {
          continue;
        }
        if (lift >= minimumLift) {
          rules.add(new Rule(antecedent,
                             consequent,
                             support,
                             confidence,
                             lift));
        }
        candidates.add(consequent);
      }

      // Combine consequents which have overlapping matching prefixes of len-1
      // items,  as per appgenrules algorithm.
      int k = itemset.length;
      while (candidates.size() > 0 && candidates.get(0).length + 1 < k) {
        // Note: candidates must be sorted!
        ArrayList<int[]> nextGen = new ArrayList<int[]>();
        int m = candidates.get(0).length;
        for (int i1=0; i1 < candidates.size(); i1++) {
          int[] c1 = candidates.get(i1);
          for (int i2=i1+1; i2 < candidates.size(); i2++) {
            int[] c2 = candidates.get(i2);
            if (prefixMatchLength(c1, c2) != m-1) {
              // Consequents in the candidates list are sorted, and the
              // candidates list itself is sorted. So we can stop
              // testing combinations once our iteration reaches another
              // candidate that no longer shares an m-1 prefix. Stopping
              // the iteration here is a significant optimization. This
              // ensures that we don't generate or test duplicate
              // rules.
              break;
            }

            int[] consequent = IntSets.union(c1, c2);
            int[] antecedent = IntSets.subtract(itemset, consequent);

            double aSupport = supports.support(antecedent);
            double confidence = support / aSupport;
            double cSupport = supports.support(consequent);
            double lift = support / (aSupport * cSupport);

            if (confidence < minimumConfidence) {
              continue;
            }
            if (lift >= minimumLift) {
              rules.add(new Rule(antecedent,
                                 consequent,
                                 support,
                                 confidence,
                                 lift));
            }
            nextGen.add(consequent);
          }
        }

        candidates = nextGen;
        candidates.sort((int[] a, int[] b) -> IntSets.compare(a,b));
      }
  }
  /**
   * For each frequent itemset of size >= 2, generate all possible
   * antecedent => consequent pairs that satisfy minimum support,
   * confidence and lift constraints.
   */
  public ArrayList<Rule> generate(ArrayList<FrequentPattern> itemsets,
                                 int numTransactions,
                                 double minimumConfidence,
                                 double minimumLift)
  {
    // Create a lookup table of itemset support for fast access.
    ItemsetSupport supports = ItemsetSupport.make(itemsets, numTransactions);

    ArrayList<Rule> rules = new ArrayList<Rule>();
    for (FrequentPattern itemset : itemsets) {
      if (itemset.itemset.length < 2) {
        continue;
      }
      double support = (double)itemset.count / (double)numTransactions;
      generateRulesFor(itemset.itemset,
                       support,
                       rules,
                       supports,
                       minimumConfidence,
                       minimumLift);
    }
    return rules;
  }
}