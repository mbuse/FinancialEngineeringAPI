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
  
  /**
   * Calculates the net present value of a given cashflow, using a fixed rate
   * 
   * @param rate the fixed interest rate
   * @param cashflow the cashflow starting at t=0. 
   * @return the sum of all cashflows
   */
  public static double npv(double rate, double[] cashflow) {
    double presentValue = 0.0;
    for (int t=0; t<cashflow.length; t++) {
      presentValue += pv(cashflow[t], rate, t);
    }
    return presentValue;
  }
  
  /**
   * calculates the present value of a given amount a given number of periods 
   * ahead, discounted using a given fixed rate.
   * 
   * @param amount  the future value at the given period
   * @param rate    the rate per period
   * @param periods number of periods the future value is ahead
   * @return 
   */
  public static double pv(double amount, double rate, int periods) {
    return amount / Math.pow(1 + rate, periods);
  }
  
  /**
   * calculates the future value of a given amount a given number of periods 
   * ahead, compounded by a given fixed rate.
   * 
   * @param amount  the present value
   * @param rate    the rate per period
   * @param periods the number of periods the future value is ahead
   * @return 
   */
  public static double fv(double amount, double rate, int periods) {
    return amount * Math.pow(1 + rate, periods);
  }
  
}
