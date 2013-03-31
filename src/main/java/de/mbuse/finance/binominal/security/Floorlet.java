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
public class Floorlet implements Security {

  private LatticeConfiguration lattice;
  private Double strike;
  private int maturity;

  public Floorlet(LatticeConfiguration lattice, Double strike, int maturity) {
    this.lattice = lattice;
    this.strike = strike;
    this.maturity = maturity;
  }
  
  
  
  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int u) {
    assert u<=t;
    assert u<=0;
    int periodOfSettlement = maturity -1;
    if (t>periodOfSettlement) {
      return 0.0;
    }
    else if (t==periodOfSettlement) {
      Double rate = lattice.getRate().getRate(periodOfSettlement, u);
      return Math.max(0.0, (strike-rate))/(1 + rate);
    }
    else {
      Double rate = lattice.getRate().getRate(t, u);
      Double qu = lattice.getRiskFreePropabilityForUp();
      Double qd = lattice.getRiskFreePropabilityForDown();
      Double su = getValue(t+1, u+1);
      Double sd = getValue(t+1, u);
      return (qu * su + qd * sd) / (1.0 + rate);
    }
  }

  @Override
  public String toString() {
    return "Floorlet (maturity: " + maturity
            + ", strike: " + strike
            + ", lattice: " + lattice + ")";
  }
  
  
}
