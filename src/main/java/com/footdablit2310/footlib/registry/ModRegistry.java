package com.footdablit2310.footlib.registry;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.item.MultiBlockValidatorItem;
import com.footdablit2310.footlib.menu.MultiblockValidatorItemMenu;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRegistry {
    public static final FootEasyRegisterSystem FER = FootLib.FER;
    public static DeferredHolder<Item, MultiBlockValidatorItem> MULTIBLOCK_VALIDATOR_ITEM;
    public static DeferredHolder<MenuType<?>, MenuType<AbstractContainerMenu>> MULTIBLOCK_VALIDATOR_ITEM_MENU;
    public static void register() {
        MULTIBLOCK_VALIDATOR_ITEM =FER.item("multiblock_validator_item", ()-> new MultiBlockValidatorItem(new Item.Properties().rarity(Rarity.EPIC))).creativeTab().register();
        MULTIBLOCK_VALIDATOR_ITEM_MENU= FER.menu("multiblock_validator_item_menu").factory(MultiblockValidatorItemMenu::new).register();
    }
}
