package de.mbuse.accounting.examples;

import de.mbuse.accounting.accounts.AccumulatedTAccount;
import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.accounts.TAccount.Type;
import de.mbuse.accounting.journal.Transaction;
import de.mbuse.accounting.util.Formatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mbuse
 */
public class JournalEntryExample {
  
  static final DateFormat DATE_FMT = new SimpleDateFormat("dd.MM.yy");
  static final Formatter FMT = new Formatter();
  
  static {
    FMT.setDateFormat(DATE_FMT);
  }
  
  public static void main(String... args) throws Exception {
    TAccount cash = new TAccount("Cash", Type.ASSET);
    TAccount mortgage = new TAccount("Mortgage", Type.LIABILITY);
    TAccount building = new TAccount("Building", Type.ASSET);
    TAccount land = new TAccount("Land", Type.ASSET);
    TAccount inventory = new TAccount("Inventory", Type.ASSET);
    TAccount stocks = new TAccount("Stocks", Type.SHAREHOLDERS_EQUITY);
    
    Transaction tx;
    tx = new Transaction(DATE_FMT.parse("01.06.12"), "Issuing stocks to owners.");
    tx.addDebit(cash, 100000.)
      .addCredit(stocks, 100000.)
      .postToAccounts();
    
    FMT.print(tx);
    
    tx = new Transaction(DATE_FMT.parse("10.06.12"), "Buying land and building");
    tx.addDebit(land, 100000.)
      .addDebit(building, 80000.)
      .addCredit(cash, 40000.)
      .addCredit(mortgage, 140000.)
      .postToAccounts();
    
    FMT.print(tx);
    
    tx = new Transaction(DATE_FMT.parse("05.06.12"), "Buying inventory");
    tx.addDebit(inventory, 10000.)
      .addCredit(cash, 10000.)
      .postToAccounts();
    
    FMT.print(tx);
    
    FMT.print(cash);
    FMT.print(land);
    FMT.print(building);
    FMT.print(inventory);
    FMT.print(mortgage);
    FMT.print(stocks);
    
    TAccount netCurrentAssets = new AccumulatedTAccount("Net Current Assets", Type.ASSET, cash, inventory);
    
    FMT.print(netCurrentAssets);
    
    
    
    
  }
  
}
