package umpaz.brewinandchewin.data;

import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import umpaz.brewinandchewin.common.registry.BnCDamageTypes;
import umpaz.brewinandchewin.data.loot.BnCBlockLoot;
import umpaz.brewinandchewin.data.recipe.BnCEntityTypeTags;
import umpaz.brewinandchewin.data.world.BnCWildCropGeneration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Datagen entry point for 26.1.
 *
 * <p>26.1 reshaped NeoForge datagen: GatherDataEvent moved to
 * net.neoforged.neoforge.data.event and lost both includeServer() and getExistingFileHelper()
 * (ExistingFileHelper is gone entirely), so providers are just added. There is no
 * EventBusSubscriber on this class either - the annotation can no longer select the mod bus, so
 * BrewinAndChewinNeoForge registers it on the mod event bus.
 */
public class BnCDataGenerators {

    @net.neoforged.bus.api.SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BnCBuiltInEntries builtInEntries = new BnCBuiltInEntries(output, lookupProvider, new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, bootstrap ->
                        bootstrap.register(BnCDamageTypes.CARDIAC_ARREST, new DamageType(
                                "brewinandchewin.cardiacArrest",
                                DamageScaling.NEVER,
                                0.1F
                        ))
                )
                .add(Registries.CONFIGURED_FEATURE, BnCWildCropGeneration::bootstrapConfiguredFeatures)
                .add(Registries.PLACED_FEATURE, BnCWildCropGeneration::bootstrapPlacedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BnCWildCropGeneration::bootstrapBiomeModifiers)
        );
        event.addProvider(builtInEntries);
        lookupProvider = builtInEntries.getRegistryProvider();

        // 26.1 binds an item's default components from a data-driven pass that normally only runs
        // when a server/client reloads its registries. Datagen never does that, so item components
        // stay unbound and any ItemStack creation throws - run the same pass here. Tag-backed
        // components resolve to an empty HolderSet at this point (no tag data is loaded yet), which
        // trips NeoForge's dev-only "components must implement equals/hashCode" check, so that check
        // is suspended for the duration. It has no effect outside the datagen process.
        boolean inIde = SharedConstants.IS_RUNNING_IN_IDE;
        SharedConstants.IS_RUNNING_IN_IDE = false;
        try {
            BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(lookupProvider.join())
                    .forEach(DataComponentInitializers.PendingComponents::apply);
        } finally {
            SharedConstants.IS_RUNNING_IN_IDE = inIde;
        }

        event.addProvider(new BnCBlockTags(output, lookupProvider));
        event.addProvider(new BnCItemTags(output, lookupProvider));
        event.addProvider(new BnCFluidTags(output, lookupProvider));
        event.addProvider(new BnCMobEffectTags(output, lookupProvider));
        event.addProvider(new BnCEntityTypeTags(output, lookupProvider));
        event.addProvider(new BnCDamageTypeTags(output, lookupProvider));
        event.addProvider(new BnCBiomeTags(output, lookupProvider));
        event.addProvider(new BnCRecipes(output, lookupProvider));
        event.addProvider(new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(BnCBlockLoot::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
        event.addProvider(new AdvancementProvider(output, lookupProvider, List.of(new BnCAdvancements())));
    }
}