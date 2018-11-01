package nz.org.pearce.arm;

/**
 * Represents an association rule of the form antecedent -> consequent.
 * Also stores the support, confidence, and lift of the rule.
 */
public class Rule
{

  public int[] antecedent;
  public int[] consequent;
  public double support;
  public double confidence;
  public double lift;

  public Rule(int[] antecedent,
              int[] consequent,
              double support,
              double confidence,
              double lift)
  {
    this.antecedent = antecedent;
    this.consequent = consequent;
    this.support = support;
    this.confidence = confidence;
    this.lift = lift;
  }

  /**
   * Returns string representation.
   */
  public String toString()
  {
    StringBuffer s = new StringBuffer();
    s.append(IntSets.toString(antecedent));
    s.append(" => ");
    s.append(IntSets.toString(consequent));
    s.append(",");
    s.append(String.format("%.3f", confidence));
    s.append(",");
    s.append(String.format("%.3f", lift));
    s.append(",");
    s.append(String.format("%.3f", support));
    return s.toString();
  }
}