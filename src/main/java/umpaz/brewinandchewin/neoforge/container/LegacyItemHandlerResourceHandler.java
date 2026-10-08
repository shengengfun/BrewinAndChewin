package umpaz.brewinandchewin.neoforge.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Exposes a legacy {@link IItemHandler} as the 26.1 {@link ResourceHandler} the item capability now expects.
 * The keg's inventory is implemented against the legacy interface, so this bridges the two at the
 * capability boundary instead of rewriting the container layer.
 */
public class LegacyItemHandlerResourceHandler implements ResourceHandler<ItemResource> {
    private final IItemHandler handler;
    private final Runnable onChanged;

    public LegacyItemHandlerResourceHandler(IItemHandler handler) {
        this(handler, () -> {});
    }

    public LegacyItemHandlerResourceHandler(IItemHandler handler, Runnable onChanged) {
        this.handler = handler;
        this.onChanged = onChanged;
    }

    @Override
    public int size() {
        return handler.getSlots();
    }

    @Override
    public ItemResource getResource(int index) {
        return ItemResource.of(handler.getStackInSlot(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return handler.getStackInSlot(index).getCount();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return handler.getSlotLimit(index);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return resource.isEmpty() || handler.isItemValid(index, resource.toStack());
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        ItemStack remaining = handler.insertItem(index, resource.toStack(amount), false);
        int inserted = amount - remaining.getCount();
        if (inserted > 0)
            onChanged.run();
        return inserted;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        ItemStack extracted = handler.extractItem(index, amount, false);
        if (!extracted.isEmpty())
            onChanged.run();
        return extracted.getCount();
    }
}
