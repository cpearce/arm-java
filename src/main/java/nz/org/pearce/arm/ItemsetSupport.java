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
import java.util.HashMap;

public class ItemsetSupport
{
  private HashMap<ArrayList<Integer>, Double> map =
    new HashMap<ArrayList<Integer>, Double>();

  public static ItemsetSupport make(ArrayList<FrequentPattern> patterns,
                                    int numTransactions)
  {
    ItemsetSupport supports = new ItemsetSupport();
    for (FrequentPattern pattern : patterns) {
      supports.map.put(toArrayList(pattern.itemset),
                       (double)pattern.count / (double)numTransactions);
    }
    return supports;
  }

  public double support(int[] items) { return map.get(toArrayList(items)); }

  private static ArrayList<Integer> toArrayList(int[] items)
  {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i : items) {
      list.add(i);
    }
    return list;
  }
}