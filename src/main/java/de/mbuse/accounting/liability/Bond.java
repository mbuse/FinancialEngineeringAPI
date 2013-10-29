package de.mbuse.accounting.liability;

import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.journal.Transaction;
import de.mbuse.finance.binominal.Compounding;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mbuse
 */
public class Bond {

  
  
  public Bond(String name, double faceValue, double couponRate, double yieldToMaturity,  int maturity, CouponPayment type) {
    this.name = name;
    this.faceValue = faceValue;
    this.couponRate = couponRate;
    this.yieldToMaturity = yieldToMaturity;
    this.maturity = maturity;
    this.type = type;
    this.periodsLeft = -1;
    this.journal = new ArrayList<Transaction>();
    this.bondsPayable = new TAccount("Bonds Payable (" + name + ")", TAccount.Type.LIABILITY);
    this.interestExpense = new TAccount("Interest Expense (" + name + ")", TAccount.Type.EXPENSE);
  }
  
  // ===
  
  public void issue(Date date, TAccount cash) {
    if (periodsLeft>0) {
      throw new IllegalStateException("Bond already issued!");
    }
    double price = getInitialPrice();
    Transaction tx = new Transaction(date, "Issuing Bond (" + name + ")");
    tx.addDebit(cash, price);
    tx.addCredit(bondsPayable, price);
    tx.postToAccounts();
    journal.add(tx);
    periodsLeft = n();
  }
  
  public void payCoupon(Date date, TAccount cash) {
    if (periodsLeft<=0) {
      throw new IllegalStateException("Bond already matured");
    }
    double interest = bondsPayable.getBalance().getCredit() * yield();
    Transaction tx = new Transaction(date, "Coupon Payment (" + name + ")");
    tx.addDebit(interestExpense, interest);
    tx.addCredit(cash, coupon());
    tx.plug(bondsPayable);
    tx.postToAccounts();
    journal.add(tx);
    periodsLeft--;
  }
  
  public void mature(Date date, TAccount cash) {
    if (periodsLeft!=0) {
      throw new IllegalStateException("Bond not yet matured!");
    }
    double bal = bondsPayable.getBalance().getCredit();
    Transaction tx = new Transaction(date, "Maturity (" + name + ")");
    tx.addDebit(bondsPayable, bal);
    tx.addCredit(cash, bal);
    tx.postToAccounts();
    journal.add(tx);
  }
  
  // ===
  
  public double getInitialPrice() {
    double price = 0.;
    int n = n();
    for (int i=1; i<=n;i++) {
      price += Compounding.pv(coupon(), yield(), i);
    }
    price += Compounding.pv(faceValue, yield(), n);
    return price;
  }
  
  // ===

  public String getName() {
    return name;
  }
  
  /**
   * @return the type
   */
  public CouponPayment getType() {
    return type;
  }

  /**
   * @return the faceValue
   */
  public double getFaceValue() {
    return faceValue;
  }

  /**
   * @return the couponRate
   */
  public double getCouponRate() {
    return couponRate;
  }

  /**
   * @return the yieldToMaturity
   */
  public double getYieldToMaturity() {
    return yieldToMaturity;
  }

  /**
   * @return the maturity
   */
  public int getMaturity() {
    return maturity;
  }
  
  public static enum CouponPayment {
    SEMIANUALLY, ANUALLY
  }

  public TAccount getBondsPayable() {
    return bondsPayable;
  }

  public TAccount getInterestExpense() {
    return interestExpense;
  }

  public List<Transaction> getJournal() {
    return journal;
  }
  
  
  // ===
  
  private int n() {
    return type==CouponPayment.SEMIANUALLY
            ? maturity * 2
            : maturity;
  }
  
  private double yield() {
    return type==CouponPayment.SEMIANUALLY
            ? yieldToMaturity/2
            : yieldToMaturity;
  }
  
  private double coupon() {
    return type==CouponPayment.SEMIANUALLY
            ? faceValue * couponRate/2
            : faceValue * couponRate;
  }
  
  // ===
  
  private String name;
  private CouponPayment type;
  private double faceValue;
  private double couponRate;
  private double yieldToMaturity;
  private int maturity;
  private int periodsLeft;
  
  private TAccount bondsPayable;
  private TAccount interestExpense;
  private List<Transaction> journal;
  
}
