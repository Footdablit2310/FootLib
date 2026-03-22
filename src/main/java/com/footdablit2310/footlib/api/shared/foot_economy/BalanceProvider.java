package com.footdablit2310.footlib.api.shared.foot_economy;

import net.minecraft.world.entity.player.Player;

/**
 * Provides access to player balances for a specific currency.
 *
 * Implemented by Foot Economy. Other mods should only call these methods
 * and never attempt to store or modify balances directly.
 *
 * This interface supports full read/write operations:
 * - getBalance: read
 * - setBalance: write (direct)
 * - deposit: write (increment)
 * - withdraw: write (decrement)
 */
public interface BalanceProvider {

    /**
     * Gets the player's current balance.
     */
    long getBalance(Player player, Currency currency);

    /**
     * Sets the player's balance directly.
     * This should be used sparingly and only when necessary.
     */
    void setBalance(Player player, Currency currency, long amount);

    /**
     * Attempts to add an amount to the player's balance.
     *
     * @return a TransactionResult describing the outcome
     */
    TransactionResult deposit(Player player, Currency currency, long amount);

    /**
     * Attempts to subtract an amount from the player's balance.
     *
     * @return a TransactionResult describing the outcome
     */
    TransactionResult withdraw(Player player, Currency currency, long amount);
}