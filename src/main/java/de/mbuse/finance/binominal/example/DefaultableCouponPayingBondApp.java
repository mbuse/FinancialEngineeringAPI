package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.lattice.TermStructureLatticeConfiguration;
import de.mbuse.finance.binominal.security.CouponPayingBond;
import de.mbuse.finance.binominal.security.DefaultableCouponPayingBond;
import de.mbuse.finance.util.Out;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class DefaultableCouponPayingBondApp {
  
  public static final NumberFormat PERCENT_FMT = NumberFormat.getPercentInstance(Locale.US);
  public static final NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  
  static {
    PERCENT_FMT.setMaximumFractionDigits(2);
    PERCENT_FMT.setMinimumFractionDigits(2);
    
    CURRENCY_FMT.setMaximumFractionDigits(3);
    CURRENCY_FMT.setMinimumFractionDigits(3);
  }
  
  public static void main(String... args) {
    // simple static lattice (r=2.5%)
    LatticeConfiguration lattice = new TermStructureLatticeConfiguration(1., 1., 0.5, 0.025);
    
    Out.printLattice(lattice.getRate(), PERCENT_FMT, 5);
    
    Security cpb = new DefaultableCouponPayingBond(lattice, 2, 100., 0.05, 0.1, 0.02);
    
    Out.printLattice(cpb, CURRENCY_FMT, 4);
    Out.printPrice("Price", cpb);
   
    // === More complex ===
    
    lattice = new TermStructureLatticeConfiguration(1.1, 0.9, 0.5, 0.05);
    
    Out.print(lattice);
    Out.printLattice(lattice.getRate(), PERCENT_FMT, 10);
    
    final double a = 0.01;
    final double b = 1.01;
    Binominal<Double> hazardRate = new Binominal<Double>() {
      public Double getValue(int t, int u) {
        return a * Math.pow(b, u - t/2);
      }
      @Override
      public String toString() {
        return "hazard rate (a="+a+", b=" +b + ")";
      }
    };
    
    Out.printLattice(hazardRate, PERCENT_FMT, 10);
    
    Security zcb = new DefaultableCouponPayingBond(lattice, 10, 100.0, 0.0, 0.2, hazardRate);
    
    Out.printLattice(zcb, CURRENCY_FMT, 10);
    Out.printPrice("Zero-coupon-Bond price", zcb);
  }
}
