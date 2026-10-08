package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.Font;

/**
 * 26.1 keeps the tooltip component split but renders through GuiGraphicsExtractor.
 */
public class KegTooltip implements ClientTooltipComponent {
    private final Component text;

    public KegTooltip(KegTooltipComponent component) {
        this.text = component.text();
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
    public void extractImage(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
        graphics.text(font, text, mouseX, mouseY, 0xFFFFFFFF);
    }

    public record KegTooltipComponent(Component text) implements net.minecraft.world.inventory.tooltip.TooltipComponent {}
}