package de.mbuse.accounting.util;

import de.mbuse.accounting.accounts.AccountingValue;
import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.journal.Transaction;
import de.mbuse.accounting.journal.Transaction.Entry;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mbuse
 */
public class Formatter {
  
  private DecimalFormat moneyFormat = new DecimalFormat("#,###,##0.00 $");
  private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
  private PrintWriter out = new PrintWriter(System.out);
  
  public void setMoneyFormat(String pattern) {
    moneyFormat = new DecimalFormat(pattern);
  }
  public void setMoneyFormat(DecimalFormat format) {
    moneyFormat = format;
  }

  public void setDateFormat(DateFormat dateFormat) {
    this.dateFormat = dateFormat;
  }
 
  public void setOut(PrintWriter out) {
    this.out = out;
  }
  
  public void print(TAccount acc) {
    List<String> amounts = new ArrayList<String>();
    
    List<Entry> entries = acc.getEntries();
    for (Entry e : entries) {
      amounts.add(moneyFormat.format(e.getAmount()));
    }
    AccountingValue balance = acc.getBalance();
    amounts.add(moneyFormat.format(balance.getAmount()));
    amounts = alignStrings(amounts, 8, true);
    
    String name = acc.getName() + " [" + acc.getType().getShortName() + "]";
    int length = (entries.size()==0) 
            ? name.length()
            : 2 * amounts.get(0).length()  + 3;
    
    String hline = makeString('-', length);
    String dhline = makeString('=', length);
    
    out.println(name);
    out.println(dhline);
    for (int i=0; i<entries.size(); i++) {
      Entry entry = entries.get(i);
      Transaction tx = entry.getTransaction();
      String amount = amounts.get(i);
      String empty = makeString(' ', amount.length());
      String d = entry.isDebit() ? amount : empty;
      String c = entry.isDebit() ? empty : amount;
      out.println(d + " | " + c + "   " + formatDate(tx.getDate()) + " " + tx.getDescription());
    }
    out.println(hline);
    
    String amount = amounts.get(entries.size());
    String empty = makeString(' ', amount.length());
    String d = balance.isDebit() ? amount : empty;
    String c = balance.isDebit() ? empty : amount;
    out.println(d + " | " + c + "   TOTAL");
    //out.println(dhline);
    
    
    out.println();
    out.flush();
  }
  
  public void print(List<Transaction> journal) {
    for(Transaction tx : journal) {
      print(tx);
    }
  }
  
  public void print(Transaction tx) {
    String date = formatDate(tx.getDate());
    out.println(date + "  " + tx.getDescription());
    
    tx.sort();
    List<String> debits = new ArrayList<String>();
    List<String> credits = new ArrayList<String>();
    List<String> accounts = new ArrayList<String>();
    List<? extends Entry> entries = tx.getEntries();
    for (Transaction.Entry e : entries) {
      String prefix = (e.isDebit()) ? "Dr. " : "  Cr. ";
      accounts.add(prefix + e.getAccount().getName());
      debits.add(e.isDebit() ? moneyFormat.format(e.getAmount()) : null);
      credits.add(e.isDebit() ? null : moneyFormat.format(e.getAmount()));
    }
    accounts = alignStrings(accounts, 15, false);
    debits = alignStrings(debits, 8, true);
    credits = alignStrings(credits, 8, true);
    
    for (int i=0; i<entries.size(); i++) {
      out.println(accounts.get(i) + "  " + debits.get(i) + " " + credits.get(i));
    }
    out.println();
    out.flush();
  }
  
  private String formatDate(Date date) {
    return (date==null) ? "n/a" : dateFormat.format(date);
  }
  
  private List<String> alignStrings(List<String> lines, int minLength, boolean rightAlign) {
    int length = minLength;
    for (String l : lines) {
      if (l!=null) {
        length = (l.length() > length) ? l.length() : length;
      }
    }
    List<String> result = new ArrayList<String>();
    for (String l : lines) {
      l = (l==null) ? "" : l;
      String spaces = makeString(' ', (length - l.length()));
      result.add( (rightAlign) ? spaces + l : l + spaces);
    }
    return result;
  }
  
  private String makeString(char character, int length) {
    char[] chars = new char[length];
    Arrays.fill(chars, 0, length, character);
    return new String(chars);
    
  }
  
}
