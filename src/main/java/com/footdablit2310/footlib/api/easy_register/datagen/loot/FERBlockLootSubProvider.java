package com.footdablit2310.footlib.api.easy_register.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Set;

public abstract class FERBlockLootSubProvider extends BlockLootSubProvider {

    protected FERBlockLootSubProvider(HolderLookup.Provider lookup) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookup);
    }

    @Override
    protected abstract void generate();

    // ------------------------------------------------------------
    // Convenience helpers
    // ------------------------------------------------------------

    /** Drop the block itself */
    protected void dropSelfBlock(Block block) {
        this.dropSelf(block);
    }

    /** Drop a different item (e.g. crops, machines) */
    protected void dropOther(Block block, ItemLike drop) {
        this.add(block, LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(drop))));
    }

    /** Drop only when silk touch is used */
    protected void dropWhenSilkTouch(Block block, ItemLike drop) {
        // NEW SIGNATURE: only takes the drop item
        this.add(block, createSingleItemTableWithSilkTouch(block, drop));
    }

    /** Drop only when shears are used */
    protected void dropWithShears(Block block) {
        this.add(block, createShearsOnlyDrop(block));
    }

    /** Ore-style drop: block → item */
    protected void dropOre(Block ore, ItemLike drop) {
        // NEW SIGNATURE: drop must be Item, not ItemLike
        Item dropItem = drop.asItem();
        this.add(ore, createOreDrop(ore, dropItem));
    }
}
