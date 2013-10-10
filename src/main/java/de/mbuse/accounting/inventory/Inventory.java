package de.mbuse.accounting.inventory;

import java.util.Deque;
import java.util.LinkedList;
import org.apache.commons.math3.transform.FastCosineTransformer;

/**
 *
 * @author mbuse
 */
public class Inventory {
  
  private String name;
  private Method method;
  private Deque<StackEntry> stack = new LinkedList<StackEntry>();
  
  public Inventory(String name, Method method) {
    this.name = name;
    this.method = method;
  }
  public Inventory(Method method) {
    this("Inventory", method);
  }
  public Inventory(String name) {
    this(name, Method.FIFO);
  }
  public Inventory() {
    this(Method.FIFO);
  }
  
  public void buyAtTotalCost(double totalCost, int amount) {
    buy(amount, totalCost/amount);
  }
  public void buy(int amount, double costsPerItem) {
    stack.addLast(new StackEntry(amount, costsPerItem));
  }
  
  public double getBalance() {
    double balance = 0.;
    for (StackEntry e : stack) {
      balance += e.amount * e.costsPerItem;
    }
    return balance;
  }
  
  public double sell(int amount) {
    StackEntry entry = getEntryForSelling();
    if (entry.amount > amount) {
      // just reduce...
      entry.amount -= amount;
      return amount * entry.costsPerItem;
    }
    else {
      entry = removeEntryForSelling();
      double cogs = entry.amount * entry.costsPerItem;
      cogs += sell(amount - entry.amount);
      return cogs;
    }
  }
  
  private StackEntry getEntryForSelling() {
    switch(this.method) {
      case FIFO: return stack.peekFirst();
      case LIFO: return stack.peekLast();
    }
    throw new IllegalStateException("Unknown method");
  }
  
  private StackEntry removeEntryForSelling() {
    switch(this.method) {
      case FIFO: return stack.pollFirst();
      case LIFO: return stack.pollLast();
    }
    throw new IllegalStateException("Unknown method");
  }
  
  public static enum Method {
    FIFO, LIFO;
  }
  
  private static class StackEntry {
    private int amount;
    private double costsPerItem;

    public StackEntry(int amount, double costsPerItem) {
      this.amount = amount;
      this.costsPerItem = costsPerItem;
    }
    
    
  }
}
