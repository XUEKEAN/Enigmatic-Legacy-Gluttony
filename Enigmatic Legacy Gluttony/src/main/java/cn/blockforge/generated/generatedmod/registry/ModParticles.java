package cn.blockforge.generated.generatedmod.registry;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GeneratedMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> GLUTTONY_MIST =
            PARTICLES.register("gluttony_mist", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> GLUTTONY_RED_AURA =
            PARTICLES.register("gluttony_red_aura", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> GLUTTONY_FLY =
            PARTICLES.register("gluttony_fly", () -> new SimpleParticleType(false));

    private ModParticles() {
    }
}
