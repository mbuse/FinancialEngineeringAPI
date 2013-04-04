/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.Security;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class Util {
  
  public static final NumberFormat MONEY_FMT = NumberFormat.getCurrencyInstance(Locale.US);
  
  public static <T> void printLattice(Binominal<T> lattice, Format fmt, int periods) {
    System.out.println("===================================================================");
    System.out.println(lattice);
    System.out.println("-------------------------------------------------------------------");
    for (int t=0; t<=periods; t++) {
      System.out.print("t=" + t + "\t");
      for (int u=0; u<=t; u++) {
        T value = lattice.getValue(t, u);
        String stringRepresentation = (fmt==null) ? value.toString() : fmt.format(value);
        System.out.print(stringRepresentation + "\t");
      }
      System.out.print("\n");
    }
  }
  public static void print(LatticeConfiguration config) {
    System.out.println("===================================================================");
    System.out.println(config);
  }
  
  public static void printPrice(String msg, Security sec) {
    printPrice(msg, sec.getPrice());
  }
  public static void printPrice(String msg, Format fmt, Security sec) {
    printValue(msg, fmt, sec.getPrice());
  }
  
  public static void printPrice(String msg, Double price) {
    printValue(msg, MONEY_FMT, price);
  }
  public static void printValue(String msg, Format fmt, Double price) {
    System.out.println(msg + " : " + fmt.format(price));
  }
  
  public static void printPrices(String msg, double... prices) {
    printValues(msg, MONEY_FMT, prices);
  }
  
  public static void printValues(String msg, Format fmt, double... prices) {
    System.out.print(msg + " : ");
    for (double p : prices) {
      System.out.print(fmt.format(p) + "\t");
    }
    System.out.println();
  } 
}
