package de.mbuse.accounting.accounts;

import de.mbuse.accounting.journal.Transaction;
import de.mbuse.accounting.journal.Transaction.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author mbuse
 */
public class AccumulatedTAccount extends TAccount {
  
  public AccumulatedTAccount(String name, Type type, TAccount... accounts) {
    super(name, type);
    this.accounts = accounts;
  }

  // === METHODS ===
  
  @Override
  public AccountingValue getBalance() {
    AccountingValue value = new AccountingValue(0., getType().isDebit());
    for (TAccount ta : accounts) {
      value = value.add(ta.getBalance());
    }
    return value;
  }

  @Override
  public void addEntry(Entry entry) {
    throw new IllegalArgumentException("Adding entries to accumulated TAccounts is not supported!");
  }

  @Override
  public void sortEntries() {
    // do nothing...
  }

  @Override
  public List<Entry> getEntries() {
    List<Entry> journal = new ArrayList<Entry>();
    for (TAccount ta : accounts) {
      journal.addAll(ta.getEntries());
    }
    Collections.sort(journal, new Comparator<Transaction.Entry>() {
      public int compare(Entry e1, Entry e2) {
        return e1.getTransaction().getDate().compareTo(e2.getTransaction().getDate());
      }
    });
    return Collections.unmodifiableList(journal);
  }
  
  // === ATTRIBUTES ===
  
  private TAccount[] accounts;
}
