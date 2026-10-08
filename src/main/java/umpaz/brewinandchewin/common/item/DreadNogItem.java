package umpaz.brewinandchewin.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.utility.BnCFoodUtils;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;

public class DreadNogItem extends BoozeItem {
    public DreadNogItem(Supplier<Fluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (!level.isClientSide()) {
            var badOmen = BnCFoodUtils.getEffects(stack).stream().filter(effect -> effect.getEffect().value() == MobEffects.BAD_OMEN).findFirst();
            this.affectConsumerBadOmen(consumer, badOmen.map(MobEffectInstance::getDuration).orElse(0), badOmen.map(MobEffectInstance::getAmplifier).orElse(-1));
        }
        return super.finishUsingItem(stack, level, consumer);
    }

    public void affectConsumerBadOmen(LivingEntity consumer, int duration, int potency) {
        if (consumer.hasEffect(MobEffects.BAD_OMEN)) {
            MobEffectInstance effect = consumer.getEffect(MobEffects.BAD_OMEN);
            consumer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, effect.getDuration() == -1 ? -1 : Math.max(effect.getDuration(), duration), Math.min(effect.getAmplifier() + potency + 1, 4), effect.isAmbient(), effect.isVisible(), effect.showIcon()));
        }
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, ctx, display, tooltip, flagIn);
        tooltip.accept(BnCTextUtils.getTranslation("tooltip.dread_nog").withStyle(ChatFormatting.RED));
    }
}
