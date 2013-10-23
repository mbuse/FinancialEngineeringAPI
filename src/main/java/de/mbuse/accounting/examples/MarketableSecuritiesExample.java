package de.mbuse.accounting.examples;

import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.journal.Transaction;
import de.mbuse.accounting.util.Formatter;

/**
 *
 * @author mbuse
 */
public class MarketableSecuritiesExample {
  
  static final Formatter FMT = new Formatter();
  
  public static void main(String... args) {
    
    TAccount cash = new TAccount("Cash", TAccount.Type.ASSET);
    TAccount tradingSec = new TAccount("Trading Securities", TAccount.Type.ASSET);
    TAccount availForSaleSec = new TAccount("Available-for-Sale Securities", TAccount.Type.ASSET);
    TAccount dividentRevenue = new TAccount("Dividend Revenues", TAccount.Type.REVENUE);
    TAccount gainOnInvestments = new TAccount("Gain on Investments", TAccount.Type.REVENUE);
    TAccount lossOnInvestments = new TAccount("Loss on Investments", TAccount.Type.EXPENSE);
    TAccount aoci = new TAccount("Accum. Other Comprehensive Income", TAccount.Type.SHAREHOLDERS_EQUITY);
    
    
    Transaction tx;
    
    tx = new Transaction(null, "Buying Trading Securities");
    tx.addDebit(tradingSec, 100.)
            .addCredit(cash, 100.).postToAccounts();
    
    FMT.print(tx);
    
    tx = new Transaction(null, "Buying AFS Securities");
    tx.addDebit(availForSaleSec, 100.)
            .addCredit(cash, 100.).postToAccounts();
    
    FMT.print(tx);
    
    tx = new Transaction(null, "Receiving Dividends on TS");
    tx.addDebit(cash, 5.)
            .addCredit(dividentRevenue, 5.).postToAccounts();
    FMT.print(tx);
    
    tx = new Transaction(null, "Receiving Dividends on AFS")
            .addDebit(cash, 5.)
            .addCredit(dividentRevenue, 5.).postToAccounts();
    FMT.print(tx);
    
    tx = new Transaction(null, "Report Mark-to-Market on TS");
    tx.addDebit(tradingSec, 3.)
            .addCredit(gainOnInvestments, 3.).postToAccounts();
    FMT.print(tx);
    
    tx = new Transaction(null, "Report Mark-to-Market on AFS");
    tx.addDebit(availForSaleSec, 3.)
            .addCredit(aoci, 3.).postToAccounts();
    FMT.print(tx);
    
    tx = new Transaction(null, "Selling TS")
            .addDebit(cash, 101.)
            .addDebit(lossOnInvestments, 2.)
              .addCredit(tradingSec, 103.).postToAccounts();
    FMT.print(tx);
    
    tx = new Transaction(null, "Selling AFS")
            .addDebit(cash, 101.)
            .addDebit(aoci, 3.)
            .addCredit(gainOnInvestments, 1.)
            .addCredit(availForSaleSec, 103.).postToAccounts();
    
    FMT.print(tx);
    
    
    //===
    
    FMT.print(tradingSec);
    FMT.print(availForSaleSec);
    FMT.print(gainOnInvestments);
    FMT.print(lossOnInvestments);
    FMT.print(aoci);
    
    
    
  }
}
