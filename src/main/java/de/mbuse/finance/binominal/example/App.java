package de.mbuse.finance.binominal.example;

import de.mbuse.finance.binominal.lattice.Util;
import de.mbuse.finance.binominal.lattice.BlackScholesLatticeConfiguration;
import de.mbuse.finance.binominal.LatticeConfiguration;
import de.mbuse.finance.binominal.security.Option;
import de.mbuse.finance.binominal.security.Stock;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Hello world!
 *
 */
public class App 
{
    final static Double T = 0.25;
    final static Double r = 0.02;
    final static Double sigma = 0.30;
    final static Double c = 0.01;
    final static int    n = 15;
    
    public static void main( String[] args )
    {
        Locale.setDefault(Locale.US);
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        
        LatticeConfiguration lattice = new BlackScholesLatticeConfiguration(r, sigma, n, T, c);
        
        System.out.println("u = " + lattice.getUpFactor());
        System.out.println("d = " + lattice.getDownFactor());
        System.out.println("r = " + lattice.getRate());
        System.out.println("q = " + lattice.getRiskFreePropabilityForUp());
        
        System.out.println("---");
        
        Stock security = new Stock(100.0, lattice);
        Util.printLattice(security, moneyFormat, n);
        
        System.out.println("---");
        
        Option americanCall = new Option(security, 110.0, 15, Option.Type.CALL, Option.Region.AMERICAN);
        System.out.println("Price American Call Option : " + moneyFormat.format(americanCall.getPrice()));
        
        Option americanPut = new Option(security, 110.0, 15, Option.Type.PUT, Option.Region.AMERICAN);
        System.out.println("Price American Put Option  : " + moneyFormat.format(americanPut.getPrice()));
        
        Util.printLattice(americanPut, moneyFormat, n);
        
        Util.printLattice(americanPut.getPayOffs(), moneyFormat, n);
        
        Util.printLattice(americanPut.getShouldExecutes(), null, n);
    }
}
