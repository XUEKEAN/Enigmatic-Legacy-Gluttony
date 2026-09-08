package cn.blockforge.generated.generatedmod.devour;

import cn.blockforge.generated.generatedmod.config.ModConfig;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

/** 玩家永久吞噬数据、七条反转状态和临时成长的唯一存储入口。 */
public final class DevourData {
    public static final String TAG_ROOT = "gluttony_sin";
    public static final String TAG_DEVOURED = "devoured";
    public static final String TAG_UNIQUE = "unique_count";
    public static final String TAG_MAX_HP_BONUS = "max_hp_bonus";
    /** 第一条反转后已经同步到当前生命值的吞噬生命成长，避免每 tick 重复回血。 */
    public static final String TAG_PROTECTED_CURRENT_HP = "protected_current_hp";
    /** 上一次独立生命同步结束时的当前生命值，用于识别外部属性夹紧。 */
    public static final String TAG_LAST_PROTECTED_CURRENT_HP = "last_protected_current_hp";
    /** 新版当前生命保护已经初始化；旧存档首次升级时以独立最大生命解除历史锁定值。 */
    public static final String TAG_PROTECTED_HEALTH_INITIALIZED = "protected_health_initialized";
    /** 当前生命保护算法版本；用于把上一版遗留的 30 点锁血目标迁移掉。 */
    public static final String TAG_PROTECTED_HEALTH_VERSION = "protected_health_version";
    private static final int PROTECTED_HEALTH_VERSION = 2;
    private static final int STRONG_BODY_WINDOW_TICKS = 200;
    private static final int STRONG_BODY_TRIGGER_CROUCHES = 15;
    public static final String TAG_ATTACK_BONUS = "attack_bonus";
    public static final String TAG_SPEED_PENALTY = "speed_penalty";
    /** 旧版本的总净化标记，读取时迁移为诅咒2已反转，避免旧存档丢失移速免疫。 */
    public static final String TAG_CURED = "cured";
    public static final String TAG_COOLDOWN_UNTIL = "cooldown_until";
    public static final String TAG_FEAST = "endless_feast";
    public static final String TAG_FEAST_INDEX = "feast_index";
    public static final String TAG_FEAST_LAST = "feast_last_tick";
    public static final String TAG_CROUCH_COUNT = "crouch_count";
    public static final String TAG_LAST_CROUCH = "last_crouch";
    public static final String TAG_STRONG_STACKS = "strong_body_stacks";
    public static final String TAG_STRONG_DURATION = "strong_body_duration";
    public static final String TAG_STRONG_HP = "strong_body_hp";
    public static final String TAG_STRONG_ATTACK = "strong_body_attack";
    public static final String TAG_STRONG_SPEED = "strong_body_speed";
    public static final String TAG_STRONG_ATTACK_SPEED = "strong_body_attack_speed";
    /** 诅咒2反转后，强健身体叠加使用的10秒起身窗口。 */
    public static final String TAG_STRONG_WINDOW_COUNT = "strong_body_window_count";
    public static final String TAG_STRONG_WINDOW_TICKS = "strong_body_window_ticks";
    /** 仅通过状态同步包发送给客户端，用于显示可配置的强健身体数值。 */
    public static final String TAG_STRONG_ATTACK_PER_RISE = "strong_body_attack_per_rise";
    public static final String TAG_STRONG_HP_PER_RISE = "strong_body_hp_per_rise";
    public static final String TAG_STRONG_SPEED_PER_RISE = "strong_body_speed_per_rise";
    public static final String TAG_STRONG_ATTACK_SPEED_PER_RISE = "strong_body_attack_speed_per_rise";
    public static final String TAG_STRONG_DURATION_PER_RISE = "strong_body_duration_per_rise";
    public static final String TAG_STRONG_REVERSE_CROUCHES = "strong_body_reverse_crouches";
    public static final String TAG_PICKY_REVERSE_FOODS = "picky_reverse_foods";
    public static final String TAG_FULL_HUNGER_MINUTES = "full_hunger_minutes";
    public static final String TAG_FOOD_KINDS = "picky_food_kinds";
    public static final String TAG_FULL_HUNGER_TICKS = "full_hunger_ticks";
    public static final String TAG_HUNGER_GRACE_TICKS = "hunger_grace_ticks";
    /** 首次登录赠送普通版物品的单玩家持久化标记。 */
    public static final String TAG_STARTER_ITEM_GIVEN = "starter_item_given";

    private static final String[] REVERSED_TAGS = {
            "curse1_reversed", "curse2_reversed", "curse3_reversed", "curse4_reversed",
            "curse5_reversed", "curse6_reversed", "curse7_reversed"
    };

    public static final UUID CURSE_HEALTH_UUID = UUID.fromString("1f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a5b");
    public static final UUID HEALTH_BONUS_UUID = UUID.fromString("2f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a5c");
    public static final UUID CURSE_ATTACK_UUID = UUID.fromString("3f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a5d");
    public static final UUID ATTACK_BONUS_UUID = UUID.fromString("4f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a5e");
    public static final UUID SPEED_CURSE_UUID = UUID.fromString("5f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a5f");
    public static final UUID DREAD_SPEED_UUID = UUID.fromString("0f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a6f");
    public static final UUID FULL_HUNGER_SPEED_UUID = UUID.fromString("6f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a60");
    public static final UUID FULL_HUNGER_ATTACK_SPEED_UUID = UUID.fromString("7f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a61");
    public static final UUID EMPTY_HUNGER_HEALTH_UUID = UUID.fromString("8f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a62");
    public static final UUID EMPTY_HUNGER_ATTACK_UUID = UUID.fromString("9f3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a63");
    public static final UUID STRONG_BODY_HEALTH_UUID = UUID.fromString("af3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a64");
    public static final UUID STRONG_BODY_ATTACK_UUID = UUID.fromString("bf3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a65");
    public static final UUID STRONG_BODY_SPEED_UUID = UUID.fromString("cf3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a66");
    public static final UUID STRONG_BODY_ATTACK_SPEED_UUID = UUID.fromString("df3b7c9e-5a2d-4f0e-9b8a-6c4d2e1f0a67");

    private DevourData() {
    }

    public static CompoundTag data(Player player) {
        CompoundTag root = player.getPersistentData();
        CompoundTag persisted = root.getCompound(Player.PERSISTED_NBT_TAG);
        if (!root.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
            root.put(Player.PERSISTED_NBT_TAG, persisted);
        }
        CompoundTag tag = persisted.getCompound(TAG_ROOT);
        if (!persisted.contains(TAG_ROOT, Tag.TAG_COMPOUND)) {
            persisted.put(TAG_ROOT, tag);
        }
        // 旧版只有一个 cured 标记，唯一能安全迁移的是它原本代表的移速诅咒免疫。
        if (tag.getBoolean(TAG_CURED) && !tag.getBoolean(REVERSED_TAGS[1])) {
            tag.putBoolean(REVERSED_TAGS[1], true);
        }
        return tag;
    }

    public static long getCooldownUntil(Player player) {
        return data(player).getLong(TAG_COOLDOWN_UNTIL);
    }

    public static void setCooldownUntil(Player player, long gameTime) {
        data(player).putLong(TAG_COOLDOWN_UNTIL, gameTime);
    }

    public static void startEndlessFeast(Player player, List<LivingEntity> targets) {
        CompoundTag tag = data(player);
        ListTag list = new ListTag();
        for (LivingEntity entity : targets) {
            list.add(StringTag.valueOf(entity.getUUID().toString()));
        }
        tag.put(TAG_FEAST, list);
        tag.putInt(TAG_FEAST_INDEX, 0);
        tag.putLong(TAG_FEAST_LAST, player.level().getGameTime());
    }

    public record DevourResult(boolean firstTime, boolean growthGranted, boolean boss, boolean elite,
                               double healthBonus, double attackBonus) {
    }

    public static DevourResult devour(Player player, ResourceLocation entityId, boolean boss, boolean elite,
                                      boolean applyCurses) {
        return devour(player, entityId, boss, elite, applyCurses, false);
    }

    /**
     * 记录一次吞噬。创造版把“重复吞噬只回血”改为每次都追加成长，且仍然只把首次种类计入 unique_count。
     */
    public static DevourResult devour(Player player, ResourceLocation entityId, boolean boss, boolean elite,
                                      boolean applyCurses, boolean creative) {
        CompoundTag tag = data(player);
        CompoundTag devoured = tag.getCompound(TAG_DEVOURED);
        if (!tag.contains(TAG_DEVOURED, Tag.TAG_COMPOUND)) {
            tag.put(TAG_DEVOURED, devoured);
        }
        String key = entityId.toString();
        int count = devoured.getInt(key);
        boolean firstTime = count == 0;
        boolean growthGranted = firstTime || creative;
        double healthBonus = 0.0;
        double attackBonus = 0.0;
        if (growthGranted) {
            double growthMultiplier = isReversed(player, 1, creative) ? 2.0 : 1.0;
            healthBonus = (ModConfig.BASE_HEALTH_BONUS.get()
                    + (boss ? ModConfig.BOSS_HEALTH_BONUS.get() : 0.0)
                    + (elite ? ModConfig.ELITE_HEALTH_BONUS.get() : 0.0)) * growthMultiplier;
            attackBonus = (ModConfig.BASE_ATTACK_BONUS.get()
                    + (boss ? ModConfig.BOSS_ATTACK_BONUS.get() : 0.0)
                    + (elite ? ModConfig.ELITE_ATTACK_BONUS.get() : 0.0)) * growthMultiplier;
            devoured.putInt(key, count + 1);
            if (firstTime) {
                tag.putInt(TAG_UNIQUE, tag.getInt(TAG_UNIQUE) + 1);
                if (!isReversed(player, 2, creative) && applyCurses) {
                    tag.putDouble(TAG_SPEED_PENALTY, tag.getDouble(TAG_SPEED_PENALTY)
                            + ModConfig.SPEED_PENALTY_PER_UNIQUE.get());
                }
            }
            tag.putDouble(TAG_MAX_HP_BONUS, tag.getDouble(TAG_MAX_HP_BONUS) + healthBonus);
            tag.putDouble(TAG_ATTACK_BONUS, tag.getDouble(TAG_ATTACK_BONUS) + attackBonus);
        } else {
            devoured.putInt(key, count + 1);
            player.heal(ModConfig.SAME_ENTITY_HEAL.get().floatValue());
        }
        tag.put(TAG_DEVOURED, devoured);
        applyAttributes(player, applyCurses || creative, creative);
        return new DevourResult(firstTime, growthGranted, boss, elite, healthBonus, attackBonus);
    }

    public static int getUniqueCount(Player player) {
        return data(player).getInt(TAG_UNIQUE);
    }

    public static double getMaxHpBonus(Player player) {
        return data(player).getDouble(TAG_MAX_HP_BONUS);
    }

    public static double getAttackBonus(Player player) {
        return data(player).getDouble(TAG_ATTACK_BONUS);
    }

    public static double getSpeedPenalty(Player player) {
        return data(player).getDouble(TAG_SPEED_PENALTY);
    }

    public static boolean isCured(Player player) {
        return isReversed(player, 2);
    }

    public static boolean isReversed(Player player, int curse) {
        return curse >= 1 && curse <= 7 && data(player).getBoolean(REVERSED_TAGS[curse - 1]);
    }

    /** 创造版物品自带七条反转，不把创造版状态写入普通版玩家数据。 */
    public static boolean isReversed(Player player, int curse, boolean creative) {
        return creative || isReversed(player, curse);
    }

    public static void reverse(Player player, int curse) {
        if (curse < 1 || curse > 7 || isReversed(player, curse)) {
            return;
        }
        CompoundTag tag = data(player);
        tag.putBoolean(REVERSED_TAGS[curse - 1], true);
        if (curse == 1) {
            // 反转包含历史吞噬成长：把旧成长一次性翻倍，后续成长在 devour() 中也乘二。
            tag.putDouble(TAG_MAX_HP_BONUS, tag.getDouble(TAG_MAX_HP_BONUS) * 2.0);
            tag.putDouble(TAG_ATTACK_BONUS, tag.getDouble(TAG_ATTACK_BONUS) * 2.0);
        }
        applyAttributes(player, true);
    }

    /**
     * 管理员测试指令使用：一次性完成七条反转任务，并把可显示的任务进度补到目标值。
     * 不伪造吞噬成长，也不额外赠送强健身体的临时叠层；反转后的效果按正常规则生效。
     */
    public static void completeAllReversals(Player player) {
        CompoundTag tag = data(player);
        tag.putInt(TAG_CROUCH_COUNT, ModConfig.STRONG_BODY_REVERSE_CROUCHES.get());
        tag.putLong(TAG_FULL_HUNGER_TICKS,
                (long) ModConfig.GLUTTONY_FULL_HUNGER_MINUTES.get() * 60L * 20L);

        ListTag foods = tag.getList(TAG_FOOD_KINDS, Tag.TAG_STRING);
        int requiredFoods = ModConfig.PICKY_REVERSE_FOOD_COUNT.get();
        for (int i = foods.size(); i < requiredFoods; i++) {
            foods.add(StringTag.valueOf("generated_mod:command_food_" + i));
        }
        tag.put(TAG_FOOD_KINDS, foods);

        for (int curse = 1; curse <= 7; curse++) {
            reverse(player, curse);
        }
        applyAttributes(player, true);
    }

    public static int getCrouchCount(Player player) {
        return data(player).getInt(TAG_CROUCH_COUNT);
    }

    public static int getFoodKindCount(Player player) {
        return data(player).getList(TAG_FOOD_KINDS, Tag.TAG_STRING).size();
    }

    public static long getFullHungerTicks(Player player) {
        return data(player).getLong(TAG_FULL_HUNGER_TICKS);
    }

    public static int getStrongDuration(Player player) {
        return data(player).getInt(TAG_STRONG_DURATION);
    }

    /** 记录一次蹲下后的起身；反转前用于计数，反转后先完成10秒内15次判定，再开始叠加强健身体。 */
    public static boolean onCrouchRise(Player player) {
        return onCrouchRise(player, false);
    }

    public static boolean onCrouchRise(Player player, boolean creative) {
        CompoundTag tag = data(player);
        tag.putInt(TAG_CROUCH_COUNT, tag.getInt(TAG_CROUCH_COUNT) + 1);
        boolean newlyReversed = false;
        if (!isReversed(player, 2, creative)
                && tag.getInt(TAG_CROUCH_COUNT) >= ModConfig.STRONG_BODY_REVERSE_CROUCHES.get()) {
            reverse(player, 2);
            newlyReversed = true;
        }
        if (isReversed(player, 2, creative)) {
            int windowTicks = tag.getInt(TAG_STRONG_WINDOW_TICKS);
            int windowCount = tag.getInt(TAG_STRONG_WINDOW_COUNT);
            if (windowTicks <= 0) {
                windowCount = 0;
            }
            windowCount++;
            if (windowCount == 1) {
                windowTicks = STRONG_BODY_WINDOW_TICKS;
            }
            tag.putInt(TAG_STRONG_WINDOW_COUNT, windowCount);
            tag.putInt(TAG_STRONG_WINDOW_TICKS, windowTicks);
            // 前15次只用于满足触发条件，不提供属性；从第16次起才叠加强健身体。
            if (windowCount <= STRONG_BODY_TRIGGER_CROUCHES) {
                applyAttributes(player, true, creative);
                return newlyReversed;
            }
            int max = ModConfig.STRONG_BODY_MAX_STACKS.get();
            int stacks = tag.getInt(TAG_STRONG_STACKS);
            if (max == 0 || stacks < max) {
                tag.putInt(TAG_STRONG_STACKS, stacks + 1);
                tag.putDouble(TAG_STRONG_HP, tag.getDouble(TAG_STRONG_HP) + ModConfig.STRONG_BODY_HEALTH_PER_RISE.get());
                tag.putDouble(TAG_STRONG_ATTACK, tag.getDouble(TAG_STRONG_ATTACK) + ModConfig.STRONG_BODY_ATTACK_PER_RISE.get());
                tag.putDouble(TAG_STRONG_SPEED, tag.getDouble(TAG_STRONG_SPEED) + ModConfig.STRONG_BODY_SPEED_PER_RISE.get());
                tag.putDouble(TAG_STRONG_ATTACK_SPEED,
                        tag.getDouble(TAG_STRONG_ATTACK_SPEED) + ModConfig.STRONG_BODY_ATTACK_SPEED_PER_RISE.get());
                int duration = tag.getInt(TAG_STRONG_DURATION)
                        + (int) Math.round(ModConfig.STRONG_BODY_DURATION_SECONDS_PER_RISE.get() * 20.0);
                tag.putInt(TAG_STRONG_DURATION, duration);
            }
        }
        applyAttributes(player, true, creative);
        return newlyReversed;
    }

    /** 每秒推进强健身体的10秒窗口，超时后清零窗口次数。 */
    public static void tickStrongBodyWindow(Player player) {
        CompoundTag tag = data(player);
        int ticks = tag.getInt(TAG_STRONG_WINDOW_TICKS);
        if (ticks <= 0) {
            return;
        }
        ticks--;
        tag.putInt(TAG_STRONG_WINDOW_TICKS, ticks);
        if (ticks == 0) {
            tag.putInt(TAG_STRONG_WINDOW_COUNT, 0);
        }
    }

    /** 记录满饱食度进度，掉食后给两秒补救窗口，超时清零。 */
    public static boolean tickFullHunger(Player player) {
        CompoundTag tag = data(player);
        int food = player.getFoodData().getFoodLevel();
        boolean newlyReversed = false;
        if (food >= 20) {
            tag.putInt(TAG_HUNGER_GRACE_TICKS, 0);
            long ticks = tag.getLong(TAG_FULL_HUNGER_TICKS) + 1L;
            tag.putLong(TAG_FULL_HUNGER_TICKS, ticks);
            long required = (long) ModConfig.GLUTTONY_FULL_HUNGER_MINUTES.get() * 60L * 20L;
            if (!isReversed(player, 4) && ticks >= required) {
                reverse(player, 4);
                newlyReversed = true;
            }
        } else if (tag.getLong(TAG_FULL_HUNGER_TICKS) > 0L) {
            int grace = tag.getInt(TAG_HUNGER_GRACE_TICKS);
            if (grace == 0) {
                grace = 40;
            } else {
                grace--;
            }
            tag.putInt(TAG_HUNGER_GRACE_TICKS, grace);
            if (grace <= 0) {
                tag.putLong(TAG_FULL_HUNGER_TICKS, 0L);
            }
        }
        return newlyReversed;
    }

    public static boolean recordPickyFood(Player player, ResourceLocation itemId) {
        CompoundTag tag = data(player);
        ListTag foods = tag.getList(TAG_FOOD_KINDS, Tag.TAG_STRING);
        boolean known = false;
        for (int i = 0; i < foods.size(); i++) {
            if (itemId.toString().equals(foods.getString(i))) {
                known = true;
                break;
            }
        }
        if (!known) {
            foods.add(StringTag.valueOf(itemId.toString()));
            tag.put(TAG_FOOD_KINDS, foods);
        }
        if (!isReversed(player, 3) && foods.size() >= ModConfig.PICKY_REVERSE_FOOD_COUNT.get()) {
            reverse(player, 3);
            return true;
        }
        return false;
    }

    public static void applyAttributes(Player player) {
        applyAttributes(player, true, false);
    }

    public static void applyAttributes(Player player, boolean applyCurses) {
        applyAttributes(player, applyCurses, false);
    }

    public static void applyAttributes(Player player, boolean applyCurses, boolean creative) {
        if (player.level().isClientSide) {
            return;
        }
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attack = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        if (health == null || attack == null || speed == null || attackSpeed == null) {
            return;
        }
        // 在潘多拉等外部限制器刷新 MAX_HEALTH 之前记住本 tick 入口值。
        // 若本次刷新后最大生命恢复、但当前生命被一起夹低，后面只补回这段差值。
        double maxHealthBefore = player.getMaxHealth();
        CompoundTag tag = data(player);
        boolean reversed1 = isReversed(player, 1, creative);
        boolean reversed2 = isReversed(player, 2, creative);
        boolean reversed3 = isReversed(player, 3, creative);
        boolean reversed4 = isReversed(player, 4, creative);
        boolean curse1 = applyCurses && !reversed1;
        boolean curse2 = applyCurses && !reversed2;
        boolean curse3 = applyCurses && !reversed3;
        boolean curse4 = applyCurses && !reversed4;
        boolean full = player.getFoodData().getFoodLevel() >= 20;
        boolean empty = player.getFoodData().getFoodLevel() <= 0;
        double otherHealth = health.getBaseValue();
        double otherAttack = attack.getBaseValue();
        for (AttributeModifier modifier : health.getModifiers()) {
            if (!modifier.getId().equals(HEALTH_BONUS_UUID) && !modifier.getId().equals(CURSE_HEALTH_UUID)
                    && !modifier.getId().equals(EMPTY_HUNGER_HEALTH_UUID)
                    && !modifier.getId().equals(STRONG_BODY_HEALTH_UUID)) {
                otherHealth += modifier.getAmount();
            }
        }
        for (AttributeModifier modifier : attack.getModifiers()) {
            if (!modifier.getId().equals(ATTACK_BONUS_UUID) && !modifier.getId().equals(CURSE_ATTACK_UUID)
                    && !modifier.getId().equals(EMPTY_HUNGER_ATTACK_UUID)
                    && !modifier.getId().equals(STRONG_BODY_ATTACK_UUID)) {
                otherAttack += modifier.getAmount();
            }
        }
        setModifier(health, CURSE_HEALTH_UUID, curse1 ? -otherHealth * 0.5 : 0.0, AttributeModifier.Operation.ADDITION);
        boolean protectDevourHealth = applyCurses && reversed1;
        if (protectDevourHealth) {
            // 只有第一条反转后，吞噬生命才脱离外部模组的生命限制。
            // 潘多拉之咒的血肉效果会把所有正向 ADDITION 汇总后生成
            // flesh_negate_add；这里排除本模组的独立成长，但继续保留它
            // 对其他模组生命加成的原本限制。
            repairHealthLimiters(health);
            setModifier(health, HEALTH_BONUS_UUID,
                    protectedHealthBonus(health, tag.getDouble(TAG_MAX_HP_BONUS)),
                    AttributeModifier.Operation.ADDITION);
            // MoonStone 的倾斜异果锁血效果需要继续保留；这里只保护本模组的吞噬成长，
            // 由 DevourEvents 在效果 tick 后把被额外夹低的当前生命恢复到本 tick 应有值。
            syncProtectedCurrentHealth(player, tag, maxHealthBefore);
        } else {
            // 反转前按普通生命加成处理，外部减益可以正常影响它。
            setModifier(health, HEALTH_BONUS_UUID, tag.getDouble(TAG_MAX_HP_BONUS),
                    AttributeModifier.Operation.ADDITION);
        }
        setModifier(attack, CURSE_ATTACK_UUID, curse1 ? -otherAttack * 0.5 : 0.0, AttributeModifier.Operation.ADDITION);
        setModifier(attack, ATTACK_BONUS_UUID, tag.getDouble(TAG_ATTACK_BONUS),
                AttributeModifier.Operation.ADDITION);
        setModifier(speed, SPEED_CURSE_UUID,
                curse2 ? -tag.getDouble(TAG_SPEED_PENALTY) * 0.1 : 0.0, AttributeModifier.Operation.ADDITION);
        boolean dread = applyCurses && !isReversed(player, 5, creative)
                && player.hasEffect(cn.blockforge.generated.generatedmod.registry.ModEffects.DREAD.get());
        setModifier(speed, DREAD_SPEED_UUID, dread ? -0.50 : 0.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(speed, FULL_HUNGER_SPEED_UUID,
                applyCurses && full ? (creative && reversed3 ? 0.30 : curse3 ? -0.30 : 0.0) : 0.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(attackSpeed, FULL_HUNGER_ATTACK_SPEED_UUID,
                applyCurses && full ? (creative && reversed3 ? 0.30 : curse3 ? -0.30 : 0.0) : 0.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(health, EMPTY_HUNGER_HEALTH_UUID,
                applyCurses && empty ? (creative && reversed4 ? 0.30 : curse4 ? 0.30 : -0.30) : 0.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(attack, EMPTY_HUNGER_ATTACK_UUID,
                applyCurses && empty ? (creative && reversed4 ? 0.30 : curse4 ? 0.30 : -0.30) : 0.0,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        boolean strong = applyCurses && reversed2 && tag.getInt(TAG_STRONG_DURATION) > 0;
        setModifier(health, STRONG_BODY_HEALTH_UUID, strong ? tag.getDouble(TAG_STRONG_HP) : 0.0,
                AttributeModifier.Operation.ADDITION);
        setModifier(attack, STRONG_BODY_ATTACK_UUID, strong ? tag.getDouble(TAG_STRONG_ATTACK) : 0.0,
                AttributeModifier.Operation.ADDITION);
        setModifier(speed, STRONG_BODY_SPEED_UUID, strong ? tag.getDouble(TAG_STRONG_SPEED) : 0.0,
                AttributeModifier.Operation.ADDITION);
        setModifier(attackSpeed, STRONG_BODY_ATTACK_SPEED_UUID, strong ? tag.getDouble(TAG_STRONG_ATTACK_SPEED) : 0.0,
                AttributeModifier.Operation.ADDITION);
    }

    public static void tickStrongBody(Player player) {
        CompoundTag tag = data(player);
        if (tag.getInt(TAG_STRONG_DURATION) > 0) {
            tag.putInt(TAG_STRONG_DURATION, tag.getInt(TAG_STRONG_DURATION) - 1);
            if (tag.getInt(TAG_STRONG_DURATION) == 0) {
                tag.putDouble(TAG_STRONG_HP, 0.0);
                tag.putDouble(TAG_STRONG_ATTACK, 0.0);
                tag.putDouble(TAG_STRONG_SPEED, 0.0);
                tag.putDouble(TAG_STRONG_ATTACK_SPEED, 0.0);
            }
        }
    }

    /** 死亡时清空强健身体的临时叠层、剩余时间和对应属性修饰符。 */
    public static void clearStrongBody(Player player) {
        CompoundTag tag = data(player);
        tag.putInt(TAG_STRONG_STACKS, 0);
        tag.putInt(TAG_STRONG_DURATION, 0);
        tag.putInt(TAG_STRONG_WINDOW_COUNT, 0);
        tag.putInt(TAG_STRONG_WINDOW_TICKS, 0);
        tag.putDouble(TAG_STRONG_HP, 0.0);
        tag.putDouble(TAG_STRONG_ATTACK, 0.0);
        tag.putDouble(TAG_STRONG_SPEED, 0.0);
        tag.putDouble(TAG_STRONG_ATTACK_SPEED, 0.0);
        removeAttributes(player);
    }

    public static void removeAttributes(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        AttributeInstance[] instances = {
                player.getAttribute(Attributes.MAX_HEALTH), player.getAttribute(Attributes.ATTACK_DAMAGE),
                player.getAttribute(Attributes.MOVEMENT_SPEED), player.getAttribute(Attributes.ATTACK_SPEED)
        };
        UUID[] ids = {
                CURSE_HEALTH_UUID, HEALTH_BONUS_UUID, CURSE_ATTACK_UUID, ATTACK_BONUS_UUID, SPEED_CURSE_UUID,
                DREAD_SPEED_UUID, FULL_HUNGER_SPEED_UUID, FULL_HUNGER_ATTACK_SPEED_UUID, EMPTY_HUNGER_HEALTH_UUID,
                EMPTY_HUNGER_ATTACK_UUID, STRONG_BODY_HEALTH_UUID, STRONG_BODY_ATTACK_UUID,
                STRONG_BODY_SPEED_UUID, STRONG_BODY_ATTACK_SPEED_UUID
        };
        for (AttributeInstance instance : instances) {
            if (instance != null) {
                for (UUID id : ids) {
                    instance.removeModifier(id);
                }
            }
        }
    }

    private static void setModifier(AttributeInstance instance, UUID uuid, double amount,
                                     AttributeModifier.Operation operation) {
        AttributeModifier current = instance.getModifier(uuid);
        if (amount == 0.0) {
            if (current != null) {
                instance.removeModifier(uuid);
            }
            return;
        }
        if (current == null || Double.compare(current.getAmount(), amount) != 0 || current.getOperation() != operation) {
            instance.removeModifier(uuid);
            instance.addTransientModifier(new AttributeModifier(uuid, "gluttony_sin", amount, operation));
        }
    }

    /**
     * 计算普通 ADDITION 修饰符应使用的补偿量，使吞噬成长在外部乘区之后仍保持固定数值。
     * 例如第三方模组把最大生命乘以 0.5 时，这里把吞噬修饰符除以 0.5，最终仍保留完整吞噬生命。
     */
    private static double protectedHealthBonus(AttributeInstance health, double bonus) {
        if (bonus == 0.0) {
            return 0.0;
        }
        double multiplier = 1.0;
        for (AttributeModifier modifier : health.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE)) {
            if (!modifier.getId().equals(HEALTH_BONUS_UUID)) {
                multiplier *= 1.0 + modifier.getAmount();
            }
        }
        for (AttributeModifier modifier : health.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            if (!modifier.getId().equals(HEALTH_BONUS_UUID)) {
                multiplier *= 1.0 + modifier.getAmount();
            }
        }
        if (Math.abs(multiplier) < 0.000001) {
            return bonus;
        }
        return bonus / multiplier;
    }

    /**
     * 第一条反转时把历史吞噬成长同步进当前生命值，之后只对新增成长补差值。
     * 不能把当前生命值直接写回持久数据，否则第三方锁血效果会把锁定值保存成正常目标值。
     */
    public static void syncProtectedCurrentHealth(Player player, CompoundTag tag, double maxHealthBefore) {
                                                    
        double totalBonus = tag.getDouble(TAG_MAX_HP_BONUS);
        double appliedBonus = tag.getDouble(TAG_PROTECTED_CURRENT_HP);
        double maxHealthAfter = player.getMaxHealth();
        boolean hasLastHealth = tag.contains(TAG_LAST_PROTECTED_CURRENT_HP, Tag.TAG_DOUBLE);
        boolean initialized = tag.getBoolean(TAG_PROTECTED_HEALTH_INITIALIZED);
        int healthVersion = tag.getInt(TAG_PROTECTED_HEALTH_VERSION);
        boolean migrated = !initialized || healthVersion < PROTECTED_HEALTH_VERSION;
        double desiredHealth;
        if (migrated) {
            // 迁移旧版本：上一版可能把 MoonStone 锁定后的 30 点写进独立目标值。
            // 以当前独立最大生命重新建立目标，只绕过当前生命锁定，不移除 life_apple。
            desiredHealth = maxHealthAfter;
            tag.putBoolean(TAG_PROTECTED_HEALTH_INITIALIZED, true);
            tag.putInt(TAG_PROTECTED_HEALTH_VERSION, PROTECTED_HEALTH_VERSION);
        } else if (!hasLastHealth) {
            // 更早旧存档没有独立当前生命目标值时，以当前独立最大生命作为起点。
            desiredHealth = maxHealthAfter;
        } else {
            double growthDelta = Math.max(0.0, totalBonus - appliedBonus);
            double previousDesired = tag.getDouble(TAG_LAST_PROTECTED_CURRENT_HP);
            // 只把本模组新增加的成长加入独立生命目标；第三方属性变化不能放大该目标。
            desiredHealth = previousDesired + growthDelta;
            // 真实治疗可以提高独立生命目标，但锁血造成的较低当前值不能降低它。
            if (player.getHealth() > previousDesired) {
                desiredHealth = Math.max(desiredHealth, player.getHealth());
            }
        }
        desiredHealth = Math.max(0.0, Math.min(maxHealthAfter, desiredHealth));
        // 普通 tick 不在这里回血；PlayerTick.START 负责恢复锁血造成的夹低，
        // 新增成长仍在本次调用中补入当前生命。
        if ((migrated || !hasLastHealth || totalBonus > appliedBonus) && desiredHealth > player.getHealth()) {
            player.setHealth((float) desiredHealth);
        }
        tag.putDouble(TAG_PROTECTED_CURRENT_HP, totalBonus);
        tag.putDouble(TAG_LAST_PROTECTED_CURRENT_HP, desiredHealth);
    }

    /**
     * 修复采用“扫描属性后添加反向修饰符”实现的第三方生命限制器。
     *
     * 潘多拉之咒 2.4.26 的血肉效果使用 flesh_negate_add：它会抵消当前
     * MAX_HEALTH 上所有正向 ADDITION。这里按同样的计算原则重新生成该
     * 反向修饰符，但排除 HEALTH_BONUS_UUID，保证吞噬成长不会被当成普通
     * 外部加成削掉，同时继续保留限制器对其他模组加成的原本效果。
     */
    private static void repairHealthLimiters(AttributeInstance health) {
        for (AttributeModifier limiter : List.copyOf(health.getModifiers(AttributeModifier.Operation.ADDITION))) {
            if (!isAdditionLimiter(limiter)) {
                continue;
            }
            double externalPositiveAdditions = 0.0;
            for (AttributeModifier modifier : health.getModifiers(AttributeModifier.Operation.ADDITION)) {
                if (!modifier.getId().equals(HEALTH_BONUS_UUID)
                        && !isAdditionLimiter(modifier) && modifier.getAmount() > 0.0) {
                    externalPositiveAdditions += modifier.getAmount();
                }
            }
            double expected = -externalPositiveAdditions;
            if (Math.abs(expected) < 0.000001) {
                health.removeModifier(limiter.getId());
            } else if (Double.compare(limiter.getAmount(), expected) != 0) {
                health.removeModifier(limiter.getId());
                health.addTransientModifier(new AttributeModifier(limiter.getId(), limiter.getName(), expected,
                        AttributeModifier.Operation.ADDITION));
            }
        }
    }

    /**
     * 第一条诅咒反转后的 MoonStone 兼容：倾斜异果会给玩家添加
     * moonstone:life_apple（锁血）负面效果。该效果不改变最大生命属性，
     * 但会在效果持续期间反复改写当前生命值，因此仅修复 MAX_HEALTH 修饰符
     * 不足以解除影响。这里按注册 ID 精确移除，不影响其他模组的同名效果。
     */
    /**
     * 判断指定效果是否为 MoonStone 倾斜异果对应的锁血效果。
     * 只按完整注册 ID 判断，避免误伤其他模组可能注册的同名效果。
     */
    public static boolean isMoonstoneLifeLock(MobEffect effect) {
        MobEffect lifeApple = ForgeRegistries.MOB_EFFECTS.getValue(
                ResourceLocation.fromNamespaceAndPath("moonstone", "life_apple"));
        return lifeApple != null && lifeApple == effect;
    }

    /**
     * 清除旧存档或兼容层已经留下的 MoonStone 锁血效果。
     * 新的效果会在 Forge 的 Applicable 事件中直接被拦截，正常情况下这里不会反复执行。
     */
    public static boolean removeMoonstoneLifeLock(Player player) {
        MobEffect lifeApple = ForgeRegistries.MOB_EFFECTS.getValue(
                ResourceLocation.fromNamespaceAndPath("moonstone", "life_apple"));
        return lifeApple != null && player.getEffect(lifeApple) != null && player.removeEffect(lifeApple);
    }

    private static boolean isAdditionLimiter(AttributeModifier modifier) {
        String name = modifier.getName().toLowerCase(Locale.ROOT);
        return name.contains("negate") && name.contains("add");
    }
}
