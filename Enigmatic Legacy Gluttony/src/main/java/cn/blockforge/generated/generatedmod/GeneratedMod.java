package cn.blockforge.generated.generatedmod;

import cn.blockforge.generated.generatedmod.config.ModConfig;
import cn.blockforge.generated.generatedmod.net.DevourPacket;
import cn.blockforge.generated.generatedmod.registry.ModItems;
import cn.blockforge.generated.generatedmod.registry.ModParticles;
import cn.blockforge.generated.generatedmod.registry.ModEffects;
import cn.blockforge.generated.generatedmod.registry.ModEnchantments;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GeneratedMod.MOD_ID)
public class GeneratedMod {
    public static final String MOD_ID = "generated_mod";
    public static final String CHANNEL_VERSION = "1";

    public GeneratedMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModItems.TABS.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModLoadingContext.get().registerConfig(Type.SERVER, ModConfig.SPEC);
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.GLOBAL_SPEC);
        modEventBus.addListener(ModConfig::onConfigLoading);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        DevourPacket.register();
    }
}
