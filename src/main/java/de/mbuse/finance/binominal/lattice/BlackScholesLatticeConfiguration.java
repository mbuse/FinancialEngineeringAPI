/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.lattice;

import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.FixedRate;
import de.mbuse.finance.binominal.rate.Rate;

/**
 *
 * @author mbuse
 */
public class BlackScholesLatticeConfiguration extends LatticeConfiguration {
  
  private Double rate;
  private Double volatility;
  private int numberOfPeriods;
  private Double time;
  private Double dividendFactor;

  /**
   * 
   * @param rate            the continiously compounded annual interest rate
   * @param volatility      the annualized volatility (sigma)
   * @param numberOfPeriods the number of periods in the binominal model
   * @param time            the time T, which defines the time length of the model.
   * @param dividendFactor  the dividents factor c 
   */
  public BlackScholesLatticeConfiguration(Double rate, Double volatility, int numberOfPeriods, Double time, Double dividendFactor) {
    this.rate = rate;
    this.volatility = volatility;
    this.numberOfPeriods = numberOfPeriods;
    this.time = time;
    this.dividendFactor = dividendFactor;
  }
  
  @Override
  public Double getUpFactor() {
    return Math.exp(volatility * Math.sqrt(time/numberOfPeriods));
  }

  public Double getRiskFreeFactor() {
    return Math.exp(rate * time/numberOfPeriods);
  }

  @Override
  public Rate getRate() {
    return new FixedRate(getRiskFreeFactor() - 1.0);
  }

  @Override
  public Double getRiskFreePropabilityForUp() {
    return (Math.exp((rate-dividendFactor)*time/numberOfPeriods) - getDownFactor())/(getUpFactor() - getDownFactor());
  }
  
  @Override
  public String toString() {
    return "Black-Scholes (r: " + rate
            + ", volatility: " + volatility
            + ", time: " + time + "y"
            + ", periods: " + numberOfPeriods + ")";
  }
  
  
  
}
