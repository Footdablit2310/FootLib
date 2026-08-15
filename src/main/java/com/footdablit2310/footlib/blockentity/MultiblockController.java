package com.footdablit2310.footlib.blockentity;

import com.footdablit2310.footlib.multiblock.FootLibMultiblockUtil;
import com.footdablit2310.footlib.multiblock.MultiBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class MultiblockController extends BlockEntity {

    private ResourceLocation activeMultiblockId;
    private BlockPos controllerOrigin;
    private boolean multiblockActive;

    public MultiblockController(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // ---------------------------
    // Activation setters
    // ---------------------------

    public void activate(ResourceLocation id, BlockPos origin) {
        this.activeMultiblockId = id;
        this.controllerOrigin = origin;
        this.multiblockActive = true;
        setChanged();
    }

    public void deactivate() {
        this.multiblockActive = false;
        setChanged();
    }

    // ---------------------------
    // Tick logic
    // ---------------------------

    public static void tick(Level level, BlockPos pos, BlockEntity be) {
        if (!(be instanceof MultiblockController controller)) return;
        controller.tickInternal(level);
    }

    private void tickInternal(Level level) {
        if (level.isClientSide) return;
        if (!multiblockActive) return;

        MultiBlockData data = FootLibMultiblockUtil.getById(activeMultiblockId);
        if (data == null) {
            deactivate();
            return;
        }

        if (!FootLibMultiblockUtil.isStillValid(level, controllerOrigin, data)) {
            deactivate();
            return;
        }

        runMultiblock();
    }

    // ---------------------------
    // Abstract machine logic
    // ---------------------------

    protected abstract void runMultiblock();

    // ---------------------------
    // Save / Load
    // ---------------------------

    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);

        if (activeMultiblockId != null)
            tag.putString("ActiveMultiblock", activeMultiblockId.toString());

        if (controllerOrigin != null)
            tag.putLong("ControllerOrigin", controllerOrigin.asLong());

        tag.putBoolean("MultiblockActive", multiblockActive);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("ActiveMultiblock"))
            activeMultiblockId = ResourceLocation.fromNamespaceAndPath(tag.getString("ActiveMultiblock"));

        if (tag.contains("ControllerOrigin"))
            controllerOrigin = BlockPos.of(tag.getLong("ControllerOrigin"));

        multiblockActive = tag.getBoolean("MultiblockActive");
    }
}
