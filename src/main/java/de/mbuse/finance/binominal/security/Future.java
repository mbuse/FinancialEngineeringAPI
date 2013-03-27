/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.LatticeConfiguration;

/**
 *
 * @author mbuse
 */
public class Future implements Security {
  
  private LatticeConfiguration lattice;
  private Security security;
  private int maturity;
  private Double q;

  public Future(Security tradeable, int maturity, LatticeConfiguration lattice) {
    this.lattice = lattice;
    this.security = tradeable;
    this.maturity = maturity;
    this.q = lattice.getRiskFreePropabilityForUp();
  }
  
  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public int getMaturity() {
    return maturity;
  }

  public Security getTradeable() {
    return security;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int upMoves) {
    assert t >= upMoves;
    assert upMoves >= 0;
    
    if (t > maturity) {
      return 0.0;
    }
    if (t == maturity) {
      return security.getValue(t, upMoves);
    }
    else {
      Double su = getValue(t+1, upMoves+1);
      Double sd = getValue(t+1, upMoves);
      
      return (q * su + (1-q) * sd);
    }
  }

  @Override
  public String toString() {
    return "Future on " + security + " (maturity: " + maturity + ", lattice: " + lattice + ")"; 
  }
  
  
  
}
