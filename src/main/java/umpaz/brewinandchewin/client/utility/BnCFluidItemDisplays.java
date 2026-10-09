package umpaz.brewinandchewin.client.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.IdentifierException;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps a fluid to the item that stands for it, from the {@code fluid_item_displays} resource folder.
 *
 * <p>1.21.1 registered this as an {@code IdentifiableListener} so the reload dispatcher could sort
 * it; 26.1 hands the listener its own {@link Identifier} instead, so that interface is gone.
 *
 * <p>The JSON is kept raw and only decoded on first use: in 26.1 item default components come from
 * a datapack registry pass, so an {@link ItemStack} cannot be parsed while resource packs are still
 * being loaded.
 */
public class BnCFluidItemDisplays {
    private static final Map<Either<TagKey<Fluid>, Fluid>, JsonElement> RAW_DISPLAYS = new HashMap<>();
    private static final Map<Either<TagKey<Fluid>, Fluid>, FluidBasedItemStack> FLUID_TYPE_TO_ITEM_MAP = new HashMap<>();

    public static ItemStack getFluidItemDisplay(HolderLookup.Provider lookup, AbstractedFluidStack fluid) {
        FluidBasedItemStack exact = resolve(Either.right(fluid.fluid()));
        if (exact != null)
            return exact.getStack(lookup, fluid);

        for (Either<TagKey<Fluid>, Fluid> key : RAW_DISPLAYS.keySet()) {
            if (key.left().isEmpty() || !fluid.fluid().is(key.left().get()))
                continue;
            FluidBasedItemStack byTag = resolve(key);
            if (byTag != null)
                return byTag.getStack(lookup, fluid);
        }

        if (fluid.fluid().getBucket() != Items.AIR)
            return fluid.fluid().getBucket().getDefaultInstance();
        return ItemStack.EMPTY;
    }

    private static @Nullable FluidBasedItemStack resolve(Either<TagKey<Fluid>, Fluid> key) {
        if (FLUID_TYPE_TO_ITEM_MAP.containsKey(key))
            return FLUID_TYPE_TO_ITEM_MAP.get(key);
        JsonElement json = RAW_DISPLAYS.get(key);
        if (json == null)
            return null;
        try {
            FluidBasedItemStack parsed = FluidBasedItemStack.createFromJson(json, key);
            FLUID_TYPE_TO_ITEM_MAP.put(key, parsed);
            return parsed;
        } catch (IllegalArgumentException | IllegalStateException | JsonParseException |
                 IdentifierException ex) {
            BrewinAndChewin.LOG.error("Couldn't parse fluid item display for '{}'", key, ex);
            return null;
        }
    }

    public static class Loader extends SimplePreparableReloadListener<Map<Either<TagKey<Fluid>, Fluid>, JsonElement>> {
        public static final Loader INSTANCE = new Loader();
        private static final Gson GSON = new GsonBuilder().create();

        protected Loader() {
        }

        @Override
        protected Map<Either<TagKey<Fluid>, Fluid>, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
            FileToIdConverter fileToIdConverter = FileToIdConverter.json("brewinandchewin/fluid_item_displays");
            Map<Either<TagKey<Fluid>, Fluid>, JsonElement> map = new HashMap<>();
            for (Map.Entry<Identifier, List<Resource>> entry : fileToIdConverter.listMatchingResourceStacks(resourceManager).entrySet()) {
                for (Resource resource : entry.getValue()) {
                    try (Reader reader = resource.openAsReader()) {
                        JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                        for (var e : jsonObject.entrySet()) {
                            boolean isCurrentOptional = e.getValue().isJsonObject() && e.getValue().getAsJsonObject().has("optional") && e.getValue().getAsJsonObject().get("optional").getAsBoolean();
                            Either<TagKey<Fluid>, Fluid> either;
                            if (e.getKey().startsWith("#")) {
                                either = Either.left(TagKey.create(Registries.FLUID, Identifier.parse(e.getKey().substring(1))));
                            } else {
                                Identifier fluidLocation = Identifier.parse(e.getKey());
                                if (!BuiltInRegistries.FLUID.containsKey(fluidLocation)) {
                                    if (isCurrentOptional)
                                        continue;
                                    BrewinAndChewin.LOG.error("Could not find fluid '{}' from fluid item display JSON at location '{}' from pack '{}'.", e.getKey(), entry.getKey(), resource.sourcePackId());
                                    continue;
                                }
                                either = Either.right(BuiltInRegistries.FLUID.getValue(fluidLocation));
                            }
                            map.put(either, e.getValue());
                        }
                    } catch (IllegalArgumentException | IllegalStateException | IOException | JsonParseException |
                             IdentifierException ex) {
                        BrewinAndChewin.LOG.error("Couldn't parse fluid item display JSON at location '{}' from pack '{}'. ", entry.getKey(), resource.sourcePackId(), ex);
                    }
                }
            }
            return map;
        }

        @Override
        protected void apply(Map<Either<TagKey<Fluid>, Fluid>, JsonElement> obj, ResourceManager resourceManager, ProfilerFiller profiler) {
            RAW_DISPLAYS.clear();
            RAW_DISPLAYS.putAll(obj);
            FLUID_TYPE_TO_ITEM_MAP.clear();
            FluidBasedItemStack.CACHE.clear();
        }
    }

    public record FluidBasedItemStack(Either<TagKey<Fluid>, Fluid> fluid, FluidItemComponentRemapper dataComponentRemapper) {
        private static final HashMap<Pair<Fluid, DataComponentMap>, ItemStack> CACHE = new HashMap<>(32);

        private static FluidBasedItemStack createFromJson(JsonElement json, Either<TagKey<Fluid>, Fluid> fluid) {
            return new FluidBasedItemStack(fluid, FluidItemComponentRemapper.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst());
        }

        private ItemStack getStack(HolderLookup.Provider lookup, AbstractedFluidStack stack) {
            var pair = Pair.of(stack.fluid(), stack.components());
            if (CACHE.containsKey(pair))
                return CACHE.get(pair);

            ItemStack item = dataComponentRemapper.convert(lookup, stack);
            CACHE.put(pair, item);
            return item;
        }
    }
}
