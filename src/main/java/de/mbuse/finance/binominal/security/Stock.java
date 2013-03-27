/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.LatticeConfiguration;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public class Stock implements Security {
  private LatticeConfiguration lattice;
  private Double startPrice;

  public Stock(Double startPrice, LatticeConfiguration lattice) {
    this.startPrice = startPrice;
    this.lattice = lattice;
  }
  
  public Double getValue(int t, int upMoves) {
    assert t > 0;
    assert t >= upMoves;
    
    double s = startPrice;
    
    for (int n=1; n<=upMoves; n++) {
      s = s * lattice.getUpFactor();
    }
    for (int n=upMoves+1; n<=t; n++) {
      s = s * lattice.getDownFactor();
    }
    return s;
  }

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return startPrice;
  }
  
  public String toString() {
    NumberFormat moneyFmt = NumberFormat.getCurrencyInstance();
    return "Stock (startPrice: " + moneyFmt.format(startPrice)
            + ", lattice config: " + lattice +")";
  }
  
  
}
