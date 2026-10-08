package umpaz.brewinandchewin.client.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;

import java.util.Comparator;
import java.util.Optional;

public class BnCClientRecipeUtils {
    public static ItemStack getPouredItemFromFluid(AbstractedFluidStack fluid) {
        if (fluid.isEmpty() || Minecraft.getInstance().level == null)
            return ItemStack.EMPTY;
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        RecipeManager recipeManager = (RecipeManager) Minecraft.getInstance().level.recipeAccess();
        Optional<KegPouringRecipe> recipe = umpaz.brewinandchewin.common.utility.BnCRecipeLookup.all(recipeManager, BnCRecipeTypes.KEG_POURING).stream().map(RecipeHolder::value).sorted(Comparator.comparing(KegPouringRecipe::isStrict)).filter(kegPouringRecipe -> kegPouringRecipe.getRawFluid().matches(fluid)).findFirst();
        return recipe.map(kegPouringRecipe -> kegPouringRecipe.getOutput().create()).orElse(ItemStack.EMPTY);
    }
}
