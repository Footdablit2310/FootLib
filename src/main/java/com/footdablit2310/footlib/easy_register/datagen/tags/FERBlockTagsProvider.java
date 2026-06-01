package com.footdablit2310.footlib.easy_register.datagen.tags;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class FERBlockTagsProvider extends BlockTagsProvider {

    private final FootEasyRegisterSystem reg;

    public FERBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookup,
            String modid,
            ExistingFileHelper helper,
            FootEasyRegisterSystem reg
    ) {
        super(output, lookup, modid, helper);
        this.reg = reg;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        reg.getBlockTagEntries().forEach((tag, blocks) -> {
            var builder = tag(tag);
            for (Block block : blocks) {
                builder.add(block);
            }
        });
    }
}
