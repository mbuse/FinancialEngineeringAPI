/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.fincance.capm;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author mbuse
 */
public class Portfolio {
  private RealVector meanReturns;
  private RealMatrix covariances;
  private RealVector positions;
  private int numberOfPositions;
  private double riskFreeRate = 0.0;

  public Portfolio(int numberOfPositions) {
    this.meanReturns = new ArrayRealVector(numberOfPositions);
    this.covariances = new Array2DRowRealMatrix(numberOfPositions, numberOfPositions);
    this.positions = new ArrayRealVector(numberOfPositions);  
    this.numberOfPositions = numberOfPositions;
  }
  
  protected Portfolio(int numberOfPositions, RealVector meanReturns, RealMatrix covariances, RealVector positions, double riskFreeRate) {
    this.numberOfPositions = numberOfPositions;
    this.meanReturns = meanReturns;
    this.covariances = covariances;
    this.positions = positions;
    this.riskFreeRate = riskFreeRate;
  }

  public void setRiskFreeRate(double riskFreeRate) {
    this.riskFreeRate = riskFreeRate;
  }

  public double getRiskFreeRate() {
    return riskFreeRate;
  }
  
  public void setMeanReturns(double... returns) {
    assert returns.length == numberOfPositions;
    for (int i=0;i<numberOfPositions; i++) {
      meanReturns.setEntry(i, returns[i]);
    }
  }

  public void setPositions(double... pos) {
    assert pos.length == numberOfPositions;
    setVector(positions, pos);
  }
  
  public RealVector getPositions() {
    return positions;
  }
  
  public RealVector getNormalizedPositions() {
    return positions.mapDivide(positions.getL1Norm());
  }
  
  public void setCovariancesForPosition(int position, double... covs) {
    assert covs.length == numberOfPositions;
    assert 0 <= position && position < numberOfPositions;
    
    for (int i=0; i<numberOfPositions; i++) {
      setCovarianceForPositions(i, position, covs[i]);
    }
  }
  public void setCovarianceForPositions(int pos1, int pos2, double cov) {
    assert 0<=pos1 && pos1 < numberOfPositions;
    assert 0<=pos2 && pos2 < numberOfPositions;
    
    covariances.setEntry(pos1, pos2, cov);
    if (pos1!=pos2) {
      covariances.setEntry(pos2, pos1, cov);
    }
  }
  
  public int getNumberOfPositions() {
    return numberOfPositions;
  }

  public RealVector getMeanReturns() {
    return meanReturns;
  }
  
  public RealVector getExcessReturns() {
    return meanReturns.mapSubtract(riskFreeRate);
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
  
  public double getPortfolioMeanReturn() {
    RealVector normPos = getNormalizedPositions();
    double sum = 0.0;
    for (int i=0; i<numberOfPositions; i++) {
      sum += this.meanReturns.getEntry(i) * normPos.getEntry(i);
    }
    return sum;
  }
  
  public double getPortfolioVolatility() {
    RealVector normPos = getNormalizedPositions();
    return Math.sqrt(normPos.dotProduct(covariances.operate(normPos)));
  }
  
  public Portfolio calculateSharpeOptimalPortfolio() {
    RealMatrix inverseCovariances = new EigenDecomposition(covariances, 0.0001).getSolver().getInverse();
    RealVector sharpePositions = inverseCovariances.operate(getExcessReturns());
    
    double sumOfSharpePositions = sharpePositions.getL1Norm();  
    RealVector sharpePortfolio = sharpePositions.mapDivide(sumOfSharpePositions);
    
    return new Portfolio(numberOfPositions, meanReturns, covariances, sharpePortfolio, riskFreeRate);
  }
  
  private void setVector(RealVector v, double... values) {
    assert values.length == v.getDimension();
    for (int i=0;i<values.length; i++) {
      v.setEntry(i, values[i]);
    }
  }
}
