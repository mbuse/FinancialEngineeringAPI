/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal;

import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;

/**
 *
 * @author mbuse
 */
public interface Security extends Binominal<Double> {
  
  LatticeConfiguration getLattice();
  
  Double getPrice();
}
