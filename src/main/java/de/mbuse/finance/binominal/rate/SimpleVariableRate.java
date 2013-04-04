/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.rate;

import de.mbuse.finance.binominal.LatticeConfiguration;

/**
 *
 * @author mbuse
 */
public class SimpleVariableRate extends AbstractRate {

  private LatticeConfiguration lattice;
  private Double rateAtTimeZero;
  
  public SimpleVariableRate(Double rateAtTimeZero, LatticeConfiguration lattice) {
    this.rateAtTimeZero = rateAtTimeZero;
    this.lattice = lattice;
  }
  
  public Double getRate(int t, int upMoves) {
    assert t>=upMoves;
    if (t==0) {
      return rateAtTimeZero;
    }
    else if (upMoves>0) {
      return lattice.getUpFactor() * getValue(t-1, upMoves-1);
    }
    else {
      return lattice.getDownFactor() * getValue(t-1, 0);
    }
  }

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  @Override
  public String toString() {
    return "SimpleVariableRate (r0=" + rateAtTimeZero + ")";
  }
  
  
  
}
