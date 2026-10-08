package umpaz.brewinandchewin.common.loot;

import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import umpaz.brewinandchewin.common.registry.BnCItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Map;

public class BnCInnardsDrops {
    public static final Map<Identifier, float[]> DROPS = Map.of(
            Identifier.withDefaultNamespace("entities/sheep"), new float[]{0.0F, 1.0F},
            Identifier.withDefaultNamespace("entities/pig"), new float[]{0.0F, 2.0F},
            Identifier.withDefaultNamespace("entities/cow"), new float[]{1.0F, 2.0F},
            Identifier.withDefaultNamespace("entities/horse"), new float[]{1.0F, 3.0F},
            Identifier.withDefaultNamespace("entities/hoglin"), new float[]{3.0F, 5.0F});

    public static LootPool.Builder pool(float min, float max) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(BnCItems.INNARDS)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))))
                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER,
                        EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment()
                                .mainhand(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES)))));
    }
}
