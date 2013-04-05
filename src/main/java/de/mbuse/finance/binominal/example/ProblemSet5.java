/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.lattice.TermStructureLatticeConfiguration;
import de.mbuse.finance.util.Out;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.security.CouponPayingBond;
import de.mbuse.finance.binominal.security.ElementarySecurity;
import de.mbuse.finance.binominal.security.ForwardSwap;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.Swap;
import de.mbuse.finance.binominal.security.ZeroCouponBond;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class ProblemSet5 {
  
  public static final int MATURITY = 10;
  public static final Double RATE_0 = 0.05;
  public static final Double UP = 1.1;
  public static final Double DOWN = 0.9;
  public static final Double Q = 0.5;
  public static final NumberFormat PERCENT_FMT = NumberFormat.getPercentInstance();
  public static final NumberFormat MONEY_FMT = NumberFormat.getCurrencyInstance();
  public static final NumberFormat NUMBER_FMT = new DecimalFormat("0.00000");
  
  static {
    PERCENT_FMT.setMinimumFractionDigits(2);
    Locale.setDefault(Locale.US);
  }
  
  public static void main(String... args) {
    final LatticeConfiguration lattice = new TermStructureLatticeConfiguration(UP, DOWN, Q, RATE_0);
    final Rate shortRates = lattice.getRate();
    
    Out.print(lattice);
    Out.printLattice(shortRates, PERCENT_FMT, MATURITY);
    
    // Q1 : Zero Coupon Bond price
    CouponPayingBond zcb = new CouponPayingBond(lattice, 100.0, 0.0, MATURITY);
    //Util.printLattice(zcb, MONEY_FMT, MATURITY);
    outPrice("Q1: Price of ZCB", zcb);
    
    // Q2 : Forward on ZCB
    Security forward = zcb.createForward(4);
    outPrice("Q2: Price of Fwd ZCB : ", forward);
    
    // Q3 : Future on ZCB
    Security future = zcb.createFuture(4);
    outPrice("Q3: Price of Fut ZCB : ", future);
    
    // Q4 : American Call Option on ZCB (maturity: 6, strike: 80
    Option optionZCB = new Option(zcb, 80.0, 6, Option.Type.CALL, Option.Region.AMERICAN);
    outPrice("Q4: Price of Option on ZCB", optionZCB);  
    
    // Q5 : Compute the initial value of a forward-starting swap that begins at t=1, with maturity t=10 and a fixed rate of 4.5%.
    final int start = 1;
    final int maturity = MATURITY;
    final double fixedRate = 0.045;
    final double nominal = 1000000.;
    
    Binominal<Double> forwardSwapPmts = new Binominal<Double>() {

      public Double getValue(int t, int u) {
        if (t>=start && t<=maturity) {
          Double rate = shortRates.getRate(t, u);       
          return (fixedRate-rate)/(1.0 + rate);
        }
        else {
          return 0.0;
        }
      }

      @Override
      public String toString() {
        return "Forward Swap Payments";
      }
      
    };
    
    ElementarySecurity esec = new ElementarySecurity(lattice);
    Out.printLattice(esec, NUMBER_FMT, MATURITY);
    Out.printLattice(forwardSwapPmts, PERCENT_FMT, MATURITY);
    Out.printPrice("Q5", nominal * esec.calculatePrice(forwardSwapPmts, MATURITY));
    
    ForwardSwap fwdswp = new ForwardSwap(lattice, fixedRate, start, maturity);
    Out.printLattice(fwdswp, NUMBER_FMT, maturity);
    Out.printPrice("Q5 (alternativ)", nominal * fwdswp.getPrice());
    
    // Q6: Compute the initial price of a swaption that matures at time t=5 and has a strike of 0.
    
    Option swaption = new Option(fwdswp, 0.0, 5, Option.Type.CALL, Option.Region.EUROPEAN);
    Out.printPrice("Q6", nominal * swaption.getPrice());
  }
  
  private static void outPrice(String msg, Security sec) {
    outPrice(msg, sec.getPrice());
  }
  private static void outPrice(String msg, Double price) {
    System.out.println(msg + " : " + MONEY_FMT.format(price));
  }
}
