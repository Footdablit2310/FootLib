package com.footdablit2310.footlib.api.compat_connect.footop_jei;

import com.footdablit2310.footlib.api.integration.jei.JEICategoryLayoutHelper;
import com.footdablit2310.footlib.api.integration.jei.JEICategoryBuilder;
import com.footdablit2310.footlib.api.integration.jei.JEIRecipeWrapper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PTFHeatedCategory implements IRecipeCategory<JEIRecipeWrapper> {

    public static final RecipeType<JEIRecipeWrapper> TYPE =
            JEICategoryBuilder.type("footorganicprocessing:ptf_heated", JEIRecipeWrapper.class);

    private final IDrawable background;
    @SuppressWarnings("unused")
    private final IDrawable iconHeated; //These are fake warnings
    @SuppressWarnings("unused")
    private final IDrawable iconSuperheated; //These are fake warnings
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
    public IDrawable getIcon() {
        return iconUltraheated;
    }

    /**
     * JEI 1.21.x: draw background manually instead of getBackground()
     */
    @Override
    public void draw(JEIRecipeWrapper recipe, IRecipeSlotsView slots, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIRecipeWrapper recipe, IFocusGroup focuses) {
        JEICategoryLayoutHelper.basicItemInOut(builder, recipe.inputs(), recipe.outputs());
    }
}
