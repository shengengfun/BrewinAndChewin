package umpaz.brewinandchewin.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class JamJarItem extends ConsumableItem {
    public JamJarItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK.value();
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK.value();
    }
}
