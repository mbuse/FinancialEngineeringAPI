package de.mbuse.accounting.investment;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import de.mbuse.accounting.accounts.AccountingValue;
import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.journal.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mbuse
 */
public class MarketableSecurity {
  
  public static enum Method {
    TRADING,
    AVAILABLE_FOR_SALES,
    HOLD_TO_MATURITY
  }
  
  public MarketableSecurity(String name, Method method) {
    this.name = name;
    this.method = method;
    this.asset = new TAccount(name, TAccount.Type.ASSET);
    this.gains = new TAccount("Gain on investments on " + name, TAccount.Type.REVENUE);
    this.losses = new TAccount("Loss on investments on " + name, TAccount.Type.EXPENSE);
    this.aoci = new TAccount("AOCI on " + name, TAccount.Type.SHAREHOLDERS_EQUITY);
    this.journal = new ArrayList<Transaction>();
  }
  
  public void buy(Date date, int amount, double pricePerShare, TAccount cash) {
    amountOfShares += amount;
    double totalPrice = amount * pricePerShare;
    Transaction tx = new Transaction(date, "Buying " + amount + " of " + name + " at " + pricePerShare + " per share.");
    tx.addDebit(asset, totalPrice);
    tx.addCredit(cash, totalPrice);
    tx.postToAccounts();
    journal.add(tx);
  }
  
  public void sell(Date date, int amount, double pricePerShare, TAccount cash) {
    double percentage = ((double) amount)/amountOfShares;
    amountOfShares -= amount;
    
    double salesAmount = amount * pricePerShare;
    double bookAmount = percentage * getBookValue();
    
    Transaction tx = new Transaction(date, "Selling " + amount + " shares of " + name + " for " + pricePerShare);
    
    tx.addDebit(cash, salesAmount);
    tx.addCredit(asset, bookAmount);
    
    double gainOrLoss = salesAmount - bookAmount;
    
    if (method == Method.AVAILABLE_FOR_SALES) {
      AccountingValue aociBalance = aoci.getBalance();
      
      double changeInAOCI = percentage * 
              ((aociBalance.isDebit()) ? aociBalance.getDebit() : aociBalance.getCredit());
      
      if (aociBalance.isDebit()) {
        tx.addCredit(aoci, changeInAOCI);
      }
      else {
        tx.addDebit(aoci, changeInAOCI);
      }
    }
    else {
    }
    
    tx.plug(accountForGainsOrLosses(tx.isDebit()));
    tx.postToAccounts();
    journal.add(tx);
  }
  
  public void markToMarket(Date date, double marketPrice) {
    double fairValue = amountOfShares * marketPrice;
    double gainOrLoss = fairValue - getBookValue();
    
    if (gainOrLoss!=0. && method!=Method.HOLD_TO_MATURITY) {
      boolean isGain = gainOrLoss>0.;
      TAccount gainOrLossAccount = accountForMarkToMarketGainsOrLosses(isGain);
      
      Transaction tx = new Transaction(date, "Report Mark-to-Market, price per share: " + marketPrice);
      if (isGain) {
        tx.addDebit(asset, gainOrLoss);
        tx.addCredit(gainOrLossAccount, gainOrLoss);
      }
      else {
        tx.addDebit(gainOrLossAccount, -gainOrLoss);
        tx.addCredit(asset, -gainOrLoss);
      }
      tx.postToAccounts();
      journal.add(tx);
    }
  }
 
  
  private TAccount accountForGainsOrLosses(boolean isGain) {
    return (isGain) ? this.gains : this.losses;
  }
  
  private TAccount accountForMarkToMarketGainsOrLosses(boolean isGain) {
    if (method==Method.TRADING) {
      return (isGain) ? this.gains : this.losses;
    }
    else if (method==Method.AVAILABLE_FOR_SALES) {
      return this.aoci;
    }
    else {
      return null;
    }
  }
  
  public double getBookValue() {
    AccountingValue balance = asset.getBalance();
    return (balance.isDebit()) ? balance.getDebit() : -balance.getCredit();
  }
  

  public TAccount getAsset() {
    return asset;
  }

  public TAccount getAOCI() {
    return aoci;
  }

  public TAccount getGains() {
    return gains;
  }

  public TAccount getLosses() {
    return losses;
  }

  public List<Transaction> getJournal() {
    return journal;
  }

  public int getAmountOfShares() {
    return amountOfShares;
  }

  public String getName() {
    return name;
  }

  public Method getMethod() {
    return method;
  }
  
  
  
  private TAccount asset;
  private TAccount gains;
  private TAccount losses;
  private TAccount aoci;
  private List<Transaction> journal;
  
  private String name;
  private Method method;
  private int amountOfShares = 0;
  
}
