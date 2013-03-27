/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.Rate;
import java.text.NumberFormat;

/**
 *
 * @author mbuse
 */
public class Option implements Security {

  
  
  public static enum Type { PUT, CALL };
  public static enum Region { AMERICAN, EUROPEAN };
  
  private Security security;
  private Double   strikePrice;
  private int      maturity;
  private Type     type;
  private Region   region;
  private Rate     rate;
  
  private Double   q;

  public Option(Security security, Double strikePrice, int maturity) {
    this(security, strikePrice, maturity, Type.CALL, Region.AMERICAN);
  }

  public Option(Security security, Double strikePrice, int maturity, Type type, Region region) {
    this.security = security;
    this.strikePrice = strikePrice;
    this.maturity = maturity;
    this.type = type;
    this.region = region;
    this.rate = security.getLattice().getRate();
    
    this.q = security.getLattice().getRiskFreePropabilityForUp();
  }
  
  public LatticeConfiguration getLattice() {
    return security.getLattice();
  }
  
  public Double getPrice() {
    return getValue(0,0);
  }
  
  public Double getValue(int t, int upMoves) {
    assert t > 0;
    assert t >= upMoves;
    
    if (t==maturity) {
      return getPayOff(t, upMoves);
    }
    else if (t>maturity) {
      return 0.0;
    }
    else {
      
      Double s = getEuropeanValue(t, upMoves);
      
      switch (region) {
        case EUROPEAN: return s;
        case AMERICAN: return Math.max(s, getPayOff(t, upMoves));
      }
    }
    return null;
  }
  
  protected Double getEuropeanValue(int t, int upMoves) {
    Double su = getValue(t+1, upMoves+1);
    Double sd = getValue(t+1, upMoves);
    Double rateFactor = rate.getRateFactor(t, upMoves);
    Double s = (q*su + (1-q)*sd)/rateFactor;
    return s;
  }
  
  public Binominal<Double> getPayOffs() {
    return new Binominal<Double>() {

      public Double getValue(int t, int upMoves) {
        return getPayOff(t, upMoves);
      }
      
      public String toString() {
        return "Pay Offs of " + Option.this;
      }
    };
  }
  
  public Double getPayOff(int t, int upMoves) {
    assert t > 0;
    assert t >= upMoves;
    switch (region) {
      case AMERICAN: return getAmericanPayOff(t, upMoves);
      case EUROPEAN: return getEuropeanPayOff(t, upMoves);
    }
    return null;
  }
  
  private Double getAmericanPayOff(int t, int upMoves) {
    if (t<=maturity) {
      Double securityPrice = security.getValue(t, upMoves);
      switch (type) {
        case PUT : return Math.max(strikePrice - securityPrice, 0.0);
        case CALL : return Math.max(securityPrice - strikePrice, 0.0);
      }
      return null;
    }
    else {
      return 0.0;
    }
  }
  
  private Double getEuropeanPayOff(int t, int upMoves) {
    if (t==maturity) {
      Double securityPrice = security.getValue(t, upMoves);
      switch (type) {
        case PUT : return Math.max(strikePrice - securityPrice, 0.0);
        case CALL : return Math.max(securityPrice - strikePrice, 0.0);
      }
      return null;
    }
    else {
      return 0.0;
    }
  }
  
  public Binominal<Boolean> getShouldExecutes() {
    
    return new Binominal<Boolean>() {

      public Boolean getValue(int t, int upMoves) {
        return shouldExecute(t, upMoves);
      }

      @Override
      public String toString() {
        return "Should we execute " + Option.this + "?";
      }
    };
    
  }
  
  public boolean shouldExecute(int t, int upMoves) {
    if (region==Region.EUROPEAN) {
      if (t == maturity) {
        return getEuropeanPayOff(t, upMoves) > 0.0;
      }
    }
    else {
      if (t<=maturity) { 
        Double payOff = getPayOff(t, upMoves);
        return (payOff>0.0) && (payOff >= getEuropeanValue(t, upMoves));
      }
    }
    
    return false;
  }
  
  public int getEarliestExecutionPeriod() {
    for (int t=0; t<= maturity; t++) {
      for (int u=0; u<=t; u++) {
        if (shouldExecute(t, u)) {
          return t;
        }
      }
    }
    return -1;
  }
  
  public String toString() {
    NumberFormat moneyFmt = NumberFormat.getCurrencyInstance();
    return region + " " + type + " Option." 
            + "(strike: " + moneyFmt.format(strikePrice)
            + ", maturity: " + maturity + " periods"
            + ", security: " + security + ")";
  }
  
  
  
  
}
