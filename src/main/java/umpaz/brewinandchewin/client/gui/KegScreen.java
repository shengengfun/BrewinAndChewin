package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;

/**
 * 26.1 moved GUI drawing to an extract/render split (GuiGraphicsExtractor + extractRenderState),
 * and the keg's recipe book was rebuilt around GhostSlots/RecipeDisplay. Until those are ported,
 * the screen keeps the container interaction (slots, shift-click) and lets the vanilla container
 * screen draw the frame. See reports/bac-26.1-status.md.
 */
public class KegScreen extends AbstractContainerScreen<KegMenu> {
    public KegScreen(KegMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }
}