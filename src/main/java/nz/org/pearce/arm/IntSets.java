package nz.org.pearce.arm;

public class IntSets
{

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

  public static void verifySorted(int[] A) {
    for (int i = 1; i < A.length; i++) {
      assert (A[i-1] <= A[i]);
    }
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

  // Subtract B from A;
  public static int[] subtract(int[] A, int[] B) {

    // Make 1 pass to count length of result.
    int ap = 0;
    int bp = 0;
    int count = 0;
    while (ap < A.length && bp < B.length) {
        if (A[ap] < B[bp]) {
            count++;
            ap++;
        } else {
            ap++;
            bp++;
        }
    }
    count += A.length - ap;

    // Make a second pass, writing result.
    int[] C = new int[count];
    int i = 0;
    ap = 0;
    bp = 0;
    while (ap < A.length && bp < B.length) {
        if (A[ap] < B[bp]) {
            C[i++] = A[ap];
            ap++;
        } else {
            ap++;
            bp++;
        }
    }
    while (ap < A.length) {
      C[i++] = A[ap++];
    }

    return C;
  }

  public static int compare(int[] a, int[] b) {
    if (a == null) {
        return b == null ? 0 : -1;
    }
    if (b == null) {
        return 1;
    }
    if (a.length < b.length) {
      return -1;
    }
    if (b.length < a.length) {
      return 1;
    }
    int cmp;
    for (int i = 0; i < a.length; i++) {
        cmp = Integer.compare(a[i], b[i]);
        if (cmp != 0) {
            return cmp;
        }
    }
    return 0;
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