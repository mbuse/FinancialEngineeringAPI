/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.util;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.Security;
import java.io.PrintStream;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author mbuse
 */
public class Out {
  
  public static final NumberFormat DECIMAL_FMT = NumberFormat.getInstance(Locale.US);
  public static final NumberFormat MONEY_FMT = NumberFormat.getCurrencyInstance(Locale.US);

  private static PrintStream out = System.out;
  private static boolean silent = false;
  
  public static void setSilent(boolean flag) {
    silent = flag;
  }
  
  public static void setOut(PrintStream o) {
    out = o;
  }
  
  public static <T> void printLattice(Binominal<T> lattice, Format fmt, int periods) {
    if (silent) return;
    out.println("===================================================================");
    out.println(lattice);
    out.println("-------------------------------------------------------------------");
    for (int t=0; t<=periods; t++) {
      out.print("t=" + t + "\t");
      for (int u=0; u<=t; u++) {
        T value = lattice.getValue(t, u);
        String stringRepresentation = (fmt==null) ? value.toString() : fmt.format(value);
        out.print(stringRepresentation + "\t");
      }
      out.print("\n");
    }
  }
  public static void print(LatticeConfiguration config) {
    if (silent) return;
    out.println("===================================================================");
    out.println(config);
  }
  
  public static void printPrice(String msg, Security sec) {
    if (silent) return;
    printPrice(msg, sec.getPrice());
  }
  public static void printPrice(String msg, Format fmt, Security sec) {
    if (silent) return;
    printValue(msg, fmt, sec.getPrice());
  }
  
  public static void printPrice(String msg, Double price) {
    if (silent) return;
    printValue(msg, MONEY_FMT, price);
  }
  public static void printValue(String msg, Format fmt, Double value) {
    if (silent) return;
    out.println(msg + " : " + fmt.format(value));
  }
  
  public static void printPrices(String msg, double... prices) {
    if (silent) return;
    print(msg, MONEY_FMT, prices);
  }
  
  public static void print(String msg, double... values) {
    if (silent) return;
    print(msg, DECIMAL_FMT, values);
  }
  
  public static void print(String msg, Format fmt, double... values) {
    if (silent) return;
    out.print(msg + " : ");
    for (int i=0; i<values.length; i++) {
      if (i==values.length-1) {
        out.print(fmt.format(values[i]));
      }
      else {
        out.print(fmt.format(values[i]) + "\t");
      }
    }
    out.println();
  } 
}
