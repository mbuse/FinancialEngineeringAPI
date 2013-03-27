/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.rate;

import de.mbuse.finance.binominal.Binominal;

/**
 *
 * @author mbuse
 */
public interface Rate extends Binominal<Double> {
  
  Double getRate(int t, int u);
  Double getRateFactor(int t, int u);
  
}
