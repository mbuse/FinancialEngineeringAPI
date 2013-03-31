/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;

/**
 *
 * @author mbuse
 */
public class ForwardSwap implements Security {

  private LatticeConfiguration lattice;
  private Double fixedRate;
  private int startPeriod;
  private int maturity;

  public ForwardSwap(LatticeConfiguration lattice, Double fixedRate, int startPeriod, int maturity) {
    this.lattice = lattice;
    this.fixedRate = fixedRate;
    this.startPeriod = startPeriod;
    this.maturity = maturity;
  }
  
  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int u) {
    assert t>=u && u>=0;
    double qu = lattice.getRiskFreePropabilityForUp();
    double qd = lattice.getRiskFreePropabilityForDown();
    
    if (t > maturity) {
      return Double.NaN;
    }
    else if (t == maturity) {
      return getPayment(t, u);
    }
    else if (t >= startPeriod) {
      double su = getValue(t+1, u+1);
      double sd = getValue(t+1, u);
      double r = lattice.getRate().getRate(t, u);
      return getPayment(t, u) + (qu * su + qd * sd)/(1 + r);
    }
    else {
      assert t<startPeriod;
      double su = getValue(t+1, u+1);
      double sd = getValue(t+1, u);
      double r = lattice.getRate().getRate(t, u);
      return (qu * su + qd * sd)/(1 + r);
    }
  }
  
  public Double getPayment(int t, int u) {
    Double rate = lattice.getRate().getRate(t, u);
    return (rate - fixedRate)/(1.0 + rate);
  }
}
