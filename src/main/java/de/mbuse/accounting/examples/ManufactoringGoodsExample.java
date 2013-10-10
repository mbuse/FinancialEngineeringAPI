package de.mbuse.accounting.examples;

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
public class ManufactoringGoodsExample {
  
  static final DateFormat DATE_FMT = new SimpleDateFormat("dd.MM.yy");
  static final Formatter FMT = new Formatter();
  
  static {
    FMT.setDateFormat(DATE_FMT);
  }
  
  public static void main(String... args) throws Exception {
    TAccount accountPayables = new TAccount("Account Payables", Type.LIABILITY);
    TAccount rawMaterial = new TAccount("Raw Material", Type.ASSET);
    TAccount workInProcess = new TAccount("Work in Process", Type.ASSET);
    TAccount accumDepreciation = new TAccount("Accumulated Depreciation", Type.XASSET);
    TAccount finishedGoods = new TAccount("Finished Goods", Type.ASSET);
    TAccount cash = new TAccount("Cash", Type.ASSET);
    TAccount accountReceivables = new TAccount("Account Receivables", Type.ASSET);
    TAccount costsOfGoodSold = new TAccount("Cost of Goods Sold", Type.EXPENSE);
    TAccount salesRevenue = new TAccount("Sales Revenue", Type.REVENUE);
    
    Transaction tx;
    
    tx = new Transaction(DATE_FMT.parse("01.01.2012"), "Purchasing raw material")
            .addDebit(rawMaterial, 865.)
            .addCredit(accountPayables, 865.)
            .postToAccounts();
    FMT.print(tx);
   
    FMT.print(new Transaction(DATE_FMT.parse("02.01.2012"), "Using raw material for production")
            .addDebit(workInProcess, 806.)
            .addCredit(rawMaterial, 806.)
            .postToAccounts());
    
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Manufactoring labour")
            .addDebit(workInProcess, 524.)
            .addCredit(cash, 524.)
            .postToAccounts());
    
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Power, heat and other overhead")
            .addDebit(workInProcess, 423.)
            .addCredit(cash, 423.)
            .postToAccounts());
    
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Depreciation for plant equipment")
            .addDebit(workInProcess, 81.)
            .addCredit(accumDepreciation, 81.)
            .postToAccounts());
    
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Finishing manufacturing goods")
            .addDebit(finishedGoods, 1960.)
            .addCredit(workInProcess, 1960.)
            .postToAccounts());
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Selling Goods to Customers on account")
            .addDebit(accountReceivables, 2862.)
            .addCredit(salesRevenue, 2862.)
            .postToAccounts());
    FMT.print(new Transaction(DATE_FMT.parse("31.01.2012"), "Costs of goods sold")
            .addDebit(costsOfGoodSold, 1938.)
            .addCredit(finishedGoods, 1938.)
            .postToAccounts());
    
    FMT.print(cash);
    FMT.print(accountPayables);
    FMT.print(accumDepreciation);
    FMT.print(rawMaterial);
    FMT.print(workInProcess);
    FMT.print(finishedGoods);
    FMT.print(costsOfGoodSold);
    FMT.print(salesRevenue);
    
    
  }
  
}
