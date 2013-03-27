/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal;
/**
 *
 * @author mbuse
 */
public interface Binominal<T> {
  
  public T getValue(int t, int upMoves);
  
  
}
