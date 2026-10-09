package umpaz.brewinandchewin.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.BrewinAndChewinClient;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplays;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.crafting.KegRecipeDisplay;
import umpaz.brewinandchewin.common.registry.BnCRecipeBookCategories;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.BnCRecipeLookup;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;
import umpaz.brewinandchewin.common.utility.FluidUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The keg's screen.
 *
 * <p>26.1 splits rendering into {@code extractBackground} / {@code extractLabels} /
 * {@code extractTooltip}, and moved the recipe book into {@link AbstractRecipeBookScreen}, so the
 * screen only draws the keg's own decoration and hands the tank over to the platform helper.
 */
public class KegScreen extends AbstractRecipeBookScreen<KegMenu> {
    public static final Identifier BACKGROUND_TEXTURE = BrewinAndChewin.asResource("textures/gui/keg.png");
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private static final int PROGRESS_ARROW_X = 80;
    private static final int PROGRESS_ARROW_Y = 25;
    private static final int PROGRESS_ARROW_HEIGHT = 18;

    private static final int COLD_BAR_X = 35;
    private static final int CHILLY_BAR_X = 43;
    private static final int WARM_BAR_X = 60;
    private static final int HOT_BAR_X = 69;
    private static final int TEMPERATURE_BAR_Y = 55;
    private static final int TEMPERATURE_BAR_WIDTH = 42;
    private static final int TEMPERATURE_BAR_HEIGHT = 4;

    private static final int LEFT_BUBBLE_X = 109;
    private static final int RIGHT_BUBBLE_X = 147;
    private static final int BUBBLE_Y = 44;
    private static final int BUBBLE_WIDTH = 9;
    private static final int BUBBLE_HEIGHT = 24;

    private static final int TANK_X = 120;
    private static final int TANK_Y = 19;
    private static final int TANK_WIDTH = 24;
    private static final int TANK_HEIGHT = 28;
    private static final int TANK_ITEM_X = 124;
    private static final int TANK_ITEM_Y = 23;

    private static final Map<Fluid, Component> FLUID_CONTAINER_COMPONENTS = new HashMap<>();

    private final KegRecipeBookComponent kegRecipeBook;

    public KegScreen(KegMenu menu, Inventory inventory, Component title) {
        this(menu, new KegRecipeBookComponent(menu, createTabInfos()), inventory, title);
    }

    private KegScreen(KegMenu menu, KegRecipeBookComponent recipeBook, Inventory inventory, Component title) {
        super(menu, recipeBook, inventory, title);
        this.kegRecipeBook = recipeBook;
        this.titleLabelX = 28;
    }

    private static List<RecipeBookComponent.TabInfo> createTabInfos() {
        return List.of(
                new RecipeBookComponent.TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), BnCRecipeBookCategories.FERMENTING_SEARCH),
                new RecipeBookComponent.TabInfo(new ItemStack(umpaz.brewinandchewin.common.registry.BnCItems.BEER), Optional.empty(), BnCRecipeBookCategories.FERMENTING_DRINKS),
                new RecipeBookComponent.TabInfo(new ItemStack(umpaz.brewinandchewin.common.registry.BnCItems.UNRIPE_FLAXEN_CHEESE_WHEEL), Optional.empty(), BnCRecipeBookCategories.FERMENTING_MEALS),
                new RecipeBookComponent.TabInfo(new ItemStack(umpaz.brewinandchewin.common.registry.BnCItems.KIMCHI), Optional.empty(), BnCRecipeBookCategories.FERMENTING_MISC)
        );
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = 38;
        if (!BnCConfiguration.common().recipeBook().enabled()) {
            this.kegRecipeBook.hide();
        }
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
    }

    private void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, u, v, width, height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        this.blit(graphics, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int progress = this.menu.getFermentProgressionScaled();
        this.blit(graphics, this.leftPos + PROGRESS_ARROW_X, this.topPos + PROGRESS_ARROW_Y,
                176, 4, progress + 1, PROGRESS_ARROW_HEIGHT);

        if (this.menu.isFermenting()) {
            int bubbleScale = (int) (this.menu.getProgression() / 80) * BUBBLE_HEIGHT % (BUBBLE_HEIGHT + 1);
            this.blit(graphics, this.leftPos + LEFT_BUBBLE_X, this.topPos + BUBBLE_Y - bubbleScale,
                    176, 79 - bubbleScale, BUBBLE_WIDTH, bubbleScale + 1);
            this.blit(graphics, this.leftPos + RIGHT_BUBBLE_X, this.topPos + BUBBLE_Y - bubbleScale,
                    186, 79 - bubbleScale, BUBBLE_WIDTH, bubbleScale + 1);
        }

        int temperature = this.menu.getKegTemperature();
        if (temperature == 1) {
            this.blit(graphics, this.leftPos + COLD_BAR_X, this.topPos + TEMPERATURE_BAR_Y, 176, 0, 8, TEMPERATURE_BAR_HEIGHT);
        }
        if (temperature < 3) {
            this.blit(graphics, this.leftPos + CHILLY_BAR_X, this.topPos + TEMPERATURE_BAR_Y, 184, 0, 9, TEMPERATURE_BAR_HEIGHT);
        }
        if (temperature > 3) {
            this.blit(graphics, this.leftPos + WARM_BAR_X, this.topPos + TEMPERATURE_BAR_Y, 201, 0, 9, TEMPERATURE_BAR_HEIGHT);
        }
        if (temperature == 5) {
            this.blit(graphics, this.leftPos + HOT_BAR_X, this.topPos + TEMPERATURE_BAR_Y, 210, 0, 8, TEMPERATURE_BAR_HEIGHT);
        }

        this.extractTankContents(graphics);
    }

    private void extractTankContents(GuiGraphicsExtractor graphics) {
        AbstractedFluidStack fluidStack = this.menu.kegTank.getAbstractedFluid();
        if (fluidStack.isEmpty() || !this.tankMatchesGhostRecipe(fluidStack)) {
            return;
        }

        if (BnCConfiguration.client().renderFluidInKeg()) {
            BrewinAndChewinClient.getHelper().renderFluidInKeg(fluidStack, graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, 1.0F);
        }

        ItemStack itemDisplay = BnCFluidItemDisplays.getFluidItemDisplay(this.minecraft.level.registryAccess(), fluidStack).copy();
        if (itemDisplay.isEmpty()) {
            return;
        }

        Optional<KegPouringRecipe> pouringRecipe = pouringRecipes().stream()
                .sorted(Comparator.comparing(KegPouringRecipe::isStrict))
                .filter(recipe -> recipe.isStrict()
                        ? ItemStack.isSameItemSameComponents(itemDisplay, recipe.getOutput().create())
                        : ItemStack.isSameItem(itemDisplay, recipe.getOutput().create()))
                .findFirst();
        int pourCount = pouringRecipe
                .map(recipe -> (int) (Math.min(this.menu.kegTank.getFluidCapacity(), fluidStack.amount()) / recipe.getLoaderAmount()))
                .orElse(1);
        itemDisplay.setCount(pourCount);

        graphics.item(itemDisplay, this.leftPos + TANK_ITEM_X, this.topPos + TANK_ITEM_Y);
        graphics.itemDecorations(this.minecraft.font, itemDisplay, this.leftPos + TANK_ITEM_X, this.topPos + TANK_ITEM_Y);
    }

    /**
     * While a recipe is ghosted, the tank only shows fluid when it holds what that recipe asks for -
     * otherwise the player reads the current contents as the ghost's input.
     */
    private boolean tankMatchesGhostRecipe(AbstractedFluidStack fluidStack) {
        KegRecipeDisplay ghost = this.kegRecipeBook.getGhostDisplay();
        if (ghost == null) {
            return true;
        }
        if (ghost.fluidIngredient().isEmpty()) {
            return this.menu.kegTank.isEmpty();
        }
        return ghost.fluidIngredient().get().ingredient().matches(fluidStack);
    }

    private List<KegPouringRecipe> pouringRecipes() {
        return BnCRecipeLookup.all(BnCRecipeLookup.manager(this.minecraft.level), BnCRecipeTypes.KEG_POURING).stream()
                .map(RecipeHolder::value)
                .toList();
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        this.extractTankTooltip(graphics, mouseX, mouseY);
        this.extractTemperatureTooltip(graphics, mouseX, mouseY);
        super.extractTooltip(graphics, mouseX, mouseY);
    }

    /** Called on /reload. */
    public static void clearFluidContainerComponents() {
        FLUID_CONTAINER_COMPONENTS.clear();
    }

    private void extractTankTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!this.isHovering(TANK_X, TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY) || this.menu.kegTank.isEmpty()) {
            return;
        }

        AbstractedFluidStack tankFluid = this.menu.kegTank.getAbstractedFluid();
        KegRecipeDisplay ghost = this.kegRecipeBook.getGhostDisplay();
        if (ghost != null && !this.tankMatchesGhostRecipe(tankFluid)) {
            return;
        }

        Component containerComponent = BnCTextUtils.getTranslation("container.keg.served_in",
                FLUID_CONTAINER_COMPONENTS.computeIfAbsent(tankFluid.fluid(), fluid -> {
                    MutableComponent component = Component.empty().withStyle(ChatFormatting.GRAY);
                    int amountAdded = 0;
                    for (KegPouringRecipe recipe : this.pouringRecipesFor(tankFluid)) {
                        if (amountAdded > 0) {
                            component.append(", ");
                        }
                        component.append(recipe.getContainer().getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
                        ++amountAdded;
                    }
                    return component;
                })).withStyle(ChatFormatting.GRAY);

        Component amount = Component.literal(BnCConfiguration.client().displayUnit()
                .shortFormat(" (%s/%s").formatted(
                        FluidUnit.convert(tankFluid.amount(), FluidUnit.getLoaderUnit(), BnCConfiguration.client().displayUnit()),
                        FluidUnit.convert(this.menu.kegTank.getFluidCapacity(), FluidUnit.getLoaderUnit(), BnCConfiguration.client().displayUnit())) + ")");

        List<Component> components = new ArrayList<>(List.of(
                BrewinAndChewin.getHelper().getFluidDisplayName(tankFluid).copy().append(amount),
                containerComponent));

        BnCConfiguration.Client.DisplaySettings oppositeSetting = BnCConfiguration.client().oppositeFluidDisplay();
        if (oppositeSetting == BnCConfiguration.Client.DisplaySettings.ALWAYS
                || oppositeSetting == BnCConfiguration.Client.DisplaySettings.ADVANCED_TOOLTIPS && this.minecraft.options.advancedItemTooltips) {
            FluidUnit opposite = FluidUnit.getOpposite(BnCConfiguration.client().displayUnit());
            components.add(Component.literal(opposite.shortFormat("%s/%s").formatted(
                            FluidUnit.convert(tankFluid.amount(), FluidUnit.getLoaderUnit(), opposite),
                            FluidUnit.convert(this.menu.kegTank.getFluidCapacity(), FluidUnit.getLoaderUnit(), opposite)))
                    .withStyle(ChatFormatting.GRAY));
        }

        if (this.minecraft.options.advancedItemTooltips) {
            Identifier fluidId = BuiltInRegistries.FLUID.getKey(tankFluid.fluid());
            components.add(Component.literal(fluidId.toString()).withStyle(ChatFormatting.DARK_GRAY));
            if (!tankFluid.components().isEmpty()) {
                components.add(Component.translatable("item.components", tankFluid.components().size()).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        graphics.setComponentTooltipForNextFrame(this.font, components, mouseX, mouseY);
    }

    private List<KegPouringRecipe> pouringRecipesFor(AbstractedFluidStack tankFluid) {
        return this.pouringRecipes().stream()
                .filter(recipe -> recipe.isStrict()
                        ? recipe.getRawFluid().matches(tankFluid)
                        : recipe.getRawFluid().fluid().isSame(tankFluid.fluid()))
                .sorted(Comparator.comparing(recipe -> recipe.getContainer().getHoverName().getString()))
                .toList();
    }

    private void extractTemperatureTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!this.isHovering(COLD_BAR_X, TEMPERATURE_BAR_Y - 1, TEMPERATURE_BAR_WIDTH, 5, mouseX, mouseY)) {
            return;
        }

        KegRecipeDisplay ghost = this.kegRecipeBook.getGhostDisplay();
        if (ghost != null && !KegBlockEntity.isValidTemp(this.menu.getKegTemperature(), ghost.temperature())) {
            return;
        }

        MutableComponent key = switch (this.menu.getKegTemperature()) {
            case 1 -> BnCTextUtils.getTranslation("container.keg.cold");
            case 2 -> BnCTextUtils.getTranslation("container.keg.chilly");
            case 4 -> BnCTextUtils.getTranslation("container.keg.warm");
            case 5 -> BnCTextUtils.getTranslation("container.keg.hot");
            default -> BnCTextUtils.getTranslation("container.keg.normal");
        };
        graphics.setComponentTooltipForNextFrame(this.font, List.of(key), mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
