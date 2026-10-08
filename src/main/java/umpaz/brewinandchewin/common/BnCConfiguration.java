package umpaz.brewinandchewin.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import umpaz.brewinandchewin.common.utility.FluidUnit;

/**
 * Config for the 26.1 port.
 *
 * <p>GreenhouseConfig has no 26.1 build, so this is a straight port onto NeoForge's own
 * {@link ModConfigSpec}. The file keys are unchanged so existing config files keep working, and
 * {@code common()} / {@code client()} still hand back the same records, which is why no call site
 * had to change.
 *
 * <p>Known behaviour change: GreenhouseConfig network-synchronised the common config to joining
 * clients. A NeoForge {@code COMMON} config is per-side, so a client on a server uses its own
 * values for the Tipsy scramble thresholds instead of the server's.
 */
public class BnCConfiguration {

    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    private static final ModConfigSpec.IntValue LEVEL_CHAT_SCRAMBLE;
    private static final ModConfigSpec.IntValue LEVEL_SIGN_SCRAMBLE;
    private static final ModConfigSpec.IntValue LEVEL_NAME_SCRAMBLE;
    private static final ModConfigSpec.IntValue KEG_COLD;
    private static final ModConfigSpec.IntValue KEG_CHILLY;
    private static final ModConfigSpec.IntValue KEG_WARM;
    private static final ModConfigSpec.IntValue KEG_HOT;
    private static final ModConfigSpec.BooleanValue KEG_BIOME_TEMP;
    private static final ModConfigSpec.BooleanValue KEG_DIM_TEMP;
    private static final ModConfigSpec.BooleanValue ENABLE_RECIPE_BOOK;

    private static final ModConfigSpec.ConfigValue<String> FLUID_DISPLAY_UNIT;
    private static final ModConfigSpec.ConfigValue<String> OPPOSITE_FLUID_DISPLAY;
    private static final ModConfigSpec.BooleanValue NUMBED_HEART_FLICKERING;
    private static final ModConfigSpec.BooleanValue INTOXICATION_FOOD_OVERLAY;
    private static final ModConfigSpec.BooleanValue SCRAMBLE_CHAT;
    private static final ModConfigSpec.BooleanValue SCRAMBLE_NAME;
    private static final ModConfigSpec.BooleanValue SCRAMBLE_SIGN;
    private static final ModConfigSpec.BooleanValue RENDER_FLUID_IN_KEG;

    static {
        ModConfigSpec.Builder common = new ModConfigSpec.Builder();

        common.comment("At what Tipsy amplifier the client starts scrambling text.").push("root");
        LEVEL_CHAT_SCRAMBLE = common
                .comment("At what amplifier of Tipsy should the chat scramble?")
                .defineInRange("levelChatScramble", Common.Root.DEFAULT.levelChatScramble(), 1, 10);
        LEVEL_SIGN_SCRAMBLE = common
                .comment("At what amplifier of Tipsy should signs scramble?")
                .defineInRange("levelSignScramble", Common.Root.DEFAULT.levelSignScramble(), 1, 10);
        LEVEL_NAME_SCRAMBLE = common
                .comment("At what amplifier of Tipsy should nametags scramble?")
                .defineInRange("levelNameScramble", Common.Root.DEFAULT.levelNameScramble(), 1, 10);
        common.pop();

        common.comment("How the Keg reads its ambient temperature.").push("keg");
        KEG_COLD = common
                .comment("How many cold blocks are required for a cold temperature in the Keg?")
                .defineInRange("kegCold", Common.Keg.DEFAULT.cold(), 1, Integer.MAX_VALUE);
        KEG_CHILLY = common
                .comment("How many cold blocks are required for a chilly temperature in the Keg?")
                .defineInRange("kegChilly", Common.Keg.DEFAULT.chilly(), 1, Integer.MAX_VALUE);
        KEG_WARM = common
                .comment("How many hot blocks are required for a warm temperature in the Keg?")
                .defineInRange("kegWarm", Common.Keg.DEFAULT.warm(), 1, Integer.MAX_VALUE);
        KEG_HOT = common
                .comment("How many hot blocks are required for a hot temperature in the Keg?")
                .defineInRange("kegHot", Common.Keg.DEFAULT.hot(), 1, Integer.MAX_VALUE);
        KEG_BIOME_TEMP = common
                .comment("Should the biome temperature influence the temperature in the Keg?")
                .define("kegBiomeTemp", Common.Keg.DEFAULT.biomeTemp());
        KEG_DIM_TEMP = common
                .comment("Should the dimension temperature influence the temperature in the Keg?")
                .define("kegDimTemp", Common.Keg.DEFAULT.dimTemp());
        common.pop();

        common.push("recipe_book");
        ENABLE_RECIPE_BOOK = common
                .comment("Should the Keg have a Recipe Book available on its interface?")
                .define("enableRecipeBookKeg", Common.RecipeBook.DEFAULT.enabled());
        common.pop();

        COMMON_SPEC = common.build();

        ModConfigSpec.Builder client = new ModConfigSpec.Builder();

        FLUID_DISPLAY_UNIT = client
                .comment("Which unit the fluid display in the keg should use.",
                        "Should be 'liters', 'millibuckets' or 'droplets'",
                        "1 L = 1 mB = 81 d")
                .define("fluidDisplayUnit", Client.DEFAULT.displayUnit().getSerializedName());
        OPPOSITE_FLUID_DISPLAY = client
                .comment("When the opposite fluid display unit should be shown.",
                        "Should be one of 'never', 'advanced_tooltips' or 'always'")
                .define("oppositeFluidDisplay", Client.DEFAULT.oppositeFluidDisplay().getSerializedName());
        NUMBED_HEART_FLICKERING = client
                .comment("Should the numbed hearts obtained from being damaged when Tipsy flicker when you are about to take damage?")
                .define("numbedHeartFlickering", Client.DEFAULT.numbedHeartFlickering());
        INTOXICATION_FOOD_OVERLAY = client
                .comment("Should the food bar have a yellow overlay when the player has the Intoxication effect?")
                .define("intoxicationFoodOverlay", Client.DEFAULT.intoxicationFoodOverlay());
        SCRAMBLE_CHAT = client
                .comment("Should the chat scramble when the player has the Tipsy effect?")
                .define("scrambleChat", Client.DEFAULT.scrambleChat());
        SCRAMBLE_NAME = client
                .comment("Should other player's nametags scramble when the player has the Tipsy effect?")
                .define("scrambleName", Client.DEFAULT.scrambleName());
        SCRAMBLE_SIGN = client
                .comment("Should signs scramble when the player has the Tipsy effect?")
                .define("scrambleSign", Client.DEFAULT.scrambleSign());
        RENDER_FLUID_IN_KEG = client
                .comment("Should kegs render the fluid texture in the background of the fluid slot?")
                .define("renderFluidInKeg", Client.DEFAULT.renderFluidInKeg());

        CLIENT_SPEC = client.build();
    }

    public static Common common() {
        return new Common(
                new Common.Root(readInt(LEVEL_CHAT_SCRAMBLE, Common.Root.DEFAULT.levelChatScramble()),
                        readInt(LEVEL_SIGN_SCRAMBLE, Common.Root.DEFAULT.levelSignScramble()),
                        readInt(LEVEL_NAME_SCRAMBLE, Common.Root.DEFAULT.levelNameScramble())),
                new Common.Keg(readInt(KEG_COLD, Common.Keg.DEFAULT.cold()),
                        readInt(KEG_CHILLY, Common.Keg.DEFAULT.chilly()),
                        readInt(KEG_WARM, Common.Keg.DEFAULT.warm()),
                        readInt(KEG_HOT, Common.Keg.DEFAULT.hot()),
                        readBoolean(KEG_BIOME_TEMP, Common.Keg.DEFAULT.biomeTemp()),
                        readBoolean(KEG_DIM_TEMP, Common.Keg.DEFAULT.dimTemp())),
                new Common.RecipeBook(readBoolean(ENABLE_RECIPE_BOOK, Common.RecipeBook.DEFAULT.enabled()))
        );
    }

    public static Client client() {
        return new Client(
                readFluidUnit(),
                readDisplaySettings(),
                readBoolean(NUMBED_HEART_FLICKERING, Client.DEFAULT.numbedHeartFlickering()),
                readBoolean(INTOXICATION_FOOD_OVERLAY, Client.DEFAULT.intoxicationFoodOverlay()),
                readBoolean(SCRAMBLE_CHAT, Client.DEFAULT.scrambleChat()),
                readBoolean(SCRAMBLE_NAME, Client.DEFAULT.scrambleName()),
                readBoolean(SCRAMBLE_SIGN, Client.DEFAULT.scrambleSign()),
                readBoolean(RENDER_FLUID_IN_KEG, Client.DEFAULT.renderFluidInKeg())
        );
    }

    public static void init() {}

    // Values are read through MODIFIED accessors with a fallback, because a caller can reach
    // these before the config file has been loaded (the mod constructor does).
    private static int readInt(ModConfigSpec.IntValue value, int fallback) {
        try {
            return value.getAsInt();
        } catch (IllegalStateException e) {
            return fallback;
        }
    }

    private static boolean readBoolean(ModConfigSpec.BooleanValue value, boolean fallback) {
        try {
            return value.getAsBoolean();
        } catch (IllegalStateException e) {
            return fallback;
        }
    }

    private static String readString(ModConfigSpec.ConfigValue<String> value, String fallback) {
        try {
            return value.get();
        } catch (IllegalStateException e) {
            return fallback;
        }
    }

    private static FluidUnit readFluidUnit() {
        String name = readString(FLUID_DISPLAY_UNIT, Client.DEFAULT.displayUnit().getSerializedName());
        for (FluidUnit unit : FluidUnit.values()) {
            if (unit.getSerializedName().equals(name)) {
                return unit;
            }
        }
        return Client.DEFAULT.displayUnit();
    }

    private static Client.DisplaySettings readDisplaySettings() {
        String name = readString(OPPOSITE_FLUID_DISPLAY, Client.DEFAULT.oppositeFluidDisplay().getSerializedName());
        for (Client.DisplaySettings setting : Client.DisplaySettings.values()) {
            if (setting.getSerializedName().equals(name)) {
                return setting;
            }
        }
        return Client.DEFAULT.oppositeFluidDisplay();
    }

    public record Common(Root root, Keg keg, RecipeBook recipeBook) {
        public static final Common DEFAULT = new Common(Root.DEFAULT, Keg.DEFAULT, RecipeBook.DEFAULT);

        public record Root(int levelChatScramble, int levelSignScramble, int levelNameScramble) {
            public static final Root DEFAULT = new Root(3, 3, 3);
        }

        public record Keg(int cold, int chilly, int warm, int hot, boolean biomeTemp, boolean dimTemp) {
            public static final Keg DEFAULT = new Keg(2, 1, 1, 2, true, true);
        }

        public record RecipeBook(boolean enabled) {
            public static final RecipeBook DEFAULT = new RecipeBook(true);
        }
    }

    public record Client(FluidUnit displayUnit, DisplaySettings oppositeFluidDisplay,
                         boolean numbedHeartFlickering, boolean intoxicationFoodOverlay,
                         boolean scrambleChat, boolean scrambleName, boolean scrambleSign,
                         boolean renderFluidInKeg) {
        public static final Client DEFAULT = new Client(
                FluidUnit.MILLIBUCKET,
                DisplaySettings.ADVANCED_TOOLTIPS,
                true, true,
                true, true, true,
                true
        );

        public enum DisplaySettings implements net.minecraft.util.StringRepresentable {
            NEVER("never"),
            ADVANCED_TOOLTIPS("advanced_tooltips"),
            ALWAYS("always");

            final String name;

            DisplaySettings(String name) {
                this.name = name;
            }

            @Override
            public @NotNull String getSerializedName() {
                return name;
            }
        }
    }
}
