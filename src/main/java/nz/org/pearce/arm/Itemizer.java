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

import java.util.HashMap;
import java.util.Arrays;

public class Itemizer
{
  public int idOf(String item)
  {
    Integer i = strToId.get(item);
    if (i == null) {
      int id = nextId;
      nextId++;
      strToId.put(item, id);
      idToStr.put(id, item);
      return id;
    } else {
      return i;
    }
  }

  public String nameOf(int id)
  {
    String name = idToStr.get(id);
    if (name == null) {
      throw new Error("Lookup of id failed");
    }
    return name;
  }

  public String[] namesOf(int[] items)
  {
    String[] names = new String[items.length];
    int index = 0;
    for (int item : items) {
      names[index++] = nameOf(item);
    }
    return names;
  }

  public ItemCounter sort(ItemCounter itemCounter)
  {
    String[] itemNames = new String[idToStr.size()];
    int index = 0;
    for (String name : strToId.keySet()) {
      itemNames[index++] = name;
    }
    Arrays.sort(itemNames);

    HashMap<Integer, String> newIdToStr = new HashMap<Integer, String>();
    HashMap<String, Integer> newStrToId = new HashMap<String, Integer>();
    ItemCounter newItemCounter = new ItemCounter();

    for (index = 0; index < itemNames.length; index++) {
      String name = itemNames[index];
      int id = index + 1;
      newIdToStr.put(id, name);
      newStrToId.put(name, id);
      int oldId = idOf(name);
      int count = itemCounter.count(oldId);
      newItemCounter.increment(id, count);
    }

    idToStr = newIdToStr;
    strToId = newStrToId;

    return newItemCounter;
  }

  private HashMap<Integer, String> idToStr = new HashMap<Integer, String>();
  private HashMap<String, Integer> strToId = new HashMap<String, Integer>();
  private int nextId = 1;
}