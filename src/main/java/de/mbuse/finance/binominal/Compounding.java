/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal;

import java.util.Arrays;

/**
 *
 * @author mbuse
 */
public class Compounding {
  
  public static double spotRate(int t, double presentValue, double futureValue) {
    assert 0<=t;
    if (presentValue==0.0) {
      return Double.NaN;
    }
    return (t==0) 
            ? 0.0
            : Math.pow(futureValue/presentValue, 1.0/t) - 1.0;
  }
  
  /**
   * !!!CONVENTION!!!
   * The index of the array futureValue represents the time t. therefore the
   * first entry futureValue[t] with t=0 is the present value!
   *
   * The resulting array of spot rates has the same interpretation of index t.
   * So the first rate will be 0 (because the rate between fv and pv is 0).
   * 
   * @param futureValues
   * @return spotrates
   */
  public static double[] spotRates(double... futureValues) {
    assert futureValues.length > 0;
    assert futureValues[0] != 0;
    double presentValue = futureValues[0];
    double[] spotRates = new double[futureValues.length];
    for (int t=0; t<futureValues.length; t++) {
      spotRates[t] = spotRate(t, presentValue, futureValues[t]);
    }
    return spotRates;
  }
  
  /**
   * Inverted spot rates are the spot rates calculated based on an array of 
   * present values (not future values like in {@link #spotRates(double[])} in
   * relation to a fixed future value. This is usefull when evaluating spot
   * rates of ZCB, where the face value is fixed, but prices vary over time.
   * 
   * !!!CONVENTION!!!
   * The index of the array presentValues represents the time t. therefore the
   * first entry presentValues[t] with t=0 is the future value!
   *
   * The resulting array of spot rates has the same interpretation of index t.
   * So the first rate will be 0 (because the rate between fv and pv is 0).
   * 
   * @param presentValues
   * @return spotrates
   */
  public static double[] invSpotRates(double... presentValues) {
    assert presentValues.length > 0;
    
    double futureValue = presentValues[0];
    double[] spotRates = new double[presentValues.length];
    for (int t=0; t<presentValues.length; t++) {
      spotRates[t] = spotRate(t, presentValues[t], futureValue);
    }
    return spotRates;
  }
  
}
