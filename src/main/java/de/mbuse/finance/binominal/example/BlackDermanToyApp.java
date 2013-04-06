/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.Compounding;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.lattice.BlackDermanToyLatticeConfiguration;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.security.ElementarySecurity;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.Swap;
import de.mbuse.finance.optimization.Optimizer;
import de.mbuse.finance.util.ArrayUtil;
import de.mbuse.finance.util.Out;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    
    final double a[] = ArrayUtil.init(14, 0.05);
    final double b[] = ArrayUtil.init(14, 0.005);
    final double q   = 0.5;
    
    // Create Black-Derman-Toy lattice
    
    LatticeConfiguration lattice = new BlackDermanToyLatticeConfiguration(q, a, b);
    
    Out.print(lattice);
    Out.printLattice(lattice.getRate(), PERCENT_FMT, a.length);
    
    // Elementary Security prices
    
    ElementarySecurity elementarySecurity = new ElementarySecurity(lattice);
    Out.printLattice(elementarySecurity, CURRENCY_FMT, a.length);
    
    double[] prices = elementarySecurity.getZCBPrices(a.length);
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
        double[] prices = elementarySecurity.getZCBPrices(a.length);
        double[] spotRates = Compounding.invSpotRates(prices);
        return ArrayUtil.squaredDistance(MARKET_SPOT_RATES, spotRates);
      }
    };
 
    /**
     * calibrate the parameter a in order to get spot rates matching the 
     * observed market spot rates.
     */
    Optimizer solver = new Optimizer();
    solver.setObjectiveFunction(objectiveFunction);
    solver.setInitialParameters(a);
    solver.setMaxAbsoluteDifference(0.00001);
    solver.minimize();
    double[] optimizedParams = solver.getOptimizedParameters();
    
    Out.print("Optimized a[t] : ", PERCENT_FMT, optimizedParams);
    
    /* Use the calibrated lattice to calculate the price of a Payer Swaption
     */
    LatticeConfiguration calibratedLattice = new BlackDermanToyLatticeConfiguration(q, optimizedParams, b);
    Out.print(calibratedLattice);
    Rate calibratedRate = lattice.getRate();
    Out.printLattice(calibratedRate, PERCENT_FMT, 10);
    
    Swap payerSwap = new Swap(calibratedLattice, 0.1165, 10, Swap.Type.PAYER);
    Out.printLattice(payerSwap, PERCENT_FMT, 10);
    
    Option swaption = new Option(payerSwap, 0.0, 2, Option.Type.CALL, Option.Region.EUROPEAN);
    
    Out.printLattice(swaption, new DecimalFormat("0.00000"), 2);
  }
  
}
