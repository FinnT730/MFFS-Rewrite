package dev.su5ed.mffs.setup;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static dev.su5ed.mffs.MFFSMod.location;

public final class ModTags {
    public static final TagKey<Item> FORTRON_FUEL = itemTag("fortron_fuel");
    public static final TagKey<Block> FORCEFIELD_REPLACEABLE = blockTag("forcefield_replaceable");

    private static TagKey<Item> itemTag(String name) {
        return ItemTags.create(location(name));
    }

    private static TagKey<Block> blockTag(String name) {
        return BlockTags.create(location(name));
    }

    private ModTags() {}
}
