/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.lattice.TermStructureLatticeConfiguration;
import de.mbuse.finance.binominal.lattice.Util;
import de.mbuse.finance.binominal.security.CouponPayingBond;
import de.mbuse.finance.binominal.security.ElementarySecurity;
import de.mbuse.finance.binominal.security.Swap;
import de.mbuse.finance.binominal.security.ZeroCouponBond;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class ElementarySecurityApp {
  
  private final static NumberFormat PERCENT_FMT = new DecimalFormat("#.0000", DecimalFormatSymbols.getInstance(Locale.US));
  private final static NumberFormat MONEY_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  public static final Double RATE_0 = 0.06;
  public static final Double UP = 1.25;
  public static final Double DOWN = 0.9;
  public static final Double Q = 0.5;
  public static final Double FACE_VALUE = 100.0;
  public static final int MATURITY = 4;
  public static final double COUPON = 0.1;
  
  
  public static void main(String... args) {
    final LatticeConfiguration lattice = new TermStructureLatticeConfiguration(UP, DOWN, Q, RATE_0);
    
    System.out.println("Lattice: " + lattice);
    
    ElementarySecurity esec = new ElementarySecurity(lattice);
    
    Util.printLattice(esec, PERCENT_FMT, 10);
    
    // calculate Zero Coupon Bond (mat=4) price based on Elementary Security:
    
    Double calcPrice = 0.0;
    for (int u=0; u<=4; u++) {
      calcPrice += 100 * esec.getValue(4, u);
    }
    System.out.println("Calc price of ZCB(fv:100,m:4) : " +  MONEY_FMT.format(calcPrice) );
    
    ZeroCouponBond zcb = new ZeroCouponBond(lattice, FACE_VALUE, MATURITY);
    System.out.println("Real price of ZCB(fv:100,m:4) : " + MONEY_FMT.format(zcb.getPrice()));
    
    System.out.println("=== COUPON BOND === ");
    CouponPayingBond cpb = new CouponPayingBond(lattice, FACE_VALUE, COUPON, MATURITY);
    
    Binominal<Double> cpbPayoffs = new Binominal<Double>() {

      public Double getValue(int t, int u) {
        assert t >= u && u >= 0;
        
        if (t>MATURITY) {
          return 0.0;
        }
        else if (t==MATURITY) {
          return (1 + COUPON) * FACE_VALUE;
        }
        else {
          return COUPON * FACE_VALUE;
        }
      }

      @Override
      public String toString() {
        return "Payouts of a coupon bond.";
      }
    };
    
    Util.printLattice(cpbPayoffs, MONEY_FMT, MATURITY+1);
    
    System.out.println("Coupon Bond Price (Elementary Security Pricing)    : " + MONEY_FMT.format(esec.calculatePrice(cpbPayoffs, MATURITY)));
    System.out.println("Coupon Bond Price (Coupon Paying Bond calculation) : " + MONEY_FMT.format(cpb.getPrice()));  

    final double fixed = 0.05;
    final double nominal = 1000000.;
    
    Swap swap = new Swap(lattice, fixed, 3);
    Util.printLattice(swap, PERCENT_FMT, 3);
    Util.printPrice("Swap Price", swap.getPrice() * nominal);
    
    Binominal<Double> forwardSwap = new Binominal<Double>() {
      public Double getValue(int t, int u) {
        if (t>=1 && t <3) {
          Double rate = lattice.getRate().getRate(t, u);
          return  (rate-fixed)/(1.0 + rate);
        }
        else {
          return 0.0;
        }
      }
    };
    Util.printLattice(forwardSwap, PERCENT_FMT, 5);
    Util.printPrice("Forward Swap Price", nominal * esec.calculatePrice(forwardSwap, 3));
  }
}
