package umpaz.brewinandchewin.client.renderer.texture.modifier;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import umpaz.brewinandchewin.BrewinAndChewin;

public class PotionTintTextureModifier implements TextureModifier {
    public static final Identifier ID = BrewinAndChewin.asResource("potion_tint");
    public static final MapCodec<PotionTintTextureModifier> CODEC = MapCodec.unit(PotionTintTextureModifier::new);

    @Override
    public int color(BlockAndTintGetter level, BlockState state, BlockPos pos, ItemStack stack, int previous) {
        int color = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
        return ARGB.color(255, ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MapCodec<? extends TextureModifier> codec() {
        return CODEC;
    }
}
