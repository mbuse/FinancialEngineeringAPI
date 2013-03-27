/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.rate;

import de.mbuse.finance.binominal.Binominal;

/**
 *
 * @author mbuse
 */
public abstract class AbstractRate implements Rate {
  
  public Double getValue(int t, int upMoves) {
    return getRate(t, upMoves);
  }

  public abstract Double getRate(int t, int u);

  public Double getRateFactor(int t, int u) {
    return 1.0 + getRate(t, u);
  }
  
  public Binominal<Double> getRateFactor() {
    return new Binominal<Double>() {
      public Double getValue(int t, int upMoves) {
        return getRateFactor(t, upMoves);
      }
    };
  }
  
}
