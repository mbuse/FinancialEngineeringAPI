/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.fincance.capm;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author mbuse
 */
public class Portfolio {
  private RealVector meanReturns;
  private RealMatrix covariances;
  private int numberOfPositions;

  public Portfolio(int numberOfPositions) {
    this.meanReturns = new ArrayRealVector(numberOfPositions);
    this.covariances = new Array2DRowRealMatrix(numberOfPositions, numberOfPositions);
    this.numberOfPositions = numberOfPositions;
  }

  public void setMeanReturns(double... returns) {
    assert returns.length == numberOfPositions;
    for (int i=0;i<numberOfPositions; i++) {
      meanReturns.setEntry(i, returns[i]);
    }
  }
  
  public void setCovariancesForPosition(int position, double... covs) {
    assert covs.length == numberOfPositions;
    assert 0 <= position && position < numberOfPositions;
    
    for (int i=0; i<numberOfPositions; i++) {
      covariances.setEntry(i, position, covs[i]);
      covariances.setEntry(position, i, covs[i]);
    }
  }
  
  public int getNumberOfPositions() {
    return numberOfPositions;
  }

  public RealVector getMeanReturns() {
    return meanReturns;
  }

  public RealMatrix getCovariances() {
    return covariances;
  }
  
  public RealVector getVolatilities() {
    RealVector volatilities = new ArrayRealVector(numberOfPositions);
    for (int i=0; i<numberOfPositions; i++) {
      volatilities.setEntry(i, Math.sqrt(covariances.getEntry(i, i)));
    }
    return volatilities;
  }
  
  
  
}
