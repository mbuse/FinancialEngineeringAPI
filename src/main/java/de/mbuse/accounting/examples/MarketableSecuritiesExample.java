package de.mbuse.accounting.examples;

import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.investment.MarketableSecurity;
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
    
    MarketableSecurity ts = new MarketableSecurity("Trading Security", MarketableSecurity.Method.TRADING);
    MarketableSecurity afs = new MarketableSecurity("Avail-for-Sale", MarketableSecurity.Method.AVAILABLE_FOR_SALES);
    
    ts.buy(null, 100, 25., cash);
    afs.buy(null, 100, 25., cash);
    
    ts.markToMarket(null, 23.);
    afs.markToMarket(null, 23.);
    
    ts.markToMarket(null, 27.);
    afs.markToMarket(null, 27.);
    
    ts.sell(null, 70, 26., cash);
    afs.sell(null, 70, 26., cash);
    
    FMT.print(ts.getJournal());
    FMT.print(ts.getAsset());
    FMT.print(ts.getGains());
    FMT.print(ts.getLosses());
    
    FMT.print(afs.getJournal());
    FMT.print(afs.getAsset());
    FMT.print(afs.getAOCI());
    FMT.print(afs.getGains());
    FMT.print(afs.getLosses());
    
    
    
  }
}
