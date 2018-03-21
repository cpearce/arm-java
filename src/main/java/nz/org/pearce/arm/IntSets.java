package nz.org.pearce.arm;

public class IntSets
{

  public static int[] without(int[] A, int value)
  {
    if (A.length == 0) {
      return new int[0];
    }
    int[] B = new int[A.length - 1];
    int index = 0;
    for (int i : A) {
      if (i != value) {
        B[index++] = i;
      }
    }
    return B;
  }

  public static int[] union(int[] A, int[] B)
  {
    int a = 0;
    int b = 0;

    // Count number of elements in the union.
    int count = 0;
    while (a < A.length && b < B.length) {
      if (A[a] < B[b]) {
        a++;
      } else if (B[b] < A[a]) {
        b++;
      } else {
        a++;
        b++;
      }
      count++;
    }
    count += A.length - a;
    count += B.length - b;

    // Build result.
    int[] C = new int[count];
    int index = 0;
    a = 0;
    b = 0;
    while (a < A.length && b < B.length) {
      int value;
      if (A[a] < B[b]) {
        value = A[a];
        a++;
      } else if (B[b] < A[a]) {
        value = B[b];
        b++;
      } else {
        value = A[a];
        a++;
        b++;
      }
      C[index] = value;
      index++;
    }
    while (a < A.length) {
      C[index++] = A[a++];
    }
    while (b < B.length) {
      C[index++] = B[b++];
    }
    return C;
  }

  public static int intersectionSize(int[] A, int[] B)
  {
    int a = 0;
    int b = 0;

    // Count number of elements in the union.
    int count = 0;
    while (a < A.length && b < B.length) {
      if (A[a] < B[b]) {
        a++;
      } else if (B[b] < A[a]) {
        b++;
      } else {
        count++;
        a++;
        b++;
      }
    }

    return count;
  }

  public static int[] intersection(int[] A, int[] B)
  {
    verifySorted(A);
    verifySorted(B);

    int a = 0;
    int b = 0;

    // Count number of elements in the union.
    int count = intersectionSize(A, B);

    // Build result.
    int[] C = new int[count];
    int index = 0;
    a = 0;
    b = 0;
    while (a < A.length && b < B.length) {
      if (A[a] < B[b]) {
        a++;
      } else if (B[b] < A[a]) {
        b++;
      } else {
        C[index++] = A[a];
        a++;
        b++;
      }
    }
    return C;
  }

  public static String toString(int[] A)
  {
    StringBuffer s = new StringBuffer();
    boolean first = true;
    s.append("[");
    for (int i : A) {
      if (!first) {
        s.append(",");
      } else {
        first = false;
      }
      s.append(i);
    }
    s.append("]");
    return s.toString();
  }
}