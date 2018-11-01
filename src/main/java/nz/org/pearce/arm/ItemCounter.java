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

public class ItemCounter
{
  public void increment(int item) { increment(item, 1); }

  public void increment(int item, int count)
  {
    counter.put(item, count(item) + count);
  }

  public int count(int item)
  {
    Integer count = counter.get(item);
    return (count == null) ? 0 : count;
  }

  public ArrayList<Integer> itemsWithCountAtLeast(int minCount)
  {
    ArrayList<Integer> items = new ArrayList<Integer>();
    counter.forEach((item, count) -> {
      if (count >= minCount) {
        items.add(item);
      }
    });
    return items;
  }

  private HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();
}