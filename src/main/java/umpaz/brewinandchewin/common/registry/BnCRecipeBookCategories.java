package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import umpaz.brewinandchewin.BrewinAndChewin;

/**
 * The keg's recipe-book categories.
 *
 * <p>1.21.1 got these by enum-extending {@code net.minecraft.client.RecipeBookCategories}, which
 * 26.1 turned into a plain class, so that trick is gone. {@code RecipeBookCategory} is now a real
 * registry entry ({@code BuiltInRegistries.RECIPE_BOOK_CATEGORY}) and that is what
 * {@code Recipe#recipeBookCategory()} returns - so the categories are registered like any other
 * mod content.
 *
 * <p>The fields are assigned in {@link #registerAll()} rather than at class-init because they are
 * first read while recipes are built (datapack load), which always happens after the register
 * events.
 */
public class BnCRecipeBookCategories {
    public static RecipeBookCategory FERMENTING_SEARCH;
    public static RecipeBookCategory FERMENTING_DRINKS;
    public static RecipeBookCategory FERMENTING_MEALS;
    public static RecipeBookCategory FERMENTING_MISC;

    public static void registerAll() {
        FERMENTING_SEARCH = register("fermenting_search");
        FERMENTING_DRINKS = register("fermenting_drinks");
        FERMENTING_MEALS = register("fermenting_meals");
        FERMENTING_MISC = register("fermenting_misc");
    }

    private static RecipeBookCategory register(String path) {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, BrewinAndChewin.asResource(path), new RecipeBookCategory());
    }
}
