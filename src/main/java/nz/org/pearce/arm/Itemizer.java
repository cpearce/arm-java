package nz.org.pearce.arm;

import java.util.HashMap;

public class Itemizer {

  public Itemizer() {
    idToStr = new HashMap<Integer, String>();
    strToId = new HashMap<String, Integer>();
  }

  public int idOf(String item) {
    Integer i = strToId.get(item);
    if (i == null) {
      int id = nextId;
      nextId++;
      strToId.put(item, id);
      return id;
    } else {
      return i;
    }
  }

  public String nameOf(int id) {
    String n = idToStr.get(id);
    if (n == null) {
      return "UnknownItem";
    }
    return n;
  }

  private HashMap<Integer, String> idToStr;
  private HashMap<String, Integer> strToId;
  private int nextId = 1;
}