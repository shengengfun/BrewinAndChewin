package umpaz.brewinandchewin.common.compat.nomansland;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import umpaz.brewinandchewin.common.registry.BnCFoods;
import umpaz.brewinandchewin.common.registry.BnCItems;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class BnCNMLItems {
    public static final Item RICE_PUDDING = new ConsumableItem(BnCFoods.RICE_PUDDING.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL)), true);
    public static final Item MAPLE_FUDGE = new ConsumableItem(BnCFoods.MAPLE_FUDGE.apply(new Item.Properties()));

    public static void registerAll() {
        BnCItems.registerWithTab("rice_pudding", RICE_PUDDING);
        BnCItems.registerWithTab("maple_fudge", MAPLE_FUDGE);
    }
}
