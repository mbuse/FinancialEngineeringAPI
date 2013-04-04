/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.lattice.TermStructureLatticeConfiguration;
import de.mbuse.finance.binominal.lattice.Util;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.rate.SimpleVariableRate;
import de.mbuse.finance.binominal.security.CouponPayingBond;
import de.mbuse.finance.binominal.security.Future;
import de.mbuse.finance.binominal.security.ZeroCouponBond;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class CouponBondApp {
  public static final Double RATE_0 = 0.06;
  public static final Double UP = 1.25;
  public static final Double DOWN = 0.9;
  public static final Double Q = 0.5;
  public static final NumberFormat PERCENT_FMT = NumberFormat.getPercentInstance();
  public static final NumberFormat MONEY_FMT = NumberFormat.getCurrencyInstance();
  
  static {
    PERCENT_FMT.setMinimumFractionDigits(2);
    Locale.setDefault(Locale.US);
  }
  public static void main(String... args) {
    
    LatticeConfiguration lattice = new TermStructureLatticeConfiguration(UP, DOWN, Q, RATE_0);
    
    Util.print(lattice);
    Util.printLattice(lattice.getRate(), PERCENT_FMT, 5);
    
    CouponPayingBond cpb = new CouponPayingBond(lattice, 100.0, 0.0, 6);
    
    Util.printLattice(cpb, MONEY_FMT, 6);
    
    Util.printLattice(cpb.getValuesBeforeCoupon(), MONEY_FMT, 6);
    
    Security forwardOnCpb = cpb.createForward(4);
    Util.printLattice(forwardOnCpb, MONEY_FMT, 6);
    System.out.println("Forward Price: " + MONEY_FMT.format(forwardOnCpb.getPrice()));
    
    Future futureOnCpb = cpb.createFuture(4);
    Util.printLattice(futureOnCpb, MONEY_FMT, 6);
    System.out.println("Future Price: " + MONEY_FMT.format(futureOnCpb.getPrice()));
    
    // === Using Coupon Paying Bonds with coupon 0 for calculating ZCB prices...
    
    ZeroCouponBond zcb = new ZeroCouponBond(lattice, 100.0, 6);
    System.out.println("ZCB price            : " + MONEY_FMT.format(zcb.getPrice()));
    
    CouponPayingBond cpbZeroCoupon = new CouponPayingBond(lattice, 100.0, 0.0, 6);
    System.out.println("CPB (coupon=0) price : " + MONEY_FMT.format(cpbZeroCoupon.getPrice()));
    
    
  }
}
