package com.footdablit2310.footlib.multiblock;

import com.footdablit2310.footlib.FootLibRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class MultiblockRegistry {

    /** Validate by position. Resolves BE and delegates to the registered type. */
    public static ValidationResult validate(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) {
            return ValidationResult.fail("multiblock.footlib.no_block_entity", pos);
        }
        return validate(level, pos, be);
    }

    /** Validate with a known BE. */
    public static ValidationResult validate(Level level, BlockPos pos, BlockEntity be) {
        if (!(be instanceof IHasMultiblockType hasType)) {
            return ValidationResult.fail("multiblock.footlib.not_a_controller");
        }

        IMultiblockType type = FootLibRegistries.MULTIBLOCK_TYPES.get(hasType.getMultiblockTypeId());
        if (type == null) {
            return ValidationResult.fail("multiblock.footlib.unknown_type", hasType.getMultiblockTypeId());
        }

        return type.validate(level, pos, be);
    }

    public static void form(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return;
        if (!(be instanceof IHasMultiblockType hasType)) return;

        IMultiblockType type = FootLibRegistries.MULTIBLOCK_TYPES.get(hasType.getMultiblockTypeId());
        if (type == null) return;

        type.form(level, pos, be);
        hasType.onMultiblockFormed(type);
    }

    public static void breakMultiblock(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return;
        if (!(be instanceof IHasMultiblockType hasType)) return;

        IMultiblockType type = FootLibRegistries.MULTIBLOCK_TYPES.get(hasType.getMultiblockTypeId());
        if (type == null) return;

        type.breakMultiblock(level, pos, be);
        hasType.onMultiblockBroken();
    }

    public static boolean isFormed(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof IMultiblockController ctrl && ctrl.isFormed();
    }

    public static IMultiblockType getType(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IHasMultiblockType hasType)) return null;
        return FootLibRegistries.MULTIBLOCK_TYPES.get(hasType.getMultiblockTypeId());
    }

    private MultiblockRegistry() {}
}