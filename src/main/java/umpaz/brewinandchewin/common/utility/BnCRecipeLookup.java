package umpaz.brewinandchewin.common.utility;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Recipe lookups for 26.1.
 *
 * <p>26.1 removed {@code RecipeManager#getAllRecipesFor} and took the recipe manager away from
 * {@code Level}: {@code Level#recipeAccess()} returns the client-side {@code RecipeAccess} (just
 * property sets), and only {@link ServerLevel} narrows it to a real {@link RecipeManager}.
 */
public final class BnCRecipeLookup {
    private BnCRecipeLookup() {}

    /** The recipe manager behind a level, or {@code null} when the level is client-side. */
    public static @Nullable RecipeManager manager(Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.recipeAccess() : null;
    }

    /** The recipe manager of a running server. */
    public static RecipeManager manager(MinecraftServer server) {
        return server.getRecipeManager();
    }

    /** Every recipe of the given type. Replaces the removed {@code getAllRecipesFor}. */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeHolder<T>> all(RecipeManager manager, RecipeType<T> type) {
        return manager.getRecipes().stream()
                .filter(holder -> holder.value().getType() == type)
                .map(holder -> (RecipeHolder<T>) holder)
                .toList();
    }
}
