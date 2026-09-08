package cn.blockforge.generated.generatedmod.compat;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

/** Farmer's Delight 的可选兼容：复刻其刀击杀额外掉落，不引入编译期依赖。 */
public final class FarmersDelightCompat {
    private static final TagKey<Item> KNIFE_TAG = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("farmersdelight", "tools/knives"));
    private static final ResourceLocation HAM_ID =
            ResourceLocation.fromNamespaceAndPath("farmersdelight", "ham");
    private static final ResourceLocation SMOKED_HAM_ID =
            ResourceLocation.fromNamespaceAndPath("farmersdelight", "smoked_ham");

    private FarmersDelightCompat() {
    }

    public static void handleDevourDrops(LivingDropsEvent event, Player player, boolean wasOnFire) {
        if (!ModList.get().isLoaded("farmersdelight")) {
            return;
        }
        LivingEntity target = event.getEntity();
        EntityType<?> type = target.getType();
        boolean mainhandKnife = isKnife(player.getMainHandItem());
        int looting = mainhandKnife
                ? EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, player.getMainHandItem()) : 0;

        if (type == EntityType.PIG || type == EntityType.HOGLIN) {
            Item ham = getItem(HAM_ID);
            Item smokedHam = getItem(SMOKED_HAM_ID);
            if (ham == null || smokedHam == null) {
                return;
            }
            // Farmer's Delight 原生修改器已经可能加入一份火腿；吞噬路径统一替换，避免重复。
            event.getDrops().removeIf(drop -> drop.getItem().is(ham) || drop.getItem().is(smokedHam));
            float chance = type == EntityType.HOGLIN ? 1.0F : 0.5F;
            chance = Math.min(1.0F, chance + looting * 0.1F);
            if (target.level().getRandom().nextFloat() < chance) {
                Item item = wasOnFire ? smokedHam : ham;
                addDrop(event, target.level(), target, new ItemStack(item, Math.min(3, 1 + looting)));
            }
            return;
        }

        // 主手持刀时 Farmer's Delight 自己会通过全局掉落修改器添加这些物品。
        if (mainhandKnife) {
            return;
        }
        // 吞噬本身视为刀具击杀，因此复刻 scavenging_* 的额外掉落。
        if (type == EntityType.CHICKEN) {
            addDrop(event, target.level(), target, new ItemStack(Items.FEATHER));
        } else if (type == EntityType.COW || type == EntityType.MOOSHROOM
                || type == EntityType.HORSE || type == EntityType.DONKEY
                || type == EntityType.MULE || type == EntityType.LLAMA
                || type == EntityType.TRADER_LLAMA) {
            addDrop(event, target.level(), target, new ItemStack(Items.LEATHER));
        } else if (type == EntityType.RABBIT) {
            addDrop(event, target.level(), target, new ItemStack(Items.RABBIT_HIDE));
        } else if (type == EntityType.SHULKER) {
            addDrop(event, target.level(), target, new ItemStack(Items.SHULKER_SHELL));
        } else if (type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER) {
            addDrop(event, target.level(), target, new ItemStack(Items.STRING));
        }
    }

    private static boolean isKnife(ItemStack stack) {
        return !stack.isEmpty() && stack.is(KNIFE_TAG);
    }

    private static Item getItem(ResourceLocation id) {
        Item item = ForgeRegistries.ITEMS.getValue(id);
        return item == null || item == Items.AIR ? null : item;
    }

    private static void addDrop(LivingDropsEvent event, Level level, LivingEntity target, ItemStack stack) {
        if (!stack.isEmpty()) {
            event.getDrops().add(new ItemEntity(level, target.getX(), target.getY(), target.getZ(), stack));
        }
    }
}
