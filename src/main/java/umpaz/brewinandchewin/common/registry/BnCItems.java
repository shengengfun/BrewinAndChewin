package umpaz.brewinandchewin.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.item.*;
import umpaz.brewinandchewin.common.block.GrapeColour;
import umpaz.brewinandchewin.common.compat.nomansland.BnCNMLItems;
import umpaz.brewinandchewin.common.compat.nomansland.NMLIntegration;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.LinkedHashSet;
public class BnCItems {
    public static LinkedHashSet<Item> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    public static void registerWithTab(String name, Item item) {
        registerWithTab(name, item, null);
    }

    public static void registerWithTab(String name, Item item, @Nullable String requiredMod) {
        Registry.register(BuiltInRegistries.ITEM, BrewinAndChewin.asResource(name), item);
        if (requiredMod == null || BrewinAndChewin.getHelper().isModLoaded(requiredMod))
            CREATIVE_TAB_ITEMS.add(item);
    }
    
    public static final Item KEG = new KegItem(BnCBlocks.KEG, new Item.Properties().stacksTo(1));
    public static final Item HEATING_CASK = new BlockItem(BnCBlocks.HEATING_CASK, new Item.Properties());
    public static final Item ICE_CRATE = new BlockItem(BnCBlocks.ICE_CRATE, new Item.Properties());
    public static final Item COASTER = new BlockItem(BnCBlocks.COASTER, new Item.Properties());

    public static final Item AGING_CASK = new BlockItem(BnCBlocks.AGING_CASK, new Item.Properties());
    public static final Item BOTTLE_RACK = new BlockItem(BnCBlocks.BOTTLE_RACK, new Item.Properties());

    public static Item TANKARD = new Item(new Item.Properties());

    public static final Item RENNET = new Item(BnCFoods.RENNET.apply(new Item.Properties()));

    public static final Item RED_GRAPES = new Item(BnCFoods.GRAPES.apply(new Item.Properties()));
    public static final Item WHITE_GRAPES = new Item(BnCFoods.GRAPES.apply(new Item.Properties()));
    public static final Item RED_GRAPE_SEEDS = new GrapeSeedsItem(BnCBlocks.RED_GRAPE_BUSH, GrapeColour.RED, new Item.Properties());
    public static final Item WHITE_GRAPE_SEEDS = new GrapeSeedsItem(BnCBlocks.WHITE_GRAPE_BUSH, GrapeColour.WHITE, new Item.Properties());

    public static final Item RED_WINE = new WineItem(WineType.RED, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item WHITE_WINE = new WineItem(WineType.WHITE, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item CURRANT_WINE = new WineItem(WineType.CURRANT, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item VERRUCA_WINE = new WineItem(WineType.VERRUCA, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item TWISTED_WINE = new WineItem(WineType.TWISTED, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item RICE_WINE = new WineItem(WineType.RICE, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item VODKA = new WineItem(WineType.VODKA, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item OLD_WINE = new WineItem(WineType.OLD, new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE).rarity(Rarity.UNCOMMON));

    public static final Item BRANDY = new DistillateItem(2, 4800, false, false, new Item.Properties().stacksTo(16));
    public static final Item AQUA_VITAE = new DistillateItem(5, 6000, false, false, new Item.Properties().stacksTo(16));
    public static final Item SICKENING_TINCTURE = new DistillateItem(0, 0, true, false, new Item.Properties().stacksTo(16));
    public static final Item DELICIOUS_TINCTURE = new DistillateItem(0, 0, false, true, new Item.Properties().stacksTo(16));

    public static final Item LABEL = new LabelItem(new Item.Properties());

    public static final Item BEER = new BoozeItem(() -> BnCFluids.BEER, BnCFoods.BEER.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item MEAD = new BoozeItem(() -> BnCFluids.MEAD, BnCFoods.MEAD.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item PALE_JANE = new BoozeItem(() -> BnCFluids.PALE_JANE, BnCFoods.PALE_JANE.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item EGG_GROG = new BoozeItem(() -> BnCFluids.EGG_GROG, BnCFoods.EGG_GROG.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item GLITTERING_GRENADINE = new BoozeItem(() -> BnCFluids.GLITTERING_GRENADINE, BnCFoods.GLITTERING_GRENADINE.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item SACCHARINE_RUM = new BoozeItem(() -> BnCFluids.SACCHARINE_RUM, BnCFoods.SACCHARINE_RUM.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item SALTY_FOLLY = new BoozeItem(() -> BnCFluids.SALTY_FOLLY, BnCFoods.SALTY_FOLLY.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item BLOODY_MARY = new BoozeItem(() -> BnCFluids.BLOODY_MARY,  BnCFoods.BLOODY_MARY.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item RED_RUM = new BoozeItem(() -> BnCFluids.RED_RUM, BnCFoods.RED_RUM.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item STRONGROOT_ALE = new BoozeItem(() -> BnCFluids.STRONGROOT_ALE, BnCFoods.STRONGROOT_ALE.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item STEEL_TOE_STOUT = new BoozeItem(() -> BnCFluids.STEEL_TOE_STOUT, BnCFoods.STEEL_TOE_STOUT.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item DREAD_NOG = new DreadNogItem(() -> BnCFluids.DREAD_NOG, BnCFoods.DREAD_NOG.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));
    public static final Item WITHERING_DROSS = new BoozeItem(() -> BnCFluids.WITHERING_DROSS, BnCFoods.WITHERING_DROSS.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));

    public static final Item KOMBUCHA = new BoozeItem(() -> BnCFluids.KOMBUCHA, BnCFoods.KOMBUCHA.apply(new Item.Properties()
            .stacksTo(16).craftRemainder(BnCItems.TANKARD)));

    public static final Item UNRIPE_FLAXEN_CHEESE_WHEEL = new BlockItem(BnCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL, new Item.Properties().stacksTo(16));
    public static final Item FLAXEN_CHEESE_WHEEL = new BlockItem(BnCBlocks.FLAXEN_CHEESE_WHEEL, new Item.Properties().stacksTo(16));
    public static final Item FLAXEN_CHEESE_WEDGE = new Item(BnCFoods.FLAXEN_CHEESE.apply(new Item.Properties()));

    public static final Item UNRIPE_SCARLET_CHEESE_WHEEL = new BlockItem(BnCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL, new Item.Properties().stacksTo(16));
    public static final Item SCARLET_CHEESE_WHEEL = new BlockItem(BnCBlocks.SCARLET_CHEESE_WHEEL, new Item.Properties().stacksTo(16));
    public static final Item SCARLET_CHEESE_WEDGE = new Item(BnCFoods.SCARLET_CHEESE.apply(new Item.Properties()));

    public static final Item VEGETABLE_OMELET = new ConsumableItem(BnCFoods.VEGETABLE_OMELET.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);
    public static final Item CREAMY_ONION_SOUP = new ConsumableItem(BnCFoods.CREAMY_ONION_SOUP.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);
    public static final Item CHEESY_PASTA = new ConsumableItem(BnCFoods.CHEESY_PASTA.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);
    public static final Item HORROR_LASAGNA = new ConsumableItem(BnCFoods.HORROR_LASAGNA.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);
    public static final Item SCARLET_PIEROGI = new ConsumableItem(BnCFoods.SCARLET_PIEROGI.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);

    public static final Item FIERY_FONDUE_POT = new BlockItem(BnCBlocks.FIERY_FONDUE_POT, new Item.Properties().stacksTo(1));
    public static final Item FIERY_FONDUE = new ConsumableItem(BnCFoods.FIERY_FONDUE.apply(new Item.Properties().stacksTo(16)).craftRemainder(Items.BOWL), true);

    public static final Item PIZZA = new BlockItem(BnCBlocks.PIZZA, new Item.Properties().stacksTo(1));
    public static final Item QUICHE = new BlockItem(BnCBlocks.QUICHE, new Item.Properties());

    public static final Item PIZZA_SLICE = new Item(BnCFoods.PIZZA_SLICE.apply(new Item.Properties()));
    public static final Item QUICHE_SLICE = new Item(BnCFoods.QUICHE_SLICE.apply(new Item.Properties()));

    public static final Item HAM_AND_CHEESE_SANDWICH = new Item(BnCFoods.HAM_AND_CHEESE_SANDWICH.apply(new Item.Properties()));

    public static final Item RICH_CHOCOLATE_CAKE = new BlockItem(BnCBlocks.RICH_CHOCOLATE_CAKE, new Item.Properties().stacksTo(1));
    public static final Item SLICE_OF_RICH_CHOCOLATE_CAKE = new ConsumableItem(BnCFoods.RICH_CHOCOLATE_CAKE_SLICE.apply(new Item.Properties()));
    public static final Item GLOW_BERRY_MERINGUE_PIE = new BlockItem(BnCBlocks.GLOW_BERRY_MERINGUE_PIE, new Item.Properties().stacksTo(1));
    public static final Item SLICE_OF_GLOW_BERRY_MERINGUE_PIE = new ConsumableItem(BnCFoods.GLOW_BERRY_MERINGUE_PIE_SLICE.apply(new Item.Properties()));
    public static final Item PUMPKIN_ROLL = new BlockItem(BnCBlocks.PUMPKIN_ROLL, new Item.Properties().stacksTo(1));
    public static final Item SLICE_OF_PUMPKIN_ROLL = new ConsumableItem(BnCFoods.PUMPKIN_ROLL_SLICE.apply(new Item.Properties()));

    public static final Item ASPIC_CUBE = new ConsumableItem(BnCFoods.ASPIC_CUBE.apply(new Item.Properties()));
    public static final Item CHOPPED_LIVER = new ConsumableItem(BnCFoods.CHOPPED_LIVER.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL)), true);
    public static final Item GRITS = new ConsumableItem(BnCFoods.GRITS.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL)));

    public static final Item GLOW_BROWNIE = new ConsumableItem(BnCFoods.GLOW_BROWNIE.apply(new Item.Properties()));
    public static final Item APPLE_TURNOVER = new ConsumableItem(BnCFoods.APPLE_TURNOVER.apply(new Item.Properties()));
    public static final Item JAM_SANDWICH = new Item(BnCFoods.JAM_SANDWICH.apply(new Item.Properties()));
    public static final Item BUTTERSCOTCH_CANDY = new Item(BnCFoods.BUTTERSCOTCH_CANDY.apply(new Item.Properties()));

    public static final Item RAW_CROISSANT = new ConsumableItem(BnCFoods.RAW_CROISSANT.apply(new Item.Properties()));
    public static final Item CROISSANT = new Item(BnCFoods.CROISSANT.apply(new Item.Properties()));
    public static final Item RAW_SAUSAGE = new Item(BnCFoods.RAW_SAUSAGE.apply(new Item.Properties()));
    public static final Item COOKED_SAUSAGE = new Item(BnCFoods.COOKED_SAUSAGE.apply(new Item.Properties()));
    public static final Item HAGGIS = new ConsumableItem(BnCFoods.HAGGIS.apply(new Item.Properties()));
    public static final Item INNARDS = new ConsumableItem(BnCFoods.INNARDS.apply(new Item.Properties()));

    public static final Item CORN = new Item(BnCFoods.CORN.apply(new Item.Properties()));
    public static final Item COOKED_CORN = new Item(BnCFoods.COOKED_CORN.apply(new Item.Properties()));
    public static final Item POPPED_CORN = new Item(BnCFoods.POPPED_CORN.apply(new Item.Properties()));
    public static final Item CANDIED_CORN = new Item(BnCFoods.CANDIED_CORN.apply(new Item.Properties()));
    public static final Item CORN_KERNELS = new ItemNameBlockItem(BnCBlocks.CORN_CROP, new Item.Properties());
    public static final Item WILD_CORN = new BlockItem(BnCBlocks.WILD_CORN, new Item.Properties());
    public static final Item WILD_GRAPES = new BlockItem(BnCBlocks.WILD_GRAPES, new Item.Properties());
    public static final Item TRELLIS = new BlockItem(BnCBlocks.TRELLIS, new Item.Properties());
    public static final Item CORNMEAL = new Item(new Item.Properties());
    public static final Item CORN_DOUGH = new ConsumableItem(BnCFoods.CORN_DOUGH.apply(new Item.Properties()));
    public static final Item RAW_MUFFIN = new ConsumableItem(BnCFoods.RAW_MUFFIN.apply(new Item.Properties()));
    public static final Item CORN_BREAD = new Item(BnCFoods.CORN_BREAD.apply(new Item.Properties()));
    public static final Item CORN_MUFFIN = new Item(BnCFoods.CORN_MUFFIN.apply(new Item.Properties()));
    public static final Item TORTILLA = new Item(BnCFoods.TORTILLA.apply(new Item.Properties()));

    public static final Item KIMCHI = new ConsumableItem(BnCFoods.KIMCHI.apply(new Item.Properties()));
    public static final Item JERKY = new ConsumableItem(BnCFoods.JERKY.apply(new Item.Properties()));
    public static final Item PICKLED_PICKLES = new ConsumableItem(BnCFoods.PICKLED_PICKLES.apply(new Item.Properties()));
    public static final Item KIPPERS = new ConsumableItem(BnCFoods.KIPPERS.apply(new Item.Properties()));
    public static final Item COCOA_FUDGE = new ConsumableItem(BnCFoods.COCOA_FUDGE.apply(new Item.Properties()));

    public static final Item SWEET_BERRY_JAM = new JamJarItem(BnCFoods.SWEET_BERRY_JAM.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final Item GLOW_BERRY_MARMALADE = new JamJarItem(BnCFoods.GLOW_BERRY_MARMALADE.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final Item APPLE_JELLY = new JamJarItem(BnCFoods.APPLE_JELLY.apply(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));

    public static void registerAll() {
        registerWithTab("keg", KEG);
        registerWithTab("heating_cask", HEATING_CASK);
        registerWithTab("ice_crate", ICE_CRATE);
        registerWithTab("coaster", COASTER);
        registerWithTab("aging_cask", AGING_CASK);
        registerWithTab("bottle_rack", BOTTLE_RACK);

        registerWithTab("tankard", TANKARD);

        registerWithTab("red_grapes", RED_GRAPES);
        registerWithTab("white_grapes", WHITE_GRAPES);
        registerWithTab("red_grape_seeds", RED_GRAPE_SEEDS);
        registerWithTab("white_grape_seeds", WHITE_GRAPE_SEEDS);

        registerWithTab("red_wine", RED_WINE);
        registerWithTab("white_wine", WHITE_WINE);
        registerWithTab("currant_wine", CURRANT_WINE);
        registerWithTab("verruca_wine", VERRUCA_WINE);
        registerWithTab("twisted_wine", TWISTED_WINE);
        registerWithTab("rice_wine", RICE_WINE);
        registerWithTab("old_wine", OLD_WINE);

        registerWithTab("brandy", BRANDY);
        registerWithTab("aqua_vitae", AQUA_VITAE);
        registerWithTab("sickening_tincture", SICKENING_TINCTURE);
        registerWithTab("delicious_tincture", DELICIOUS_TINCTURE);

        registerWithTab("label", LABEL);

        registerWithTab("beer", BEER);
        registerWithTab("vodka", VODKA);
        registerWithTab("mead", MEAD);
        registerWithTab("pale_jane", PALE_JANE);
        registerWithTab("egg_grog", EGG_GROG);
        registerWithTab("glittering_grenadine", GLITTERING_GRENADINE);
        registerWithTab("saccharine_rum", SACCHARINE_RUM);
        registerWithTab("salty_folly", SALTY_FOLLY);
        registerWithTab("bloody_mary", BLOODY_MARY);
        registerWithTab("red_rum", RED_RUM);
        registerWithTab("strongroot_ale", STRONGROOT_ALE);
        registerWithTab("steel_toe_stout", STEEL_TOE_STOUT);
        registerWithTab("dread_nog", DREAD_NOG);
        registerWithTab("withering_dross", WITHERING_DROSS);

        registerWithTab("kombucha", KOMBUCHA, "farmersrespite");

        registerWithTab("unripe_flaxen_cheese_wheel", UNRIPE_FLAXEN_CHEESE_WHEEL);
        registerWithTab("flaxen_cheese_wheel", FLAXEN_CHEESE_WHEEL);
        registerWithTab("flaxen_cheese_wedge", FLAXEN_CHEESE_WEDGE);

        registerWithTab("unripe_scarlet_cheese_wheel", UNRIPE_SCARLET_CHEESE_WHEEL);
        registerWithTab("scarlet_cheese_wheel", SCARLET_CHEESE_WHEEL);
        registerWithTab("scarlet_cheese_wedge", SCARLET_CHEESE_WEDGE);

        registerWithTab("vegetable_omelet", VEGETABLE_OMELET);
        registerWithTab("creamy_onion_soup", CREAMY_ONION_SOUP);
        registerWithTab("cheesy_pasta", CHEESY_PASTA);
        registerWithTab("horror_lasagna", HORROR_LASAGNA);
        registerWithTab("scarlet_pierogi", SCARLET_PIEROGI);

        registerWithTab("fiery_fondue_pot", FIERY_FONDUE_POT);
        registerWithTab("fiery_fondue", FIERY_FONDUE);

        registerWithTab("pizza", PIZZA);
        registerWithTab("quiche", QUICHE);

        registerWithTab("pizza_slice", PIZZA_SLICE);
        registerWithTab("quiche_slice", QUICHE_SLICE);

        registerWithTab("ham_and_cheese_sandwich", HAM_AND_CHEESE_SANDWICH);

        registerWithTab("rennet", RENNET);

        registerWithTab("rich_chocolate_cake", RICH_CHOCOLATE_CAKE);
        registerWithTab("slice_of_rich_chocolate_cake", SLICE_OF_RICH_CHOCOLATE_CAKE);
        registerWithTab("glow_berry_meringue_pie", GLOW_BERRY_MERINGUE_PIE);
        registerWithTab("slice_of_glow_berry_meringue_pie", SLICE_OF_GLOW_BERRY_MERINGUE_PIE);
        registerWithTab("pumpkin_roll", PUMPKIN_ROLL);
        registerWithTab("slice_of_pumpkin_roll", SLICE_OF_PUMPKIN_ROLL);

        registerWithTab("aspic_cube", ASPIC_CUBE);
        registerWithTab("chopped_liver", CHOPPED_LIVER);
        registerWithTab("grits", GRITS);

        registerWithTab("glow_brownie", GLOW_BROWNIE);
        registerWithTab("apple_turnover", APPLE_TURNOVER);
        registerWithTab("jam_sandwich", JAM_SANDWICH);
        registerWithTab("butterscotch_candy", BUTTERSCOTCH_CANDY);

        registerWithTab("raw_croissant", RAW_CROISSANT);
        registerWithTab("croissant", CROISSANT);
        registerWithTab("raw_sausage", RAW_SAUSAGE);
        registerWithTab("cooked_sausage", COOKED_SAUSAGE);
        registerWithTab("haggis", HAGGIS);
        registerWithTab("innards", INNARDS);

        registerWithTab("corn", CORN);
        registerWithTab("cooked_corn", COOKED_CORN);
        registerWithTab("popped_corn", POPPED_CORN);
        registerWithTab("candied_corn", CANDIED_CORN);
        registerWithTab("corn_kernels", CORN_KERNELS);
        registerWithTab("wild_corn", WILD_CORN);
        registerWithTab("wild_grapes", WILD_GRAPES);
        registerWithTab("trellis", TRELLIS);
        registerWithTab("cornmeal", CORNMEAL);
        registerWithTab("corn_dough", CORN_DOUGH);
        registerWithTab("raw_muffin", RAW_MUFFIN);
        registerWithTab("corn_bread", CORN_BREAD);
        registerWithTab("corn_muffin", CORN_MUFFIN);
        registerWithTab("tortilla", TORTILLA);

        if (NMLIntegration.isLoaded())
            BnCNMLItems.registerAll();

        registerWithTab("kimchi", KIMCHI);
        registerWithTab("jerky", JERKY);
        registerWithTab("pickled_pickles", PICKLED_PICKLES);
        registerWithTab("kippers", KIPPERS);
        registerWithTab("cocoa_fudge", COCOA_FUDGE);

        registerWithTab("sweet_berry_jam", SWEET_BERRY_JAM);
        registerWithTab("glow_berry_marmalade", GLOW_BERRY_MARMALADE);
        registerWithTab("apple_jelly", APPLE_JELLY);
    }
}
