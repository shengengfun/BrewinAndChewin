package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;

/**
 * 26.1 keeps the tooltip component split but renders through GuiGraphicsExtractor.
 */
public class KegTooltip implements ClientTooltipComponent {
    private final Component text;

    public KegTooltip(KegTooltipComponent component) {
        this.text = component.fluid().isEmpty() ? Component.empty() : Component.translatable("brewinandchewin.keg.contents", component.fluid().amount());
    }

    @Override
    public int getHeight(Font font) {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(text);
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        graphics.text(font, text, x, y, 0xFFFFFFFF);
    }

    public record KegTooltipComponent(AbstractedFluidStack fluid) implements net.minecraft.world.inventory.tooltip.TooltipComponent {}
}
