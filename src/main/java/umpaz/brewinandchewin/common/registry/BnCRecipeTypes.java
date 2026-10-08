package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeType;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;

public class BnCRecipeTypes {
    // 26.1 provides RecipeType.simple(Identifier) for mod-defined types.
    public static final RecipeType<KegFermentingRecipe> FERMENTING = RecipeType.simple(BrewinAndChewin.asResource("fermenting"));
    public static final RecipeType<KegPouringRecipe> KEG_POURING = RecipeType.simple(BrewinAndChewin.asResource("keg_pouring"));

    public static void registerAll() {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, BrewinAndChewin.asResource("fermenting"), FERMENTING);
        Registry.register(BuiltInRegistries.RECIPE_TYPE, BrewinAndChewin.asResource("keg_pouring"), KEG_POURING);
    }
}
