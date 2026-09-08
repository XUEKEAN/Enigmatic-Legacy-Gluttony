package cn.blockforge.generated.generatedmod.config;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ModConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec GLOBAL_SPEC;
    private static final ForgeConfigSpec.BooleanValue GLOBAL_DEFAULTS_APPLIED;
    public static final ForgeConfigSpec.DoubleValue COOLDOWN_SECONDS;
    public static final ForgeConfigSpec.DoubleValue DEVOUR_RADIUS;
    public static final ForgeConfigSpec.DoubleValue BASE_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue BASE_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue BOSS_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue BOSS_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue ELITE_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue ELITE_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue SAME_ENTITY_HEAL;
    public static final ForgeConfigSpec.DoubleValue SPEED_PENALTY_PER_UNIQUE;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_RING_OF_SEVEN_CURSES;
    public static final ForgeConfigSpec.BooleanValue ALLOW_FRIENDLY_TARGETS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DEVOUR_BLACKLIST_IDS;
    public static final ForgeConfigSpec.BooleanValue GIVE_STARTER_ITEM;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RING_OF_SEVEN_CURSES_IDS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENDLESS_BADGE_IDS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CURE_ITEM_IDS;
    public static final ForgeConfigSpec.ConfigValue<String> DIVINE_FRUIT_PIE_ID;
    public static final ForgeConfigSpec.ConfigValue<String> CURSE_ONE_FALLBACK_ID;
    public static final ForgeConfigSpec.IntValue STRONG_BODY_REVERSE_CROUCHES;
    public static final ForgeConfigSpec.IntValue STRONG_BODY_MAX_STACKS;
    public static final ForgeConfigSpec.DoubleValue STRONG_BODY_ATTACK_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue STRONG_BODY_HEALTH_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue STRONG_BODY_SPEED_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue STRONG_BODY_ATTACK_SPEED_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue STRONG_BODY_DURATION_SECONDS_PER_RISE;
    public static final ForgeConfigSpec.IntValue PICKY_REVERSE_FOOD_COUNT;
    public static final ForgeConfigSpec.IntValue GLUTTONY_FULL_HUNGER_MINUTES;
    public static final ForgeConfigSpec.DoubleValue DREAD_REVERSE_HEALTH_THRESHOLD;

    public static final ForgeConfigSpec.DoubleValue GLOBAL_COOLDOWN_SECONDS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_DEVOUR_RADIUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_BASE_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_BASE_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_BOSS_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_BOSS_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_ELITE_HEALTH_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_ELITE_ATTACK_BONUS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_SAME_ENTITY_HEAL;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_SPEED_PENALTY_PER_UNIQUE;
    public static final ForgeConfigSpec.BooleanValue GLOBAL_REQUIRE_RING_OF_SEVEN_CURSES;
    public static final ForgeConfigSpec.BooleanValue GLOBAL_ALLOW_FRIENDLY_TARGETS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GLOBAL_DEVOUR_BLACKLIST_IDS;
    public static final ForgeConfigSpec.BooleanValue GLOBAL_GIVE_STARTER_ITEM;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GLOBAL_RING_OF_SEVEN_CURSES_IDS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GLOBAL_ENDLESS_BADGE_IDS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GLOBAL_CURE_ITEM_IDS;
    public static final ForgeConfigSpec.ConfigValue<String> GLOBAL_DIVINE_FRUIT_PIE_ID;
    public static final ForgeConfigSpec.ConfigValue<String> GLOBAL_CURSE_ONE_FALLBACK_ID;
    public static final ForgeConfigSpec.IntValue GLOBAL_STRONG_BODY_REVERSE_CROUCHES;
    public static final ForgeConfigSpec.IntValue GLOBAL_STRONG_BODY_MAX_STACKS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_STRONG_BODY_ATTACK_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_STRONG_BODY_HEALTH_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_STRONG_BODY_SPEED_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_STRONG_BODY_ATTACK_SPEED_PER_RISE;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_STRONG_BODY_DURATION_SECONDS_PER_RISE;
    public static final ForgeConfigSpec.IntValue GLOBAL_PICKY_REVERSE_FOOD_COUNT;
    public static final ForgeConfigSpec.IntValue GLOBAL_GLUTTONY_FULL_HUNGER_MINUTES;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_DREAD_REVERSE_HEALTH_THRESHOLD;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("神秘遗物暴食附属 / Enigmatic Legacy: Gluttony 服务端配置（神秘遗物 / 神秘佳肴 附属）")
                .push("gluttony_sin");
        COOLDOWN_SECONDS = builder.comment("吞噬主动技能冷却时间（秒），默认 60；佩戴神秘佳肴的无止暴食之证时无冷却")
                .defineInRange("cooldownSeconds", 60.0, 0.0, 3600.0);
        DEVOUR_RADIUS = builder.comment("无止暴食之证范围吞噬半径（格），默认 3")
                .defineInRange("devourRadius", 3.0, 1.0, 32.0);
        BASE_HEALTH_BONUS = builder.comment("首次吞噬新生物增加的最大生命值（默认 1.0 = 半颗心，原设定 2.0 的一半）")
                .defineInRange("baseHealthBonus", 1.0, 0.0, 10000.0);
        BASE_ATTACK_BONUS = builder.comment("首次吞噬新生物增加的攻击力（默认 0.5，原设定 1.0 的一半）")
                .defineInRange("baseAttackBonus", 0.5, 0.0, 10000.0);
        BOSS_HEALTH_BONUS = builder.comment("首次吞噬BOSS生物额外增加的最大生命值")
                .defineInRange("bossHealthBonus", 5.0, 0.0, 10000.0);
        BOSS_ATTACK_BONUS = builder.comment("首次吞噬BOSS生物额外增加的攻击力")
                .defineInRange("bossAttackBonus", 2.5, 0.0, 10000.0);
        ELITE_HEALTH_BONUS = builder.comment("首次吞噬精英生物额外增加的最大生命值")
                .defineInRange("eliteHealthBonus", 2.0, 0.0, 10000.0);
        ELITE_ATTACK_BONUS = builder.comment("首次吞噬精英生物额外增加的攻击力")
                .defineInRange("eliteAttackBonus", 1.0, 0.0, 10000.0);
        SAME_ENTITY_HEAL = builder.comment("重复吞噬同一生物回复的生命值")
                .defineInRange("sameEntityHeal", 2.0, 0.0, 10000.0);
        SPEED_PENALTY_PER_UNIQUE = builder.comment("诅咒2：每吞噬一种新生物永久减少的移动速度比例（0.01=1%）")
                .defineInRange("speedPenaltyPerUnique", 0.01, 0.0, 1.0);
        REQUIRE_RING_OF_SEVEN_CURSES = builder.comment("是否要求佩戴神秘遗物的七咒之戒才能触发吞噬")
                .define("requireRingOfSevenCurses", true);
        ALLOW_FRIENDLY_TARGETS = builder.comment("是否允许吞噬友方或有主人/驯服关系的生物，默认关闭")
                .define("allowFriendlyTargets", false);
        DEVOUR_BLACKLIST_IDS = builder.comment("禁止吞噬的生物注册ID列表，例如 minecraft:iron_golem；默认空")
                .defineList("devourBlacklistIds", List.of(), o -> o instanceof String);
        GIVE_STARTER_ITEM = builder.comment("是否在每名玩家第一次进入服务器时赠送一个普通版暴食原罪，默认开启")
                .define("giveStarterItem", true);
        RING_OF_SEVEN_CURSES_IDS = builder.comment("七咒之戒的注册ID（神秘遗物原版物品）")
                .defineList("ringOfSevenCursesIds",
                        List.of("enigmaticlegacy:cursed_ring"), o -> o instanceof String);
        ENDLESS_BADGE_IDS = builder.comment("无止暴食之证的注册ID（神秘佳肴 Enigmatic Delicacy 原版物品，佩戴后吞噬无冷却并开启范围吞噬）")
                .defineList("endlessBadgeIds",
                        List.of("enigmaticdelicacy:gluttony_charm"), o -> o instanceof String);
        CURE_ITEM_IDS = builder.comment("吃下后可永久免疫移速诅咒的物品注册ID（神秘遗物/神秘佳肴原版天体果实及切片）")
                .defineList("cureItemIds", List.of(
                        "enigmaticlegacy:astral_fruit",
                        "enigmaticdelicacy:astral_fruit_slice"), o -> o instanceof String);
        DIVINE_FRUIT_PIE_ID = builder.comment("神秘佳肴盘装神谕灵果派的注册ID；只读取联动模组已有物品，不会在本模组注册")
                .define("divineFruitPieId", "enigmaticdelicacy:divine_fruit_pie");
        CURSE_ONE_FALLBACK_ID = builder.comment("未加载神秘佳肴时，诅咒1使用的神秘遗物天体果实注册ID")
                .define("curseOneFallbackId", "enigmaticlegacy:astral_fruit");
        STRONG_BODY_REVERSE_CROUCHES = builder.comment("诅咒2反转所需的蹲下后起身次数")
                .defineInRange("strongBodyReverseCrouches", 5000, 1, 1000000);
        STRONG_BODY_MAX_STACKS = builder.comment("强健身体成长次数上限，0表示无上限")
                .defineInRange("strongBodyMaxStacks", 0, 0, 1000000);
        STRONG_BODY_ATTACK_PER_RISE = builder.comment("强健身体每次起身增加的攻击力")
                .defineInRange("strongBodyAttackPerRise", 0.1, 0.0, 10000.0);
        STRONG_BODY_HEALTH_PER_RISE = builder.comment("强健身体每次起身增加的最大生命值")
                .defineInRange("strongBodyHealthPerRise", 0.1, 0.0, 10000.0);
        STRONG_BODY_SPEED_PER_RISE = builder.comment("强健身体每次起身增加的移动速度")
                .defineInRange("strongBodySpeedPerRise", 0.00001, 0.0, 10000.0);
        STRONG_BODY_ATTACK_SPEED_PER_RISE = builder.comment("强健身体每次起身增加的攻击速度")
                .defineInRange("strongBodyAttackSpeedPerRise", 0.001, 0.0, 10000.0);
        STRONG_BODY_DURATION_SECONDS_PER_RISE = builder.comment("强健身体每次起身增加的持续时间（秒）")
                .defineInRange("strongBodyDurationSecondsPerRise", 3.0, 0.0, 86400.0);
        PICKY_REVERSE_FOOD_COUNT = builder.comment("诅咒3反转所需的不同高营养食物数量")
                .defineInRange("pickyReverseFoodCount", 10, 1, 1000000);
        GLUTTONY_FULL_HUNGER_MINUTES = builder.comment("诅咒4需要保持满饱食度的分钟数")
                .defineInRange("gluttonyFullHungerMinutes", 10, 1, 1440);
        DREAD_REVERSE_HEALTH_THRESHOLD = builder.comment("诅咒6击杀凋零时允许的最大生命值，默认4点生命值（两颗心）")
                .defineInRange("dreadReverseHealthThreshold", 4.0, 0.1, 1000000.0);
        GLOBAL_DEFAULTS_APPLIED = builder.comment("内部标记：该世界是否已经从全局配置初始化过默认值")
                .define("globalDefaultsApplied", false);
        builder.pop();
        SPEC = builder.build();

        ForgeConfigSpec.Builder globalBuilder = new ForgeConfigSpec.Builder();
        globalBuilder.comment("神秘遗物暴食附属 / Enigmatic Legacy: Gluttony 全局默认配置（只作为新创建世界的初始值，世界SERVER配置优先）")
                .push("gluttony_sin");
        GLOBAL_COOLDOWN_SECONDS = globalBuilder.defineInRange("cooldownSeconds", 60.0, 0.0, 3600.0);
        GLOBAL_DEVOUR_RADIUS = globalBuilder.defineInRange("devourRadius", 3.0, 1.0, 32.0);
        GLOBAL_BASE_HEALTH_BONUS = globalBuilder.defineInRange("baseHealthBonus", 1.0, 0.0, 10000.0);
        GLOBAL_BASE_ATTACK_BONUS = globalBuilder.defineInRange("baseAttackBonus", 0.5, 0.0, 10000.0);
        GLOBAL_BOSS_HEALTH_BONUS = globalBuilder.defineInRange("bossHealthBonus", 5.0, 0.0, 10000.0);
        GLOBAL_BOSS_ATTACK_BONUS = globalBuilder.defineInRange("bossAttackBonus", 2.5, 0.0, 10000.0);
        GLOBAL_ELITE_HEALTH_BONUS = globalBuilder.defineInRange("eliteHealthBonus", 2.0, 0.0, 10000.0);
        GLOBAL_ELITE_ATTACK_BONUS = globalBuilder.defineInRange("eliteAttackBonus", 1.0, 0.0, 10000.0);
        GLOBAL_SAME_ENTITY_HEAL = globalBuilder.defineInRange("sameEntityHeal", 2.0, 0.0, 10000.0);
        GLOBAL_SPEED_PENALTY_PER_UNIQUE = globalBuilder.defineInRange("speedPenaltyPerUnique", 0.01, 0.0, 1.0);
        GLOBAL_REQUIRE_RING_OF_SEVEN_CURSES = globalBuilder.define("requireRingOfSevenCurses", true);
        GLOBAL_ALLOW_FRIENDLY_TARGETS = globalBuilder.define("allowFriendlyTargets", false);
        GLOBAL_DEVOUR_BLACKLIST_IDS = globalBuilder.defineList("devourBlacklistIds", List.of(), o -> o instanceof String);
        GLOBAL_GIVE_STARTER_ITEM = globalBuilder.define("giveStarterItem", true);
        GLOBAL_RING_OF_SEVEN_CURSES_IDS = globalBuilder.defineList("ringOfSevenCursesIds",
                List.of("enigmaticlegacy:cursed_ring"), o -> o instanceof String);
        GLOBAL_ENDLESS_BADGE_IDS = globalBuilder.defineList("endlessBadgeIds",
                List.of("enigmaticdelicacy:gluttony_charm"), o -> o instanceof String);
        GLOBAL_CURE_ITEM_IDS = globalBuilder.defineList("cureItemIds", List.of(
                "enigmaticlegacy:astral_fruit", "enigmaticdelicacy:astral_fruit_slice"), o -> o instanceof String);
        GLOBAL_DIVINE_FRUIT_PIE_ID = globalBuilder.define("divineFruitPieId", "enigmaticdelicacy:divine_fruit_pie");
        GLOBAL_CURSE_ONE_FALLBACK_ID = globalBuilder.define("curseOneFallbackId", "enigmaticlegacy:astral_fruit");
        GLOBAL_STRONG_BODY_REVERSE_CROUCHES = globalBuilder.defineInRange("strongBodyReverseCrouches", 5000, 1, 1000000);
        GLOBAL_STRONG_BODY_MAX_STACKS = globalBuilder.defineInRange("strongBodyMaxStacks", 0, 0, 1000000);
        GLOBAL_STRONG_BODY_ATTACK_PER_RISE = globalBuilder.defineInRange("strongBodyAttackPerRise", 0.1, 0.0, 10000.0);
        GLOBAL_STRONG_BODY_HEALTH_PER_RISE = globalBuilder.defineInRange("strongBodyHealthPerRise", 0.1, 0.0, 10000.0);
        GLOBAL_STRONG_BODY_SPEED_PER_RISE = globalBuilder.defineInRange("strongBodySpeedPerRise", 0.00001, 0.0, 10000.0);
        GLOBAL_STRONG_BODY_ATTACK_SPEED_PER_RISE = globalBuilder.defineInRange("strongBodyAttackSpeedPerRise", 0.001, 0.0, 10000.0);
        GLOBAL_STRONG_BODY_DURATION_SECONDS_PER_RISE = globalBuilder.defineInRange("strongBodyDurationSecondsPerRise", 3.0, 0.0, 86400.0);
        GLOBAL_PICKY_REVERSE_FOOD_COUNT = globalBuilder.defineInRange("pickyReverseFoodCount", 10, 1, 1000000);
        GLOBAL_GLUTTONY_FULL_HUNGER_MINUTES = globalBuilder.defineInRange("gluttonyFullHungerMinutes", 10, 1, 1440);
        GLOBAL_DREAD_REVERSE_HEALTH_THRESHOLD = globalBuilder.defineInRange("dreadReverseHealthThreshold", 4.0, 0.1, 1000000.0);
        globalBuilder.pop();
        GLOBAL_SPEC = globalBuilder.build();
    }

    private ModConfig() {
    }

    public static void onConfigLoading(net.minecraftforge.fml.event.config.ModConfigEvent.Loading event) {
        if (event.getConfig().getType() != net.minecraftforge.fml.config.ModConfig.Type.SERVER
                || GLOBAL_DEFAULTS_APPLIED.get()) {
            return;
        }
        if (hasServerOverride()) {
            GLOBAL_DEFAULTS_APPLIED.set(true);
            SPEC.save();
            return;
        }
        COOLDOWN_SECONDS.set(GLOBAL_COOLDOWN_SECONDS.get());
        DEVOUR_RADIUS.set(GLOBAL_DEVOUR_RADIUS.get());
        BASE_HEALTH_BONUS.set(GLOBAL_BASE_HEALTH_BONUS.get());
        BASE_ATTACK_BONUS.set(GLOBAL_BASE_ATTACK_BONUS.get());
        BOSS_HEALTH_BONUS.set(GLOBAL_BOSS_HEALTH_BONUS.get());
        BOSS_ATTACK_BONUS.set(GLOBAL_BOSS_ATTACK_BONUS.get());
        ELITE_HEALTH_BONUS.set(GLOBAL_ELITE_HEALTH_BONUS.get());
        ELITE_ATTACK_BONUS.set(GLOBAL_ELITE_ATTACK_BONUS.get());
        SAME_ENTITY_HEAL.set(GLOBAL_SAME_ENTITY_HEAL.get());
        SPEED_PENALTY_PER_UNIQUE.set(GLOBAL_SPEED_PENALTY_PER_UNIQUE.get());
        REQUIRE_RING_OF_SEVEN_CURSES.set(GLOBAL_REQUIRE_RING_OF_SEVEN_CURSES.get());
        ALLOW_FRIENDLY_TARGETS.set(GLOBAL_ALLOW_FRIENDLY_TARGETS.get());
        DEVOUR_BLACKLIST_IDS.set(GLOBAL_DEVOUR_BLACKLIST_IDS.get());
        GIVE_STARTER_ITEM.set(GLOBAL_GIVE_STARTER_ITEM.get());
        RING_OF_SEVEN_CURSES_IDS.set(GLOBAL_RING_OF_SEVEN_CURSES_IDS.get());
        ENDLESS_BADGE_IDS.set(GLOBAL_ENDLESS_BADGE_IDS.get());
        CURE_ITEM_IDS.set(GLOBAL_CURE_ITEM_IDS.get());
        DIVINE_FRUIT_PIE_ID.set(GLOBAL_DIVINE_FRUIT_PIE_ID.get());
        CURSE_ONE_FALLBACK_ID.set(GLOBAL_CURSE_ONE_FALLBACK_ID.get());
        STRONG_BODY_REVERSE_CROUCHES.set(GLOBAL_STRONG_BODY_REVERSE_CROUCHES.get());
        STRONG_BODY_MAX_STACKS.set(GLOBAL_STRONG_BODY_MAX_STACKS.get());
        STRONG_BODY_ATTACK_PER_RISE.set(GLOBAL_STRONG_BODY_ATTACK_PER_RISE.get());
        STRONG_BODY_HEALTH_PER_RISE.set(GLOBAL_STRONG_BODY_HEALTH_PER_RISE.get());
        STRONG_BODY_SPEED_PER_RISE.set(GLOBAL_STRONG_BODY_SPEED_PER_RISE.get());
        STRONG_BODY_ATTACK_SPEED_PER_RISE.set(GLOBAL_STRONG_BODY_ATTACK_SPEED_PER_RISE.get());
        STRONG_BODY_DURATION_SECONDS_PER_RISE.set(GLOBAL_STRONG_BODY_DURATION_SECONDS_PER_RISE.get());
        PICKY_REVERSE_FOOD_COUNT.set(GLOBAL_PICKY_REVERSE_FOOD_COUNT.get());
        GLUTTONY_FULL_HUNGER_MINUTES.set(GLOBAL_GLUTTONY_FULL_HUNGER_MINUTES.get());
        DREAD_REVERSE_HEALTH_THRESHOLD.set(GLOBAL_DREAD_REVERSE_HEALTH_THRESHOLD.get());
        GLOBAL_DEFAULTS_APPLIED.set(true);
        SPEC.save();
    }

    /** SERVER 中已有非默认值时，说明世界配置被明确设置过，必须保持 SERVER 的更高优先级。 */
    private static boolean hasServerOverride() {
        return Double.compare(COOLDOWN_SECONDS.get(), 60.0) != 0
                || Double.compare(DEVOUR_RADIUS.get(), 3.0) != 0
                || Double.compare(BASE_HEALTH_BONUS.get(), 1.0) != 0
                || Double.compare(BASE_ATTACK_BONUS.get(), 0.5) != 0
                || Double.compare(BOSS_HEALTH_BONUS.get(), 5.0) != 0
                || Double.compare(BOSS_ATTACK_BONUS.get(), 2.5) != 0
                || Double.compare(ELITE_HEALTH_BONUS.get(), 2.0) != 0
                || Double.compare(ELITE_ATTACK_BONUS.get(), 1.0) != 0
                || Double.compare(SAME_ENTITY_HEAL.get(), 2.0) != 0
                || Double.compare(SPEED_PENALTY_PER_UNIQUE.get(), 0.01) != 0
                || REQUIRE_RING_OF_SEVEN_CURSES.get() != true
                || ALLOW_FRIENDLY_TARGETS.get() != false
                || !DEVOUR_BLACKLIST_IDS.get().isEmpty()
                || GIVE_STARTER_ITEM.get() != true
                || !RING_OF_SEVEN_CURSES_IDS.get().equals(List.of("enigmaticlegacy:cursed_ring"))
                || !ENDLESS_BADGE_IDS.get().equals(List.of("enigmaticdelicacy:gluttony_charm"))
                || !CURE_ITEM_IDS.get().equals(List.of(
                        "enigmaticlegacy:astral_fruit", "enigmaticdelicacy:astral_fruit_slice"))
                || !"enigmaticdelicacy:divine_fruit_pie".equals(DIVINE_FRUIT_PIE_ID.get())
                || !"enigmaticlegacy:astral_fruit".equals(CURSE_ONE_FALLBACK_ID.get())
                || STRONG_BODY_REVERSE_CROUCHES.get() != 5000
                || STRONG_BODY_MAX_STACKS.get() != 0
                || Double.compare(STRONG_BODY_ATTACK_PER_RISE.get(), 0.1) != 0
                || Double.compare(STRONG_BODY_HEALTH_PER_RISE.get(), 0.1) != 0
                || Double.compare(STRONG_BODY_SPEED_PER_RISE.get(), 0.00001) != 0
                || Double.compare(STRONG_BODY_ATTACK_SPEED_PER_RISE.get(), 0.001) != 0
                || Double.compare(STRONG_BODY_DURATION_SECONDS_PER_RISE.get(), 3.0) != 0
                || PICKY_REVERSE_FOOD_COUNT.get() != 10
                || GLUTTONY_FULL_HUNGER_MINUTES.get() != 10
                || Double.compare(DREAD_REVERSE_HEALTH_THRESHOLD.get(), 4.0) != 0;
    }
}
