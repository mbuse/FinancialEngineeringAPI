package de.mbuse.accounting.examples;

import de.mbuse.accounting.accounts.TAccount;
import de.mbuse.accounting.liability.Bond;
import de.mbuse.accounting.util.Formatter;
import java.util.Calendar;

/**
 *
 * @author mbuse
 */
public class LiabilityBondExample {
  static final Formatter FMT = new Formatter();
  
  public static void main(String... args) throws Exception {
    TAccount cash = new TAccount("Cash", TAccount.Type.ASSET);
    Bond bond = new Bond("Premium Bond", 10000, 0.05, 0.04, 3, Bond.CouponPayment.SEMIANUALLY);
    
    System.out.println("=== Premium Bond Example ===\n");
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(FMT.getDateFormat().parse("01.01.2010"));
    bond.issue(cal.getTime(), cash);
    
    for (int p=0; p<6; p++) {
      cal.add(Calendar.MONTH, 6);
      bond.payCoupon(cal.getTime(), cash);
    }
    bond.mature(cal.getTime(), cash);
    
    FMT.print(bond.getJournal());
    FMT.print(cash);
    FMT.print(bond.getBondsPayable());
    FMT.print(bond.getInterestExpense());
    
    
    System.out.println("\n\n=== Discount Bond Example ===\n");
    
    cash = new TAccount("Cash", TAccount.Type.ASSET);
    bond = new Bond("Discount Bond", 10000, 0.05, 0.06, 3, Bond.CouponPayment.SEMIANUALLY);
    
    System.out.println("Bond price: " + FMT.formatMoney(bond.getInitialPrice()));
    
    cal = Calendar.getInstance();
    cal.setTime(FMT.getDateFormat().parse("01.01.2010"));
    bond.issue(cal.getTime(), cash);
    
    for (int p=0; p<6; p++) {
      cal.add(Calendar.MONTH, 6);
      bond.payCoupon(cal.getTime(), cash);
    }
    bond.mature(cal.getTime(), cash);
    
    FMT.print(bond.getJournal());
    FMT.print(cash);
    FMT.print(bond.getBondsPayable());
    FMT.print(bond.getInterestExpense());
    
    
    System.out.println("\n\n=== Bond Retirment Example ===\n");
    
    cash = new TAccount("Cash", TAccount.Type.ASSET);
    bond = new Bond("Retirement", 10000, 0.05, 0.04, 3, Bond.CouponPayment.SEMIANUALLY);
    cal = Calendar.getInstance();
    cal.setTime(FMT.getDateFormat().parse("01.01.2010"));
    bond.issue(cal.getTime(), cash);
    
    for (int p=0; p<3; p++) {
      cal.add(Calendar.MONTH, 6);
      bond.payCoupon(cal.getTime(), cash);
    }
    bond.retire(cal.getTime(), cash, 0.06);
    
    FMT.print(bond.getJournal());
    FMT.print(cash);
    FMT.print(bond.getBondsPayable());
    FMT.print(bond.getInterestExpense());
    FMT.print(bond.getGainOnRetirement());
  }
}
