package umpaz.brewinandchewin.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;
import umpaz.brewinandchewin.common.crafting.KegRecipeDisplay;

import java.util.List;

/**
 * The keg's recipe book.
 *
 * <p>26.1 turned {@code RecipeBookComponent} into a generic class with five small hooks, so the keg
 * no longer needs the accessor mixins 1.21.1 used to reach the ghost recipe: everything the screen
 * wants to know is either a hook override or {@link #getGhostDisplay()}.
 */
public class KegRecipeBookComponent extends RecipeBookComponent<KegMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            BrewinAndChewin.asResource("recipe_book/keg_filter_enabled"),
            BrewinAndChewin.asResource("recipe_book/keg_filter_disabled"),
            BrewinAndChewin.asResource("recipe_book/keg_filter_enabled_highlighted"),
            BrewinAndChewin.asResource("recipe_book/keg_filter_disabled_highlighted")
    );
    private static final Component FILTER_NAME = Component.translatable("brewinandchewin.container.recipe_book.fermentable");

    private @Nullable KegRecipeDisplay ghostDisplay;

    public KegRecipeBookComponent(KegMenu menu, List<RecipeBookComponent.TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected Component getRecipeFilterName() {
        return FILTER_NAME;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index < this.menu.getGridWidth() * this.menu.getGridHeight();
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(stackedContents, display -> display instanceof KegRecipeDisplay);
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        if (!(recipe instanceof KegRecipeDisplay kegRecipe)) {
            return;
        }

        List<Slot> slots = this.menu.slots;
        for (int i = 0; i < kegRecipe.inputs().size() && i < this.menu.getGridWidth() * this.menu.getGridHeight(); i++) {
            ghostSlots.setInput(slots.get(i), context, kegRecipe.inputs().get(i));
        }
        ghostSlots.setResult(slots.get(this.menu.getResultSlotIndex()), context, kegRecipe.result());
    }

    @Override
    public void fillGhostRecipe(RecipeDisplay recipe) {
        this.ghostDisplay = recipe instanceof KegRecipeDisplay kegRecipe ? kegRecipe : null;
        super.fillGhostRecipe(recipe);
    }

    /**
     * The recipe currently ghosted into the keg, or null.
     *
     * <p>The tank is drawn by the screen rather than by a slot, so the screen needs the fluid
     * ingredient and the temperature the book is suggesting.
     */
    /**
     * The recipe currently ghosted into the keg, or null.
     *
     * <p>The tank is drawn by the screen rather than by a slot, so the screen needs the fluid
     * ingredient and the temperature the book is suggesting.
     */
    public @Nullable KegRecipeDisplay getGhostDisplay() {
        return this.ghostDisplay;
    }

    /** The keg's recipe book is disabled by config, so never let it become visible. */
    public void hide() {
        this.setVisible(false);
    }

    @Override
    protected void setVisible(boolean visible) {
        super.setVisible(visible && BnCConfiguration.common().recipeBook().enabled());
    }
}
