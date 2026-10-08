package umpaz.brewinandchewin.neoforge.container;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import umpaz.brewinandchewin.common.container.AbstractedFluidTank;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.FluidUnit;

public class KegFluidTankNeoForge extends FluidTank implements AbstractedFluidTank {
    public KegFluidTankNeoForge(int capacity) {
        super(capacity);
    }

    @Override
    public long getFluidCapacity(int slot) {
        return getCapacity();
    }

    @Override
    public AbstractedFluidStack getAbstractedFluid() {
        return new AbstractedFluidStack(fluid.getFluid(), fluid.getAmount(), fluid.getComponents(), FluidUnit.MILLIBUCKET, fluid);
    }

    @Override
    public void setAbstractedFluid(AbstractedFluidStack stack) {
        setFluid(unwrapFluid(stack));
    }

    @Override
    public AbstractedFluidStack fill(AbstractedFluidStack fluidStack, boolean simulate) {
        fill(unwrapFluid(fluidStack), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        return getAbstractedFluid();
    }

    @Override
    public AbstractedFluidStack drain(int slot, long maxDrain, FluidUnit unit, boolean simulate) {
        int newAmount = (int) unit.convertToLoader(maxDrain);
        FluidStack drained = drain(newAmount, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        return new AbstractedFluidStack(drained.getFluid(), drained.getAmount(), drained.getComponents(), FluidUnit.MILLIBUCKET, drained);
    }

    @Override
    public void serialize(ValueOutput output) {
        super.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
    }

    private FluidStack unwrapFluid(AbstractedFluidStack stack)  {
        if (stack.loaderSpecific() instanceof FluidStack fluidStack)
            return fluidStack;

        if (stack.isEmpty())
            return FluidStack.EMPTY;

        return new FluidStack(stack.fluid().builtInRegistryHolder(), (int) stack.unit().convertToLoader(stack.amount()), stack.components() instanceof PatchedDataComponentMap patched ? patched.asPatch() : DataComponentPatch.EMPTY);
    }
}
