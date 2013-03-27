/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.lattice.BlackScholesLatticeConfiguration;
import de.mbuse.finance.binominal.security.ChooserOption;
import de.mbuse.finance.binominal.security.Future;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.Stock;

import de.mbuse.finance.binominal.security.Option.Region;
import de.mbuse.finance.binominal.security.Option.Type;
import de.mbuse.finance.binominal.lattice.Util;
import java.text.NumberFormat;
import java.util.Locale;
/**
 *
 * @author mbuse
 */
public class ProblemSet2 {
  public static final double T = 0.25;
  public static final double S_0 = 100.0;
  public static final double r = 0.02;
  public static final double sigma = 0.3;
  public static final double c = 0.01;
  
  
  public static final LatticeConfiguration config
          = new BlackScholesLatticeConfiguration(r, sigma, 15, T, c);
  public static final NumberFormat CUR_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  
  public static void main(String... args) {
    question1();
    question2();
    question3();
    question6();
    question7();
    question8();
  }
  
  public static void question1() {
    Stock stock = new Stock(S_0, config);
    Option opt = new Option(stock, 110.00, 15, Type.CALL, Region.AMERICAN);
    //Util.printLattice(opt, NumberFormat.getCurrencyInstance(), 15);
    System.out.println("Q1 - Price at t=0: " + CUR_FMT.format(opt.getPrice()));
  }
  
  public static void question2() {
    Stock stock = new Stock(S_0, config);
    Option opt = new Option(stock, 110.00, 15, Type.PUT, Region.AMERICAN);
    System.out.println("Q2 - Price at t=0: " + CUR_FMT.format(opt.getPrice()));
  }
  
  public static void question3() {
    Stock stock = new Stock(S_0, config);
    Option opt = new Option(stock, 110.00, 15, Type.PUT, Region.AMERICAN);
    Util.printLattice(opt.getShouldExecutes(), null, 15);
    
    System.out.println("Q3 - Earliest possible execution at t=" + opt.getEarliestExecutionPeriod());
  }
  
  public static void question6() {
    Stock stock = new Stock(S_0, config);
    Future future = new Future(stock, 15, config);
    Option opt = new Option(future, 110.00, 10, Type.CALL, Region.AMERICAN);
    
    Util.printLattice(stock, CUR_FMT, 15);
    Util.printLattice(future, CUR_FMT, 15);
    Util.printLattice(opt, CUR_FMT, 15);
    System.out.println("Q6 - Fair value : " + opt.getPrice());
  }
  
  public static void question7() {
    Stock stock = new Stock(S_0, config);
    Future future = new Future(stock, 15, config);
    Option opt = new Option(future, 110.00, 10, Type.CALL, Region.AMERICAN);
    
    Util.printLattice(opt.getShouldExecutes(), null, 15);
    
    System.out.println("Q7 - Earliest possible execution at t=" + opt.getEarliestExecutionPeriod());
  }
  
  public static void question8() {
    Stock stock = new Stock(S_0, config);
    Option call = new Option(stock, 100.00, 15, Type.CALL, Region.EUROPEAN );
    Option put  = new Option(stock, 100.00, 15, Type.PUT, Region.EUROPEAN );
    
    ChooserOption chooser = new ChooserOption(call, put, 10, config);
    
    Util.printLattice(call, CUR_FMT, 15);
    Util.printLattice(put, CUR_FMT, 15);
    Util.printLattice(chooser, CUR_FMT, 11);
    Util.printLattice(chooser.getPreferedOptions(), null, 11);
    
    System.out.println("Q8 - fair value of chooser : " + CUR_FMT.format(chooser.getPrice()));
    
  }
}
