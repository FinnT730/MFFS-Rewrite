package dev.su5ed.mffs.item;

import dev.su5ed.mffs.api.Projector;
import dev.su5ed.mffs.network.DrawHologramPacket;
import dev.su5ed.mffs.network.Network;
import dev.su5ed.mffs.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.Set;

public class StabilizationModuleItem extends ModuleItem {
    private int blockCount = 0;

    public StabilizationModuleItem() {
        super(ModItems.itemProperties().stacksTo(1), 20);
        withDescription();
    }

    @Override
    public boolean beforeProject(Projector projector, Set<BlockPos> fields) {
        this.blockCount = 0;
        return false;
    }

    @Override
    public ProjectAction onProject(Projector projector, BlockPos position) {
        if (projector.getTicks() % 40 == 0) {
            BlockEntity be = (BlockEntity) projector;
            Level level = be.getLevel();
            BlockPos pos = be.getBlockPos();

            for (Direction side : Direction.values()) {
                IItemHandler handler =  Optional.ofNullable(level.getBlockEntity(pos.relative(side)))
                    .flatMap(neighbor -> neighbor.getCapability(ForgeCapabilities.ITEM_HANDLER, side).resolve())
                    .orElse(null);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.extractItem(i, 1, true);

                        if (stack.getItem() instanceof BlockItem blockItem && level.setBlockAndUpdate(position, blockItem.getBlock().defaultBlockState())) {
                            handler.extractItem(i, 1, false);
                            Vec3 start = Vec3.atLowerCornerOf(pos);
                            Vec3 target = Vec3.atLowerCornerOf(position);
                            Network.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(position)), new DrawHologramPacket(start, target, DrawHologramPacket.Type.CONSTRUCT));

                            if (this.blockCount++ >= projector.getModuleCount(ModItems.SPEED_MODULE.get()) / 3) {
                                return ProjectAction.INTERRUPT;
                            } else {
                                return ProjectAction.SKIP;
                            }
                        }
                    }
                }
            }
        }
        return ProjectAction.SKIP;
    }
}
