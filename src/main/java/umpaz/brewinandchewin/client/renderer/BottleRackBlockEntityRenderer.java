package umpaz.brewinandchewin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.BottleRackBlock;
import umpaz.brewinandchewin.common.block.entity.BottleRackBlockEntity;
import umpaz.brewinandchewin.common.item.WineItem;
import umpaz.brewinandchewin.common.utility.BnCLabelUtils;
import umpaz.brewinandchewin.common.utility.BnCWineUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws the bottles sitting in a bottle rack.
 *
 * <p>The rack itself is an ordinary blockstate model; only the bottles and labels are hand placed,
 * and each is a standalone model because the choice depends on the wine and its age.
 */
public class BottleRackBlockEntityRenderer implements BlockEntityRenderer<BottleRackBlockEntity, BottleRackBlockEntityRenderer.BottleRackRenderState> {
    private static final float SHALLOW_DEPTH = 5.0F / 16.0F;
    private static final float LARGE_DEPTH = -3.0F / 16.0F;
    private static final float FIRST_COLUMN = 12.0F / 16.0F;
    private static final float FIRST_ROW = 12.0F / 16.0F;
    private static final float SLOT_SPACING = 5.0F / 16.0F;
    private static final float LABEL_DROP = 2.0F / 16.0F;
    private static final int[] EMPTY_TINTS = new int[0];

    public BottleRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static Identifier bottleModel(WineItem wine, boolean finelyAged) {
        return BrewinAndChewin.asResource("block/bottle_rack_bottle_" + wine.getWineType().getSerializedName()
                + (finelyAged ? "_fine" : ""));
    }

    public static Identifier labelModel(int slot) {
        return BrewinAndChewin.asResource("block/bottle_rack_bottle_label_" + slot);
    }

    @Override
    public BottleRackRenderState createRenderState() {
        return new BottleRackRenderState();
    }

    @Override
    public void extractRenderState(BottleRackBlockEntity blockEntity, BottleRackRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        if (!(blockState.getBlock() instanceof BottleRackBlock)) {
            return;
        }

        state.facing = blockState.getValue(BottleRackBlock.FACING);
        state.depth = blockState.getValue(BottleRackBlock.LARGE) ? LARGE_DEPTH : SHALLOW_DEPTH;
        state.bottles.clear();

        for (int slot = 0; slot < BottleRackBlockEntity.SLOT_COUNT; ++slot) {
            ItemStack stack = blockEntity.getItem(slot);
            if (!(stack.getItem() instanceof WineItem wine)) {
                continue;
            }

            boolean finelyAged = BnCWineUtils.getContents(stack).isFinelyAged(wine.getWineType());
            BlockStateModelPart bottle = BnCStandaloneModels.part(bottleModel(wine, finelyAged));
            if (bottle == null) {
                BrewinAndChewin.LOG.error("Failed to get bottle model for '{}'", bottleModel(wine, finelyAged));
                continue;
            }
            BlockStateModelPart label = BnCLabelUtils.getLabel(stack).isPresent()
                    ? BnCStandaloneModels.part(labelModel(slot))
                    : null;
            state.bottles.add(new BottlePlacement(slot, bottle, label));
        }
    }

    @Override
    public void submit(BottleRackRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotationOf(state.facing)));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        for (BottlePlacement placement : state.bottles) {
            float x = FIRST_COLUMN - (placement.slot() % BottleRackBlock.COLUMNS) * SLOT_SPACING;
            float y = FIRST_ROW - (placement.slot() / BottleRackBlock.COLUMNS) * SLOT_SPACING;

            poseStack.pushPose();
            poseStack.translate(x, y, state.depth);
            submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), List.of(placement.bottle()),
                    EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            if (placement.label() != null) {
                poseStack.pushPose();
                poseStack.translate(0.0F, -LABEL_DROP, 0.0F);
                submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), List.of(placement.label()),
                        EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static float rotationOf(Direction facing) {
        return switch (facing) {
            case EAST -> 90.0F;
            case SOUTH -> 180.0F;
            case WEST -> 270.0F;
            default -> 0.0F;
        };
    }

    public static class BottleRackRenderState extends BlockEntityRenderState {
        public Direction facing = Direction.NORTH;
        public float depth = SHALLOW_DEPTH;
        public final List<BottlePlacement> bottles = new ObjectArrayList<>();
    }

    public record BottlePlacement(int slot, BlockStateModelPart bottle, @Nullable BlockStateModelPart label) {
    }
}
