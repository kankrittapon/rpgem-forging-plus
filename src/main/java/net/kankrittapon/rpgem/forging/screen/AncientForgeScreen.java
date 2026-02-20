package net.kankrittapon.rpgem.forging.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kankrittapon.rpgem.core.init.ModAttachments;
import net.kankrittapon.rpgem.core.init.ModItems;
import net.kankrittapon.rpgem.forging.init.ModForgingItems;
import net.kankrittapon.rpgem.forging.menu.AncientForgeMenu;
import net.kankrittapon.rpgem.forging.network.PacketUpgradeItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class AncientForgeScreen extends AbstractContainerScreen<AncientForgeMenu> {

    // Use vanilla dispenser texture as placeholder until a custom texture is added
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft",
            "textures/gui/container/dispenser.png");

    private static final int BG_WIDTH = 176;
    private static final int BG_HEIGHT = 166;

    // Button position
    private static final int BUTTON_X = 60;
    private static final int BUTTON_Y = 75;
    private static final int BUTTON_W = 56;
    private static final int BUTTON_H = 20;

    // Label positions
    private static final int TEXT_MODE_X = 8;
    private static final int TEXT_MODE_Y = 20;
    private static final int TEXT_FS_X = 28;
    private static final int TEXT_FS_Y = 63;

    private Button upgradeButton;

    public AncientForgeScreen(AncientForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BG_WIDTH;
        this.imageHeight = BG_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        // Hide default labels (we draw custom ones)
        this.titleLabelX = 10000;
        this.inventoryLabelX = 10000;

        this.upgradeButton = this.addRenderableWidget(
                Button.builder(Component.literal("Upgrade"), button -> PacketDistributor.sendToServer(
                        new PacketUpgradeItem(this.menu.blockEntity.getBlockPos())))
                        .bounds(leftPos + BUTTON_X, topPos + BUTTON_Y, BUTTON_W, BUTTON_H)
                        .build());
    }

    @Override
    public void containerTick() {
        super.containerTick();
        boolean isRepair = this.menu.getSlot(1).getItem()
                .is(ModForgingItems.MEMORY_FRAGMENT.get());
        this.upgradeButton.setMessage(
                Component.literal(isRepair ? "Repair" : "Upgrade"));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Mode label
        boolean isRepair = this.menu.getSlot(1).getItem()
                .is(ModForgingItems.MEMORY_FRAGMENT.get());
        String modeText = isRepair ? "REPAIR" : "UPGRADE";
        int modeColor = isRepair ? 0x55FF55 : 0xFFAA00;
        guiGraphics.drawString(this.font, modeText, TEXT_MODE_X, TEXT_MODE_Y, modeColor, false);

        // Fail Stack label
        int failStack = 0;
        if (this.minecraft != null && this.minecraft.player != null) {
            failStack = this.minecraft.player.getData(ModAttachments.FAIL_STACK);
        }
        guiGraphics.drawString(this.font, "FS: " + failStack,
                TEXT_FS_X, TEXT_FS_Y, 0xFF5555, false);
    }
}
