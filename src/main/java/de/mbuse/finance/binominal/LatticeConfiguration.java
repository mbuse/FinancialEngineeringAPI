/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal;

import de.mbuse.finance.binominal.rate.Rate;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public abstract class LatticeConfiguration {

  public abstract Rate getRate();
  
  public abstract Double getUpFactor();
  public Double getDownFactor() {
    return 1.0/getUpFactor();
  }
  
  public abstract Double getRiskFreePropabilityForUp();
  public final Double getRiskFreePropabilityForDown() {
    return 1.0 - getRiskFreePropabilityForUp();
  }

  @Override
  public String toString() {
    NumberFormat pctFmt = new DecimalFormat("0.00###");
    return "Lattice (u: " + pctFmt.format(getUpFactor()) 
            + ", r: " +getRate()
            + ", q: " +pctFmt.format(getRiskFreePropabilityForUp()) + ")";
  }
  
}
