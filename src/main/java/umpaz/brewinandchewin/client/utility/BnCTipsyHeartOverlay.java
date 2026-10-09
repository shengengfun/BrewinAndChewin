package umpaz.brewinandchewin.client.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.attachment.TipsyHeartsAttachment;
import umpaz.brewinandchewin.common.registry.BnCEffects;

/**
 * Draws the numbed hearts the tipsy effect leaves behind.
 *
 * <p>1.21.1 wrapped the individual {@code Gui#renderHeart} calls, which meant recomputing nothing
 * but relying on local-capture ordinals. 26.1 gets the same result from a single hook at the end of
 * {@code Gui#extractHearts}: the health bar layout is handed over as arguments, so this only has to
 * walk the hearts and redraw the numbed ones on top.
 */
public final class BnCTipsyHeartOverlay {
    private static final int HEART_SIZE = 9;

    private static float numbedAlpha = 1.0F;
    private static boolean increaseNumbedAlpha = true;

    /**
     * @param healthRowHeight the distance between health rows; hearts on lower rows sit further up
     * @param heartOffsetIndex the vanilla regeneration bump, or -1
     */
    public static void render(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase, int healthRowHeight,
                              int heartOffsetIndex, float maxHealth, int currentHealth, int absorption) {
        TipsyHeartsAttachment attachment = BrewinAndChewin.getHelper().getTipsyHeartsAttachment(player);
        if (!player.hasEffect(BnCEffects.TIPSY) || attachment == null || attachment.getNumbedHealth() <= 0) {
            numbedAlpha = 1.0F;
            increaseNumbedAlpha = true;
            return;
        }

        float numbedHealth = attachment.getNumbedHealth();
        if (numbedHealth > currentHealth) {
            return;
        }

        boolean hardcore = player.level().getLevelData().isHardcore();
        boolean absorbing = absorption > 0;
        float numbedStart = currentHealth - numbedHealth;

        boolean flickering = BnCConfiguration.client().numbedHeartFlickering()
                && numbedHealth > 1
                && attachment.getTicksUntilDamage() < 80;
        if (flickering && !Minecraft.getInstance().isPaused()) {
            float increase = Mth.lerp((80 - attachment.getTicksUntilDamage()) / 80.0F, 0.0F, 0.06F);
            numbedAlpha = Mth.clamp(numbedAlpha + (increaseNumbedAlpha ? increase : -increase), -0.01F, 1.01F);
            if (numbedAlpha < 0.0F) {
                increaseNumbedAlpha = true;
            }
            if (numbedAlpha > 1.0F) {
                increaseNumbedAlpha = false;
            }
        } else {
            numbedAlpha = 1.0F;
            increaseNumbedAlpha = true;
        }

        int healthContainerCount = Mth.ceil(maxHealth / 2.0F);
        for (int containerIndex = healthContainerCount - 1; containerIndex >= 0; containerIndex--) {
            int leftHalf = containerIndex * 2;
            boolean leftNumbed = leftHalf < currentHealth && leftHalf >= numbedStart;
            boolean rightNumbed = leftHalf + 1 < currentHealth && leftHalf + 1 >= numbedStart;
            if (!leftNumbed && !rightNumbed) {
                continue;
            }

            int row = containerIndex / 10;
            int column = containerIndex % 10;
            int xo = xLeft + column * 8;
            int yo = yLineBase - row * healthRowHeight;
            if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex) {
                yo -= 2;
            }

            Identifier sprite;
            if (leftNumbed && rightNumbed) {
                sprite = BnCHudIcons.getTipsyFullHeartTexture(absorbing, hardcore);
            } else if (rightNumbed) {
                sprite = BnCHudIcons.getTipsyRightHeartTexture(absorbing, hardcore);
            } else {
                sprite = BnCHudIcons.getTipsyHalfHeartTexture(absorbing, hardcore);
            }
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xo, yo, HEART_SIZE, HEART_SIZE, numbedAlpha);
        }
    }

    private BnCTipsyHeartOverlay() {
    }
}
