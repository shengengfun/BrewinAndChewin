package umpaz.brewinandchewin.common.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import umpaz.brewinandchewin.client.utility.BnCTipsyHeartOverlay;

/**
 * Adds the tipsy effect's numbed hearts to the health bar.
 *
 * <p>The hook sits at the end of {@code extractHearts} rather than around each heart, so the
 * numbed hearts land on top of the vanilla ones without reimplementing the layout.
 */
@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "extractHearts", at = @At("TAIL"))
    private void brewinandchewin$extractNumbedHearts(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase,
                                                     int healthRowHeight, int heartOffsetIndex, float maxHealth,
                                                     int currentHealth, int oldHealth, int absorption, boolean blink,
                                                     CallbackInfo ci) {
        BnCTipsyHeartOverlay.render(graphics, player, xLeft, yLineBase, healthRowHeight, heartOffsetIndex, maxHealth,
                currentHealth, absorption);
    }
}
