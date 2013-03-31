/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.rate.Rate;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public class CouponPayingBond implements Security {
  
  private LatticeConfiguration lattice;
  private Double faceValue;
  private Double coupon;
  private int maturity;

  public CouponPayingBond(LatticeConfiguration lattice, Double faceValue, Double coupon, int maturity) {
    this.lattice = lattice;
    this.faceValue = faceValue;
    this.coupon = coupon;
    this.maturity = maturity;
  }
  

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int u) {
    assert t>=u;
    assert u>=0;
    
    if (t>maturity) {
      return 0.0;
    }
    else if (t==maturity) {
      return faceValue + getCouponValue(t);
    }
    else {
      Double rateFactor = lattice.getRate().getRateFactor(t, u);
      Double qu = lattice.getRiskFreePropabilityForUp();
      Double qd = lattice.getRiskFreePropabilityForDown();
      return getCouponValue(t) + (qu * getValue(t+1, u+1) + qd*getValue(t+1, u))/rateFactor;
    }
  }
  
  public Double getValueBeforeCoupon(int t, int u) {
    return getValue(t, u) - getCouponValue(t);
  }
  
  public Security getValuesBeforeCoupon() {
    return new Security() {

      public LatticeConfiguration getLattice() {
        return lattice;
      }

      public Double getPrice() {
        return getValue(0, 0);
      }

      
      public Double getValue(int t, int upMoves) {
        return getValueBeforeCoupon(t, upMoves);
      }
      
      @Override
      public String toString() {
        return "Values before Coupon payment for " + CouponPayingBond.this.toString();
      }
      
    };
  }
  
  public Future createFuture(int expiration) {
    return new Future(getValuesBeforeCoupon(), expiration, lattice);
  }
  
  public Security createForward(final int expiration) {
    assert expiration>0 && expiration <= maturity;
    final ZeroCouponBond zcbFV1 = new ZeroCouponBond(lattice, 1.0, expiration);
    Security forward = new Security() {

      public LatticeConfiguration getLattice() {
        return lattice;
      }

      public Double getPrice() {
        return getValue(0,0) / zcbFV1.getPrice();
      }

      public Double getValue(int t, int u) {
        assert t>=0 && u>=0;
        assert u<=t;
        if (t> expiration) {
          return 0.0;
        }
        else if (t == expiration) {
          return getValueBeforeCoupon(t, u);
        }
        else {
          double qu = lattice.getRiskFreePropabilityForUp();
          double qd = lattice.getRiskFreePropabilityForDown();
          double rateFactor = lattice.getRate().getRateFactor(t, u);
          return (qu * getValue(t+1, u+1) + qd * getValue(t+1, u))/rateFactor;
        }
      }

      @Override
      public String toString() {
        return "Forward (expiration=" + expiration
                + ") on " + CouponPayingBond.this.toString();
      }
      
      
    };
    return forward;
  }
  
  public Double getCouponValue(int t) {
    assert t>=0;
    if (t>maturity) {
      return 0.0;
    }
    else {
      return faceValue * coupon;
    }
  }

  @Override
  public String toString() {
    return "Coupon Bond (faceValue: " + NumberFormat.getCurrencyInstance().format(faceValue)
            + ", coupon: " + coupon
            + ", maturity: " + maturity
            + ", lattice config: " + lattice +")";
  }
  
  
  
}
