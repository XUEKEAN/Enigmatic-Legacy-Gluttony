package cn.blockforge.generated.generatedmod.devour;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.compat.CuriosCompat;
import cn.blockforge.generated.generatedmod.compat.FarmersDelightCompat;
import cn.blockforge.generated.generatedmod.config.ModConfig;
import cn.blockforge.generated.generatedmod.net.DevourPacket;
import cn.blockforge.generated.generatedmod.registry.ModItems;
import cn.blockforge.generated.generatedmod.registry.ModEffects;
import cn.blockforge.generated.generatedmod.registry.ModParticles;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID)
public final class DevourEvents {
    private static final int FEAST_INTERVAL_TICKS = 5;
    /** 记录每位玩家上一 tick 的位置，用于服务端稳定检测“是否正在移动”（服务端 getDeltaMovement 经常为 0）。 */
    private static final Map<UUID, Vec3> LAST_POS = new HashMap<>();
    /** 氧气耗尽后仍在移动时的溺水伤害计数（按原版溺水节奏每 20 tick 造成 2 点伤害）。 */
    private static final Map<UUID, Integer> DROWN_TICKS = new HashMap<>();
    /** 第一诅咒反转后，记录本 tick 锁血效果处理前的生命值。 */
    private static final Map<UUID, Float> HEALTH_AT_TICK_START = new HashMap<>();
    /** 本 tick 是否受到真实伤害；受伤时不能把生命恢复误判为锁血压低。 */
    private static final Map<UUID, Boolean> HEALTH_DAMAGED_THIS_TICK = new HashMap<>();
    /** 本 tick 经最终伤害事件确认的生命伤害，用于保留 life_apple 的固定受伤效果。 */
    private static final Map<UUID, Float> HEALTH_DAMAGE_AMOUNT_THIS_TICK = new HashMap<>();

    private DevourEvents() {
    }

    /** 客户端按当前绑定的吞噬按键发送目标实体 ID；改绑模式允许手持任意物品。 */
    public static void handleKeyRequest(ServerPlayer player, int targetId, boolean requireEmptyHand) {
        Entity target = player.level().getEntity(targetId);
        if (target == null || target.distanceToSqr(player) > 36.0) {
            return;
        }
        tryDevour(player, target, requireEmptyHand);
    }

    private static boolean tryDevour(Player player, Entity target, boolean requireEmptyHand) {
        Level level = player.level();
        if (level.isClientSide || target == player || !(target instanceof LivingEntity living)
                || !living.isAlive() || living instanceof Player) {
            return false;
        }
        boolean creative = hasCreativeGluttonyItem(player);
        if (requireEmptyHand && !creative && !player.getMainHandItem().isEmpty()) {
            return false;
        }
        if (!hasGluttonyItem(player) && !creative) {
            return false;
        }
        if (!creative && !canDevourTarget(player, living)) {
            return false;
        }
        if (!creative && !canDevourByHealth(player, living)) {
            player.displayClientMessage(Component.translatable("message.generated_mod.stronger"), true);
            return false;
        }
        if (!creative && ModConfig.REQUIRE_RING_OF_SEVEN_CURSES.get()
                && !CuriosCompat.hasRingOfSevenCursesEquipped(player)) {
            player.displayClientMessage(Component.translatable("message.generated_mod.need_ring"), true);
            return false;
        }
        boolean endless = hasEndlessBadge(player);
        long now = level.getGameTime();
        if (!creative && !endless && now < DevourData.getCooldownUntil(player)) {
            long remain = (DevourData.getCooldownUntil(player) - now + 19) / 20;
            player.displayClientMessage(Component.translatable("message.generated_mod.cooldown", remain), true);
            return false;
        }
        if (endless) {
            double radius = ModConfig.DEVOUR_RADIUS.get();
            AABB box = new AABB(player.blockPosition()).inflate(radius);
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
                    e -> e != player && e.isAlive() && !(e instanceof Player)
                            && (creative || canDevourTarget(player, e))
                            && (creative || e.getHealth() < player.getHealth()));
            targets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
            if (targets.isEmpty()) {
                return false;
            }
            DevourData.startEndlessFeast(player, targets);
            devourOne(player, targets.get(0), true, creative);
        } else {
            devourOne(player, living, false, creative);
        }
        return true;
    }

    /**
     * Applicable 事件在 Forge 1.20.1 中不可取消，不能调用 setCanceled()。
     * 锁血效果只有在玩家实际佩戴倾斜异果时才允许存在；否则清理残留效果，避免未佩戴时凭空获得锁血。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMoonstoneEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide
                || !DevourData.isMoonstoneLifeLock(event.getEffectInstance().getEffect())) {
            return;
        }
        if (!CuriosCompat.isSkewedFruitEquipped(player)) {
            DevourData.removeMoonstoneLifeLock(player);
        }
    }

    /**
     * 在玩家 tick 开始时先恢复上一个 tick 被锁血效果压低的当前生命值。
     * 不能只依赖 LivingTickEvent：部分锁血效果会在实体 tick 过程中改写生命值，
     * 如果等到 LivingTickEvent 才记录，记录到的就已经是错误的锁定值。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTickStart(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.player.level().isClientSide) {
            return;
        }
        Player player = event.player;
        boolean creative = hasCreativeGluttonyItem(player);
        if (!(creative || hasGluttonyItem(player)) || !DevourData.isReversed(player, 1, creative)
                || !CuriosCompat.isSkewedFruitEquipped(player)
                || player.getActiveEffects().stream()
                        .noneMatch(effect -> DevourData.isMoonstoneLifeLock(effect.getEffect()))) {
            HEALTH_AT_TICK_START.remove(player.getUUID());
            HEALTH_DAMAGED_THIS_TICK.remove(player.getUUID());
            HEALTH_DAMAGE_AMOUNT_THIS_TICK.remove(player.getUUID());
            return;
        }
        CompoundTag tag = DevourData.data(player);
        if (tag.contains(DevourData.TAG_LAST_PROTECTED_CURRENT_HP, Tag.TAG_DOUBLE)) {
            float desired = (float) Math.min(player.getMaxHealth(),
                    tag.getDouble(DevourData.TAG_LAST_PROTECTED_CURRENT_HP));
            if (desired > player.getHealth()) {
                player.setHealth(desired);
            }
        }
        HEALTH_AT_TICK_START.put(player.getUUID(), player.getHealth());
        HEALTH_DAMAGED_THIS_TICK.put(player.getUUID(), false);
    }

    /**
     * 在玩家实体 tick 的最前段清理没有倾斜异果时的旧效果，并作为备用捕获点。
     * 第一诅咒反转后不移除锁血效果：PlayerTickEvent 的末段只恢复被该效果额外压低的
     * 当前生命，从而让锁血效果仍显示/保留，但不影响暴食原罪的吞噬成长。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide
                || player.getActiveEffects().stream()
                        .noneMatch(effect -> DevourData.isMoonstoneLifeLock(effect.getEffect()))) {
            return;
        }
        if (!CuriosCompat.isSkewedFruitEquipped(player)) {
            DevourData.removeMoonstoneLifeLock(player);
            HEALTH_AT_TICK_START.remove(player.getUUID());
            HEALTH_DAMAGED_THIS_TICK.remove(player.getUUID());
            HEALTH_DAMAGE_AMOUNT_THIS_TICK.remove(player.getUUID());
            return;
        }
        // 当前 tick 的独立生命值已在 PlayerTickEvent.START 捕获；这里不再覆盖它，
        // 否则第三方锁血效果若先于该事件改写生命值，会再次把锁定值记录下来。
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player.level().isClientSide) {
            return;
        }
        Vec3 nowPos = player.position();
        Vec3 prevPos = LAST_POS.get(player.getUUID());
        double moved = prevPos == null ? 0.0 : Math.sqrt(
                (nowPos.x - prevPos.x) * (nowPos.x - prevPos.x)
                        + (nowPos.z - prevPos.z) * (nowPos.z - prevPos.z));
        LAST_POS.put(player.getUUID(), nowPos);
        boolean creative = hasCreativeGluttonyItem(player);
        if (creative) {
            DevourData.applyAttributes(player, true, true);
        } else if (hasGluttonyItem(player)) {
            CuriosCompat.syncGluttonyCurseEnchantments(player);
            DevourData.applyAttributes(player, true);
        } else {
            DevourData.removeAttributes(player);
        }
        // 当前生命的最终修复统一放到 ServerTickEvent.END；此处之后 MoonStone 仍可能执行实体效果逻辑。
        if (creative || hasGluttonyItem(player)) {
            if (DevourData.isReversed(player, 7, creative)) {
                applyBreathRegeneration(player);
            } else {
                applyAsthmaCurse(player, moved);
            }
            DevourData.tickStrongBody(player);
            DevourData.tickStrongBodyWindow(player);
            if (!DevourData.isReversed(player, 5, creative) && player.hasEffect(ModEffects.DREAD.get())) {
                AABB fear = player.getBoundingBox().inflate(8.0);
                for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class, fear, e -> e != player && e.isAlive())) {
                    Vec3 away = mob.position().subtract(player.position());
                    if (away.lengthSqr() > 0.01) mob.setDeltaMovement(mob.getDeltaMovement().add(away.normalize().scale(0.08)));
                }
            }
            if (!DevourData.isReversed(player, 4, creative)) {
                if (DevourData.tickFullHunger(player)) {
                    player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 4), true);
                }
            }
            CompoundTag state = DevourData.data(player);
            boolean crouching = player.isShiftKeyDown();
            boolean wasCrouching = state.getBoolean(DevourData.TAG_LAST_CROUCH);
            state.putBoolean(DevourData.TAG_LAST_CROUCH, crouching);
            if (wasCrouching && !crouching && DevourData.onCrouchRise(player, creative)) {
                player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 2), true);
            }
        }
        long now = player.level().getGameTime();
        CompoundTag tag = DevourData.data(player);
        if (player instanceof ServerPlayer serverPlayer && now % 10L == 0L) {
            DevourPacket.sendStats(serverPlayer);
        }
        if (tag.contains(DevourData.TAG_FEAST, Tag.TAG_LIST)) {
            ListTag list = tag.getList(DevourData.TAG_FEAST, Tag.TAG_STRING);
            int index = tag.getInt(DevourData.TAG_FEAST_INDEX);
            long last = tag.getLong(DevourData.TAG_FEAST_LAST);
            if (index >= list.size()) {
                tag.remove(DevourData.TAG_FEAST);
                tag.remove(DevourData.TAG_FEAST_INDEX);
                tag.remove(DevourData.TAG_FEAST_LAST);
                return;
            }
            if (now - last >= FEAST_INTERVAL_TICKS) {
                String uuid = list.getString(index);
                tag.putInt(DevourData.TAG_FEAST_INDEX, index + 1);
                tag.putLong(DevourData.TAG_FEAST_LAST, now);
                Entity entity = ((ServerLevel) player.level()).getEntity(UUID.fromString(uuid));
                boolean feastCreative = hasCreativeGluttonyItem(player);
                if (entity instanceof LivingEntity living && living.isAlive()
                        && (feastCreative || canDevourTarget(player, living))
                        && (feastCreative || canDevourByHealth(player, living))) {
                    devourOne(player, living, true, feastCreative);
                }
            }
        }
    }

    /**
     * 在整个服务器 tick 的实体/效果处理结束后修复当前生命。
     * 这样 life_apple 仍然存在并继续负责其余效果，但不能把第一诅咒反转后的
     * 独立生命成长重新压回 30 点。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            restoreProtectedHealthAfterMoonstoneTick(player);
        }
    }

    private static void restoreProtectedHealthAfterMoonstoneTick(Player player) {
        UUID uuid = player.getUUID();
        Float before = HEALTH_AT_TICK_START.get(uuid);
        boolean damaged = Boolean.TRUE.equals(HEALTH_DAMAGED_THIS_TICK.get(uuid));
        boolean creative = hasCreativeGluttonyItem(player);
        if (before == null || !(creative || hasGluttonyItem(player))
                || !DevourData.isReversed(player, 1, creative)
                || player.getActiveEffects().stream().noneMatch(effect -> DevourData.isMoonstoneLifeLock(effect.getEffect()))) {
            HEALTH_AT_TICK_START.remove(uuid);
            HEALTH_DAMAGED_THIS_TICK.remove(uuid);
            HEALTH_DAMAGE_AMOUNT_THIS_TICK.remove(uuid);
            return;
        }
        CompoundTag tag = DevourData.data(player);
        float current = player.getHealth();
        float damageAmount = HEALTH_DAMAGE_AMOUNT_THIS_TICK.getOrDefault(uuid, 0.0F);
        double previousTarget = tag.getDouble(DevourData.TAG_LAST_PROTECTED_CURRENT_HP);
        if (damaged) {
            // 使用最终伤害值更新独立目标，不能把 MoonStone 的 30 点锁定值当成受伤后的真实生命。
            double desired = damageAmount > 0.0F ? previousTarget - damageAmount : current;
            // 同一 tick 内若确实有治疗，保留治疗后的较高生命目标。
            if (current > before) {
                desired = Math.max(desired, current);
            }
            desired = Math.max(0.0, Math.min(player.getMaxHealth(), desired));
            tag.putDouble(DevourData.TAG_LAST_PROTECTED_CURRENT_HP, desired);
            if (desired > current) {
                player.setHealth((float) desired);
            }
        } else {
            float target = (float) Math.min(player.getMaxHealth(), previousTarget);
            if (target > current) {
                player.setHealth(target);
            }
            tag.putDouble(DevourData.TAG_LAST_PROTECTED_CURRENT_HP, player.getHealth());
        }
        HEALTH_AT_TICK_START.remove(uuid);
        HEALTH_DAMAGED_THIS_TICK.remove(uuid);
        HEALTH_DAMAGE_AMOUNT_THIS_TICK.remove(uuid);
    }

    /**
     * 诅咒Ⅶ·哮喘：移动/疾跑会消耗氧气，氧气耗尽后继续移动按原版溺水节奏掉血，
     * 只有静止原地不动才恢复氧气。全程不干涉原版水下机制（玩家在水中时直接跳过）。
     * 数值目标：正常走路约 21 格耗尽氧气，疾跑约 12 格耗尽（疾跑每格消耗约为走路 1.7 倍）。
     */
    private static void applyAsthmaCurse(Player player, double moved) {
        // 水中完全交给原版机制：不消耗、不补偿、不掉血，避免与原版水下缺氧叠加
        if (player.isInWater()) {
            return;
        }
        if (moved > 0.02) {
            // 每格消耗：走路约 14 点、疾跑约 24 点（相对旧版削弱 30%）；再额外 -4 抵消原版在空气中每 tick 自动回氧 4 点。
            // 无论本处理器在原版回氧之前还是之后执行，净效果都保证稳定扣氧，不再出现回氧抵消后的闪烁或卡在 1 格。
            int rate = player.isSprinting() ? 24 : 14;
            int drain = Math.max(2, (int) Math.round(moved * rate)) + 4;
            int air = player.getAirSupply();
            if (air - drain > 0) {
                player.setAirSupply(air - drain);
                DROWN_TICKS.put(player.getUUID(), 0);
            } else {
                // 氧气耗尽仍移动：钉死在 0 点（阻止原版回氧抬回 1 格），按原版溺水节奏每 20 tick 造成 2 点伤害
                player.setAirSupply(0);
                int drown = DROWN_TICKS.getOrDefault(player.getUUID(), 0) + 1;
                DROWN_TICKS.put(player.getUUID(), drown);
                if (drown >= 20) {
                    DROWN_TICKS.put(player.getUUID(), 0);
                    player.hurt(player.damageSources().drown(), 2.0F);
                }
            }
        } else {
            player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + 4));
            DROWN_TICKS.put(player.getUUID(), 0);
        }
    }

    private static void applyBreathRegeneration(Player player) {
        double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
        double perSecond = Math.max(0.01, Math.min(0.20, speed / 0.1 * 0.01));
        player.heal((float) (player.getMaxHealth() * perSecond / 20.0));
    }

    private static boolean canDevourByHealth(Player player, LivingEntity target) {
        if (DevourData.isReversed(player, 1) || DevourData.isReversed(player, 6)) {
            // 赐/神力使用完整最大生命总和，包含外部属性和本模组真实吞噬成长。
            return target.getMaxHealth() < player.getMaxHealth();
        }
        return player.getHealth() > target.getHealth();
    }

    /** 配置黑名单优先；关闭友方开关时保护驯服、拥有主人或同队的实体。 */
    private static boolean canDevourTarget(Player player, LivingEntity target) {
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (key != null) {
            for (String blockedId : ModConfig.DEVOUR_BLACKLIST_IDS.get()) {
                if (key.toString().equalsIgnoreCase(blockedId.trim())) {
                    return false;
                }
            }
        }
        return ModConfig.ALLOW_FRIENDLY_TARGETS.get() || !isFriendlyTarget(player, target);
    }

    private static boolean isFriendlyTarget(Player player, LivingEntity target) {
        if (target instanceof TamableAnimal tamable && tamable.isTame()) {
            return true;
        }
        if (target instanceof OwnableEntity ownable && ownable.getOwner() != null) {
            return true;
        }
        return target.isAlliedTo(player);
    }

    private static boolean hasLeashedCat(Player player) {
        AABB area = player.getBoundingBox().inflate(16.0);
        List<Cat> cats = player.level().getEntitiesOfClass(Cat.class, area, Entity::isAlive);
        for (Cat cat : cats) {
            // 原版苦力怕不能稳定作为拴绳目标；改为只要附近有一只确实被拴住的猫即可。
            // 不限制拴绳另一端是玩家还是栅栏，符合“用拴绳拴着猫时击杀苦力怕”。
            if (cat.isLeashed()) {
                return true;
            }
        }
        return false;
    }

    private static void applySanctifyingSlow(Player player) {
        AABB area = player.getBoundingBox().inflate(8.0);
        for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive())) {
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 255, false, true, true));
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerRespawnEvent event) {
        Player p = event.getEntity();
        if (hasGluttonyItem(p) || hasCreativeGluttonyItem(p)) p.getFoodData().setFoodLevel(2);
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && event.getAmount() > 0.0F) {
            HEALTH_DAMAGED_THIS_TICK.put(player.getUUID(), true);
        }
        if (event.getSource().getEntity() instanceof Player player && hasGluttonyItem(player)
                && !DevourData.isReversed(player, 6)
                && (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty())) {
            event.setAmount(event.getAmount() * 0.5F);
        }
    }

    /** 记录减伤、护甲和其他处理完成后的实际生命伤害，避免把 30 点锁血值当成受伤结果。 */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFinalDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player && event.getAmount() > 0.0F) {
            UUID uuid = player.getUUID();
            HEALTH_DAMAGED_THIS_TICK.put(uuid, true);
            HEALTH_DAMAGE_AMOUNT_THIS_TICK.merge(uuid, event.getAmount(), Float::sum);
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (player.level().isClientSide) {
            return;
        }
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());
        if (key == null || !hasGluttonyItem(player)) {
            return;
        }
        boolean changed = false;
        if (!DevourData.isReversed(player, 1) && isCurseOneReversalItem(key)) {
            DevourData.reverse(player, 1);
            player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 1), true);
            changed = true;
        }
        if (!DevourData.isReversed(player, 3)) {
            FoodProperties food = event.getItem().getItem().getFoodProperties();
            if (food != null && food.getNutrition() >= 20) {
                boolean newlyReversed = DevourData.recordPickyFood(player, key);
                if (newlyReversed) {
                    player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 3), true);
                    changed = true;
                }
            }
        }
        if (!DevourData.isReversed(player, 7) && isCureItem(key)) {
            DevourData.reverse(player, 7);
            player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 7), true);
            changed = true;
        }
        if (changed && player instanceof ServerPlayer serverPlayer) {
            DevourPacket.sendStats(serverPlayer);
        }
    }

    /** 诅咒Ⅲ·挑食的第二条反转条件：完成原版“均衡饮食”进度即可直接反转。 */
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.level().isClientSide
                || !hasGluttonyItem(player)
                || DevourData.isReversed(player, 3)) {
            return;
        }
        Advancement advancement = event.getAdvancement();
        ResourceLocation id = advancement.getId();
        if (new ResourceLocation("minecraft", "husbandry/balanced_diet").equals(id)) {
            DevourData.reverse(player, 3);
            player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 3), true);
            DevourPacket.sendStats(player);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity target = event.getEntity();
        CompoundTag persistent = target.getPersistentData();
        if (!persistent.getBoolean("generated_mod_devoured_kill")) {
            return;
        }
        persistent.remove("generated_mod_devoured_kill");
        boolean wasOnFire = persistent.getBoolean("generated_mod_devoured_on_fire");
        persistent.remove("generated_mod_devoured_on_fire");
        if (event.getSource().getEntity() instanceof Player player) {
            FarmersDelightCompat.handleDevourDrops(event, player, wasOnFire);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player deadPlayer && !deadPlayer.level().isClientSide) {
            DevourData.clearStrongBody(deadPlayer);
        }
        if (!(event.getSource().getEntity() instanceof Player player) || !hasGluttonyItem(player)) {
            return;
        }
        if (event.getEntity() instanceof Creeper) {
            if (!DevourData.isReversed(player, 5) && hasLeashedCat(player)) {
                DevourData.reverse(player, 5);
                player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 5), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    DevourPacket.sendStats(serverPlayer);
                }
            }
            return;
        }
        if (!(event.getEntity() instanceof WitherBoss)
                || DevourData.isReversed(player, 6)
                || player.getHealth() > ModConfig.DREAD_REVERSE_HEALTH_THRESHOLD.get()) {
            return;
        }
        DevourData.reverse(player, 6);
        player.displayClientMessage(Component.translatable("message.generated_mod.reversed", 6), true);
        if (player instanceof ServerPlayer serverPlayer) {
            DevourPacket.sendStats(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            giveStarterItem(serverPlayer);
            DevourPacket.sendStats(serverPlayer);
        }
    }

    private static void giveStarterItem(ServerPlayer player) {
        if (!ModConfig.GIVE_STARTER_ITEM.get()) {
            return;
        }
        CompoundTag tag = DevourData.data(player);
        if (tag.getBoolean(DevourData.TAG_STARTER_ITEM_GIVEN)) {
            return;
        }
        ItemStack starter = new ItemStack(ModItems.GLUTTONY_SIN.get());
        if (!player.getInventory().add(starter)) {
            player.drop(starter, false);
        }
        tag.putBoolean(DevourData.TAG_STARTER_ITEM_GIVEN, true);
    }

    private static void devourOne(Player player, LivingEntity target, boolean endless, boolean creative) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ResourceLocation typeKey = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (typeKey == null) {
            return;
        }
        boolean boss = isBoss(target);
        boolean elite = !boss && isElite(target);
        if (!creative) {
            CompoundTag persistent = target.getPersistentData();
            persistent.putBoolean("generated_mod_devoured_kill", true);
            persistent.putBoolean("generated_mod_devoured_on_fire", target.isOnFire());
        }
        // 创造版自带第六条反转：直接移除目标，既无视防御/无敌，也不会生成掉落物或经验。
        if (creative) {
            target.discard();
        } else if (DevourData.isReversed(player, 6)) {
            // 使用原版专门的玩家伤害标记，再绕过受击/锁血流程；掉落和经验判定依赖该标记。
            target.setLastHurtByPlayer(player);
            target.setHealth(0.0F);
            target.die(target.damageSources().playerAttack(player));
            if (target.isAlive()) {
                target.kill();
            }
        } else {
            // 普通路径保留原版受击、掉落物、经验和附加抢夺结算。
            target.hurt(target.damageSources().playerAttack(player), Float.MAX_VALUE);
        }
        if (!creative && target.isAlive()) {
            CompoundTag persistent = target.getPersistentData();
            persistent.remove("generated_mod_devoured_kill");
            persistent.remove("generated_mod_devoured_on_fire");
            return;
        }
        // 吞噬成功直接清空饱食度：饥饿值、饱和度、体力一并归零，避免残留一格无法清空。
        // 普通版饱食度不为 0 时清空，为 0 时不扣但仍可吞噬；创造版完全不扣、无任何代价。
        FoodData food = player.getFoodData();
        if (!creative && food.getFoodLevel() > 0) {
            food.setFoodLevel(0);
            food.setSaturation(0.0F);
            food.setExhaustion(0.0F);
        }
        DevourData.DevourResult result = DevourData.devour(player, typeKey, boss, elite, !creative, creative);
        if (creative || DevourData.isReversed(player, 5)) {
            applySanctifyingSlow(player);
        } else {
            player.addEffect(new MobEffectInstance(ModEffects.DREAD.get(), 200, 0, false, true, true));
        }
        playDevourEffects(player, target, endless);
        if (result.growthGranted()) {
            player.displayClientMessage(Component.translatable("message.generated_mod.devoured",
                    typeKey.toString(), format(result.healthBonus()), format(result.attackBonus())), true);
        } else {
            player.displayClientMessage(Component.translatable("message.generated_mod.redevour",
                    typeKey.toString(), format(ModConfig.SAME_ENTITY_HEAL.get())), true);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            DevourPacket.sendStats(serverPlayer);
        }
        if (!endless && !creative) {
            DevourData.setCooldownUntil(player, level.getGameTime()
                    + (long) (ModConfig.COOLDOWN_SECONDS.get() * 20.0));
            player.displayClientMessage(Component.translatable("message.generated_mod.gluttony_trigger")
                    .withStyle(ChatFormatting.RED), true);
        }
    }

    private static String format(double value) {
        double rounded = Math.round(value * 100.0) / 100.0;
        if (rounded == Math.floor(rounded)) {
            return String.valueOf((long) rounded);
        }
        return String.valueOf(rounded);
    }

    private static void playDevourEffects(Player player, LivingEntity target, boolean endless) {
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            Vec3 pos = target.position();
            // 恢复 1.0.6 初版吞噬表现：目标原地薄血雾 + 玩家周身浅红气息（自定义柔圆粒子）
            serverLevel.sendParticles(ModParticles.GLUTTONY_MIST.get(),
                    pos.x, pos.y + 0.9, pos.z, 12, 0.45, 0.35, 0.45, 0.02);
            // 玩家自身周围的气息减弱：数量减半、范围收紧，避免糊在自己脸上
            serverLevel.sendParticles(ModParticles.GLUTTONY_RED_AURA.get(),
                    player.getX(), player.getY() + 0.9, player.getZ(), 9, 0.5, 0.2, 0.5, 0.01);
            // 血腥吞噬音效（响亮、层次分明）：猎物哀嚎 + 血肉挤压 + 咀嚼 + 吞咽 + 饱嗝
            serverLevel.playSound(null, target.blockPosition(), SoundEvents.PIG_HURT,
                    SoundSource.PLAYERS, 1.6F, 0.85F);
            serverLevel.playSound(null, target.blockPosition(), SoundEvents.SLIME_SQUISH,
                    SoundSource.PLAYERS, 2.0F, 0.6F);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT,
                    SoundSource.PLAYERS, 1.8F, 1.15F);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.HONEY_DRINK,
                    SoundSource.PLAYERS, 1.6F, 0.95F);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_BURP,
                    SoundSource.PLAYERS, 1.8F, 0.7F);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            DevourPacket.sendToClient(serverPlayer,
                    new DevourPacket.EffectPacket(target.getX(), target.getY(), target.getZ(), endless ? 2 : 1));
        }
    }

    private static boolean hasGluttonyItem(Player player) {
        return CuriosCompat.isEquipped(player, ModItems.GLUTTONY_SIN.get());
    }

    private static boolean hasCreativeGluttonyItem(Player player) {
        return CuriosCompat.isEquipped(player, ModItems.GLUTTONY_SIN_CREATIVE.get());
    }

    private static boolean hasEndlessBadge(Player player) {
        return hasAnyLinkedItem(player, ModConfig.ENDLESS_BADGE_IDS.get());
    }

    private static boolean hasAnyLinkedItem(Player player, List<? extends String> ids) {
        for (String id : ids) {
            ResourceLocation key = ResourceLocation.tryParse(id);
            if (key == null) {
                continue;
            }
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item == null || item == Items.AIR) {
                continue;
            }
            if (CuriosCompat.isEquipped(player, item)) {
                return true;
            }
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.isEmpty() && stack.is(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isCureItem(ResourceLocation key) {
        for (String id : ModConfig.CURE_ITEM_IDS.get()) {
            if (id.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCurseOneReversalItem(ResourceLocation key) {
        if (ModList.get().isLoaded("enigmaticdelicacy")) {
            return ModConfig.DIVINE_FRUIT_PIE_ID.get().equals(key.toString());
        }
        return ModConfig.CURSE_ONE_FALLBACK_ID.get().equals(key.toString());
    }

    private static boolean isBoss(Entity entity) {
        EntityType<?> type = entity.getType();
        return type == EntityType.ENDER_DRAGON
                || type == EntityType.WITHER
                || type == EntityType.ELDER_GUARDIAN
                || type == EntityType.WARDEN;
    }

    private static boolean isElite(Entity entity) {
        EntityType<?> type = entity.getType();
        return type == EntityType.BLAZE
                || type == EntityType.EVOKER
                || type == EntityType.VINDICATOR
                || type == EntityType.PILLAGER
                || type == EntityType.RAVAGER
                || type == EntityType.SHULKER
                || type == EntityType.VEX
                || type == EntityType.HOGLIN
                || type == EntityType.PIGLIN_BRUTE
                || type == EntityType.ENDERMAN
                || type == EntityType.IRON_GOLEM;
    }
}
