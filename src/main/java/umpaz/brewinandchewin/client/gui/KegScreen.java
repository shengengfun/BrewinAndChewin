package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;

/**
 * Placeholder shell while the keg GUI is ported to 26.1's two-phase GUI. See
 * reports/bac-26.1-status.md for the remaining work (GuiGraphicsExtractor + extractRenderState).
 */
public class KegScreen extends AbstractContainerScreen<KegMenu> {
    public KegScreen(KegMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    /**
     * The full keg screen cached fluid->item displays to redraw the tank; the 26.1 shim renders
     * through GuiGraphicsExtractor instead, so there is no cache to invalidate yet.
     */
    public static void clearFluidContainerComponents() {
    }
}
