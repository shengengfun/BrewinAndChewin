package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import umpaz.brewinandchewin.data.recipe.BnCCookingPotRecipes;
import umpaz.brewinandchewin.data.recipe.BnCCookingRecipes;
import umpaz.brewinandchewin.data.recipe.BnCCraftingRecipes;
import umpaz.brewinandchewin.data.recipe.BnCCuttingBoardRecipes;
import umpaz.brewinandchewin.data.recipe.KegFermentingRecipes;
import umpaz.brewinandchewin.data.recipe.KegPouringRecipes;
import umpaz.brewinandchewin.data.recipe.NMLRecipes;

import java.util.concurrent.CompletableFuture;

/**
 * 26.1 turned {@link RecipeProvider} into a provider that receives an already-resolved
 * {@link HolderLookup.Provider} and is launched through the nested {@link RecipeProvider.Runner}.
 */
public class BnCRecipes extends RecipeProvider.Runner {

    public BnCRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            protected void buildRecipes() {
                KegFermentingRecipes.register(output, this.items, this.registries.lookupOrThrow(net.minecraft.core.registries.Registries.FLUID));
                KegPouringRecipes.register(output);
                BnCCookingPotRecipes.register(output, this.items);
                BnCCookingRecipes.register(output, this.items);
                BnCCraftingRecipes.register(output, this.items);
                BnCCuttingBoardRecipes.register(output, this.items);
                NMLRecipes.register(output, this.items, this.registries.lookupOrThrow(net.minecraft.core.registries.Registries.FLUID));
            }
        };
    }

    @Override
    public String getName() {
        return "Brewin' And Chewin' Recipes";
    }
}
