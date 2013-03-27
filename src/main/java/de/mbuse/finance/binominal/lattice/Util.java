/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.Binominal;
import java.text.Format;

/**
 *
 * @author mbuse
 */
public class Util {
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
}
