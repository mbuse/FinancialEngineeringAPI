/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.rate;

import java.util.Arrays;

/**
 * A flexible rate which is based on the Black-Derman-Toy model.
 * 
 * Black-Derman-Toy rates are typically used to calibrate the binominal pricing
 * model.
 * 
 * BDT rates are based on the following parameters:
 * 
 * - drift parameters  a[t], 
 * - volatility parameters  b[t]   
 * 
 * The rate r[t,u] at time t and lattice state u  is calculated by the following 
 * equation:
 * 
 *   r[t,u] = a[t] * exp( b[t] * u )
 * 
 * For t>T the rate is not defined and this class will return Double.NaN.
 * @author mbuse
 */
public class BlackDermanToyRate extends AbstractRate {

  private double[] a;
  private double[] b;

  public BlackDermanToyRate(double[] a, double b) {
    this.a = a;
    this.b = new double[a.length];
    Arrays.fill(this.b, b);
  }
  
  public BlackDermanToyRate(double[] a, double[] b) {
    assert a.length == b.length;
    this.a = a;
    this.b = b;
  }
  
  
  public Double getRate(int t, int u) {
    assert 0<=u && u<=t;
    
    if (t<a.length) {
      return a[t] * Math.exp(b[t] * u);
    }
    else {
      return Double.NaN;
    }
  }

  @Override
  public String toString() {
    return "BlackDermanToyRate{a=" + Arrays.toString(a) 
            + ", b=" + Arrays.toString(b) + '}';
  }
  
  
}
