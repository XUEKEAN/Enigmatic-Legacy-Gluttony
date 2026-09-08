package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.devour.DevourData;
import cn.blockforge.generated.generatedmod.registry.ModItems;
import cn.blockforge.generated.generatedmod.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.Tag;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID, value = Dist.CLIENT)
public final class ClientGluttony {
    private static final RandomSource RANDOM = RandomSource.create();
    /** 原版红色点点粒子：直接使用 minecraft:dust，不依赖任何自定义贴图，杜绝紫黑/红黑方块。 */
    private static final ParticleOptions DEVOUR_DOT =
            new DustParticleOptions(new Vector3f(0.90F, 0.18F, 0.14F), 1.0F);
    /** 吸入流光专用红色点点：比炸开略大略亮，保证飞入体内的吸入动画清晰可见。 */
    private static final ParticleOptions FLY_DOT =
            new DustParticleOptions(new Vector3f(0.93F, 0.26F, 0.19F), 1.25F);
    private static int shakeTicks;
    private static int glowTicks;
    private static double flyX;
    private static double flyY;
    private static double flyZ;
    private static int burstTicks;
    private static int streamTicks;
    private static int effectStrength;

    /** 本存档的吞噬成长数据（由服务端 StatsPacket 同步），用于暴食原罪的 Shift 详情。 */
    public static int lastUniqueCount;
    public static double lastMaxHpBonus;
    public static double lastAttackBonus;
    public static double lastSpeedPenalty;
    public static boolean lastCured;
    public static final boolean[] lastReversed = new boolean[7];
    public static int lastCrouchCount;
    public static int lastFoodKindCount;
    public static long lastFullHungerTicks;
    public static int lastStrongDuration;
    public static int strongReverseCrouches = 5000;
    public static int pickyReverseFoods = 10;
    public static int fullHungerMinutes = 10;
    public static double strongAttackPerRise = 0.1;
    public static double strongHpPerRise = 0.1;
    public static double strongSpeedPerRise = 0.001;
    public static double strongAttackSpeedPerRise = 0.001;
    public static double strongDurationPerRise = 3.0;
    public static double lastStrongHp;
    public static double lastStrongAttack;
    public static double lastStrongSpeed;
    public static double lastStrongAttackSpeed;

    private ClientGluttony() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.GLUTTONY_SIN.get(),
                ResourceLocation.fromNamespaceAndPath(GeneratedMod.MOD_ID, "all_reversed"),
                (stack, level, entity, seed) -> allReversalsComplete() ? 1.0F : 0.0F));
    }

    /** 普通版物品只有在七条反转状态全部同步完成后才切换为创造版贴图。 */
    public static boolean allReversalsComplete() {
        for (boolean reversed : lastReversed) {
            if (!reversed) {
                return false;
            }
        }
        return true;
    }

    public static void updateStats(net.minecraft.nbt.CompoundTag data) {
        lastUniqueCount = data.getInt(DevourData.TAG_UNIQUE);
        lastMaxHpBonus = data.getDouble(DevourData.TAG_MAX_HP_BONUS);
        lastAttackBonus = data.getDouble(DevourData.TAG_ATTACK_BONUS);
        lastSpeedPenalty = data.getDouble(DevourData.TAG_SPEED_PENALTY);
        lastCured = data.getBoolean("curse2_reversed");
        for (int i = 0; i < lastReversed.length; i++) {
            lastReversed[i] = data.getBoolean("curse" + (i + 1) + "_reversed");
        }
        lastCrouchCount = data.getInt(DevourData.TAG_CROUCH_COUNT);
        lastFoodKindCount = data.getList(DevourData.TAG_FOOD_KINDS, Tag.TAG_STRING).size();
        lastFullHungerTicks = data.getLong(DevourData.TAG_FULL_HUNGER_TICKS);
        lastStrongDuration = data.getInt(DevourData.TAG_STRONG_DURATION);
        strongReverseCrouches = data.getInt(DevourData.TAG_STRONG_REVERSE_CROUCHES);
        pickyReverseFoods = data.getInt(DevourData.TAG_PICKY_REVERSE_FOODS);
        fullHungerMinutes = data.getInt(DevourData.TAG_FULL_HUNGER_MINUTES);
        strongAttackPerRise = data.getDouble(DevourData.TAG_STRONG_ATTACK_PER_RISE);
        strongHpPerRise = data.getDouble(DevourData.TAG_STRONG_HP_PER_RISE);
        strongSpeedPerRise = data.getDouble(DevourData.TAG_STRONG_SPEED_PER_RISE);
        strongAttackSpeedPerRise = data.getDouble(DevourData.TAG_STRONG_ATTACK_SPEED_PER_RISE);
        strongDurationPerRise = data.getDouble(DevourData.TAG_STRONG_DURATION_PER_RISE);
        lastStrongHp = data.getDouble(DevourData.TAG_STRONG_HP);
        lastStrongAttack = data.getDouble(DevourData.TAG_STRONG_ATTACK);
        lastStrongSpeed = data.getDouble(DevourData.TAG_STRONG_SPEED);
        lastStrongAttackSpeed = data.getDouble(DevourData.TAG_STRONG_ATTACK_SPEED);
    }

    public static void trigger(double x, double y, double z, int strength) {
        shakeTicks = 30;
        glowTicks = 30;
        flyX = x;
        flyY = y;
        flyZ = z;
        burstTicks = 3;
        // 保持原特效，播放速度略微加快：流光从约1.2秒缩至约0.9秒
        streamTicks = 18;
        effectStrength = strength;
    }

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (shakeTicks <= 0) {
            return;
        }
        shakeTicks--;
        float progress = shakeTicks / 30.0F;
        float strength = progress * 0.09F;
        event.setRoll(event.getRoll() + (RANDOM.nextFloat() - 0.5F) * strength);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        if (glowTicks <= 0 && burstTicks <= 0 && streamTicks <= 0) {
            return;
        }
        if (glowTicks > 0) {
            glowTicks--;
        }
        // 目标原地炸开：薄血雾（自定义浅红柔圆粒子）
        if (burstTicks > 0) {
            burstTicks--;
            for (int i = 0; i < 6; i++) {
                double ox = (RANDOM.nextDouble() - 0.5) * 0.9;
                double oy = RANDOM.nextDouble() * 1.2;
                double oz = (RANDOM.nextDouble() - 0.5) * 0.9;
                double vx = (RANDOM.nextDouble() - 0.5) * 0.05;
                double vy = (RANDOM.nextDouble() - 0.5) * 0.05 + 0.01;
                double vz = (RANDOM.nextDouble() - 0.5) * 0.05;
                mc.level.addParticle(ModParticles.GLUTTONY_MIST.get(),
                        flyX + ox, flyY + oy, flyZ + oz, vx, vy, vz);
            }
        }
        // 吸入流光：浅红光点沿“目标→玩家”路径连续飞入体内；光点到达玩家身体即自然消散，不额外在胸前/嘴边生成残留光点
        if (streamTicks > 0) {
            streamTicks--;
            Vec3 target = mc.player.getEyePosition().add(0.0, -0.2, 0.0);
            Vec3 origin = new Vec3(flyX, flyY + 0.9, flyZ);
            Vec3 segment = target.subtract(origin);
            if (segment.length() >= 0.5) {
                int perTick = effectStrength >= 2 ? 8 : 6;
                for (int i = 0; i < perTick; i++) {
                    // 起点随机落在路径上：越靠近玩家速度越快，形成加速吸入的视觉
                    double t = RANDOM.nextDouble();
                    Vec3 from = origin.add(segment.scale(t))
                            .add((RANDOM.nextDouble() - 0.5) * 0.5,
                                    (RANDOM.nextDouble() - 0.5) * 0.5,
                                    (RANDOM.nextDouble() - 0.5) * 0.5);
                    double speed = 0.20 + (1.0 - t) * 0.36 + RANDOM.nextDouble() * 0.08;
                    Vec3 to = target.subtract(from);
                    double dist = to.length();
                    if (dist < 0.25) {
                        continue;
                    }
                    Vec3 vDir = to.normalize();
                    mc.level.addParticle(ModParticles.GLUTTONY_FLY.get(),
                            from.x, from.y, from.z, vDir.x * speed, vDir.y * speed, vDir.z * speed);
                }
            }
        }
    }
}
