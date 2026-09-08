package cn.blockforge.generated.generatedmod.registry;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.item.CreativeGluttonySinItem;
import cn.blockforge.generated.generatedmod.item.GluttonySinItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GeneratedMod.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GeneratedMod.MOD_ID);

    public static final RegistryObject<GluttonySinItem> GLUTTONY_SIN = ITEMS.register("gluttony_sin",
            () -> new GluttonySinItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CreativeGluttonySinItem> GLUTTONY_SIN_CREATIVE = ITEMS.register("gluttony_sin_creative",
            () -> new CreativeGluttonySinItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CreativeModeTab> GLUTTONY_TAB = TABS.register("gluttony_sin",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + GeneratedMod.MOD_ID))
                    .icon(() -> new ItemStack(GLUTTONY_SIN.get()))
                    .displayItems((params, output) -> {
                        output.accept(new ItemStack(GLUTTONY_SIN.get()));
                        output.accept(new ItemStack(GLUTTONY_SIN_CREATIVE.get()));
                    })
                    .build());

    private ModItems() {
    }
}
