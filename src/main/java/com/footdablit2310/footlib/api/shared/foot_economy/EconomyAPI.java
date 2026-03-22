package com.footdablit2310.footlib.api.shared.foot_economy;

import net.minecraft.world.entity.player.Player;

/**
 * Public API for interacting with Foot Economy.
 *
 * FootLib exposes this interface, while Foot Economy provides the actual
 * implementation at runtime. Other mods should always access economy
 * features through this API to ensure compatibility.
 */
public interface EconomyAPI {

    /**
     * @return the default currency used by Foot Economy.
     */
    Currency defaultCurrency();

    /**
     * Provides access to all registered currencies.
     */
    CurrencyRegistry currencies();

    /**
     * Retrieves a currency by its unique ID.
     *
     * @param id the currency ID (e.g. "foot:sek")
     * @return the currency, or null if not registered
     */
    default Currency currency(String id) {
        return currencies().get(id);
    }

    /**
     * Provides access to balance read/write operations.
     */
    BalanceProvider balances();

    /**
     * Provides access to currency exchange utilities.
     */
    ExchangeRateProvider exchange();

    /**
     * Provides access to automatic, data-driven exchange rate adjustments.
     */
    AutoRateProvider auto();

    /**
     * Convenience method for checking if a player can afford a cost
     * in the default currency.
     */
    default boolean canAfford(Player player, long cost) {
        return balances().getBalance(player, defaultCurrency()) >= cost;
    }

    /**
     * Convenience method for directly setting a player's balance
     * in the default currency.
     */
    default void setBalance(Player player, long amount) {
        balances().setBalance(player, defaultCurrency(), amount);
    }

    /**
     * Convenience method for converting an amount between currencies.
     */
    default long convert(long amount, Currency from, Currency to) {
        return exchange().convert(amount, from, to);
    }
}