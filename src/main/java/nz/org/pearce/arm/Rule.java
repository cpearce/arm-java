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

  private Rule(int[] antecedent,
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
   * Creates a new association rule with supplied antecedent/consequent
   * provided minimum support, confidence, and lift thresholds are staisfied.
   */
  public static Rule make(int[] antecedent,
                          int[] consequent,
                          ItemsetSupport supports,
                          double minimumConfidence,
                          double minimumLift)
  {
    if (antecedent.length == 0 || consequent.length == 0) {
      return null;
    }
    int[] ac = IntSets.union(antecedent, consequent);
    double acSupport = supports.support(ac);
    double aSupport = supports.support(antecedent);
    double confidence = acSupport / aSupport;
    if (confidence < minimumConfidence) {
      return null;
    }
    double cSupport = supports.support(consequent);
    double lift = acSupport / (aSupport * cSupport);
    if (lift < minimumLift) {
      return null;
    }
    return new Rule(antecedent, consequent, acSupport, confidence, lift);
  }

  /**
   * Constructs a new rule by combining two rules such that the antecedent
   * is the intersection of both rules' antecedents, and the consequent is
   * the union of both rules consequents, provided minimum support, confidence
   * and lift thresholds are satisfied.
   */
  public static Rule merge(Rule a,
                           Rule b,
                           double minimumConfidence,
                           double minimumLift,
                           ItemsetSupport supports)
  {
    int[] antecedent = IntSets.intersection(a.antecedent, b.antecedent);
    int[] consequent = IntSets.union(a.consequent, b.consequent);
    return make(
      antecedent, consequent, supports, minimumConfidence, minimumLift);
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