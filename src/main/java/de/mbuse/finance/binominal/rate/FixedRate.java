/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.rate;

/**
 *
 * @author mbuse
 */
public class FixedRate extends AbstractRate {
 
  private Double fixedValueInPercent;

  public FixedRate(Double fixedValueInPercent) {
    this.fixedValueInPercent = fixedValueInPercent;
  }
  public Double getFixedRate() {
    return fixedValueInPercent;
  }
  
  @Override
  public String toString() {
    return fixedValueInPercent + " (fixed)";
  }

  public Double getRate(int t, int u) {
    return getFixedRate();
  }
  
  
}
