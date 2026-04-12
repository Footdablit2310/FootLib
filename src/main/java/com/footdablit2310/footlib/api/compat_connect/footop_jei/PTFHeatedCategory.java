package com.footdablit2310.footlib.api.compat_connect.footop_jei;

import com.footdablit2310.footlib.api.integration.jei.JEICategoryLayoutHelper;
import com.footdablit2310.footlib.api.integration.jei.JEICategoryBuilder;
import com.footdablit2310.footlib.api.integration.jei.JEIRecipeWrapper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PTFHeatedCategory implements mezz.jei.api.recipe.category.IRecipeCategory<JEIRecipeWrapper> {

    
    public static final RecipeType<JEIRecipeWrapper> TYPE =
            JEICategoryBuilder.type("footorganicprocessing:ptf_heated", JEIRecipeWrapper.class);

    private final IDrawable background;
    @SuppressWarnings("unused") // These are actually used by the code but the IDE doesn't recognize it.
    private final IDrawable iconHeated; // This is actually used but the IDE doesn't recognize it.
    @SuppressWarnings("unused") // These are actually used by the code but the IDE doesn't recognize it.
    private final IDrawable iconSuperheated; // This is actually used but the IDE doesn't recognize it.
    private final IDrawable iconUltraheated;

    public PTFHeatedCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 70);

        this.iconHeated = guiHelper.createDrawableIngredient(
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                new ItemStack(net.minecraft.world.item.Items.BLAZE_POWDER)
        );

        this.iconSuperheated = guiHelper.createDrawableIngredient(
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                new ItemStack(net.minecraft.world.item.Items.LAVA_BUCKET)
        );

        this.iconUltraheated = guiHelper.createDrawableIngredient(
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                new ItemStack(
                        net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                                ResourceLocation.tryParse("footorganicprocessing:coil_t3_uh")
                        )
                )
        );
    }

    @Override
    public RecipeType<JEIRecipeWrapper> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("PTF Heated Processing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return iconUltraheated; // default icon
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIRecipeWrapper recipe, IFocusGroup focuses) {
        JEICategoryLayoutHelper.basicItemInOut(builder, recipe.inputs(), recipe.outputs());
    }
}
