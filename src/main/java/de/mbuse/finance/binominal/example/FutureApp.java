/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.util.Out;
import de.mbuse.finance.binominal.lattice.BlackScholesLatticeConfiguration;
import de.mbuse.finance.binominal.security.Future;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.Stock;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class FutureApp {
  
  public static void main(String... args) {
    double rate = 0.02;
    double volatility = 0.30;
    int periods = 10;
    double time = 0.25;
    double dividendYield = 0.01;
    
    Locale.setDefault(Locale.US);
    
    LatticeConfiguration lattice = new BlackScholesLatticeConfiguration(rate, volatility, periods, time, dividendYield);
    
    
    Stock security = new Stock(100.0, lattice);
    
    Future future = new Future(security, 10, lattice);
    
    Option option = new Option(future, 100.0, 10, Option.Type.CALL, Option.Region.EUROPEAN);
    
    Out.print(lattice);
    Out.printLattice(security, NumberFormat.getCurrencyInstance(), periods);
    Out.printLattice(future, NumberFormat.getCurrencyInstance(), periods);
    Out.printLattice(option, NumberFormat.getCurrencyInstance(), periods);
    
    
    
    
    
  }
}
