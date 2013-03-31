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

  @Override
  public String toString() {
    return "Elementary Security (lattice: " + lattice + ")";
  }
  
  
  
  
  
}
