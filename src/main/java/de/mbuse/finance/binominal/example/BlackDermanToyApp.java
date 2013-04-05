/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Compounding;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.lattice.BlackDermanToyLatticeConfiguration;
import de.mbuse.finance.binominal.security.ElementarySecurity;
import de.mbuse.finance.optimization.Optimizer;
import de.mbuse.finance.util.ArrayUtil;
import de.mbuse.finance.util.Out;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.math3.analysis.MultivariateFunction;

/**
 *
 * @author mbuse
 */
public class BlackDermanToyApp {
  
  public final static NumberFormat PERCENT_FMT = NumberFormat.getPercentInstance(Locale.US);
  public final static NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  
  public final static double[] MARKET_SPOT_RATES = new double[] 
    { 0.0, 0.073, 0.0762, 0.081, 0.0845, 0.092, 0.0964, 0.1012, 0.1045, 0.1075, 0.1122, 0.1155, 0.1192, 0.122, 0.1232 };

  
  static {
    PERCENT_FMT.setMaximumFractionDigits(2);
    PERCENT_FMT.setMinimumFractionDigits(2);
    
    CURRENCY_FMT.setMaximumFractionDigits(3);
    CURRENCY_FMT.setMinimumFractionDigits(3);
  }
  
  public static void main(String... args) {
    
    final double initialA[] = new double[14]; Arrays.fill(initialA, 0.05);
    final double b   = 0.005;
    final double q   = 0.5;
    
    // Create Black-Derman-Toy lattice
    
    LatticeConfiguration lattice = new BlackDermanToyLatticeConfiguration(q, initialA, b);
    
    Out.print(lattice);
    Out.printLattice(lattice.getRate(), PERCENT_FMT, initialA.length);
    
    // Elementary Security prices
    
    ElementarySecurity elementarySecurity = new ElementarySecurity(lattice);
    Out.printLattice(elementarySecurity, CURRENCY_FMT, initialA.length);
    
    double[] prices = elementarySecurity.getZCBPrices(initialA.length);
    Out.print("ZCB Prices", CURRENCY_FMT, prices);
    
    // Spot Rates
    
    double[] spotRates = Compounding.invSpotRates(prices);
    Out.print("ZCB Spot Rates   ", PERCENT_FMT, spotRates);
    Out.print("Market Spot Rates", PERCENT_FMT, MARKET_SPOT_RATES);
    
    // Differences
    
    Out.print("Squared Differences" , ArrayUtil.squaredDifference(MARKET_SPOT_RATES, spotRates));
    Out.print("Squared Distance   " , ArrayUtil.squaredDistance(MARKET_SPOT_RATES, spotRates));
    
    // Solver...
    
    
    MultivariateFunction objectiveFunction = new MultivariateFunction() {

      public double value(double[] point) {
        LatticeConfiguration lattice = new BlackDermanToyLatticeConfiguration(q, point, b);
        ElementarySecurity elementarySecurity = new ElementarySecurity(lattice);
        double[] prices = elementarySecurity.getZCBPrices(initialA.length);
        double[] spotRates = Compounding.invSpotRates(prices);
        return ArrayUtil.squaredDistance(MARKET_SPOT_RATES, spotRates);
      }
    };
 
    Optimizer solver = new Optimizer();
    solver.setObjectiveFunction(objectiveFunction);
    solver.setInitialParameters(initialA);
    solver.setMaxAbsoluteDifference(0.00001);
    solver.minimize();
    double[] optimizedParams = solver.getOptimizedParameters();
    
    Out.print("Optimized a[t] : ", PERCENT_FMT, optimizedParams);
  }
  
}
