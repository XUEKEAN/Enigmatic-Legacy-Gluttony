package cn.blockforge.generated.generatedmod.registry;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GeneratedMod.MOD_ID);
    public static final RegistryObject<MobEffect> DREAD = EFFECTS.register("dread",
            () -> new MobEffect(MobEffectCategory.HARMFUL, 0x280008) {});
    private ModEffects() {}
}
