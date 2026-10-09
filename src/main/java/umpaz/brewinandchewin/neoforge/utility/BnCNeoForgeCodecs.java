package umpaz.brewinandchewin.neoforge.utility;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.neoforged.neoforge.fluids.FluidStack;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.FluidUnit;
import net.minecraft.core.registries.BuiltInRegistries;

public class BnCNeoForgeCodecs {
    public static final Codec<AbstractedFluidStack> FLUID_STACK_WRAPPER = RecordCodecBuilder.create(inst -> inst.group(
                    BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("id").forGetter(stack -> stack.fluid().builtInRegistryHolder()),
                    Codec.LONG.validate(l -> {
                        if (l < 1)
                            return DataResult.error(() -> "Fluid amount must be positive");
                        return DataResult.success(l);
                    }).fieldOf("amount").forGetter(AbstractedFluidStack::amount),
                    FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.MILLIBUCKET).forGetter(AbstractedFluidStack::unit),
                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(fluidStack -> fluidStack.components() instanceof PatchedDataComponentMap patched ? patched.asPatch() : DataComponentPatch.EMPTY))
            .apply(inst, (t1, t2, t3, t4) ->
                    // The FluidStack is built lazily: this codec also runs while datapacks are
                    // parsed, which is before fluid component initializers have been applied.
                    new AbstractedFluidStack(t1.value(), t2, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, t4), t3, null)));

}
