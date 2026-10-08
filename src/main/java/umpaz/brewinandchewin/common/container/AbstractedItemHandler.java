package umpaz.brewinandchewin.common.container;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import umpaz.brewinandchewin.common.utility.BnCValueIO;

public interface AbstractedItemHandler extends ValueIOSerializable {
    int getSlotCount();

    ItemStack getStackInSlot(int slot);
    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);
    ItemStack extractItem(int slot, int amount, boolean simulate);
    void setStackInSlot(int slot, ItemStack stack);

    boolean isItemValid(int slot, ItemStack stack);
    int getSlotLimit(int slot);

    @Override
    default void serialize(net.minecraft.world.level.storage.ValueOutput output) {}

    @Override
    default void deserialize(net.minecraft.world.level.storage.ValueInput input) {}

    default void readFromNbt(CompoundTag tag, HolderLookup.Provider provider) {
        deserialize(BnCValueIO.input(provider, tag));
    }

    default CompoundTag writeToNbt(HolderLookup.Provider provider) {
        return BnCValueIO.write(this, provider);
    }
}
