package de.mbuse.accounting.journal;

import de.mbuse.accounting.accounts.AccountingValue;
import de.mbuse.accounting.accounts.TAccount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mbuse
 */
public class Transaction {
  
  private List<Entry> entries = new ArrayList<Entry>();
  private String description;
  private Date date;

  public Transaction(Date date, String description) {
    this.description = description;
    this.date = date;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  
  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }
  
  public List<? extends Entry> getEntries() {
    return Collections.unmodifiableList(entries);
  }
  
  public Transaction addDebit(TAccount account, Double amount) {
    Entry e = new Entry(this, account, true, amount);
    entries.add(e);
    return this;
  }
  
  public Transaction addCredit(TAccount account, Double amount) {
    Entry e = new Entry(this, account, false, amount);
    entries.add(e);
    return this;
  }
  
  public boolean isValid() {
    double accu = 0.0;
    for (Entry e : entries) {
      accu += (e.isDebit()) ? e.getAmount() : - e.getAmount();
    }
    return accu == 0.0;
  }
  
  public Transaction postToAccounts() {
    if (isValid()) {
      for (Entry e : entries) {
        e.post();
      }
    }
    else {
      throw new IllegalArgumentException("Invalid Transaction: " + this);
    }
    return this;
  }
  
  public void sort() {
    ArrayList<Entry> debits = new ArrayList<Entry>();
    ArrayList<Entry> credits = new ArrayList<Entry>();
    for (Entry e:entries) {
      if (e.isDebit()) {
        debits.add(e);
      } else {
        credits.add(e);
      }
    }
    debits.addAll(credits);
    this.entries = debits;
  }
    
  // === 
  
  public static class Entry extends AccountingValue {
    private TAccount account;
    private Transaction transaction;

    private Entry(Transaction transaction, TAccount account, boolean debit, Double amount) {
      super(amount, debit);
      this.account = account;
      this.transaction = transaction;
    }

    /**
     * @return the account
     */
    public TAccount getAccount() {
      return account;
    }
    
    public Transaction getTransaction() {
      return transaction;
    }
    
    public void post() {
      account.addEntry(this);
    }
  }
}
