package umpaz.brewinandchewin.common.block.entity.container;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCRecipeBookTypes;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.container.AbstractedFluidTank;
import umpaz.brewinandchewin.common.container.AbstractedItemHandler;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.FluidUnit;
import umpaz.brewinandchewin.common.utility.KegRecipeWrapper;

import java.util.List;
import java.util.Objects;

public class KegMenu extends RecipeBookMenu implements ServerPlaceRecipe.CraftingMenuAccess<KegFermentingRecipe>
{
    public static final Identifier EMPTY_CONTAINER_SLOT_TANKARD = BrewinAndChewin.asResource("item/empty_container_slot_tankard");

    /** The 2x2 fermenting grid occupies slots 0..3. */
    private static final int GRID_WIDTH = 2;
    private static final int GRID_HEIGHT = 2;
    private static final int GRID_SLOTS = GRID_WIDTH * GRID_HEIGHT;
    private static final int CONTAINER_SLOT = 4;
    private static final int RESULT_SLOT = 5;

    public final KegBlockEntity blockEntity;
    public final AbstractedItemHandler inventory;
    public final AbstractedFluidTank kegTank;
    private final ContainerData kegData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;
    private final KegRecipeWrapper recipeWrapper;

    public KegMenu(final int windowId, final Inventory playerInventory, final BlockPos data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
    }

    public KegMenu(final int windowId, final Inventory playerInventory, final KegBlockEntity blockEntity, ContainerData kegDataIn) {
        super(BnCMenuTypes.KEG, windowId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.kegTank = blockEntity.getFluidTank();
        this.kegData = kegDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.recipeWrapper = BrewinAndChewin.getHelper().createRecipeWrapper(inventory, kegTank);

        // Ingredient Slots - 2 Rows x 2 Columns
        int startX = 8;
        int startY = 18;
        int inputStartX = 39;
        int inputStartY = 17;
        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                this.addSlot(BrewinAndChewin.getHelper().createKegSlot(inventory, (row * 2) + column,
                        inputStartX + (column * borderSlotSize),
                        inputStartY + (row * borderSlotSize)));
            }
        }


        // Tankard Input
        this.addSlot(BrewinAndChewin.getHelper().createKegContainerSlot(inventory, CONTAINER_SLOT, 91, 55));

        // Tankard Output
        this.addSlot(BrewinAndChewin.getHelper().createKegResultSlot(inventory, RESULT_SLOT, 124, 55));


        // Main Player Inventory
        int startPlayerInvY = startY * 4 + 12;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
                        startPlayerInvY + (row * borderSlotSize)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
        }

        this.addDataSlots(kegDataIn);
    }

    private static KegBlockEntity getTileEntity(final Inventory playerInventory, final BlockPos data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data);
        if (tileAtPos instanceof KegBlockEntity) {
            return (KegBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, BnCBlocks.KEG);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int indexContainerInput = CONTAINER_SLOT;
        int indexOutput = RESULT_SLOT;
        int startPlayerInv = indexOutput + 1;
        int endPlayerInv = startPlayerInv + 36;

        Slot slot = this.getSlot(index);
        ItemStack slotStackCopy = ItemStack.EMPTY;
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            slotStackCopy = slotStack.copy();
            if (index == indexOutput) {
                if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index > indexOutput) {
                boolean isValidContainer = slotStack.is(blockEntity.getInventory().getStackInSlot(CONTAINER_SLOT).getItem()) || blockEntity.getPouringRecipe(slotStack).isPresent();
                if (isValidContainer && !this.moveItemStackTo(slotStack, indexContainerInput, indexContainerInput + 1, false)) {
                    return ItemStack.EMPTY;
                }
                else if ( !this.moveItemStackTo(slotStack, 0, indexContainerInput, false) ) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == slotStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }


        return slotStackCopy;

    }

    public int getFermentProgressionScaled() {
        int i = this.kegData.get(0);
        int j = this.kegData.get(1);
        return j != 0 && i != 0 ? i * 22 / j : 0;
    }

    public float getProgression() {
        return this.kegData.get(0);
    }

    public int getKegTemperature() {
        return this.blockEntity.getTemperature();
    }

    public boolean isFermenting() {
        var recipe = blockEntity.getRecipeWithoutTemperature();
        return recipe.isPresent() && KegBlockEntity.isValidTemp(getKegTemperature(), recipe.get().value().getTemperature());
    }

    @Override
    public RecipeBookMenu.PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel serverLevel, Inventory inventory) {
        RecipeHolder<KegFermentingRecipe> recipeHolder = (RecipeHolder<KegFermentingRecipe>) recipe;
        List<Slot> gridSlots = this.slots.subList(0, GRID_SLOTS);
        return ServerPlaceRecipe.placeRecipe(this, GRID_WIDTH, GRID_HEIGHT, gridSlots, gridSlots, inventory, recipeHolder, useMaxItems, allowDroppingItemsToClear);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
        for (int i = 0; i < inventory.getSlotCount(); i++) {
            stackedContents.accountSimpleStack(inventory.getStackInSlot(i));
        }

        // 26.1 has no RecipePicker to patch any more, so the keg's "the tank's fluid counts as its
        // containers" rule is expressed the new way: account those containers as available stacks.
        if (kegTank.isEmpty()) {
            return;
        }
        AbstractedFluidStack tankFluid = kegTank.getAbstractedFluid();
        for (KegPouringRecipe pouring : pouringRecipes()) {
            if (!pouring.getRawFluid().fluid().isSame(tankFluid.fluid())) {
                continue;
            }
            ItemStack container = pouring.getContainer();
            if (container.isEmpty()) {
                continue;
            }
            long perContainer = pouring.getUnit().convertToLoader(pouring.getRawFluid().amount());
            if (perContainer <= 0) {
                continue;
            }
            int available = (int) Math.min(tankFluid.amount() / perContainer, container.getMaxStackSize());
            if (available > 0) {
                stackedContents.accountStack(container.copyWithCount(available));
            }
        }
    }

    /**
     * The KEG_POURING recipes, read off the server's recipe manager.
     *
     * <p>26.1 dropped {@code RecipeManager#getAllRecipesFor}, so this filters {@code getRecipes()}
     * instead. The client has no recipe manager at all any more, hence the {@link ServerLevel} gate.
     */
    private List<KegPouringRecipe> pouringRecipes() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        return serverLevel.recipeAccess().getRecipes().stream()
                .map(RecipeHolder::value)
                .filter(KegPouringRecipe.class::isInstance)
                .map(KegPouringRecipe.class::cast)
                .toList();
    }

    @Override
    public void clearCraftingContent() {
        for (int i = 0; i < GRID_SLOTS; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(RecipeHolder<KegFermentingRecipe> recipe) {
        return recipe.value().matches(recipeWrapper, level);
    }

    public int getResultSlotIndex() {
        return RESULT_SLOT;
    }

    public int getGridWidth() {
        return GRID_WIDTH;
    }

    public int getGridHeight() {
        return GRID_HEIGHT;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return BnCRecipeBookTypes.FERMENTING;
    }

    public boolean shouldMoveToInventory(int slot) {
        return slot < (getGridWidth() * getGridHeight());
    }
}
