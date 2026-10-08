package umpaz.brewinandchewin.neoforge.container;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import umpaz.brewinandchewin.common.container.AbstractedItemHandler;

public class KegItemHandlerNeoForge extends ItemStackHandler implements AbstractedItemHandler {
    public KegItemHandlerNeoForge(int size) {
        super(size);
    }

    @Override
    public int getSlotCount() {
        return getSlots();
    }

    @Override
    public void serialize(ValueOutput output) {
        super.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
    }
}
