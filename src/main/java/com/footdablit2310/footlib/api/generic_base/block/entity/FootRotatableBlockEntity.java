package com.footdablit2310.footlib.api.generic_base.block.entity;

import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import com.footdablit2310.footlib.api.rotation.RotationData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class for BlockEntities that use FootLib's quaternion rotation API.
 *
 * <p>This class owns the rotation data and handles its persistence. Subclasses
 * only need to provide their normal BlockEntity type and register the ticker
 * returned by {@link #tick(Level, BlockPos, BlockState, FootRotatableBlockEntity)}
 * if they want smooth rotation interpolation to advance automatically.</p>
 */
public abstract class FootRotatableBlockEntity extends BlockEntity implements RotatableBlockEntity {
    private final RotationData footlibRotationData = new RotationData();

    protected FootRotatableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public final RotationData footlib$getRotationData() {
        return footlibRotationData;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveRotation(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        loadRotation(tag);
    }

    /**
     * BlockEntityTicker-compatible ticker. Register it from the subclass's
     * getTicker method to advance smooth rotation interpolation.
     */
    public static void tick(Level level, BlockPos pos, BlockState state,
            FootRotatableBlockEntity blockEntity) {
        if (blockEntity.tickRotation()) {
            blockEntity.setChanged();
        }
    }
}
