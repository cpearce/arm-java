package nz.org.pearce.arm;

import java.util.HashMap;

public class Itemizer
{

  public Itemizer()
  {
    idToStr = new HashMap<Integer, String>();
    strToId = new HashMap<String, Integer>();
  }

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
    String n = idToStr.get(id);
    if (n == null) {
      return "UnknownItem";
    }
    return n;
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

  private HashMap<Integer, String> idToStr;
  private HashMap<String, Integer> strToId;
  private int nextId = 1;
}