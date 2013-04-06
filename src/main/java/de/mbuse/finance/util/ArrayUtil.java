package de.mbuse.finance.util;

import java.util.Arrays;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author mbuse
 */
public class ArrayUtil {
  
  /**
   * Create a new double[] with given length, filled with the given value.
   */
  public static double[] init(int length, double value) {
    double[] array = new double[length];
    Arrays.fill(array, value);
    return array;
  }
  
  /**
   * Substracts the vector b from a and scalar-multiplies the result with itself.
   * 
   * @param a
   * @param b
   * @return SUM_i { (a[i] - b[i])^2 }
   */
  public static double squaredDistance(double[] a, double[] b) {
    assert a.length == b.length;
    double result = 0.0;
    for (int i=0; i<a.length; i++) {
      double d = (a[i] - b[i]);
      result += d*d;
    }
    return result;
  }
  
  /**
   * Calculates the squared difference between the vector a and b.
   * 
   * @param a
   * @param b
   * @return r[i] = (a[i] - b[i])^2 
   */
  public static double[] squaredDifference(double[] a, double[] b) {
    assert a.length == b.length;
    
    double[] result = new double[a.length];
    for (int i=0; i<a.length; i++) {
      double d = (a[i] - b[i]);
      result[i] = d*d;
    }
    return result;
  }
  
  /**
   * The maximum absolute difference between the vectors a and b is the maximum 
   * of the differences of its values:
   * 
   * {@code max_abs_diff = MAX { | a[i] - b[i] | } }
   * @param a
   * @param b
   * @return 
   */
  public static double maximumAbsoluteDifference(double[] a, double[] b) {
    Double max = null;
    for (int i=0; i<a.length; i++) {
      double d = FastMath.abs(a[i] - b[i]);
      if (max==null) {
        max = d;
      }
      else {
        max = FastMath.max(max, d);
      }
    }
    return max;
  }
}
