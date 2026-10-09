package umpaz.brewinandchewin.common.crafting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;

import java.util.List;
import java.util.Optional;

/**
 * The keg's {@link RecipeDisplay}.
 *
 * <p>26.1 split {@code Recipe} into "what the game executes" and "what the recipe book shows", the
 * latter being a {@code RecipeDisplay}. Vanilla's displays only describe item slots, so the keg's
 * fluid ingredient, fluid result, temperature and duration get their own display type; the recipe
 * book builds its buttons and ghost slots straight from {@link SlotDisplay}s, and
 * {@code KegRecipeBookComponent} reads the extra fields to draw the tank.
 */
public record KegRecipeDisplay(
        List<SlotDisplay> inputs,
        Optional<FluidIngredientWithAmount> fluidIngredient,
        Either<AbstractedFluidStack, ItemStackTemplate> output,
        int duration,
        int temperature,
        float experience
) implements RecipeDisplay {

    public static final MapCodec<KegRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SlotDisplay.CODEC.listOf().fieldOf("inputs").forGetter(KegRecipeDisplay::inputs),
            FluidIngredientWithAmount.CODEC.optionalFieldOf("base_fluid").forGetter(KegRecipeDisplay::fluidIngredient),
            Codec.either(AbstractedFluidStack.CODEC, ItemStackTemplate.CODEC).fieldOf("result").forGetter(KegRecipeDisplay::output),
            Codec.INT.fieldOf("duration").forGetter(KegRecipeDisplay::duration),
            Codec.INT.optionalFieldOf("temperature", 3).forGetter(KegRecipeDisplay::temperature),
            Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(KegRecipeDisplay::experience)
    ).apply(inst, KegRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KegRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), KegRecipeDisplay::inputs,
            ByteBufCodecs.optional(FluidIngredientWithAmount.STREAM_CODEC), KegRecipeDisplay::fluidIngredient,
            ByteBufCodecs.either(AbstractedFluidStack.STREAM_CODEC, ItemStackTemplate.STREAM_CODEC), KegRecipeDisplay::output,
            ByteBufCodecs.VAR_INT, KegRecipeDisplay::duration,
            ByteBufCodecs.VAR_INT, KegRecipeDisplay::temperature,
            ByteBufCodecs.FLOAT, KegRecipeDisplay::experience,
            KegRecipeDisplay::new);

    public static final RecipeDisplay.Type<KegRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public SlotDisplay result() {
        return this.output.map(KegRecipeDisplay::fluidDisplay, SlotDisplay.ItemStackSlotDisplay::new);
    }

    /**
     * The keg is not placed as an item, so the crafting station shown to recipe viewers is the fluid
     * the recipe makes - vanilla has no "fluid slot" concept to reuse here.
     */
    @Override
    public SlotDisplay craftingStation() {
        return SlotDisplay.Empty.INSTANCE;
    }

    @Override
    public RecipeDisplay.Type<KegRecipeDisplay> type() {
        return TYPE;
    }

    /**
     * Drinks are registered as both an item and a fluid under the same id, so the item to show for a
     * fermented fluid is normally the item of the same name. Anything else falls back to the bucket.
     */
    private static SlotDisplay fluidDisplay(AbstractedFluidStack stack) {
        if (stack.isEmpty()) {
            return SlotDisplay.Empty.INSTANCE;
        }
        Identifier id = BuiltInRegistries.FLUID.getKey(stack.fluid());
        Item item = BuiltInRegistries.ITEM.getValue(id);
        if (item == Items.AIR) {
            item = stack.fluid().getBucket();
        }
        return item == Items.AIR ? SlotDisplay.Empty.INSTANCE : new SlotDisplay.ItemSlotDisplay(item);
    }
}
