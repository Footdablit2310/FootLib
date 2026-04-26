package com.footdablit2310.footlib.api.common.multiblock;

import java.util.List;
import net.minecraft.world.level.block.Block;

/**
 * A 3D shape defined by a list of relative coordinates and their corresponding blocks.
 */
public record Shape2D(List<List<Block>> grid) implements MultiBlockShape {}
