package umpaz.brewinandchewin.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;

import java.util.List;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import net.minecraft.nbt.CompoundTag;

public class BnCCopyMealFunction extends LootItemConditionalFunction {
    public static final MapCodec<BnCCopyMealFunction> CODEC = RecordCodecBuilder.mapCodec(inst ->
            commonFields(inst).apply(inst, BnCCopyMealFunction::new));

    public static final Identifier ID = BrewinAndChewin.asResource("copy_meal");
    
    private BnCCopyMealFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(BnCCopyMealFunction::new);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockEntity tile = context.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof KegBlockEntity kegTile) {
            TypedEntityData<BlockEntityType<?>> existing = stack.get(DataComponents.BLOCK_ENTITY_DATA);
            CustomData data = kegTile.writeMeal(existing == null ? new CompoundTag() : existing.copyTagWithoutId(), context.getLevel().registryAccess());
            stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(BnCBlockEntityTypes.KEG, data.copyTag()));
        }
        return stack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        Builder() {}

        protected Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new BnCCopyMealFunction(getConditions());
        }
    }
}