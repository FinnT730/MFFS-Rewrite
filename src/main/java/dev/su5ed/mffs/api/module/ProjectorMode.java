package dev.su5ed.mffs.api.module;

import dev.su5ed.mffs.api.Projector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public interface ProjectorMode
{
	/**
	 * Called when the force field projector calculates the shape of the module.
	 *
	 * @param projector - The Projector Object
	 */
	<T extends BlockEntity & Projector> Set<Vec3> getExteriorPoints(T projector);

	/**
	 * @return Gets all interior points. Not translated or rotated.
	 */
	<T extends BlockEntity & Projector> Set<BlockPos> getInteriorPoints(T projector);

	/**
	 * @return Is this specific position inside this force field?
	 */
	boolean isInField(Projector projector, BlockPos position);
}
