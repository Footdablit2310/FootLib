package com.footdablit2310.footlib.api.shared.foot_economy;

/**
 * Provides exchange rate utilities for converting between currencies.
 *
 * Exchange rates are dynamic and can be changed at runtime.
 */
public interface ExchangeRateProvider {

    /**
     * Gets the exchange rate of a currency relative to the global currency.
     *
     * @return the rate, or 1.0 if undefined
     */
    double getRate(Currency currency);

    /**
     * Sets the exchange rate of a currency relative to the global currency.
     *
     * @param currency the currency
     * @param rate     the new rate
     */
    void setRate(Currency currency, double rate);

    /**
     * Converts an amount from one currency to another.
     */
    default long convert(long amount, Currency from, Currency to) {
        if (from == null || to == null) return 0L;
        double global = amount * getRate(from);
        return Math.round(global / getRate(to));
    }
}