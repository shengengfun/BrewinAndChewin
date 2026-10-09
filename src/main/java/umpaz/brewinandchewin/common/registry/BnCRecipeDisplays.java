package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.crafting.KegRecipeDisplay;

/** The keg's recipe-book display types. */
public class BnCRecipeDisplays {

    public static void registerAll() {
        Registry.register(BuiltInRegistries.RECIPE_DISPLAY, BrewinAndChewin.asResource("keg_fermenting"), KegRecipeDisplay.TYPE);
    }

    private BnCRecipeDisplays() {
    }
}
