package umpaz.brewinandchewin.client.renderer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.IdentifierException;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.renderer.texture.BnCTextureModifiers;
import umpaz.brewinandchewin.client.renderer.texture.modifier.TextureModifier;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Reads {@code brewinandchewin/coaster/*.json}, which says which models (and texture modifiers) to
 * draw for an item resting on a coaster.
 *
 * <p>1.21.1 registered this through {@code IdentifiableListener}; 26.1 names the listener at
 * registration instead, so only the resource-walking half survives.
 */
public class CoasterModelLoader extends SimplePreparableReloadListener<Map<Identifier, List<CoasterModelLoader.ModelEntry>>> {
    public static final CoasterModelLoader INSTANCE = new CoasterModelLoader();
    public static final Identifier ID = BrewinAndChewin.asResource("coaster_models");

    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<Identifier, List<ModelEntry>> ITEM_TO_MODELS = new HashMap<>();

    protected CoasterModelLoader() {
    }

    /** The models to draw for {@code itemId}, or null when the item uses its own item model. */
    public static List<ModelEntry> getModelEntries(Identifier itemId) {
        return ITEM_TO_MODELS.get(itemId);
    }

    @Override
    protected Map<Identifier, List<ModelEntry>> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        FileToIdConverter converter = FileToIdConverter.json("brewinandchewin/coaster");
        Map<Identifier, List<ModelEntry>> map = new HashMap<>();
        for (Map.Entry<Identifier, List<Resource>> entry : converter.listMatchingResourceStacks(resourceManager).entrySet()) {
            for (Resource resource : entry.getValue()) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                    Identifier itemId = Identifier.parse(GsonHelper.getAsString(jsonObject, "item"));
                    JsonElement models = jsonObject.get("models");
                    MapCodecHolder holder = new MapCodecHolder(models);
                    map.put(itemId, holder.decode());
                } catch (IllegalArgumentException | IllegalStateException | IOException | JsonParseException |
                         IdentifierException ex) {
                    BrewinAndChewin.LOG.error("Couldn't parse coaster model JSON at location '{}' from pack '{}'. ", entry.getKey(), resource.sourcePackId(), ex);
                }
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<Identifier, List<ModelEntry>> preparations, ResourceManager resourceManager, ProfilerFiller profiler) {
        ITEM_TO_MODELS.clear();
        ITEM_TO_MODELS.putAll(preparations);
    }

    /** Wrapper so the decode error path stays in one place. */
    private record MapCodecHolder(JsonElement json) {
        List<ModelEntry> decode() {
            return ModelEntry.LIST_CODEC.decode(JsonOps.INSTANCE, this.json)
                    .getOrThrow(msg -> new JsonParseException("Failed to parse coaster models: " + msg))
                    .getFirst();
        }
    }

    public record ModelEntry(Identifier model, List<? extends TextureModifier> modifiers) {
        private static final Codec<ModelEntry> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Identifier.CODEC.fieldOf("model").forGetter(ModelEntry::model),
                BnCTextureModifiers.CODEC.listOf().optionalFieldOf("texture_modifiers", List.of()).forGetter(modelEntry -> (List) modelEntry.modifiers())
        ).apply(inst, ModelEntry::new));

        public static final Codec<List<ModelEntry>> LIST_CODEC = Codec.either(Identifier.CODEC, DIRECT_CODEC.listOf())
                .xmap(either -> either.map(id -> List.of(new ModelEntry(id, List.of())), Function.identity()), modelEntry -> {
                    if (modelEntry.size() == 1 && modelEntry.get(0).modifiers().isEmpty())
                        return Either.left(modelEntry.get(0).model());
                    return Either.right(modelEntry);
                });
    }
}
