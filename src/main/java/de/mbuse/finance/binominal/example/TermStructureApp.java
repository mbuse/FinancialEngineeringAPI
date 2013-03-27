/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.lattice.TermStructureLatticeConfiguration;
import de.mbuse.finance.binominal.lattice.Util;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.rate.ShortRate;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.ZeroCouponBond;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class TermStructureApp {
  
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
    Rate shortRates = lattice.getRate();
    
    Util.print(lattice);
    Util.printLattice(shortRates, PERCENT_FMT, 5);
    
    ZeroCouponBond zcb = new ZeroCouponBond(lattice, 100.0, 4);
    Util.printLattice(zcb, MONEY_FMT, 5);
    
    Option americanZeroOption = new Option(zcb, 88.0, 3, Option.Type.PUT, Option.Region.AMERICAN);
    Util.printLattice(americanZeroOption, MONEY_FMT, 5);
    System.out.println("Price: " + americanZeroOption.getPrice());
    
    Option euroZeroOption = new Option(zcb, 84.0, 2, Option.Type.CALL, Option.Region.EUROPEAN);
    Util.printLattice(euroZeroOption, MONEY_FMT, 5);
    System.out.println("Price: " + euroZeroOption.getPrice());
  }
  
}
