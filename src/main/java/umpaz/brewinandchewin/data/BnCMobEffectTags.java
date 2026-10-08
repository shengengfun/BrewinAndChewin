package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import umpaz.brewinandchewin.common.tag.BnCTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BnCMobEffectTags extends IntrinsicHolderTagsProvider<MobEffect>
{
    public BnCMobEffectTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.MOB_EFFECT, lookupProvider, mobEffect -> BuiltInRegistries.MOB_EFFECT.getResourceKey(mobEffect).orElseThrow(), BrewinAndChewin.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
    }

    protected void registerModTags() {
        tag(BnCTags.Effects.MILK_BOTTLE_LOW_PRIORITY)
                .add(BnCEffects.TIPSY.value());
        tag(BnCTags.Effects.TWISTED_WINE_AFFLICTIONS)
                .add(MobEffects.SLOWNESS.value())
                .add(MobEffects.WEAKNESS.value())
                .add(MobEffects.POISON.value())
                .add(MobEffects.MINING_FATIGUE.value())
                .add(MobEffects.BLINDNESS.value())
                .add(MobEffects.NAUSEA.value())
                .add(MobEffects.HUNGER.value())
                .add(MobEffects.DARKNESS.value())
                .add(MobEffects.UNLUCK.value());
        tag(BnCTags.Effects.OLD_WINE_EFFECTS)
                .add(MobEffects.SPEED.value())
                .add(MobEffects.HASTE.value())
                .add(MobEffects.STRENGTH.value())
                .add(MobEffects.JUMP_BOOST.value())
                .add(MobEffects.REGENERATION.value())
                .add(MobEffects.RESISTANCE.value())
                .add(MobEffects.FIRE_RESISTANCE.value())
                .add(MobEffects.WATER_BREATHING.value())
                .add(MobEffects.NIGHT_VISION.value())
                .add(MobEffects.ABSORPTION.value())
                .add(MobEffects.SLOWNESS.value())
                .add(MobEffects.MINING_FATIGUE.value())
                .add(MobEffects.NAUSEA.value())
                .add(MobEffects.BLINDNESS.value())
                .add(MobEffects.HUNGER.value())
                .add(MobEffects.WEAKNESS.value())
                .add(MobEffects.POISON.value())
                .add(MobEffects.LEVITATION.value())
                .add(MobEffects.GLOWING.value())
                .add(MobEffects.LUCK.value());
    }
}
