package umpaz.brewinandchewin.common.utility;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * 26.1 moved food effects out of {@code FoodProperties} and into the CONSUMABLE component as a
 * list of {@link ConsumeEffect}s, so reading "the effects this food applies" has to walk those.
 */
public final class BnCFoodUtils {
    private BnCFoodUtils() {}

    public static List<MobEffectInstance> getEffects(ItemStack stack) {
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable == null)
            return List.of();
        List<MobEffectInstance> effects = new ArrayList<>();
        for (ConsumeEffect effect : consumable.onConsumeEffects()) {
            if (effect instanceof ApplyStatusEffectsConsumeEffect applied)
                effects.addAll(applied.effects());
        }
        return effects;
    }
}
