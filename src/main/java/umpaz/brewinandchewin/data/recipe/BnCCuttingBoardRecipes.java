package umpaz.brewinandchewin.data.recipe;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.data.builder.BnCCuttingRecipeBuilder;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class BnCCuttingBoardRecipes {
    public static void register(RecipeOutput consumer, HolderGetter<Item> items) {
        // Knife
        cuttingRecipes(consumer, items);

    }

    private static void cuttingRecipes(RecipeOutput consumer, HolderGetter<Item> items) {
        Ingredient knife = Ingredient.of(items.getOrThrow(CommonTags.Items.TOOLS_KNIFE));
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.FLAXEN_CHEESE_WHEEL), knife, BnCItems.FLAXEN_CHEESE_WEDGE, 8)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.SCARLET_CHEESE_WHEEL), knife, BnCItems.SCARLET_CHEESE_WEDGE, 8)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.QUICHE), knife, BnCItems.QUICHE_SLICE, 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.PIZZA), knife, BnCItems.PIZZA_SLICE, 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.RICH_CHOCOLATE_CAKE), knife, BnCItems.SLICE_OF_RICH_CHOCOLATE_CAKE, 6)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.GLOW_BERRY_MERINGUE_PIE), knife, BnCItems.SLICE_OF_GLOW_BERRY_MERINGUE_PIE, 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.PUMPKIN_ROLL), knife, BnCItems.SLICE_OF_PUMPKIN_ROLL, 6)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.CORN), knife, BnCItems.CORN_KERNELS, 1)
                .addResult(ModItems.STRAW.get())
                .addResultWithChance(BnCItems.CORN_KERNELS, 0.5F)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.CORN_KERNELS), knife, BnCItems.CORNMEAL, 1)
                .build(consumer);
    }
}
