package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.entity.AgingCaskBlockEntity;
import umpaz.brewinandchewin.common.block.entity.BottleRackBlockEntity;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;

public class BnCBlockEntityTypes {
    public static final BlockEntityType<KegBlockEntity> KEG = new BlockEntityType<>(BrewinAndChewin.getHelper().supplyBlockEntity(), BnCBlocks.KEG);
    public static final BlockEntityType<CoasterBlockEntity> COASTER = new BlockEntityType<>(CoasterBlockEntity::new, BnCBlocks.COASTER);
    public static final BlockEntityType<AgingCaskBlockEntity> AGING_CASK = new BlockEntityType<>(BrewinAndChewin.getHelper().supplyAgingCaskBlockEntity(), BnCBlocks.AGING_CASK);
    public static final BlockEntityType<BottleRackBlockEntity> BOTTLE_RACK = new BlockEntityType<>(BottleRackBlockEntity::new, BnCBlocks.BOTTLE_RACK);

    public static void registerAll() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BrewinAndChewin.asResource("keg"), KEG);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BrewinAndChewin.asResource("coaster"), COASTER);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BrewinAndChewin.asResource("aging_cask"), AGING_CASK);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BrewinAndChewin.asResource("bottle_rack"), BOTTLE_RACK);
    }
}
