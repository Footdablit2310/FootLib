package com.footdablit2310.footlib.api.common.multiblock;

import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;

public record ShapeCoords(Map<List<Integer>, Block> blocks) implements MultiBlockShape {}
