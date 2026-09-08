package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.particle.GluttonyFlyParticle;
import cn.blockforge.generated.generatedmod.particle.GluttonyMistParticle;
import cn.blockforge.generated.generatedmod.registry.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientParticles {
    private ClientParticles() {
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.GLUTTONY_MIST.get(), GluttonyMistParticle::mistProvider);
        event.registerSpriteSet(ModParticles.GLUTTONY_RED_AURA.get(), GluttonyMistParticle::auraProvider);
        event.registerSpriteSet(ModParticles.GLUTTONY_FLY.get(), GluttonyFlyParticle::provider);
    }
}
