package umpaz.brewinandchewin.common.mixin.client;

import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import umpaz.brewinandchewin.client.utility.BnCClientTextUtils;

/**
 * Scrambles sign text for a drunk player.
 *
 * <p>26.1 moved sign text submission onto {@code AbstractSignRenderer#submitSignText}, which covers
 * both standing and hanging signs, so a single hook replaces the two 1.21.1 had (one of which
 * targeted Farmer's Delight's canvas sign renderer, which no longer exists).
 */
@Mixin(AbstractSignRenderer.class)
public class TipsySignRendererMixin {

    @ModifyVariable(method = "submitSignText", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private SignText brewinandchewin$renderSignText(SignText signText) {
        return BnCClientTextUtils.signRenderer(signText);
    }
}
