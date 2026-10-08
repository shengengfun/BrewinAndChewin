package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.neoforge.common.Tags;
import umpaz.brewinandchewin.common.compat.nomansland.NMLIntegration;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.world.BnCBiomeFeatures;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public class BnCBiomeTags extends BiomeTagsProvider {
    public BnCBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BrewinAndChewin.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BnCBiomeFeatures.HAS_WILD_CORN)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addOptional(ResourceKey.create(Registries.BIOME, NMLIntegration.PRAIRIE));
        tag(BnCBiomeFeatures.HAS_WILD_GRAPES)
                .addTag(Tags.Biomes.IS_COLD)
                .addTag(Tags.Biomes.IS_TEMPERATE);
    }
}
