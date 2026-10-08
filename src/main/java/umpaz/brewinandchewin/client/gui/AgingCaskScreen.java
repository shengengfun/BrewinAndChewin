package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import umpaz.brewinandchewin.common.block.entity.container.AgingCaskMenu;

/**
 * Placeholder shell while the aging cask GUI is ported to 26.1's two-phase GUI. See
 * reports/bac-26.1-status.md for the remaining work (GuiGraphicsExtractor + extractRenderState).
 */
public class AgingCaskScreen extends AbstractContainerScreen<AgingCaskMenu> {
    public AgingCaskScreen(AgingCaskMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 178);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }
}
