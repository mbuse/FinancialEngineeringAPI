/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.Rate;
import de.mbuse.finance.binominal.rate.ShortRate;

/**
 *
 * @author mbuse
 */
public class TermStructureLatticeConfiguration extends LatticeConfiguration {
  
  private Double upFactor;
  private Double downFactor;
  private Double riskFreePropabilityForUp;
  private Double rateAtTimeZero;

  public TermStructureLatticeConfiguration(Double upFactor, Double downFactor, Double riskFreePropabilityForUp, Double rateAtTimeZero) {
    this.upFactor = upFactor;
    this.downFactor = downFactor;
    this.riskFreePropabilityForUp = riskFreePropabilityForUp;
    this.rateAtTimeZero = rateAtTimeZero;
  }

  @Override
  public Rate getRate() {
    return new ShortRate(rateAtTimeZero, this);
  }
  
  public Double getRateAtTimeZero() {
    return rateAtTimeZero;
  }

  @Override
  public Double getUpFactor() {
    return upFactor;
  }

  @Override
  public Double getDownFactor() {
    return downFactor;
  }

  @Override
  public Double getRiskFreePropabilityForUp() {
    return riskFreePropabilityForUp;
  }
  
}
