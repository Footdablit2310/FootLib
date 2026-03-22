package com.footdablit2310.footlib.api.shared.foot_economy;

/**
 * Represents the result of a balance modification.
 *
 * This allows mods to react to failures (e.g. insufficient funds)
 * without needing to know Foot Economy's internal logic.
 */
public record TransactionResult(boolean success, long newBalance, String reason) {

    public static TransactionResult success(long newBalance) {
        return new TransactionResult(true, newBalance, "");
    }

    public static TransactionResult failure(String reason, long currentBalance) {
        return new TransactionResult(false, currentBalance, reason);
    }
}