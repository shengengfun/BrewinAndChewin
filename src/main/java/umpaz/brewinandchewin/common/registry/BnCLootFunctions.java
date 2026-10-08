package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import umpaz.brewinandchewin.common.loot.function.BnCCopyMealFunction;
import umpaz.brewinandchewin.common.loot.function.CopyDrinkFunction;
import umpaz.brewinandchewin.common.loot.function.RandomiseOldWineFunction;

public class BnCLootFunctions {
    // 26.1: LOOT_FUNCTION_TYPE is a registry of MapCodecs, so the codec itself is what gets
    // registered - there is no LootItemFunctionType wrapper any more.
    public static void registerAll() {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, CopyDrinkFunction.ID, CopyDrinkFunction.CODEC);
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, BnCCopyMealFunction.ID, BnCCopyMealFunction.CODEC);
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RandomiseOldWineFunction.ID, RandomiseOldWineFunction.CODEC);
    }
}
