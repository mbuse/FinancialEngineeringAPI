/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;

/**
 *
 * @author mbuse
 */
public class Swap implements Security {

  private LatticeConfiguration lattice;
  private Double fixedRate;
  private int maturity;
  private Type type = Type.PAYER;

  public Swap(LatticeConfiguration lattice, Double strike, int maturity) {
    this(lattice, strike, maturity, Type.PAYER);
  }
  
  public Swap(LatticeConfiguration lattice, Double strike, int maturity, Type type) {
    this.lattice = lattice;
    this.fixedRate = strike;
    this.maturity = maturity;
    this.type = type;
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
      return rateDifference(rate)/(1 + rate);
    }
    else {
      Double rate = lattice.getRate().getRate(t, u);
      Double qu = lattice.getRiskFreePropabilityForUp();
      Double qd = lattice.getRiskFreePropabilityForDown();
      Double su = getValue(t+1, u+1);
      Double sd = getValue(t+1, u);
      Double coupon = rateDifference(rate);
      return (coupon + qu * su + qd * sd) / (1.0 + rate);
    }
  }
  
  private double rateDifference(double rate) {
    return type.getFactor() * (rate - fixedRate);
  }
  
  /** returns the payments of the swap 
   *
   * Payments are arreal... that means: a payment p(t) is caluclated with the
   * rates at time t, but is paid out in the next period t+1.
   * 
   * Therefore the payment at time (t, u) is:
   * 
   * P(t,u) = (rate(t,u) - fixedRate)/(1 + rate(t,u)).
   */
  public Binominal<Double> getPayments() {
    return new Binominal<Double>() {

      @Override
      public String toString() { return "Payments of " + Swap.this;  }

      public Double getValue(int t, int u) {
        Double rate = lattice.getRate().getRate(t, u);
        return  (rate - fixedRate)/(1.0 + rate);
      }
    };
  }

  @Override
  public String toString() {
    return "Swap (maturity: " + maturity
            + ", strike: " + fixedRate
            + ", lattice: " + lattice + ")";
  }
  
  public static enum Type {
    /** Payer Swap pays (rate - fixedRate) **/
    PAYER(1),
    /** Receiver Swap pays (fixed - rate) **/
    RECEIVER(-1);

    private Type(int f) {
      factor = f;
    }

    public int getFactor() {
      return factor;
    }
    
    protected int factor;
    
  }
  
}
