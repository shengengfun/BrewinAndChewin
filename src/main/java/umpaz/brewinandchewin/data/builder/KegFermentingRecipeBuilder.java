package umpaz.brewinandchewin.data.builder;

import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.crafting.FluidIngredientWithAmount;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.crafting.FermentingBookCategory;
import umpaz.brewinandchewin.common.utility.FluidUnit;
import umpaz.brewinandchewin.neoforge.utility.KegCompatibleFluidIngredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStackTemplate;

public class KegFermentingRecipeBuilder {
    private final HolderGetter<Item> items;
    private final HolderGetter<Fluid> fluids;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final FermentingBookCategory tab;

    private Optional<FluidIngredientWithAmount> fluidIngredient = Optional.empty();
    private Optional<FluidUnit> unit = Optional.empty();
    // 26.1 binds item components only after the mod's registries are frozen, so datagen must
    // not materialise an ItemStack here - the template is expanded at runtime instead.
    private Either<AbstractedFluidStack, ItemStackTemplate> result = null;
    private final float experience;
    private final int temperature;
    private final int amount;

    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    private KegFermentingRecipeBuilder(HolderGetter<Item> items, HolderGetter<Fluid> fluids, FermentingBookCategory tab, int amount, float experience, int temperature) {
        this.items = items;
        this.fluids = fluids;
        this.tab = tab;
        this.experience = experience;
        this.temperature = temperature;
        this.amount = amount;
    }

    public static KegFermentingRecipeBuilder kegFermentingRecipe(HolderGetter<Item> items, HolderGetter<Fluid> fluids, FermentingBookCategory tab, Item item, int amount, float experience, int temperature) {
        KegFermentingRecipeBuilder i = new KegFermentingRecipeBuilder(items, fluids, tab, amount, experience, temperature);
        i.setResult(item);
        return i;
    }

    public static KegFermentingRecipeBuilder kegFermentingRecipe(HolderGetter<Item> items, HolderGetter<Fluid> fluids, FermentingBookCategory tab, Fluid fluid, int amount, float experience, int temperature) {
        KegFermentingRecipeBuilder i = new KegFermentingRecipeBuilder(items, fluids, tab, amount, experience, temperature);
        i.setResult(fluid);
        return i;
    }

    public static KegFermentingRecipeBuilder kegFermentingRecipe(HolderGetter<Item> items, HolderGetter<Fluid> fluids, FermentingBookCategory tab, Item item, int amount, float experience) {
        KegFermentingRecipeBuilder i = new KegFermentingRecipeBuilder(items, fluids, tab, amount, experience, 3);
        i.setResult(item);
        return i;
    }

    public static KegFermentingRecipeBuilder kegFermentingRecipe(HolderGetter<Item> items, HolderGetter<Fluid> fluids, FermentingBookCategory tab, Fluid fluid, int amount, float experience) {
        KegFermentingRecipeBuilder i = new KegFermentingRecipeBuilder(items, fluids, tab, amount, experience, 3);
        i.setResult(fluid);
        return i;
    }

    /**
     * Used for multi-loader implementation to make sure you can have just the one recipe.
     *
     * @param unit The unit to use for this fluid.
     */
    public KegFermentingRecipeBuilder setFluidUnit(FluidUnit unit) {
        if (result.left().isPresent() && result.left().get().unit() != unit) {
            throw new UnsupportedOperationException("You need to set your fluid unit after your result.");
        }
        this.unit = Optional.of(unit);
        return this;
    }

    private void setResult(Fluid fluid) {
        result = Either.left(new AbstractedFluidStack(fluid, amount));
    }

    private void setResult(FluidStack fluid) {
        result = Either.left(new AbstractedFluidStack(fluid.getFluid(), fluid.getAmount(), fluid.getComponents(), unit.orElse(FluidUnit.getLoaderUnit()), fluid));
    }

    private void setResult(Item item) {
        result = Either.right(new ItemStackTemplate(item, amount));
    }

    private void setResult(ItemStack stack) {
        result = Either.right(ItemStackTemplate.fromNonEmptyStack(stack).withCount(amount));
    }


    public KegFermentingRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(items.getOrThrow(tagIn)));
    }

    public KegFermentingRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public KegFermentingRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        addIngredient(Ingredient.of(itemIn), quantity);
        return this;
    }

    public KegFermentingRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public KegFermentingRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    public KegFermentingRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
        advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public KegFermentingRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return this.unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    public KegFermentingRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.advancement.addCriterion("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(items));
        return this;
    }

    public void build(RecipeOutput consumerIn) {
        if (result == null)
            throw new NullPointerException("Fermenting Recipe does not specify a result.");

        if (result.right().isPresent()) {
            if (fluidIngredient.isPresent() && fluidIngredient.get().ingredient() instanceof KegCompatibleFluidIngredients.Exact exact && !exact.displayStacks().isEmpty()) {
                Identifier baseFluidLocation = BuiltInRegistries.FLUID.getKey(exact.displayStacks().getFirst().fluid());
                Identifier resultItemLocation = BuiltInRegistries.ITEM.getKey(result.right().get().item().value());
                build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultItemLocation.getPath() + "_from_" + baseFluidLocation.getPath());
                return;
            } else if (fluidIngredient.isPresent() && fluidIngredient.get().ingredient() instanceof KegCompatibleFluidIngredients.Tag tag && tag.getTagKey() != null) {
                Identifier baseFluidLocation = tag.getTagKey().location();
                Identifier resultItemLocation = BuiltInRegistries.ITEM.getKey(result.right().get().item().value());
                build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultItemLocation.getPath() + "_from_" + baseFluidLocation.getPath());
                return;
            }
            Identifier resultItemLocation = BuiltInRegistries.ITEM.getKey(result.right().get().item().value());
            build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultItemLocation.getPath());
            return;
        }

        if (fluidIngredient.isPresent() && fluidIngredient.get().ingredient() instanceof KegCompatibleFluidIngredients.Exact exact && !exact.displayStacks().isEmpty()) {
            Identifier baseFluidLocation = BuiltInRegistries.FLUID.getKey(exact.displayStacks().getFirst().fluid());
            Identifier resultFluidLocation = BuiltInRegistries.FLUID.getKey(result.left().get().fluid());
            build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultFluidLocation.getPath() + "_from_" + baseFluidLocation.getPath());
            return;
        } else if (fluidIngredient.isPresent() && fluidIngredient.get().ingredient() instanceof KegCompatibleFluidIngredients.Tag tag && tag.getTagKey() != null) {
            Identifier baseFluidLocation = tag.getTagKey().location();
            Identifier resultFluidLocation = BuiltInRegistries.FLUID.getKey(result.left().get().fluid());
            build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultFluidLocation.getPath() + "_from_" + baseFluidLocation.getPath());
            return;
        }
        Identifier resultFluidLocation = BuiltInRegistries.FLUID.getKey(result.left().get().fluid());
        build(consumerIn, BrewinAndChewin.MODID + ":fermenting/" + resultFluidLocation.getPath());
    }

    public void build(RecipeOutput consumerIn, String save) {
        if (result == null)
            throw new NullPointerException("Fermenting Recipe " + save + " does not specify a result.");

        Identifier resourcelocation = result.map(wrapper -> BuiltInRegistries.FLUID.getKey(wrapper.fluid()), stack -> BuiltInRegistries.ITEM.getKey(stack.item().value()));
        if (resourcelocation.equals(Identifier.tryParse(save))) {
            throw new IllegalStateException("Fermenting Recipe " + save + " should remove its 'save' argument");
        } else {
            build(consumerIn, Identifier.tryParse(save));
        }
    }

    public KegFermentingRecipeBuilder addFluidIngredient(Fluid fluid, int i) {
        fluidIngredient = Optional.of(new FluidIngredientWithAmount(new KegCompatibleFluidIngredients.Exact(fluid), i, Optional.empty()));
        return this;
    }

    public KegFermentingRecipeBuilder addFluidIngredient(Fluid fluid, int i, FluidUnit unit) {
        fluidIngredient = Optional.of(new FluidIngredientWithAmount(new KegCompatibleFluidIngredients.Exact(fluid), i, Optional.of(unit)));
        return this;
    }

    public KegFermentingRecipeBuilder addFluidIngredient(TagKey<Fluid> fluid, int i) {
        fluidIngredient = Optional.of(new FluidIngredientWithAmount(new KegCompatibleFluidIngredients.Tag(fluids.getOrThrow(fluid)), i, Optional.empty()));
        return this;
    }

    public KegFermentingRecipeBuilder addFluidIngredient(TagKey<Fluid> fluid, int i, FluidUnit unit) {
        fluidIngredient = Optional.of(new FluidIngredientWithAmount(new KegCompatibleFluidIngredients.Tag(fluids.getOrThrow(fluid)), i, Optional.of(unit)));
        return this;
    }

    /**
     * Does not have an equivalent for Fabric.
     */
    public KegFermentingRecipeBuilder addFluidIngredient(FluidIngredient ingredient, int i) {
        fluidIngredient = Optional.of(new FluidIngredientWithAmount(new KegCompatibleFluidIngredients.NeoForgeIngredient(ingredient), i, Optional.empty()));
        return this;
    }

    public void build(RecipeOutput consumerIn, Identifier id) {
        ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, id);
        Identifier advancementId = id.withPath(path -> "recipes/" + path);
        
        AdvancementHolder builtAdvancement = advancement.build(advancementId);
        if (!builtAdvancement.value().criteria().isEmpty()) {
            advancement.parent(net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
                    .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                    .requirements(AdvancementRequirements.Strategy.OR);
            builtAdvancement = advancement.build(advancementId);
        } else
            builtAdvancement = null;
        consumerIn.accept(recipeKey, new KegFermentingRecipe(ingredients, tab, fluidIngredient, unit, result, experience, temperature), builtAdvancement);
    }

}