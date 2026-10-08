package umpaz.brewinandchewin.common.utility;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * 26.1 stores block entity state through {@link net.minecraft.world.level.storage.ValueInput}/
 * {@code ValueOutput} instead of raw {@link CompoundTag}. The keg's inventory/tank abstractions still
 * cross the item/block boundary as tags, so these helpers bridge the two directions.
 */
public final class BnCValueIO {
    private BnCValueIO() {}

    public static ValueInput input(HolderLookup.Provider provider, CompoundTag tag) {
        return TagValueInput.create(ProblemReporter.DISCARDING, provider, tag);
    }

    public static CompoundTag write(ValueIOSerializable serializable, HolderLookup.Provider provider) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, provider);
        serializable.serialize(output);
        return output.buildResult();
    }
}
