/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.rate.Rate;

/**
 * @author mbuse
 */
public class ElementarySecurity implements Security {
  
  private LatticeConfiguration lattice;

  public ElementarySecurity(LatticeConfiguration lattice) {
    this.lattice = lattice;
  }

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return 1.0;
  }

  public Double getValue(int t, int u) {
    assert t >= u && u >= 0;
    
    Double qu = lattice.getRiskFreePropabilityForUp();
    Double qd = lattice.getRiskFreePropabilityForDown();
    Rate rate = lattice.getRate();
    
    if (t==0 && u==0) {
      return getPrice();
    }
    else if (u==0) {
      return qu * getValue(t-1, 0)/rate.getRateFactor(t-1, 0);
    }
    else if (u==t) {
      return qd * getValue(t-1, u-1)/rate.getRateFactor(t-1, u-1);
    }
    else {
      return qu * getValue(t-1, u-1)/rate.getRateFactor(t-1, u-1)
              + qd * getValue(t-1, u)/rate.getRateFactor(t-1, u);
    }
    
  }
  
  public Double calculatePrice(Binominal<Double> payoffs, int maxt) {
    Double price = .0;
    for (int t=0; t<= maxt; t++) {
      for (int u=0; u<=t; u++) {
        price += payoffs.getValue(t, u) * getValue(t, u);
      }
    }
    return price;
  }

  /** 
   * The price of a Zero-Coupon-Bond that matures at time t.
   * The face-value of this bond is set to 1.
   * 
   * This price is given by:
   * 
   *  P(t) = SUM_u=0..t ( getValue(t, u) )
   **/
  public Double getZCBPrice(int t) {
    assert t >= 0;
    double price = 0.0;
    for (int i=0; i<=t; i++) {
      price += getValue(t, i);
    }
    return price;
  }
  
  /**
   * An array of Zero-Coupon-Bond prices, starting with t=0 and ending with t=maxT
   * @return 
   */
  public double[] getZCBPrices(int maxT) {
    double[] prices = new double[maxT + 1];
    for (int t=0; t<=maxT; t++) {
      prices[t] = getZCBPrice(t);
    }
    return prices;
  }
  
  
  
  @Override
  public String toString() {
    return "Elementary Security (lattice: " + lattice + ")";
  }
  
  
  
  
  
}
