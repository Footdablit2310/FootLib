package com.footdablit2310.footlib.api.common.hitbox;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeRotationUtil {

	public static VoxelShape rotateShape(Direction direction, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

		for (AABB box : shape.toAabbs()) {
			buffer[1] = Shapes.or(buffer[1], Shapes.create(
				rotateAABB(box, direction)
			));
		}

		return buffer[1];
	}

	private static AABB rotateAABB(AABB box, Direction direction) {
		return switch (direction) {
			case SOUTH -> new AABB(
				1 - box.maxX, box.minY,
				1 - box.maxZ, 1 - box.minX,
				box.maxY, 1 - box.minZ
			);
			case WEST -> new AABB(
				box.minZ, box.minY,
				1 - box.maxX, box.maxZ,
				box.maxY, 1 - box.minX
			);
			case EAST -> new AABB(
				1 - box.maxZ, box.minY,
				box.minX,
				1 - box.minZ, box.maxY,
				box.maxX
			);
			default -> box;
		};
	}
}
