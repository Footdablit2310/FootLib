package com.footdablit2310.footlib.multiblock;

import net.minecraft.resources.ResourceLocation;

public interface IHasMultiblockType {
    /** The registry ID of the multiblock type this controller uses. */
    ResourceLocation getMultiblockTypeId();

    /** Called after successful form(). */
    void onMultiblockFormed(IMultiblockType type);

    /** Called after breakMultiblock(). */
    void onMultiblockBroken();
}