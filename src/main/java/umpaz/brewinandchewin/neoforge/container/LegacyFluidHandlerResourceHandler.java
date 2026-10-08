package umpaz.brewinandchewin.neoforge.container;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Exposes a legacy {@link IFluidHandler} as the 26.1 {@link ResourceHandler} the fluid capability now expects.
 * See {@link LegacyItemHandlerResourceHandler} for why this bridge exists.
 */
public class LegacyFluidHandlerResourceHandler implements ResourceHandler<FluidResource> {
    private final IFluidHandler handler;
    private final Runnable onChanged;

    public LegacyFluidHandlerResourceHandler(IFluidHandler handler) {
        this(handler, () -> {});
    }

    public LegacyFluidHandlerResourceHandler(IFluidHandler handler, Runnable onChanged) {
        this.handler = handler;
        this.onChanged = onChanged;
    }

    @Override
    public int size() {
        return handler.getTanks();
    }

    @Override
    public FluidResource getResource(int index) {
        return FluidResource.of(handler.getFluidInTank(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return handler.getFluidInTank(index).getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return handler.getTankCapacity(index);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return resource.isEmpty() || handler.isFluidValid(index, resource.toStack(FluidType.BUCKET_VOLUME));
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        int filled = handler.fill(resource.toStack(amount), IFluidHandler.FluidAction.EXECUTE);
        if (filled > 0)
            onChanged.run();
        return filled;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        FluidStack drained = handler.drain(resource.toStack(amount), IFluidHandler.FluidAction.EXECUTE);
        if (!drained.isEmpty())
            onChanged.run();
        return drained.getAmount();
    }
}
