package umpaz.brewinandchewin.platform.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;

public interface BnCClientPlatformHelper {
    void renderFluidInKeg(AbstractedFluidStack stack, GuiGraphicsExtractor gui, int x, int y, float alphaModifier);
}
