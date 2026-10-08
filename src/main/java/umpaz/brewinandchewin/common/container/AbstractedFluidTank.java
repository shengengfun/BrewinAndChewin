package umpaz.brewinandchewin.common.container;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.FluidUnit;

public interface AbstractedFluidTank extends ValueIOSerializable {
    default long getFluidCapacity() {
        return getFluidCapacity(0);
    }
    long getFluidCapacity(int slot);
    AbstractedFluidStack getAbstractedFluid();
    void setAbstractedFluid(AbstractedFluidStack stack);

    AbstractedFluidStack fill(AbstractedFluidStack stack, boolean simulate);
    default AbstractedFluidStack drain(long maxDrain, FluidUnit unit, boolean simulate) {
        return drain(0, maxDrain, unit, simulate);
    }
    AbstractedFluidStack drain(int slot, long maxDrain, FluidUnit unit, boolean simulate);

    boolean isEmpty();

    @Override
    default void serialize(net.minecraft.world.level.storage.ValueOutput output) {}

    @Override
    default void deserialize(net.minecraft.world.level.storage.ValueInput input) {}

    default void readFromNbt(CompoundTag tag, HolderLookup.Provider provider) {
        deserialize(umpaz.brewinandchewin.common.utility.BnCValueIO.input(provider, tag));
    }
    default CompoundTag writeToNbt(HolderLookup.Provider provider) {
        return umpaz.brewinandchewin.common.utility.BnCValueIO.write(this, provider);
    }

    // Item Tank Fields
    // TODO: Maybe separate into a separate interface that extends this one.
    default ItemStack getContainer() {
        return ItemStack.EMPTY;
    }
    default boolean isFluidValid(AbstractedFluidStack stack) {
        return isFluidValid(0, stack);
    }
    default boolean isFluidValid(int slot, AbstractedFluidStack stack) {
        return false;
    }
}
