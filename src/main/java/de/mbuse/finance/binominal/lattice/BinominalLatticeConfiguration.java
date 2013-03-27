/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.FixedRate;
import de.mbuse.finance.binominal.rate.Rate;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public class BinominalLatticeConfiguration extends LatticeConfiguration {
  private Double upFactor;
  private FixedRate rate;
  private Double dividendFactor;

  public BinominalLatticeConfiguration(Double riskFreeRate, Double upFactor) {
    this(riskFreeRate, upFactor, 0.0);
  }
  public BinominalLatticeConfiguration(Double riskFreeRate, Double upFactor, Double dividendFactor) {
    this.upFactor = upFactor;
    this.rate = new FixedRate(riskFreeRate);
    this.dividendFactor = dividendFactor;
  }
  public Rate getRate() {
    return rate;
  }

  public Double getUpFactor() {
    return upFactor;
  }

  public Double getDividendFactor() {
    return dividendFactor;
  }
  
  @Override
  public Double getRiskFreePropabilityForUp() {
    return (rate.getFixedRate()-getDownFactor() -dividendFactor)/(upFactor - getDownFactor());
  }
  
  @Override
  public String toString() {
    NumberFormat pctFmt = new DecimalFormat("0.00###");
    return "Lattice (u: " + pctFmt.format(getUpFactor()) 
            + ", r: " +rate
            + ", c: " +pctFmt.format(dividendFactor) + ")";
  }
  
}
