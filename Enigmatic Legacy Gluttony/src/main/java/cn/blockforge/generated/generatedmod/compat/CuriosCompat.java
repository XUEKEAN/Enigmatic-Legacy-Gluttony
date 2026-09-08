package cn.blockforge.generated.generatedmod.compat;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.config.ModConfig;
import cn.blockforge.generated.generatedmod.devour.DevourData;
import cn.blockforge.generated.generatedmod.registry.ModEnchantments;
import cn.blockforge.generated.generatedmod.registry.ModItems;
import java.util.HashMap;
import com.google.common.collect.ImmutableMultimap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Curios API 反射兼容层：不编译期依赖 Curios，运行时通过反射把暴食原罪注册为
 * 可放入 Curios 饰品栏的饰品，并读取玩家当前佩戴的饰品（用于联动检测）。
 */
@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID)
public final class CuriosCompat {
    private static final String CLASS_CURIOS_CAPABILITY = "top.theillusivec4.curios.api.CuriosCapability";
    private static final String CLASS_I_CURIO = "top.theillusivec4.curios.api.type.capability.ICurio";

    private static Boolean curiosPresent;

    private CuriosCompat() {
    }

    public static boolean isCuriosPresent() {
        if (curiosPresent == null) {
            try {
                Class.forName(CLASS_CURIOS_CAPABILITY);
                curiosPresent = true;
            } catch (Throwable ignored) {
                curiosPresent = false;
            }
        }
        return curiosPresent;
    }

    /** 附加给物品的 Curios capability（ITEM），用于把暴食原罪注册为饰品。 */
    public static Capability<?> getCuriosCapability() {
        return getCapabilityField("ITEM");
    }

    /** 玩家装备栏的 Curios capability（INVENTORY），用于检测佩戴物品。 */
    public static Capability<?> getCuriosInventoryCapability() {
        return getCapabilityField("INVENTORY");
    }

    private static Capability<?> getCapabilityField(String fieldName) {
        if (!isCuriosPresent()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(CLASS_CURIOS_CAPABILITY);
            Field field = clazz.getField(fieldName);
            Object value = field.get(null);
            if (value instanceof Capability<?> capability) {
                return capability;
            }
        } catch (Throwable ignored) {
            // Curios 未安装或字段变化时按无处理
        }
        return null;
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!stack.is(ModItems.GLUTTONY_SIN.get()) && !stack.is(ModItems.GLUTTONY_SIN_CREATIVE.get())) {
            return;
        }
        Capability<?> cap = getCuriosCapability();
        if (cap == null) {
            return;
        }
        event.addCapability(ResourceLocation.fromNamespaceAndPath(GeneratedMod.MOD_ID, "curio"), new ICapabilityProvider() {
            @Override
            public LazyOptional getCapability(Capability capability, Direction side) {
                if (capability == cap) {
                    return LazyOptional.of(() -> createCurioProxy(stack));
                }
                return LazyOptional.empty();
            }
        });
    }

    /**
     * 动态代理实现 net.curios.api.ICurio：只返回安全的默认值，
     * 让物品可被放入饰品栏但不注入任何额外属性。
     * 普通版暴食原罪一经佩戴无法摘下（canUnequip=false），
     * 创造版物品或创造模式玩家均可正常摘下；canEquip 始终放行，
     * 由 onEquip 延迟复查七咒之戒，无戒指时强制摘下。
     */
    private static Object createCurioProxy(ItemStack stack) {
        try {
            Class<?> icurio = Class.forName(CLASS_I_CURIO);
            InvocationHandler handler = (proxy, method, args) -> {
                String name = method.getName();
                if ("toString".equals(name)) {
                    return "GluttonySinCurio";
                }
                if ("hashCode".equals(name)) {
                    return System.identityHashCode(proxy);
                }
                if ("equals".equals(name)) {
                    return args != null && args.length > 0 && proxy == args[0];
                }
                Class<?> returnType = method.getReturnType();
                if (returnType == void.class) {
                    if ("onEquip".equals(name) && args != null && args.length >= 1) {
                        scheduleRingVerification(stack, args[0]);
                    }
                    return null;
                }
                if (!returnType.isPrimitive()) {
                    if (returnType == ItemStack.class) {
                        return ItemStack.EMPTY;
                    }
                    if (returnType == String.class) {
                        return "";
                    }
                    if (returnType.isEnum()) {
                        Object[] constants = returnType.getEnumConstants();
                        return constants != null && constants.length > 0 ? constants[0] : null;
                    }
                    if (returnType.getName().equals("com.google.common.collect.Multimap")) {
                        return ImmutableMultimap.of();
                    }
                    if (Component.class.isAssignableFrom(returnType)) {
                        return Component.empty();
                    }
                    if (Map.class.isAssignableFrom(returnType)) {
                        return Map.of();
                    }
                    if (Set.class.isAssignableFrom(returnType)) {
                        return Set.of();
                    }
                    if (Collection.class.isAssignableFrom(returnType)
                            || Iterable.class.isAssignableFrom(returnType)) {
                        return List.of();
                    }
                    if (Optional.class.isAssignableFrom(returnType)) {
                        return Optional.empty();
                    }
                    return null;
                }
                if (returnType == boolean.class) {
                    if ("canUnequip".equals(name)) {
                        if (stack.is(ModItems.GLUTTONY_SIN_CREATIVE.get())) {
                            return true;
                        }
                        return isCreativeWearer(args);
                    }
                    if ("canEquip".equals(name)) {
                        // 登录/重进世界时 Curios 会对已装备饰品重新校验，此刻玩家饰品栏数据
                        // 尚未加载完成，戒指判定必然失败并把已装备物品弹出；这里一律放行，
                        // 改为 onEquip 后延迟复查戒指：登录不再脱出，无戒指也依然戴不上。
                        return true;
                    }
                    // 千咒卷轴需要在客户端读取饰品栈上的标准附魔；关闭同步会让服务端写入的诅咒不可见。
                    if ("canSync".equals(name)) {
                        return true;
                    }
                    return true;
                }
                if ("getDropRule".equals(name) && returnType.isEnum()
                        && stack.is(ModItems.GLUTTONY_SIN.get())) {
                    for (Object constant : returnType.getEnumConstants()) {
                        if (constant instanceof Enum<?> enumValue
                                && ("KEEP".equals(enumValue.name())
                                || "KEEP_ON_DEATH".equals(enumValue.name()))) {
                            return constant;
                        }
                    }
                }
                if (returnType == int.class) {
                    return 0;
                }
                if (returnType == long.class) {
                    return 0L;
                }
                if (returnType == float.class) {
                    return 0.0F;
                }
                if (returnType == double.class) {
                    return 0.0D;
                }
                return null;
            };
            return Proxy.newProxyInstance(CuriosCompat.class.getClassLoader(), new Class<?>[]{icurio}, handler);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 佩戴者处于创造模式时允许摘下（canUnequip 的 SlotContext 参数可反射取到佩戴者）。 */
    private static boolean isCreativeWearer(Object[] args) {
        Player wearer = extractWearer(args);
        return wearer != null && wearer.getAbilities().instabuild;
    }

    /**
     * 佩戴时延迟两个tick复查七咒之戒：登录/重进世界时 Curios 先恢复物品槽、后恢复戒指槽，
     * 立即判定会把已佩戴的物品误摘；等数据就绪后再检查，确认无戒指才强制摘下放回背包。
     */
    private static void scheduleRingVerification(ItemStack stack, Object slotContext) {
        if (stack.is(ModItems.GLUTTONY_SIN_CREATIVE.get())) {
            return;
        }
        Player wearer = extractWearer(new Object[]{slotContext});
        if (wearer == null || wearer.getAbilities().instabuild) {
            return;
        }
        MinecraftServer server = wearer.getServer();
        if (server == null) {
            return;
        }
        UUID uuid = wearer.getUUID();
        server.executeIfPossible(() -> server.executeIfPossible(() -> {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player == null || !player.isAlive()) {
                return;
            }
            if (hasRingOfSevenCursesEquipped(player)) {
                return;
            }
            unequipGluttonySin(player);
            player.displayClientMessage(
                    Component.translatable("message." + GeneratedMod.MOD_ID + ".ring_required"), true);
        }));
    }

    /** 把暴食原罪从玩家全部 Curios 槽位摘下并放回背包，背包满则掉落在地。 */
    private static void unequipGluttonySin(Player player) {
        try {
            Capability<?> cap = getCuriosInventoryCapability();
            if (cap == null) {
                return;
            }
            Object handler = player.getCapability(cap).orElse(null);
            if (handler == null) {
                return;
            }
            Object curios = unwrap(invoke(handler, "getCurios"));
            if (!(curios instanceof Map<?, ?> map)) {
                return;
            }
            for (Object slotHandler : map.values()) {
                if (slotHandler == null) {
                    continue;
                }
                try {
                    Object slotsObj = invoke(slotHandler, "getSlots");
                    if (!(slotsObj instanceof Number slots)) {
                        continue;
                    }
                    Object stackHandler = getStackHandler(slotHandler);
                    if (stackHandler == null) {
                        continue;
                    }
                    for (int i = 0; i < slots.intValue(); i++) {
                        Object stackObj = invoke(stackHandler, "getStackInSlot", i);
                        if (!(stackObj instanceof ItemStack equipped)
                                || !equipped.is(ModItems.GLUTTONY_SIN.get())) {
                            continue;
                        }
                        Object extracted = invoke(stackHandler, "extractItem", i, equipped.getCount(), false);
                        if (extracted instanceof ItemStack removed && !removed.isEmpty()) {
                            if (!player.getInventory().add(removed)) {
                                player.drop(removed, false);
                            }
                        }
                    }
                } catch (Throwable ignored) {
                    // 单个槽位处理器反射失败不影响其余槽位
                }
            }
        } catch (Throwable ignored) {
            // 反射失败时静默跳过，避免影响其他逻辑
        }
    }

    /** 从 canEquip/canUnequip 的 SlotContext 参数中反射取出佩戴者。 */
    private static Player extractWearer(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        try {
            Object context = args[0];
            Object wearer = null;
            try {
                wearer = context.getClass().getMethod("entity").invoke(context);
            } catch (NoSuchMethodException ignored) {
                wearer = context.getClass().getMethod("getWearer").invoke(context);
            }
            return wearer instanceof Player player ? player : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 玩家当前是否在 Curios 饰品栏中佩戴七咒之戒（仅在饰品栏生效，放在背包里不算）。 */
    public static boolean hasRingOfSevenCursesEquipped(Player player) {
        for (String id : ModConfig.RING_OF_SEVEN_CURSES_IDS.get()) {
            ResourceLocation key = ResourceLocation.tryParse(id);
            if (key == null) {
                continue;
            }
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item == null || item == Items.AIR) {
                continue;
            }
            if (isEquipped(player, item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 把当前未反转的暴食诅咒同步成真实隐藏附魔，供神秘遗物的千咒卷轴统计。
     * 第八条“死亡复活后仅恢复一格饱食度”没有反转条件，因此始终保留。
     */
    public static void syncGluttonyCurseEnchantments(Player player) {
        if (!isCuriosPresent() || player.level().isClientSide) {
            return;
        }
        try {
            Capability<?> cap = getCuriosInventoryCapability();
            if (cap == null) {
                return;
            }
            Object handler = player.getCapability(cap).orElse(null);
            if (handler == null) {
                return;
            }
            Object curios = unwrap(invoke(handler, "getCurios"));
            if (!(curios instanceof Map<?, ?> map)) {
                return;
            }
            for (Object slotHandler : map.values()) {
                if (slotHandler == null) {
                    continue;
                }
                Object slotsObj = invoke(slotHandler, "getSlots");
                if (!(slotsObj instanceof Number slots)) {
                    continue;
                }
                for (int i = 0; i < slots.intValue(); i++) {
                    Object stackHandler = getStackHandler(slotHandler);
                    if (stackHandler == null) {
                        continue;
                    }
                    Object stackObj = invoke(stackHandler, "getStackInSlot", i);
                    if (stackObj instanceof ItemStack stack && stack.is(ModItems.GLUTTONY_SIN.get())) {
                        syncCurseEnchantments(stackHandler, i, stack, player);
                    }
                }
            }
        } catch (Throwable ignored) {
            // Curios 版本差异不应影响暴食本体；无法读取时等待下一 tick 重试。
        }
    }

    private static void syncCurseEnchantments(Object stackHandler, int slot, ItemStack stack, Player player) {
        Map<net.minecraft.world.item.enchantment.Enchantment, Integer> desired =
                new HashMap<>(net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(stack));
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_1.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_2.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_3.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_4.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_5.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_6.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_7.get());
        removeGluttonyCurse(desired, ModEnchantments.GLUTTONY_CURSE_8.get());

        if (!DevourData.isReversed(player, 1)) desired.put(ModEnchantments.GLUTTONY_CURSE_1.get(), 1);
        if (!DevourData.isReversed(player, 2)) desired.put(ModEnchantments.GLUTTONY_CURSE_2.get(), 1);
        if (!DevourData.isReversed(player, 3)) desired.put(ModEnchantments.GLUTTONY_CURSE_3.get(), 1);
        if (!DevourData.isReversed(player, 4)) desired.put(ModEnchantments.GLUTTONY_CURSE_4.get(), 1);
        if (!DevourData.isReversed(player, 5)) desired.put(ModEnchantments.GLUTTONY_CURSE_5.get(), 1);
        if (!DevourData.isReversed(player, 6)) desired.put(ModEnchantments.GLUTTONY_CURSE_6.get(), 1);
        if (!DevourData.isReversed(player, 7)) desired.put(ModEnchantments.GLUTTONY_CURSE_7.get(), 1);
        desired.put(ModEnchantments.GLUTTONY_CURSE_8.get(), 1);

        int oldHideFlags = stack.getTag() == null ? 0 : stack.getTag().getInt("HideFlags");
        int newHideFlags = oldHideFlags | 1;
        Map<net.minecraft.world.item.enchantment.Enchantment, Integer> current =
                net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(stack);
        if (current.equals(desired) && oldHideFlags == newHideFlags) {
            return;
        }

        // 修改副本后通过 Curios 槽位处理器写回，确保服务端变更同步到客户端，供千咒卷轴读取。
        ItemStack updated = stack.copy();
        net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(desired, updated);
        updated.getOrCreateTag().putInt("HideFlags", newHideFlags);
        try {
            // IDynamicStackHandler 才是真正存放物品的处理器，写回后 Curios 才会同步装备栈。
            invoke(stackHandler, "setStackInSlot", slot, updated);
        } catch (Throwable ignored) {
            // 兼容极旧的槽位处理器：至少保留当前栈的服务端效果，下一 tick 重试写回。
            net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(desired, stack);
            stack.getOrCreateTag().putInt("HideFlags", newHideFlags);
        }
    }

    private static void removeGluttonyCurse(
            Map<net.minecraft.world.item.enchantment.Enchantment, Integer> enchantments,
            net.minecraft.world.item.enchantment.Enchantment enchantment) {
        enchantments.remove(enchantment);
    }

    /** 从 Curios 的 ICurioStacksHandler 取出真正存放 ItemStack 的 IDynamicStackHandler。 */
    private static Object getStackHandler(Object stacksHandler) {
        if (stacksHandler == null) {
            return null;
        }
        try {
            Object stacks = invoke(stacksHandler, "getStacks");
            return stacks != null ? stacks : stacksHandler;
        } catch (Throwable ignored) {
            // 兼容旧版直接把槽位处理器作为栈处理器返回的实现
            return stacksHandler;
        }
    }

    /** 按注册 ID 检查玩家是否正把指定物品佩戴在 Curios 饰品栏中。 */
    public static boolean isEquippedById(Player player, String id) {
        ResourceLocation key = ResourceLocation.tryParse(id);
        if (key == null) {
            return false;
        }
        Item item = ForgeRegistries.ITEMS.getValue(key);
        return item != null && item != Items.AIR && isEquipped(player, item);
    }

    /** MoonStone 的倾斜异果只在确实佩戴于 Curios 饰品栏时才提供锁血效果。 */
    public static boolean isSkewedFruitEquipped(Player player) {
        if (!isCuriosPresent()) {
            return false;
        }
        // 以常见 ID 为主，再扫描 MoonStone 注册表，兼容不同版本的命名路径。
        if (isEquippedById(player, "moonstone:skewed_fruit")
                || isEquippedById(player, "moonstone:skewedfruit")) {
            return true;
        }
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
            if (key != null && "moonstone".equals(key.getNamespace())
                    && key.getPath().contains("skewed") && key.getPath().contains("fruit")
                    && isEquipped(player, item)) {
                return true;
            }
        }
        return false;
    }

    /** 检查玩家是否正把指定物品佩戴在 Curios 饰品栏中（走 INVENTORY capability）。 */
    public static boolean isEquipped(Player player, Item item) {
        if (!isCuriosPresent()) {
            return false;
        }
        try {
            Capability<?> cap = getCuriosInventoryCapability();
            if (cap == null) {
                return false;
            }
            Object handler = player.getCapability(cap).orElse(null);
            if (handler == null) {
                return false;
            }
            try {
                Object equipped = unwrap(invoke(handler, "getEquippedCurios"));
                if (equipped != null && containsStack(equipped, item)) {
                    return true;
                }
            } catch (Throwable ignored) {
                // 老版本没有 getEquippedCurios 时继续走 getCurios 遍历
            }
            try {
                return containsStack(invoke(handler, "getCurios"), item);
            } catch (Throwable ignored) {
                return false;
            }
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** 解开 LazyOptional / Optional 之类的容器包装，拿到真实内容。 */
    private static Object unwrap(Object container) {
        if (container == null) {
            return null;
        }
        try {
            if (container instanceof java.util.Optional<?> optional) {
                return optional.orElse(null);
            }
            Method isPresent = container.getClass().getMethod("isPresent");
            if (Boolean.TRUE.equals(isPresent.invoke(container))) {
                Object resolved = container.getClass().getMethod("resolve").invoke(container);
                if (resolved instanceof java.util.Optional<?> optional) {
                    return optional.orElse(null);
                }
            }
        } catch (Throwable ignored) {
            // 不是包装类型，原样返回
        }
        return container;
    }

    /** 递归遍历 Curios 返回的 Map<String, ICurioStacksHandler> / 栈处理器。 */
    private static boolean containsStack(Object container, Item item) {
        if (container == null) {
            return false;
        }
        if (container instanceof ItemStack stack) {
            return stack.is(item);
        }
        if (container instanceof java.util.Map<?, ?> map) {
            for (Object value : map.values()) {
                if (containsStack(value, item)) {
                    return true;
                }
            }
            return false;
        }
        if (container instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                if (containsStack(element, item)) {
                    return true;
                }
            }
            return false;
        }
        try {
            Object stackHandler = getStackHandler(container);
            Object slotsObj = invoke(stackHandler, "getSlots");
            if (slotsObj instanceof Number slots) {
                for (int i = 0; i < slots.intValue(); i++) {
                    if (containsStack(invoke(stackHandler, "getStackInSlot", i), item)) {
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {
            // 不认识的容器类型直接跳过
        }
        return false;
    }

    /** 反射调用 Curios 接口时兼容其非 public 的内部实现类。 */
    private static Object invoke(Object target, String name, Object... args) throws Exception {
        if (target == null) {
            throw new NullPointerException("target");
        }
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = boxToPrimitive(args[i].getClass());
        }
        Method method;
        try {
            method = target.getClass().getMethod(name, types);
        } catch (NoSuchMethodException missing) {
            method = findCompatibleMethod(target.getClass(), name, types);
            if (method == null) {
                throw missing;
            }
        }
        if (!method.canAccess(target)) {
            method.setAccessible(true);
        }
        return method.invoke(target, args);
    }

    private static Method findCompatibleMethod(Class<?> type, String name, Class<?>[] types) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name) || method.getParameterCount() != types.length) {
                continue;
            }
            boolean compatible = true;
            Class<?>[] parameters = method.getParameterTypes();
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].isAssignableFrom(types[i]) && !types[i].isAssignableFrom(parameters[i])) {
                    compatible = false;
                    break;
                }
            }
            if (compatible) {
                return method;
            }
        }
        return null;
    }

    private static Class<?> boxToPrimitive(Class<?> type) {
        if (type == Integer.class) {
            return int.class;
        }
        if (type == Double.class) {
            return double.class;
        }
        if (type == Boolean.class) {
            return boolean.class;
        }
        if (type == Float.class) {
            return float.class;
        }
        if (type == Long.class) {
            return long.class;
        }
        return type;
    }
}
