package cn.blockforge.generated.generatedmod.registry;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 隐藏的真实诅咒附魔。神秘遗物的千咒卷轴按标准 Enchantment.isCurse() 统计它们，
 * 因而不需要编译期依赖神秘遗物，也不会把外部模组类硬编码进来。
 */
public final class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, GeneratedMod.MOD_ID);

    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_1 = register("gluttony_curse_1");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_2 = register("gluttony_curse_2");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_3 = register("gluttony_curse_3");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_4 = register("gluttony_curse_4");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_5 = register("gluttony_curse_5");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_6 = register("gluttony_curse_6");
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_7 = register("gluttony_curse_7");
    /** 第八条是死亡复活后饱食度仅恢复一格，没有反转条件，因此永远计入千咒卷轴。 */
    public static final RegistryObject<Enchantment> GLUTTONY_CURSE_8 = register("gluttony_curse_8");

    private static RegistryObject<Enchantment> register(String id) {
        return ENCHANTMENTS.register(id, HiddenCurseEnchantment::new);
    }

    private ModEnchantments() {
    }

    private static final class HiddenCurseEnchantment extends Enchantment {
        private HiddenCurseEnchantment() {
            super(Rarity.VERY_RARE, EnchantmentCategory.VANISHABLE, EquipmentSlot.values());
        }

        @Override
        public boolean isCurse() {
            return true;
        }

        @Override
        public boolean isTreasureOnly() {
            return true;
        }

        @Override
        public boolean isDiscoverable() {
            return false;
        }

        @Override
        public boolean canEnchant(net.minecraft.world.item.ItemStack stack) {
            return false;
        }

        @Override
        public boolean isAllowedOnBooks() {
            return false;
        }
    }
}
