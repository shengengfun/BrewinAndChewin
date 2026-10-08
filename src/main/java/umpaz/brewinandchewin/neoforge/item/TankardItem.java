package umpaz.brewinandchewin.neoforge.item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.FluidResourceHandlerItemAdapter;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import umpaz.brewinandchewin.common.registry.BnCItems;


public class TankardItem extends Item {
    public static final int CAPACITY = 500;

    public TankardItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        ResourceHandler<FluidResource> blockHandler = Capabilities.Fluid.BLOCK.getCapability(level, pos, state, be, context.getClickedFace());
        IFluidHandler handler = blockHandler == null ? null : IFluidHandler.of(blockHandler);

        if (handler == null)
            return InteractionResult.PASS;

        ItemAccess access = ItemAccess.forStack(stack);
        ResourceHandler<FluidResource> itemResourceHandler = Capabilities.Fluid.ITEM.getCapability(stack, access);
        IFluidHandlerItem itemHandler = itemResourceHandler == null ? null : new FluidResourceHandlerItemAdapter(itemResourceHandler, access);
        if (itemHandler == null)
            return InteractionResult.PASS;

        FluidStack contained = itemHandler.getFluidInTank(0);
        if (contained.isEmpty())
            return InteractionResult.PASS;

        int filled = handler.fill(contained, IFluidHandler.FluidAction.EXECUTE);
        if (filled <= 0)
            return InteractionResult.PASS;

        itemHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);

        context.getPlayer().setItemInHand(context.getHand(), new ItemStack(BnCItems.TANKARD));

        return InteractionResult.SUCCESS;
    }
}

