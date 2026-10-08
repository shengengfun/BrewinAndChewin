package umpaz.brewinandchewin.neoforge.client;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSources;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import umpaz.brewinandchewin.client.BnCClientSetup;
import umpaz.brewinandchewin.client.BrewinAndChewinClient;
import umpaz.brewinandchewin.client.gui.AgingCaskScreen;
import umpaz.brewinandchewin.client.gui.KegScreen;
import umpaz.brewinandchewin.client.gui.KegTooltip;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticle;
import umpaz.brewinandchewin.client.particle.RagingParticle;
import umpaz.brewinandchewin.common.registry.BnCFluids;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.common.registry.BnCParticleTypes;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.fluid.BnCFluidConstants;
import umpaz.brewinandchewin.neoforge.client.platform.BnCClientPlatfomHelperNeoForge;

@Mod(value = BrewinAndChewin.MODID, dist = Dist.CLIENT)
public class BrewinAndChewinNeoForgeClient {
    public BrewinAndChewinNeoForgeClient(IEventBus eventBus) {
        BrewinAndChewinClient.init(new BnCClientPlatfomHelperNeoForge());
        // 26.1 dropped EventBusSubscriber.Bus, so the mod-bus client handlers are registered here.
        eventBus.register(ModEvents.class);
        BrewinAndChewin.isClient = true;
    }

    @EventBusSubscriber(modid = BrewinAndChewin.MODID, value = Dist.CLIENT)
    public static class GameEvents {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            BnCClientSetup.appendLabelTooltip(event.getItemStack(), event.getToolTip(), event.getContext().tickRate());
        }
    }

    public static class ModEvents {
        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(BnCMenuTypes.KEG, KegScreen::new);
            event.register(BnCMenuTypes.AGING_CASK, AgingCaskScreen::new);
        }

        @SubscribeEvent
        public static void registerItemProperties(FMLClientSetupEvent event) {
            // 26.1 replaced ItemProperties/ClampedItemPropertyFunction with registry-backed item
            // models and JSON tint_sources, so there is nothing to register here yet.
        }

        // 26.1 moved fluid textures and tint off IClientFluidTypeExtensions onto baked FluidModels.
        @SubscribeEvent
        public static void registerFluidModels(RegisterFluidModelsEvent event) {
            registerAlcohol(event, BnCFluids.BEER, BnCFluids.FLOWING_BEER, BnCFluidConstants.Colors.BEER);
            registerAlcohol(event, BnCFluids.VODKA, BnCFluids.FLOWING_VODKA, BnCFluidConstants.Colors.VODKA);
            registerAlcohol(event, BnCFluids.EGG_GROG, BnCFluids.FLOWING_EGG_GROG, BnCFluidConstants.Colors.EGG_GROG);
            registerAlcohol(event, BnCFluids.STRONGROOT_ALE, BnCFluids.FLOWING_STRONGROOT_ALE, BnCFluidConstants.Colors.STRONGROOT_ALE);
            registerAlcohol(event, BnCFluids.RICE_WINE, BnCFluids.FLOWING_RICE_WINE, BnCFluidConstants.Colors.RICE_WINE);
            registerAlcohol(event, BnCFluids.GLITTERING_GRENADINE, BnCFluids.FLOWING_GLITTERING_GRENADINE, BnCFluidConstants.Colors.GLITTERING_GRENADINE);
            registerAlcohol(event, BnCFluids.STEEL_TOE_STOUT, BnCFluids.FLOWING_STEEL_TOE_STOUT, BnCFluidConstants.Colors.STEEL_TOE_STOUT);
            registerAlcohol(event, BnCFluids.DREAD_NOG, BnCFluids.FLOWING_DREAD_NOG, BnCFluidConstants.Colors.DREAD_NOG);
            registerAlcohol(event, BnCFluids.KOMBUCHA, BnCFluids.FLOWING_KOMBUCHA, BnCFluidConstants.Colors.KOMBUCHA);
            registerAlcohol(event, BnCFluids.SACCHARINE_RUM, BnCFluids.FLOWING_SACCHARINE_RUM, BnCFluidConstants.Colors.SACCHARINE_RUM);
            registerAlcohol(event, BnCFluids.PALE_JANE, BnCFluids.FLOWING_PALE_JANE, BnCFluidConstants.Colors.PALE_JANE);
            registerAlcohol(event, BnCFluids.SALTY_FOLLY, BnCFluids.FLOWING_SALTY_FOLLY, BnCFluidConstants.Colors.SALTY_FOLLY);
            registerAlcohol(event, BnCFluids.BLOODY_MARY, BnCFluids.FLOWING_BLOODY_MARY, BnCFluidConstants.Colors.BLOODY_MARY);
            registerAlcohol(event, BnCFluids.RED_RUM, BnCFluids.FLOWING_RED_RUM, BnCFluidConstants.Colors.RED_RUM);
            registerAlcohol(event, BnCFluids.WITHERING_DROSS, BnCFluids.FLOWING_WITHERING_DROSS, BnCFluidConstants.Colors.WITHERING_DROSS);
            registerAlcohol(event, BnCFluids.RED_WINE, BnCFluids.FLOWING_RED_WINE, BnCFluidConstants.Colors.RED_WINE);
            registerAlcohol(event, BnCFluids.WHITE_WINE, BnCFluids.FLOWING_WHITE_WINE, BnCFluidConstants.Colors.WHITE_WINE);
            registerAlcohol(event, BnCFluids.CURRANT_WINE, BnCFluids.FLOWING_CURRANT_WINE, BnCFluidConstants.Colors.CURRANT_WINE);
            registerAlcohol(event, BnCFluids.VERRUCA_WINE, BnCFluids.FLOWING_VERRUCA_WINE, BnCFluidConstants.Colors.VERRUCA_WINE);
            registerAlcohol(event, BnCFluids.TWISTED_WINE, BnCFluids.FLOWING_TWISTED_WINE, BnCFluidConstants.Colors.TWISTED_WINE);

            registerHoney(event, BnCFluids.HONEY, BnCFluids.FLOWING_HONEY, BnCFluidConstants.Colors.DEFAULT);
            registerHoney(event, BnCFluids.MEAD, BnCFluids.FLOWING_MEAD, BnCFluidConstants.Colors.MEAD);

            event.register(new FluidModel.Unbaked(
                    new Material(BnCFluidConstants.Textures.FLAXEN_STILL_TEXTURE),
                    new Material(BnCFluidConstants.Textures.FLAXEN_FLOWING_TEXTURE),
                    null,
                    FluidTintSources.constant(BnCFluidConstants.Colors.DEFAULT)), BnCFluids.FLAXEN_CHEESE, BnCFluids.FLOWING_FLAXEN_CHEESE);
            event.register(new FluidModel.Unbaked(
                    new Material(BnCFluidConstants.Textures.SCARLET_STILL_TEXTURE),
                    new Material(BnCFluidConstants.Textures.SCARLET_FLOWING_TEXTURE),
                    null,
                    FluidTintSources.constant(BnCFluidConstants.Colors.DEFAULT)), BnCFluids.SCARLET_CHEESE, BnCFluids.FLOWING_SCARLET_CHEESE);
        }

        private static void registerAlcohol(RegisterFluidModelsEvent event, net.minecraft.world.level.material.FlowingFluid still, net.minecraft.world.level.material.FlowingFluid flowing, int color) {
            event.register(new FluidModel.Unbaked(
                    new Material(BnCFluidConstants.Textures.FLUID_STILL_TEXTURE),
                    new Material(BnCFluidConstants.Textures.FLUID_FLOWING_TEXTURE),
                    null,
                    FluidTintSources.constant(color)), still, flowing);
        }

        private static void registerHoney(RegisterFluidModelsEvent event, net.minecraft.world.level.material.FlowingFluid still, net.minecraft.world.level.material.FlowingFluid flowing, int color) {
            event.register(new FluidModel.Unbaked(
                    new Material(BnCFluidConstants.Textures.HONEY_FLUID_STILL_TEXTURE),
                    new Material(BnCFluidConstants.Textures.HONEY_FLUID_FLOWING_TEXTURE),
                    null,
                    FluidTintSources.constant(color)), still, flowing);
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(BnCParticleTypes.DRUNK_BUBBLE, DrunkBubbleParticle.Factory::new);
            event.registerSpriteSet(BnCParticleTypes.RAGING_STAGE_1, RagingParticle.Factory::new);
            event.registerSpriteSet(BnCParticleTypes.RAGING_STAGE_2, RagingParticle.Factory::new);
            event.registerSpriteSet(BnCParticleTypes.RAGING_STAGE_3, RagingParticle.Factory::new);
            event.registerSpriteSet(BnCParticleTypes.RAGING_STAGE_4, RagingParticle.Factory::new);
        }

        @SubscribeEvent
        public static void registerKegTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(KegTooltip.KegTooltipComponent.class, KegTooltip::new);
        }
    }
}
