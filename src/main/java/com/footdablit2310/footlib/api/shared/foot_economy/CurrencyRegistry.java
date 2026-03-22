package com.footdablit2310.footlib.api.shared.foot_economy;

import java.util.Collection;

/**
 * Registry for all currencies available in the Foot Economy ecosystem.
 *
 * Implemented by Foot Economy. Other mods may register their own currencies
 * during initialization, as long as they use unique IDs.
 */
public interface CurrencyRegistry {

    /**
     * Registers a new currency.
     *
     * @param currency the currency to register
     * @return true if registration succeeded, false if the ID already exists
     */
    boolean register(Currency currency);

    /**
     * Retrieves a currency by its unique ID.
     *
     * @param id the currency ID
     * @return the currency, or null if not found
     */
    Currency get(String id);

    /**
     * @return all registered currencies
     */
    Collection<Currency> all();
}