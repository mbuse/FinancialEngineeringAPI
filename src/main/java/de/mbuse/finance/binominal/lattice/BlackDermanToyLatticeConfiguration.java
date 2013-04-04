/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.BlackDermanToyRate;
import de.mbuse.finance.binominal.rate.Rate;
import java.util.Arrays;

/**
 * A lattice configuration which is based on the Black-Derman-Toy model.
 * 
 * Black-Derman-Toy model is typically used to calibrate the binominal pricing
 * model.
 * 
 * In the BDT model the rates are based on the following parameters:
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
 * 
 * The model uses the risk neutral probability q for pricing. The value of q is
 * typically set to 0.5.
 * 
 * The methods {@link #getUpFactor()} and {@link #getDownFactor()} are also not
 * applicable in this lattice. These methods will return {@link Double#NaN}
 * 
 * @author mbuse
 */
public class BlackDermanToyLatticeConfiguration extends LatticeConfiguration {

  private double[] a;
  private double[] b;
  private double q;

  public BlackDermanToyLatticeConfiguration(double q, double[] a, double[] b) {
    assert a.length == b.length;
    assert 0 <= q && q <= 1;
    this.a = a;
    this.b = b;
    this.q = q;
  }
  public BlackDermanToyLatticeConfiguration(double q, double[] a, double b) {
    assert 0 <= q && q <= 1;
    this.a = a;
    this.b = new double[a.length]; Arrays.fill(this.b, b);
    this.q = q;
  }
  
  @Override
  public Rate getRate() {
    return new BlackDermanToyRate(a, b);
  }

  @Override
  public Double getUpFactor() {
    return Double.NaN;
  }

  @Override
  public Double getRiskFreePropabilityForUp() {
    return q;
  }

  @Override
  public String toString() {
    return "BlackDermanToyLatticeConfiguration{"
            + "q=" + q
            + ", a=" + Arrays.toString(a) 
            + ", b=" + Arrays.toString(b)  + '}';
  }
  
  
  
}
