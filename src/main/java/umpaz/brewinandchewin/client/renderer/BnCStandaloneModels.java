package umpaz.brewinandchewin.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The block models the coaster and bottle rack draw by hand.
 *
 * <p>Both blocks render geometry that no blockstate can express (the coaster rotates in 16 steps
 * and hides its base depending on its contents), so 26.1's blockstate models cannot describe them.
 * NeoForge's standalone models are the supported way to load a loose model file and get the baked
 * {@link BlockStateModelPart} back, which {@code SubmitNodeCollector#submitBlockModel} can then
 * draw at any pose.
 *
 * <p>The list is static because model registration has to happen before any resource is read; a
 * pack adding a new coaster model would need a matching entry here.
 */
public final class BnCStandaloneModels {
    /** 99 models. */
    public static final List<Identifier> COASTER = List.of(
            Identifier.parse("brewinandchewin:block/coaster"),
            Identifier.parse("brewinandchewin:block/coaster_tray"),
            Identifier.parse("brewinandchewin:block/coaster_apple"),
            Identifier.parse("brewinandchewin:block/coaster_apple_cider"),
            Identifier.parse("brewinandchewin:block/coaster_apple_jelly"),
            Identifier.parse("brewinandchewin:block/coaster_baked_cod_stew"),
            Identifier.parse("brewinandchewin:block/coaster_beef"),
            Identifier.parse("brewinandchewin:block/coaster_beef_stew"),
            Identifier.parse("brewinandchewin:block/coaster_beer"),
            Identifier.parse("brewinandchewin:block/coaster_beetroot"),
            Identifier.parse("brewinandchewin:block/coaster_beetroot_soup"),
            Identifier.parse("brewinandchewin:block/coaster_bloody_mary"),
            Identifier.parse("brewinandchewin:block/coaster_bone_broth"),
            Identifier.parse("brewinandchewin:block/coaster_bowl"),
            Identifier.parse("brewinandchewin:block/coaster_bread"),
            Identifier.parse("brewinandchewin:block/coaster_builders_tea"),
            Identifier.parse("brewinandchewin:block/coaster_cabbage"),
            Identifier.parse("brewinandchewin:block/coaster_carrot"),
            Identifier.parse("brewinandchewin:block/coaster_chicken"),
            Identifier.parse("brewinandchewin:block/coaster_chicken_soup"),
            Identifier.parse("brewinandchewin:block/coaster_chorus_fruit"),
            Identifier.parse("brewinandchewin:block/coaster_cod"),
            Identifier.parse("brewinandchewin:block/coaster_cod_roll"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_beef"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_chicken"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_cod"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_mutton"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_porkchop"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_rabbit"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_rice"),
            Identifier.parse("brewinandchewin:block/coaster_cooked_salmon"),
            Identifier.parse("brewinandchewin:block/coaster_cookie"),
            Identifier.parse("brewinandchewin:block/coaster_creamy_onion_soup"),
            Identifier.parse("brewinandchewin:block/coaster_dog_food"),
            Identifier.parse("brewinandchewin:block/coaster_dread_nog"),
            Identifier.parse("brewinandchewin:block/coaster_egg"),
            Identifier.parse("brewinandchewin:block/coaster_egg_grog"),
            Identifier.parse("brewinandchewin:block/coaster_fiery_fondue"),
            Identifier.parse("brewinandchewin:block/coaster_fish_stew"),
            Identifier.parse("brewinandchewin:block/coaster_flaxen_cheese_wedge"),
            Identifier.parse("brewinandchewin:block/coaster_fried_egg"),
            Identifier.parse("brewinandchewin:block/coaster_fried_rice"),
            Identifier.parse("brewinandchewin:block/coaster_fruit_salad"),
            Identifier.parse("brewinandchewin:block/coaster_glistering_melon_slice"),
            Identifier.parse("brewinandchewin:block/coaster_glittering_grenadine"),
            Identifier.parse("brewinandchewin:block/coaster_glow_berries"),
            Identifier.parse("brewinandchewin:block/coaster_glow_berry_custard"),
            Identifier.parse("brewinandchewin:block/coaster_glow_berry_marmalade"),
            Identifier.parse("brewinandchewin:block/coaster_golden_apple"),
            Identifier.parse("brewinandchewin:block/coaster_golden_carrot"),
            Identifier.parse("brewinandchewin:block/coaster_honey_bottle"),
            Identifier.parse("brewinandchewin:block/coaster_honey_cookie"),
            Identifier.parse("brewinandchewin:block/coaster_hot_cocoa"),
            Identifier.parse("brewinandchewin:block/coaster_kelp_roll"),
            Identifier.parse("brewinandchewin:block/coaster_kelp_roll_slice"),
            Identifier.parse("brewinandchewin:block/coaster_kombucha"),
            Identifier.parse("brewinandchewin:block/coaster_mead"),
            Identifier.parse("brewinandchewin:block/coaster_melon_juice"),
            Identifier.parse("brewinandchewin:block/coaster_melon_popsicle"),
            Identifier.parse("brewinandchewin:block/coaster_melon_slice"),
            Identifier.parse("brewinandchewin:block/coaster_milk_bottle"),
            Identifier.parse("brewinandchewin:block/coaster_mixed_salad"),
            Identifier.parse("brewinandchewin:block/coaster_mushroom_stew"),
            Identifier.parse("brewinandchewin:block/coaster_mutton"),
            Identifier.parse("brewinandchewin:block/coaster_nether_salad"),
            Identifier.parse("brewinandchewin:block/coaster_noodle_soup"),
            Identifier.parse("brewinandchewin:block/coaster_onion"),
            Identifier.parse("brewinandchewin:block/coaster_pale_jane"),
            Identifier.parse("brewinandchewin:block/coaster_poisonous_potato"),
            Identifier.parse("brewinandchewin:block/coaster_porkchop"),
            Identifier.parse("brewinandchewin:block/coaster_potato"),
            Identifier.parse("brewinandchewin:block/coaster_potion_bottle"),
            Identifier.parse("brewinandchewin:block/coaster_potion_contents"),
            Identifier.parse("brewinandchewin:block/coaster_pufferfish"),
            Identifier.parse("brewinandchewin:block/coaster_pumpkin_pie"),
            Identifier.parse("brewinandchewin:block/coaster_pumpkin_slice"),
            Identifier.parse("brewinandchewin:block/coaster_pumpkin_soup"),
            Identifier.parse("brewinandchewin:block/coaster_rabbit"),
            Identifier.parse("brewinandchewin:block/coaster_rabbit_stew"),
            Identifier.parse("brewinandchewin:block/coaster_red_rum"),
            Identifier.parse("brewinandchewin:block/coaster_rotten_tomato"),
            Identifier.parse("brewinandchewin:block/coaster_saccharine_rum"),
            Identifier.parse("brewinandchewin:block/coaster_salmon"),
            Identifier.parse("brewinandchewin:block/coaster_salmon_roll"),
            Identifier.parse("brewinandchewin:block/coaster_salty_folly"),
            Identifier.parse("brewinandchewin:block/coaster_scarlet_cheese_wedge"),
            Identifier.parse("brewinandchewin:block/coaster_steel_toe_stout"),
            Identifier.parse("brewinandchewin:block/coaster_strongroot_ale"),
            Identifier.parse("brewinandchewin:block/coaster_stuffed_pumpkin"),
            Identifier.parse("brewinandchewin:block/coaster_suspicious_stew"),
            Identifier.parse("brewinandchewin:block/coaster_sweet_berries"),
            Identifier.parse("brewinandchewin:block/coaster_sweet_berry_cookie"),
            Identifier.parse("brewinandchewin:block/coaster_sweet_berry_jam"),
            Identifier.parse("brewinandchewin:block/coaster_tankard"),
            Identifier.parse("brewinandchewin:block/coaster_tomato"),
            Identifier.parse("brewinandchewin:block/coaster_tomato_sauce"),
            Identifier.parse("brewinandchewin:block/coaster_tropical_fish"),
            Identifier.parse("brewinandchewin:block/coaster_vegetable_soup"),
            Identifier.parse("brewinandchewin:block/coaster_withering_dross")
    );

    /** 23 models. */
    public static final List<Identifier> BOTTLE_RACK = List.of(
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_currant"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_currant_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_0"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_1"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_2"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_3"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_4"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_5"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_6"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_7"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_label_8"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_old"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_old_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_red"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_red_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_rice"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_rice_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_twisted"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_twisted_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_verruca"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_verruca_fine"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_white"),
            Identifier.parse("brewinandchewin:block/bottle_rack_bottle_white_fine")
    );

    /** Every registered model, keyed by the file it was baked from. */
    public static final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> KEYS = createKeys();

    private static Map<Identifier, StandaloneModelKey<BlockStateModelPart>> createKeys() {
        Map<Identifier, StandaloneModelKey<BlockStateModelPart>> keys = new LinkedHashMap<>();
        for (Identifier id : all()) {
            keys.put(id, new StandaloneModelKey<>(() -> id.toString()));
        }
        return Map.copyOf(keys);
    }

    private static List<Identifier> all() {
        List<Identifier> all = new java.util.ArrayList<>(COASTER);
        all.addAll(BOTTLE_RACK);
        return List.copyOf(all);
    }

    /**
     * The baked geometry for {@code model}, or null when the model was not registered.
     *
     * <p>Models are baked before the first frame, so this is only null for a model id that has no
     * entry above - which happens when a pack points a coaster entry at a model the port never
     * registered.
     */
    public static @Nullable BlockStateModelPart part(Identifier model) {
        StandaloneModelKey<BlockStateModelPart> key = KEYS.get(model);
        if (key == null) {
            return null;
        }
        return Minecraft.getInstance().getModelManager().getStandaloneModel(key);
    }

    private BnCStandaloneModels() {
    }
}
