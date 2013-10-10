package de.mbuse.accounting.examples;

import de.mbuse.accounting.inventory.Inventory;
import de.mbuse.accounting.util.Formatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author mbuse
 */
public class InventoryCOGSExample {
  static final DateFormat DATE_FMT = new SimpleDateFormat("dd.MM.yy");
  static final Formatter FMT = new Formatter();
  
  static {
    FMT.setDateFormat(DATE_FMT);
  }
  
  public static void main(String... args) throws Exception {
    Inventory inventory = new Inventory("Flux Capacitors", Inventory.Method.LIFO);
    
    inventory.buyAtTotalCost(100000., 100);
    inventory.buyAtTotalCost( 55000., 50);
    inventory.buyAtTotalCost( 65000., 50);
    inventory.buyAtTotalCost(140000., 100);
    double cogs = inventory.sell(150);
    double balance = inventory.getBalance();
    
    System.out.println("Costs of Goods Sold  : " + cogs);
    System.out.println("Balance in inventory : " + balance);
  }
}
