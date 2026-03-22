package com.footdablit2310.footlib.api.shared.foot_economy;

import net.minecraft.world.entity.player.Player;

/**
 * Provides automatic exchange rate adjustments based on real server activity.
 *
 * Exchange rates are influenced by transaction volume, currency velocity,
 * and the stability factor. Higher stability values cause more aggressive
 * rate changes.
 */
public interface AutoRateProvider {

    /**
     * Records a purchase or transaction event that affects currency demand.
     *
     * @param player   the player involved
     * @param currency the currency used
     * @param amount   the amount spent
     */
    void recordTransaction(Player player, Currency currency, long amount);

    /**
     * Performs a periodic update to adjust exchange rates based on
     * accumulated transaction data and the stability factor.
     *
     * Typically called on a schedule (e.g. once per minute).
     */
    void updateRates();

    /**
     * Enables or disables automatic exchange rate mode.
     */
    void setAutoMode(boolean enabled);

    /**
     * @return true if automatic exchange rate mode is active.
     */
    boolean isAutoMode();

    /**
     * Sets the stability factor for automatic rate adjustments.
     * Default is 1.0.
     *
     * Higher values = more aggressive rate changes.
     * Lower values = smoother, slower changes.
     * 0.0 = no automatic changes (effectively frozen).
     */
    void setStability(double stability);

    /**
     * @return the current stability factor.
     */
    double getStability();
}