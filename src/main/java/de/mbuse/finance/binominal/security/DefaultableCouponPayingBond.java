package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.rate.FixedRate;

/**
 *
 * @author mbuse
 */
public class DefaultableCouponPayingBond implements Security {
  
  private LatticeConfiguration lattice;
  private Double faceValue;
  private Double coupon;
  private Double recovery;
  private Binominal<Double> hazardRate;
  private int maturity;

  public DefaultableCouponPayingBond(LatticeConfiguration lattice,  int maturity, Double faceValue, Double coupon, Double recovery, Binominal<Double> hazardRate) {
    this.lattice = lattice;
    this.faceValue = faceValue;
    this.coupon = coupon;
    this.recovery = recovery;
    this.hazardRate = hazardRate;
    this.maturity = maturity;
  }
  
  public DefaultableCouponPayingBond(LatticeConfiguration lattice,  int maturity, Double faceValue, Double coupon, Double recovery, double hazardRate) {
    this(lattice, maturity, faceValue, coupon, recovery, new FixedRate(hazardRate));
  }

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0, 0);
  }

  public Double getValue(int t, int u) {
    return getValue(t, u, false);
  }
  
  protected Double getValue(int t, int u, boolean hasDefaulted) {
    assert t>=u && u>=0;
    if (hasDefaulted) {
      // if defaulted, we pay the recovery of the face value, no coupon.
      return recovery * faceValue;
    }
    else {
      if (t>maturity) {
        return 0.0;
      }
      if (t==maturity) {
        return (1 + coupon) * faceValue;
      }
      else {
        double discount = 1.0/lattice.getRate().getRateFactor(t, u);
        double qu = lattice.getRiskFreePropabilityForUp();
        double qd = lattice.getRiskFreePropabilityForDown();
        double h1 = hazardRate.getValue(t, u);
        double h0 = 1.0 - h1;
        
        double onePeriodAhead 
                = (qu * h0 * getValue(t+1, u+1, false))
                + (qd * h0 * getValue(t+1, u, false))
                + (qu * h1 * getValue(t+1, u+1, true))
                + (qd * h1 * getValue(t+1, u  , true));
       
        return getCouponPayment(t) + discount * onePeriodAhead;
      }
    }
  }
  
  protected double getCouponPayment(int t) {
    assert t>=0;
    if (t>maturity) {
      return 0.0;
    }
    else if (t==0) {
      return 0.0;
    }
    else {
      return coupon * faceValue;
    }
  }
  
  
  
  
}
