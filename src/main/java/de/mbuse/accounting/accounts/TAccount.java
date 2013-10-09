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
public class TAccount {
  
  private String name;
  private Type type;
  private List<Transaction.Entry> entries = new ArrayList<Transaction.Entry>();

  public TAccount(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }
  
  public boolean isPermanent() {
    return type.isPermanent();
  }
  
  public boolean isTemporary() {
    return !isPermanent();
  }
  
  public AccountingValue getBalance() {
    return AccountingValue.sum(entries);
  }
  
  public void addEntry(Transaction.Entry entry) {
    if (entry.getAccount()!=this) {
      throw new IllegalArgumentException("Invalid T-Account!");
    }
    if (entries.contains(entry)) {
      throw new IllegalArgumentException("Transaction entry cannot be added twice!");
    }
    entries.add(entry);
  }
  
  public void sortEntries() {
    Collections.sort(entries, new Comparator<Transaction.Entry>() {
      public int compare(Entry e1, Entry e2) {
        return e1.getTransaction().getDate().compareTo(e2.getTransaction().getDate());
      }
    });
  }

  public List<Entry> getEntries() {
    return Collections.unmodifiableList(entries);
  }
  
  
  
  // === TACCOUNT TYPE ===
  public static enum Type {
    ASSET("A", true, true),
    XASSET("XA", false, true),
    LIABILITY("L", false, true),
    SHAREHOLDERS_EQUITY("SE", false, true),
    RETAINED_EARNINGS("RE", false, true),
    REVENUE("R", false, false),
    EXPENSE("E", true, false);

    private Type(String shortName, boolean isDebit, boolean isPermanent) {
      this.shortName = shortName;
      this.isDebit = isDebit;
      this.isPermanent = isPermanent;
    }
    /**
     * @return the shortName
     */
    public String getShortName() {
      return shortName;
    }

    /**
     * @return the isDebit
     */
    public boolean isDebit() {
      return isDebit;
    }

    /**
     * @return the isPermanent
     */
    public boolean isPermanent() {
      return isPermanent;
    }
    
    
    
    private String shortName;
    private boolean isDebit;
    private boolean isPermanent;
  }
}
