package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;

/**
 * Food definitions.
 *
 * <p>26.1 split a food in two: {@link FoodProperties} is now only
 * {@code (nutrition, saturation, canAlwaysEat)}, and the status effects an item applies when eaten
 * moved to the {@code CONSUMABLE} component as a list of {@link ConsumeEffect}s. The old
 * {@code FoodProperties.Builder#fast()} is gone as well - a quick bite is
 * {@code Consumable#consumeSeconds(0.8F)}, which is what the vanilla dried kelp uses.
 *
 * <p>So each entry here pairs the two halves, and call sites use
 * {@link Food#apply(Item.Properties)} (which installs both components at once) instead of
 * {@code Item.Properties#food(FoodProperties)}.
 */
public class BnCFoods {

    /** The two halves of a 26.1 food. */
    public record Food(FoodProperties foodProperties, Consumable consumable) {
        public Item.Properties apply(Item.Properties properties) {
            return properties.food(foodProperties, consumable);
        }
    }

    /** A drink: no nutrition, always edible. */
    private static Food drink(ConsumeEffect... effects) {
        return new Food(new FoodProperties.Builder().alwaysEdible().build(), consumable(false, effects));
    }

    private static Food food(int nutrition, float saturationModifier, ConsumeEffect... effects) {
        return new Food(new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .build(), consumable(false, effects));
    }

    /** "fast" food: eaten in 0.8s, the 26.1 equivalent of the removed {@code Builder#fast()}. */
    private static Food fastFood(int nutrition, float saturationModifier, ConsumeEffect... effects) {
        return new Food(new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .build(), consumable(true, effects));
    }

    private static Consumable consumable(boolean fast, ConsumeEffect... effects) {
        Consumable.Builder builder = Consumables.defaultFood();
        if (fast) {
            builder.consumeSeconds(0.8F);
        }
        for (ConsumeEffect effect : effects) {
            builder.onConsume(effect);
        }
        return builder.build();
    }

    /** Applies with certainty, matching the old {@code effect(effect, 1.0F)}. */
    private static ConsumeEffect effect(MobEffectInstance effect) {
        return new ApplyStatusEffectsConsumeEffect(effect, 1.0F);
    }

    private static ConsumeEffect effect(MobEffectInstance effect, float probability) {
        return new ApplyStatusEffectsConsumeEffect(effect, probability);
    }

    public static final Food BEER = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, true, false)));
    public static final Food MEAD = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(BnCEffects.SWEET_HEART, 2400, 0, false, false)));
    public static final Food PALE_JANE = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 3600, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 3000, 0, false, false)),
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 2400, 0, false, false)));
    public static final Food EGG_GROG = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0)));
    public static final Food GLITTERING_GRENADINE = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.GLOWING, 600, 0)),
            effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0)));
    public static final Food SACCHARINE_RUM = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 3600, 1)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 2400, 0, false, false)),
            effect(new MobEffectInstance(BnCEffects.SWEET_HEART, 3600, 0, false, false)));
    public static final Food SALTY_FOLLY = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 3600, 1)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 3000, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1800, 0)));
    public static final Food BLOODY_MARY = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 1)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(BnCEffects.RAGING, 1200, 0)));
    public static final Food RED_RUM = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 2)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(BnCEffects.RAGING, 2400, 0)));
    public static final Food STRONGROOT_ALE = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 1)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0)));
    public static final Food STEEL_TOE_STOUT = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 2)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.RESISTANCE, 1200, 0)));
    public static final Food DREAD_NOG = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 4800, 2)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 4200, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.BAD_OMEN, 72000, 0)));
    public static final Food WITHERING_DROSS = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 3600, 2)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 3000, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0)),
            effect(new MobEffectInstance(MobEffects.WEAKNESS, 3000, 0)),
            effect(new MobEffectInstance(MobEffects.SLOWNESS, 3000, 0)),
            effect(new MobEffectInstance(MobEffects.WITHER, 1200, 0)));

    // TODO: Give this more attention after the Farmer's Respite rework.
    public static final Food KOMBUCHA = drink(
            effect(new MobEffectInstance(BnCEffects.TIPSY, 2400, 0)),
            effect(new MobEffectInstance(BnCEffects.INTOXICATION, 1800, 0, false, false)),
            effect(new MobEffectInstance(MobEffects.HASTE, 1200, 1)));

    public static final Food GRAPES = fastFood(2, 0.3F);

    public static final Food FLAXEN_CHEESE = food(4, 1.0F);
    public static final Food SCARLET_CHEESE = food(4, 1.0F);

    public static final Food VEGETABLE_OMELET = food(12, 0.8F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false)));
    public static final Food CREAMY_ONION_SOUP = food(14, 0.75F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0, false, false)));
    public static final Food CHEESY_PASTA = food(14, 0.75F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0, false, false)));
    public static final Food HORROR_LASAGNA = food(16, 0.55F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0, false, false)));
    public static final Food SCARLET_PIEROGI = food(12, 1.0F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 8400, 0, false, false)));
    public static final Food FIERY_FONDUE = food(14, 0.75F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 8400, 0, false, false)));

    public static final Food PIZZA_SLICE = food(5, 1.0F);
    public static final Food QUICHE_SLICE = fastFood(4, 0.8F);

    public static final Food HAM_AND_CHEESE_SANDWICH = food(9, 1.0F);

    public static final Food RICH_CHOCOLATE_CAKE_SLICE = fastFood(2, 0.2F,
            effect(new MobEffectInstance(MobEffects.SPEED, 400, 0)));
    public static final Food GLOW_BERRY_MERINGUE_PIE_SLICE = fastFood(3, 0.2F,
            effect(new MobEffectInstance(MobEffects.SPEED, 600, 0)));
    public static final Food PUMPKIN_ROLL_SLICE = fastFood(3, 1.0F,
            effect(new MobEffectInstance(MobEffects.REGENERATION, 160, 0)));
    public static final Food ASPIC_CUBE = food(6, 0.6F);
    public static final Food RICE_PUDDING = food(12, 0.8F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0)));
    public static final Food GLOW_BROWNIE = fastFood(6, 0.6F,
            effect(new MobEffectInstance(MobEffects.SPEED, 800, 0)));
    public static final Food APPLE_TURNOVER = fastFood(5, 0.6F,
            effect(new MobEffectInstance(MobEffects.SPEED, 400, 0)));
    public static final Food JAM_SANDWICH = food(8, 0.6F);
    public static final Food RAW_CROISSANT = food(2, 0.6F,
            effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F));
    public static final Food CROISSANT = food(5, 1.2F);
    public static final Food RAW_SAUSAGE = food(3, 0.6F);
    public static final Food COOKED_SAUSAGE = food(8, 0.9F);
    public static final Food HAGGIS = food(8, 0.8F);
    public static final Food CHOPPED_LIVER = food(14, 0.75F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0)));
    public static final Food MAPLE_FUDGE = food(7, 0.6F,
            effect(new MobEffectInstance(MobEffects.SPEED, 800, 0)));
    public static final Food INNARDS = food(2, 0.2F,
            effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0)));
    public static final Food RENNET = food(1, 0.2F);

    public static final Food CORN = food(1, 0.6F);
    public static final Food COOKED_CORN = food(4, 0.6F);
    public static final Food POPPED_CORN = fastFood(2, 0.6F);
    public static final Food CANDIED_CORN = fastFood(1, 0.2F);
    public static final Food BUTTERSCOTCH_CANDY = fastFood(1, 0.2F);
    public static final Food CORN_DOUGH = food(2, 0.6F,
            effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F));
    public static final Food RAW_MUFFIN = food(2, 0.6F,
            effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F));
    public static final Food CORN_BREAD = food(6, 1.0F);
    public static final Food CORN_MUFFIN = food(6, 1.0F);
    public static final Food TORTILLA = food(4, 1.0F);
    public static final Food GRITS = food(6, 0.5F,
            effect(new MobEffectInstance(ModEffects.NOURISHMENT, 600, 0)));
    public static final Food KIMCHI = food(4, 0.6F);
    public static final Food JERKY = fastFood(3, 0.7F);
    public static final Food PICKLED_PICKLES = food(4, 0.3F);
    public static final Food KIPPERS = food(6, 0.5F);
    public static final Food COCOA_FUDGE = food(4, 0.8F,
            effect(new MobEffectInstance(MobEffects.SPEED, 800, 0, false, false)));

    public static final Food SWEET_BERRY_JAM = food(6, 0.4F);
    public static final Food GLOW_BERRY_MARMALADE = food(6, 0.4F);
    public static final Food APPLE_JELLY = food(10, 0.6F);
}
