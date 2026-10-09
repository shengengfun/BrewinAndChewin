package umpaz.brewinandchewin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.renderer.texture.modifier.TextureModifier;
import umpaz.brewinandchewin.common.block.CoasterBlock;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws the coaster base and whatever is resting on it.
 *
 * <p>26.1 splits block entity rendering into "extract the state, then submit it", so everything
 * that needs the level (model choice, tint colours, glint) is resolved in
 * {@link #extractRenderState} and {@link #submit} only walks the pose stack. The base is a
 * blockstate model that no blockstate can select - it depends on the block's contents - so it is
 * loaded as a standalone model.
 */
public class CoasterBlockEntityRenderer implements BlockEntityRenderer<CoasterBlockEntity, CoasterBlockEntityRenderer.CoasterRenderState> {
    private static final Identifier COASTER_MODEL = BrewinAndChewin.asResource("block/coaster");
    private static final Identifier TRAY_MODEL = BrewinAndChewin.asResource("block/coaster_tray");
    private static final int[] EMPTY_TINTS = new int[0];

    private final ItemModelResolver itemModelResolver;

    public CoasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CoasterRenderState createRenderState() {
        return new CoasterRenderState();
    }

    @Override
    public void extractRenderState(CoasterBlockEntity blockEntity, CoasterRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.rotation = blockState.getValue(CoasterBlock.ROTATION);
        state.invisible = blockState.getValue(CoasterBlock.INVISIBLE);
        state.seed = blockEntity.getBlockPos().asLong();
        state.baseModel = blockState.getValue(CoasterBlock.SIZE) > 1 ? TRAY_MODEL : COASTER_MODEL;

        state.contents.clear();
        BlockAndTintGetter tintGetter = blockEntity.getLevel() instanceof ClientLevel clientLevel ? clientLevel : BlockAndTintGetter.EMPTY;
        for (ItemStack stack : blockEntity.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            CoasterContent content = new CoasterContent();
            content.stack = stack.copy();
            List<CoasterModelLoader.ModelEntry> entries = CoasterModelLoader.getModelEntries(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if (entries != null) {
                for (CoasterModelLoader.ModelEntry entry : entries) {
                    BlockStateModelPart part = BnCStandaloneModels.part(entry.model());
                    if (part == null) {
                        BrewinAndChewin.LOG.error("Failed to get coaster model '{}'", entry.model());
                        continue;
                    }
                    int color = 0xFFFFFFFF;
                    RenderType renderType = Sheets.cutoutBlockSheet();
                    for (TextureModifier modifier : entry.modifiers()) {
                        color = modifier.color(tintGetter, blockState, blockEntity.getBlockPos(), stack, color);
                        renderType = modifier.renderType(tintGetter, blockState, blockEntity.getBlockPos(), stack, renderType);
                    }
                    content.models.add(new CoasterModel(part, color, renderType));
                }
            } else {
                content.itemModel = new ItemStackRenderState();
                this.itemModelResolver.updateForTopItem(content.itemModel, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), blockEntity,
                        (int) blockEntity.getBlockPos().asLong());
            }
            state.contents.add(content);
        }
    }

    @Override
    public void submit(CoasterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        RandomSource random = new LegacyRandomSource(state.seed);
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, -(360f / 16f) * state.rotation), 0.5f, 0, 0.5f);

        if (!state.invisible || state.contents.isEmpty()) {
            BlockStateModelPart base = BnCStandaloneModels.part(state.baseModel);
            if (base != null) {
                poseStack.pushPose();
                submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), List.of(base),
                        EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }

        int count = state.contents.size();
        for (int i = 0; i < count; i++) {
            CoasterContent content = state.contents.get(i);
            poseStack.pushPose();
            poseUtil(poseStack, count, i, random, state.invisible);

            if (!content.models.isEmpty()) {
                for (CoasterModel model : content.models) {
                    submitNodeCollector.submitBlockModel(poseStack, model.renderType(), List.of(model.part()),
                            model.tintColor() == 0xFFFFFFFF ? EMPTY_TINTS : new int[]{model.tintColor()},
                            state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                }
            } else if (content.itemModel != null) {
                poseStack.translate(0.51, 0.05, 0.5);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                content.itemModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
            poseStack.popPose();
        }
    }

    private static void poseUtil(PoseStack poseStack, int fullCount, int curCount, RandomSource seededRandom, boolean invisible) {
        Vector2f translateVec = switch (fullCount) {
            case 2 -> curCount == 0 ? new Vector2f(-0.20F, -0.15F) : new Vector2f(0.20F, 0.15F);
            case 3 -> switch (curCount) {
                case 0 -> new Vector2f(0.05F, 0.25F);
                case 1 -> new Vector2f(-0.25F, -0.15F);
                default -> new Vector2f(0.25F, -0.25F);
            };
            case 4 -> switch (curCount) {
                case 0 -> new Vector2f(0.20F, 0.25F);
                case 1 -> new Vector2f(-0.25F, 0.20F);
                case 2 -> new Vector2f(0.25F, -0.20F);
                default -> new Vector2f(-0.20F, -0.25F);
            };
            default -> new Vector2f();
        };

        float rotation = switch (fullCount) {
            case 2 -> curCount == 0 ? 190 : 10;
            case 3 -> switch (curCount) {
                case 0 -> -20;
                case 1 -> 220;
                default -> 100;
            };
            case 4 -> switch (curCount) {
                case 0 -> -5;
                case 1 -> 265;
                case 2 -> 85;
                default -> 175;
            };
            default -> 0;
        };

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, rotation + seededRandom.nextFloat() * 20.0f - 10.0f),
                0.5f + translateVec.x(), 0f, 0.5f + translateVec.y());
        poseStack.translate(translateVec.x(), invisible ? 0 : 1.0 / 16f, translateVec.y());
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return false;
    }

    public AABB getRenderBoundingBox(CoasterBlockEntity blockEntity) {
        return blockEntity.getRenderBoundingBox();
    }

    /** Per-item resolved contents: either custom coaster models, or the item's own model. */    public static class CoasterRenderState extends BlockEntityRenderState {
        public int rotation;
        public boolean invisible;
        public long seed;
        public Identifier baseModel = COASTER_MODEL;
        public final List<CoasterContent> contents = new ObjectArrayList<>();
    }

    public static class CoasterContent {
        public ItemStack stack = ItemStack.EMPTY;
        public @Nullable ItemStackRenderState itemModel;
        public final List<CoasterModel> models = new ArrayList<>();
    }

    public record CoasterModel(BlockStateModelPart part, int tintColor, RenderType renderType) {
    }
}
