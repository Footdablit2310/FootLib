package com.footdablit2310.footlib.api.common.multiblock;

import java.util.List;
import net.minecraft.world.level.block.Block;

public record Shape3D(List<List<List<Block>>> grid) implements MultiBlockShape {}
