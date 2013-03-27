/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.rate.ShortRate;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public class ZeroCouponBond implements Security{

  private LatticeConfiguration lattice;
  private int maturity;
  private Double faceValue;
  private Rate rate;

  public ZeroCouponBond(LatticeConfiguration lattice, Double faceValue, int maturity) {
    this.lattice = lattice;
    this.maturity = maturity;
    this.rate = lattice.getRate();
    this.faceValue = faceValue;
  }
  
  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int upMoves) {
    assert t >= upMoves;
    if (t > maturity) {
      return 0.0;
    }
    else if (t == maturity) {
      return faceValue;
    }
    else {
      Double currentRate = rate.getValue(t, upMoves);
      double discounting = 1/(1.0 + currentRate);
      return discounting * (lattice.getRiskFreePropabilityForUp() * getValue(t+1, upMoves+1) + lattice.getRiskFreePropabilityForDown() * getValue(t+1, upMoves));
    }
  }
  
  public String toString() {
    NumberFormat moneyFmt = NumberFormat.getCurrencyInstance();
    return "ZeroCouponBond (faceValue: " + moneyFmt.format(faceValue)
            + ", maturity: " + maturity
            + ", shortRate: " + rate
            + ", lattice config: " + lattice +")";
  }
  
}
