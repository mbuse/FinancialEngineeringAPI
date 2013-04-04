/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Compounding;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.lattice.BlackDermanToyLatticeConfiguration;
import de.mbuse.finance.binominal.lattice.Util;
import de.mbuse.finance.binominal.security.ElementarySecurity;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class BlackDermanToyApp {
  
  public final static NumberFormat PERCENT_FMT = NumberFormat.getPercentInstance(Locale.US);
  public final static NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  
  static {
    PERCENT_FMT.setMaximumFractionDigits(2);
    PERCENT_FMT.setMinimumFractionDigits(2);
    
    CURRENCY_FMT.setMaximumFractionDigits(3);
    CURRENCY_FMT.setMinimumFractionDigits(3);
  }
  
  public static void main(String... args) {
    
    double a[] = new double[14]; Arrays.fill(a, 0.05);
    double b   = 0.005;
    double q   = 0.5;
    
    // Create Black-Derman-Toy lattice
    
    LatticeConfiguration lattice = new BlackDermanToyLatticeConfiguration(q, a, b);
    
    Util.print(lattice);
    Util.printLattice(lattice.getRate(), PERCENT_FMT, a.length);
    
    // Elementary Security prices
    
    ElementarySecurity elementarySecurity = new ElementarySecurity(lattice);
    Util.printLattice(elementarySecurity, CURRENCY_FMT, a.length);
    
    double[] prices = elementarySecurity.getZCBPrices(a.length);
    Util.printValues("ZCB Prices", CURRENCY_FMT, prices);
    
    // Spot Rates
    
    double[] spotRates = Compounding.invSpotRates(prices);
    Util.printValues("Spot Rates", PERCENT_FMT, spotRates);
  }
  
}
