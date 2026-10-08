package umpaz.brewinandchewin.data.builder;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.FluidUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStackTemplate;

public class KegPouringRecipeBuilder {
    // 26.1 binds item components only after the registry is frozen, so datagen builds
    // templates and the recipe expands them at runtime.
    private ItemStackTemplate container;
    private final Fluid fluid;
    private final int amount;
    private Optional<FluidUnit> unit = Optional.empty();
    private final ItemStackTemplate output;
    private final boolean strict;
    private final boolean filling;
    private final List<ICondition> conditions = new ArrayList<>();
    private boolean includeCreateRecipes = true;

    private KegPouringRecipeBuilder(Fluid fluid, int amount, ItemStackTemplate output, boolean strict, boolean filling) {
        this.fluid = fluid;
        this.amount = amount;
        this.output = output;
        this.strict = strict;
        this.filling = filling;
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(Fluid fluid, int amount, ItemStack output, boolean strict) {
        return new KegPouringRecipeBuilder(fluid, amount, ItemStackTemplate.fromNonEmptyStack(output), strict, true);
    }

    /**
     * 26.1's item components are only readable after the registry is frozen, so datagen passes a
     * template rather than a materialised stack.
     */
    public static KegPouringRecipeBuilder kegPouringRecipe(Fluid fluid, int amount, ItemStackTemplate output, boolean strict) {
        return new KegPouringRecipeBuilder(fluid, amount, output, strict, true);
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(Fluid fluid, int amount, ItemStack output, boolean strict, boolean filling) {
        return new KegPouringRecipeBuilder(fluid, amount, ItemStackTemplate.fromNonEmptyStack(output), strict, filling);
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(Fluid fluid, int amount, ItemLike output) {
        return new KegPouringRecipeBuilder(fluid, amount, new ItemStackTemplate(output.asItem()), false, true);
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(Fluid fluid, int amount, ItemLike output, boolean filling) {
        return new KegPouringRecipeBuilder(fluid, amount, new ItemStackTemplate(output.asItem()), false, filling);
    }

    /**
     * Used for multi-loader implementation to make sure you can have just the one recipe.
     *
     * @param unit The unit to use for this fluid.
     */
    public KegPouringRecipeBuilder setFluidUnit(FluidUnit unit) {
        this.unit = Optional.of(unit);
        return this;
    }

    public KegPouringRecipeBuilder withContainer(ItemLike container) {
        this.container = new ItemStackTemplate(container.asItem());
        return this;
    }

    public KegPouringRecipeBuilder withCondition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public KegPouringRecipeBuilder excludeCreateCompat() {
        includeCreateRecipes = false;
        return this;
    }

    public void build(RecipeOutput consumerIn) {
        Identifier outputLocation = BuiltInRegistries.ITEM.getKey(output.item().value());
        build(consumerIn, BrewinAndChewin.MODID + ":pouring/" + outputLocation.getPath());
    }

    public void build(RecipeOutput consumerIn, String save) {
        Identifier resourcelocation = BuiltInRegistries.ITEM.getKey(output.item().value());
        if (resourcelocation.equals(Identifier.tryParse(save))) {
            throw new IllegalStateException("Pouring Recipe " + save + " should remove its 'save' argument");
        } else {
            build(consumerIn, Identifier.tryParse(save));
        }
    }

    public void build(RecipeOutput consumerIn, Identifier id) {
        consumerIn.accept(ResourceKey.create(Registries.RECIPE, id), new KegPouringRecipe(new AbstractedFluidStack(fluid, amount), Optional.ofNullable(container), output, unit, strict, filling), null);

        // TODO: Create recipe compat when Create updates.
//        if (ForgeRegistries.ITEMS.getKey(output.getItem()).getNamespace().equals("create") || !includeCreateRecipes)
//            return;
//
//        var fillingBuilder = new ProcessingRecipeBuilder<>(FillingRecipe::new, new Identifier(BrewinAndChewinNeoForge.MODID, "create/" + id.getPath().replace("pouring/", "")))
//                .require(fluid, amount)
//                .require(container == null ? output.getCraftingRemainingItem().getItem() : container.getItem())
//                .output(output)
//                .withCondition(new ModLoadedCondition("create"));
//
//        for (ICondition condition : conditions)
//            fillingBuilder.withCondition(condition);
//
//        fillingBuilder.build(consumerIn);
//
//        if (!filling)
//            return;
//
//        var emptyingBuilder = new ProcessingRecipeBuilder<>(EmptyingRecipe::new, new Identifier(BrewinAndChewinNeoForge.MODID, "create/" + id.getPath().replace("pouring/", "")))
//                .output(fluid, amount)
//                .output(container == null ? output.getCraftingRemainingItem().getItem() : container.getItem())
//                .withCondition(new ModLoadedCondition("create"));
//
//        if (strict)
//            emptyingBuilder.require(StrictNBTIngredient.of(output));
//        else
//            emptyingBuilder.require(output.getItem());
//
//        for (ICondition condition : conditions)
//            emptyingBuilder.withCondition(condition);
//
//        emptyingBuilder.build(consumerIn);
    }
}
