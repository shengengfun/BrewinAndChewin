package umpaz.brewinandchewin.neoforge.client.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.platform.client.BnCClientPlatformHelper;

public class BnCClientPlatfomHelperNeoForge implements BnCClientPlatformHelper {
    // NOTE(26.1): getModel/registerItemProperty/tesselateModel were built on BakedModel,
    // ModelResourceLocation and ItemProperties#register, all removed in 26.1 (models are
    // now BlockStateModel/ItemModel; item predicates are JSON range_dispatch). Dropped for
    // now - see bac-26.1-status.md.

    @Override
    public void renderFluidInKeg(AbstractedFluidStack stack, GuiGraphicsExtractor gui, int x, int y, float alphaModifier) {
        FluidStack fluidStack = (FluidStack) stack.loaderSpecific();
        if (fluidStack == null)
            return;
        // 26.1 keeps fluid sprites/tints on the baked FluidModel rather than on a client
        // FluidType extension; the models themselves are registered in BrewinAndChewinNeoForgeClient.
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidStack.getFluid().defaultFluidState());
        TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();
        FluidTintSource tintSource = fluidModel.fluidTintSource();
        int tintColor = tintSource != null ? tintSource.color(fluidStack.getFluid().defaultFluidState()) : 0xFFFFFFFF;

        float alpha = ((tintColor >> 24) & 0xFF) / 255f * alphaModifier;
        float red = ((tintColor >> 16) & 0xFF) / 255f;
        float green = ((tintColor >> 8) & 0xFF) / 255f;
        float blue = (tintColor & 0xFF) / 255f;

        float capacity = Math.min(KegBlockEntity.localizedCapacity(), stack.unit().convertToLoader(stack.amount())) / (float) KegBlockEntity.localizedCapacity();
        if (capacity > 0.57) {
            int y1 = y + (int) (12 * (1 - ((capacity - 0.57F) / .43F)));
            int y2 = y + 12;
            float topCapacity = (capacity - 0.57F) / 0.43F;
            float vDistance = sprite.getV1() - sprite.getV0();
            float v0 = sprite.getV0() + (0.25F * vDistance) + (0.75F * vDistance * (1 - topCapacity));
            gui.blit(sprite.atlasLocation(), x, y1, 16, y2 - y1, sprite.getU0(), sprite.getU1(), v0, sprite.getV1());
            gui.blit(sprite.atlasLocation(), x + 16, y1, 8, y2 - y1, sprite.getU0(), sprite.getU0() + 0.5F * (sprite.getU1() - sprite.getU0()), v0, sprite.getV1());

        }
        int y1 = y + 12 + (int) (16 * (1 - Math.min(1, (capacity / .57F))));
        int y2 = y + 12 + 16;
        float vDistance = sprite.getV1() - sprite.getV0();
        float v0 = sprite.getV0() + (vDistance * (1 - Math.min(1, (capacity / .57F))));
        gui.blit(sprite.atlasLocation(), x, y1, 16, y2 - y1, sprite.getU0(), sprite.getU1(), v0, sprite.getV1());
        gui.blit(sprite.atlasLocation(), x + 16, y1, 8, y2 - y1, sprite.getU0(), sprite.getU0() + 0.5F * (sprite.getU1() - sprite.getU0()), v0, sprite.getV1());
    }
}
