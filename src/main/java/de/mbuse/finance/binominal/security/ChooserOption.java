/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mbuse.finance.binominal.security;

import de.mbuse.finance.binominal.Security;
import de.mbuse.finance.binominal.Binominal;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.rate.Rate;

/**
 *
 * @author mbuse
 */
public class ChooserOption implements Security {
  
  private int maturity;
  private Option option1;
  private Option option2;
  private LatticeConfiguration lattice;
  
  private double q;
  private Rate rate;

  public ChooserOption(Option option1, Option option2, int maturity, LatticeConfiguration lattice) {
    this.maturity = maturity;
    this.option1 = option1;
    this.option2 = option2;
    this.lattice = lattice;
    this.q = lattice.getRiskFreePropabilityForUp();
    this.rate = lattice.getRate();
  }
  
  

  public LatticeConfiguration getLattice() {
    return lattice;
  }

  public Double getPrice() {
    return getValue(0,0);
  }

  public Double getValue(int t, int u) {
    assert t >= u;
    assert u >= 0;
    
    if (t > maturity) {
      return 0.0;
    }
    else if (t == maturity) {
      double s1 = option1.getValue(t, u);
      double s2 = option2.getValue(t, u);
      return Math.max(s1, s2);
    }
    else  {
      double su = getValue(t+1, u + 1);
      double sd = getValue(t+1, u);
      return (q*su + (1-q)*sd)/rate.getRateFactor(t, u);
    }
  }
  
  public Binominal<Integer> getPreferedOptions() {
    return new Binominal<Integer>() {
      public Integer getValue(int t, int u) {
        return getPreferedOption(t, u);
      }
      public String toString() {
        return "Prefered option for " + ChooserOption.this;
      }
    };
  }
  
  public int getPreferedOption(int t, int u) {
    if (t>maturity) {
      return 0;
    }
    double s1 = option1.getValue(t, u);
    double s2 = option2.getValue(t, u);
    if (s1 == s2) {
      return s1==0 ? 0 : 3;
    }
    return (s1>s2) ? 1 : 2;
  }

  @Override
  public String toString() {
    return "Chooser Option (maturity: " + maturity
            + ", option 1: " + option1
            + ", option 2: " + option2
            + ", lattice: " + lattice + ")";
  }
  
  
}
