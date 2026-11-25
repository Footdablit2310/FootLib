package com.footdablit2310.footlib.api.rotation.client;

import com.footdablit2310.footlib.api.rotation.menu.RotationMenu;
import com.footdablit2310.footlib.api.rotation.network.RotationSubmitPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/** Four-component quaternion editor. */
public final class RotationEditorScreen extends AbstractContainerScreen<RotationMenu> {
    private EditBox xField, yField, zField, wField;

    public RotationEditorScreen(RotationMenu menu, net.minecraft.world.entity.player.Inventory inventory,
            Component title) {
        super(menu, inventory, title);
        this.imageWidth = 220;
        this.imageHeight = 132;
    }

    @Override
    protected void init() {
        super.init();
        int left = (width - imageWidth) / 2;
        int top = (height - imageHeight) / 2;
        xField = field(left + 12, top + 35, "X", menu.initialInput().x);
        yField = field(left + 62, top + 35, "Y", menu.initialInput().y);
        zField = field(left + 112, top + 35, "Z", menu.initialInput().z);
        wField = field(left + 162, top + 35, "W", menu.initialInput().w);
        xField.setEditable(menu.allowedAxes().contains("x"));
        yField.setEditable(menu.allowedAxes().contains("y"));
        zField.setEditable(menu.allowedAxes().contains("z"));
        wField.setEditable(menu.allowedAxes().contains("w"));
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> submit())
                .bounds(left + 112, top + 92, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(left + 12, top + 92, 96, 20).build());
    }

    private EditBox field(int x, int y, String label, double value) {
        EditBox box = new EditBox(font, x, y, 42, 20, Component.literal(label));
        box.setValue(Double.toString(value));
        box.setMaxLength(32);
        addRenderableWidget(box);
        return box;
    }

    private void submit() {
        try {
            double x = Double.parseDouble(xField.getValue());
            double y = Double.parseDouble(yField.getValue());
            double z = Double.parseDouble(zField.getValue());
            double w = Double.parseDouble(wField.getValue());
            PacketDistributor.sendToServer(new RotationSubmitPayload(menu.blockPos(), x, y, z, w));
        } catch (NumberFormatException ignored) {
            // Keep the editor open when any field is not a valid number.
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF202020);
        graphics.drawString(font, "Quaternion (x, y, z, w)", leftPos + 12, topPos + 16, 0xFFFFFF);
        graphics.drawString(font, "X", leftPos + 12, topPos + 25, 0xA0A0A0);
        graphics.drawString(font, "Y", leftPos + 62, topPos + 25, 0xA0A0A0);
        graphics.drawString(font, "Z", leftPos + 112, topPos + 25, 0xA0A0A0);
        graphics.drawString(font, "W", leftPos + 162, topPos + 25, 0xA0A0A0);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
