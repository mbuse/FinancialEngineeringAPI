/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.fincance.capm.example;

import de.mbuse.fincance.capm.Portfolio;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author mbuse
 */
public class SharpesEffectivePortfolioExample {
  
  public static void main(String... args) {
    
    Portfolio portfolio = new Portfolio(3);
    
    portfolio.setMeanReturns(0.060, 0.02, 0.04);
    
    portfolio.setCovariancesForPosition(0,  0.00800, -0.00200,   0.00400);
    portfolio.setCovariancesForPosition(1, -0.00200,  0.00200,  -0.00200);
    portfolio.setCovariancesForPosition(2,  0.00400, -0.00200,   0.00800);
    
    
    System.out.println("Mean Returns : " + portfolio.getMeanReturns());
    System.out.println("Covariances  : " + portfolio.getCovariances());
    System.out.println("Volatilities : " + portfolio.getVolatilities());
    
    final double interestRate = 0.01;
    
    RealVector excessReturn = portfolio.getMeanReturns().mapSubtract(interestRate);
    
    System.out.println("Excess Return  : " + excessReturn);
    
    RealMatrix inverseCovariances = new EigenDecomposition(portfolio.getCovariances(), 0.0001).getSolver().getInverse();
    RealVector sharpePositions = inverseCovariances.operate(excessReturn);
    
    double sumOfSharpePositions = sharpePositions.getL1Norm();  
    
    System.out.println("Sharpe positions : " + sharpePositions );
    System.out.println("Sum              : " + sumOfSharpePositions);
    
    RealVector sharpePortfolio = sharpePositions.mapDivide(sumOfSharpePositions);
    
    System.out.println("Sharpe portfolio : " + sharpePortfolio);
  }
}
