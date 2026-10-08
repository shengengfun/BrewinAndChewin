package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import umpaz.brewinandchewin.common.block.entity.container.AgingCaskMenu;

/** See KegScreen for why this is currently the vanilla frame only. */
public class AgingCaskScreen extends AbstractContainerScreen<AgingCaskMenu> {
    public AgingCaskScreen(AgingCaskMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 178;
    }
}