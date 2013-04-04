/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.portfolio.example;

import de.mbuse.finance.portfolio.Portfolio;

/**
 *
 * @author mbuse
 */
public class SharpesEffectivePortfolioExample {
  
  public static void main(String... args) {
    
    Portfolio portfolio = new Portfolio(3);
    
    portfolio.setPositions( 1, 1, 1);
    
    portfolio.setMeanReturns(0.060, 0.02, 0.04);
    
    portfolio.setCovariancesForPosition(0,  0.00800, -0.00200,   0.00400);
    portfolio.setCovariancesForPosition(1, -0.00200,  0.00200,  -0.00200);
    portfolio.setCovariancesForPosition(2,  0.00400, -0.00200,   0.00800);
    
    portfolio.setRiskFreeRate(0.01);
    
    
    System.out.println("Mean Returns   : " + portfolio.getMeanReturns());
    System.out.println("Covariances    : " + portfolio.getCovariances());
    System.out.println("Volatilities   : " + portfolio.getVolatilities());
    System.out.println("Risk free rate : " + portfolio.getRiskFreeRate());
    System.out.println("Excess Returns : " + portfolio.getExcessReturns());
    
    System.out.println("Portfolio mean return : " + portfolio.getPortfolioMeanReturn());
    System.out.println("Portfolio volatility  : " + portfolio.getPortfolioVolatility());
    
    Portfolio sharpeOptimizedPortfolio = portfolio.calculateSharpeOptimalPortfolio();
    
    System.out.println("Sharpe portfolio : " + sharpeOptimizedPortfolio.getPositions());
    System.out.println("Sharpe mean return : " + sharpeOptimizedPortfolio.getPortfolioMeanReturn());
    System.out.println("Sharpe portfolio volatility : " + sharpeOptimizedPortfolio.getPortfolioVolatility());
  }
}
