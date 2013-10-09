package de.mbuse.accounting.accounts;

import java.util.List;

/**
 *
 * @author mbuse
 */
public class AccountingValue {
  
  private double amount;

  private AccountingValue(double amount) {
    this.amount = amount;
  }
  
  public AccountingValue(double amount, boolean isDebit) {
    if (amount < 0.) throw new IllegalArgumentException("Amount must be positive!");
    this.amount = isDebit ? amount : -amount;
  }

  public boolean isDebit() {
    return this.amount > 0.;
  }
  
  public boolean isCredit() {
    return this.amount < 0.;
  }
  
  public boolean isBalanced() {
    return this.amount == 0.;
  }
  
  public double getDebit() {
    return isDebit() ? this.amount : 0.;
  }

  public double getCredit() {
    return isCredit() ? -this.amount : 0.; 
  }
  
  public double getAmount() {
    return Math.abs(this.amount);
  }
  
  public AccountingValue add(AccountingValue dc) {
    return new AccountingValue(amount + dc.amount);
  }
  
  public AccountingValue substract(AccountingValue dc) {
    return new AccountingValue(amount - dc.amount);
  }
  
  public String toString() {
    return ((isDebit()) ? "DEBIT(" : "CREDIT(") + getAmount() + ")";
  }
  
  public static AccountingValue sum(List<? extends AccountingValue> summands) {
    double amount = 0.;
    
    for (AccountingValue dc : summands) {
      amount += dc.amount;
    }
    
    return new AccountingValue(amount);
  }
  
}
