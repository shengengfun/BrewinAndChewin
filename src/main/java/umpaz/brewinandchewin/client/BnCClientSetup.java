package umpaz.brewinandchewin.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.common.utility.BnCLabelUtils;

import java.util.List;

/**
 * Client-side setup shim.
 *
 * 26.1 rewrote the pieces this used to assemble: block render layers are derived from the material
 * (so the registration is gone), BlockColor/ItemColor became registry-backed tint sources plus JSON
 * tint_sources, the dynamic coaster model moved to BlockStateModel/ItemModel, and particle and
 * block-entity-renderer registration moved to the new render-state APIs. Each of those is recorded
 * in reports/bac-26.1-status.md; what remains here is the tooltip helper, which is unchanged.
 */
public class BnCClientSetup {

    public static void appendLabelTooltip(ItemStack stack, List<Component> tooltip, float tickRate) {
        BnCLabelUtils.appendLabelTooltip(stack, tooltip, tickRate);
    }
}