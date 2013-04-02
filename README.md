Financial Engineering API
=========================

Real hot stuff like Pricing of American PUT Options on Futures of Zero Coupon Bonds in the 
Binominal Model... Oh my god!!!


# Disclaimer

This API contains functionality to calculate prices, payoffs, probabilities of financial assets
like bonds, stocks, options, futures, etc... based on financial models like the binomial asset 
pricing model, CAPM etc.

The primary goal of this API is educational. It should help you to understand how the pricing of all those fancy
financial products like options and swaptions works. The API is not designed for performance and efficiency
because these optimizations would make the code harder to understand and would interfere with the primary goal.

The author of this API claims that he is not responsible for any losses, generated by investments 
which are based on any kind of calculations using this API. If your investment does not work out 
as calculated this may have two different reasons:

 1. No software is bug free. And so isn't this API.
 2. This API is based on financial models. And the probability that these financial models are absolutely
    nonsense is even higher than the propability that this API contains bugs.


# Features

The following features are implemented by this API.

## Binominal Lattice Configurations

 * [Binominal Lattice Configuration](src/main/java/de/mbuse/finance/binominal/lattice/BinominalLatticeConfiguration.java): A configuration which implements risk neutral propability based on the up and down factors and the fixed discount rate.
 * [Black-Scholes Lattice Configuration](src/main/java/de/mbuse/finance/binominal/lattice/BlackScholesLatticeConfiguration.java): A lattice based on volatility using the Black-Scholes model.
 * [Term Structure Lattice Configuration](src/main/java/de/mbuse/finance/binominal/lattice/TermStructureLatticeConfiguration.java): Implements variable discount rates.

## Securities
 * [Stocks](src/main/java/de/mbuse/finance/binominal/security/Stock.java)
 * [Zero Coupon Bonds](src/main/java/de/mbuse/finance/binominal/security/ZeroCouponBond.java)
 * [Coupon Paying Bonds](src/main/java/de/mbuse/finance/binominal/security/CouponPayingBond.java)
 * [Options](src/main/java/de/mbuse/finance/binominal/security/Option.java): American and European Call and Put Options on arbitrary securities
 * [Chooser Optiona](src/main/java/de/mbuse/finance/binominal/security/ChooserOption.java): A combination of a Call and a Put option
 * [Swaps](src/main/java/de/mbuse/finance/binominal/security/Swap.java)
 * [Forward Swaps](src/main/java/de/mbuse/finance/binominal/security/ForwardSwap.java): Swaps which start in the future.
 * [Futures](src/main/java/de/mbuse/finance/binominal/security/Future.java)
 * [Caplets](src/main/java/de/mbuse/finance/binominal/security/Caplet.java)
 * [Floorlets](src/main/java/de/mbuse/finance/binominal/security/Floorlet.java)
 * [Elementary Security](src/main/java/de/mbuse/finance/binominal/security/ElementarySecurity.java): Not a real security, but a nice tool to calculate prices based on future cash flows.

