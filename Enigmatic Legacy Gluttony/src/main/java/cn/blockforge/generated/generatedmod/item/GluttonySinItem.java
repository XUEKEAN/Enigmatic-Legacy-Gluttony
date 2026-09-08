package cn.blockforge.generated.generatedmod.item;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.client.ClientGluttony;
import cn.blockforge.generated.generatedmod.compat.CuriosCompat;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

public class GluttonySinItem extends Item {
    public GluttonySinItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(ChatFormatting.DARK_RED);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            appendShiftDetails(tooltip);
        } else if (Screen.hasAltDown()) {
            appendAltDetails(tooltip);
        } else {
            appendRelicHeader(tooltip);
            appendRelicStory(tooltip);
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.tooltip_lock")
                    .withStyle(ChatFormatting.GOLD));
            if (allReversed()) {
                tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.recognized")
                        .withStyle(ChatFormatting.GREEN));
            }
            appendDefaultControls(tooltip);
            appendRequirements(tooltip);
        }
    }

    static void appendRelicHeader(List<Component> tooltip) {
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.relic_label")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.slot_label")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.slot_value")
                        .withStyle(ChatFormatting.YELLOW)));
        tooltip.add(Component.empty());
    }

    private static void appendRelicStory(List<Component> tooltip) {
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.story1")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.story2")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.story3")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.attune")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }

    private static void appendDefaultControls(List<Component> tooltip) {
        tooltip.add(Component.literal("按住 ").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal("Shift").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" 查看详情。").withStyle(ChatFormatting.DARK_PURPLE)));
        tooltip.add(Component.literal("按住 ").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal("Alt").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" 查看赐福条件。").withStyle(ChatFormatting.DARK_PURPLE)));
    }

    private static void appendRequirements(List<Component> tooltip) {
        ChatFormatting requirementColor = hasRingEquipped()
                ? ChatFormatting.GOLD : ChatFormatting.DARK_RED;
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.requirement1")
                .withStyle(requirementColor));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.requirement2")
                .withStyle(requirementColor));
    }

    /** 提示文本只在客户端显示；没有当前玩家时按未佩戴处理，保持原版深红色。 */
    private static boolean hasRingEquipped() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player != null && CuriosCompat.hasRingOfSevenCursesEquipped(minecraft.player);
    }

    private static void appendShiftDetails(List<Component> tooltip) {
        appendRelicHeader(tooltip);
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.erosion_header")
                .withStyle(ChatFormatting.DARK_PURPLE));
        for (int i = 1; i <= 7; i++) {
            boolean reversed = ClientGluttony.lastReversed[i - 1];
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".curse" + i
                    + (reversed ? ".reversed" : ""))
                    .withStyle(reversed ? ChatFormatting.GREEN : ChatFormatting.DARK_PURPLE));
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID
                    + (reversed ? ".bonus" : ".curse") + i + ".detail")
                    .withStyle(reversed ? ChatFormatting.GREEN : ChatFormatting.DARK_PURPLE));
        }
        appendEndlessLink(tooltip);
        appendGrowthDetails(tooltip);
        appendRequirements(tooltip);
    }

    private static void appendAltDetails(List<Component> tooltip) {
        appendRelicHeader(tooltip);
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.alt_header")
                .withStyle(ChatFormatting.GOLD));
        for (int i = 1; i <= 7; i++) {
            boolean reversed = ClientGluttony.lastReversed[i - 1];
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".curse" + i
                    + (reversed ? ".reversed" : ""))
                    .withStyle(reversed ? ChatFormatting.GREEN : ChatFormatting.DARK_PURPLE));
            if (reversed) {
                appendActiveBonusDetail(tooltip, i, true);
            } else {
                appendReversalDetail(tooltip, i);
            }
        }
        appendEndlessLink(tooltip);
        appendGrowthDetails(tooltip);
        appendRequirements(tooltip);
    }

    private static void appendEndlessLink(List<Component> tooltip) {
        if (ModList.get().isLoaded("enigmaticdelicacy")) {
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".endless_link")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    private static boolean allReversed() {
        for (boolean reversed : ClientGluttony.lastReversed) {
            if (!reversed) {
                return false;
            }
        }
        return true;
    }

    private static void appendReversalDetail(List<Component> tooltip, int curse) {
        if (curse == 1) {
            String key = "item." + GeneratedMod.MOD_ID + ".curse1.reverse_detail."
                    + (ModList.get().isLoaded("enigmaticdelicacy") ? "delicacy" : "legacy");
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GOLD));
        } else if (curse == 2) {
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".curse2.reverse_detail",
                    ClientGluttony.lastCrouchCount, ClientGluttony.strongReverseCrouches)
                    .withStyle(ChatFormatting.GOLD));
        } else if (curse == 3) {
            String key = "item." + GeneratedMod.MOD_ID + ".curse3.reverse_detail";
            tooltip.add(Component.translatable(key, ClientGluttony.lastFoodKindCount,
                    ClientGluttony.pickyReverseFoods).withStyle(ChatFormatting.GOLD));
        } else if (curse == 4) {
            String key = "item." + GeneratedMod.MOD_ID + ".curse4.reverse_detail";
            tooltip.add(Component.translatable(key, format(ClientGluttony.lastFullHungerTicks / 1200.0),
                    ClientGluttony.fullHungerMinutes).withStyle(ChatFormatting.GOLD));
        } else {
            String key = "item." + GeneratedMod.MOD_ID + ".curse" + curse + ".reverse_detail";
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GOLD));
        }
    }

    private static void appendActiveBonusDetail(List<Component> tooltip, int bonus, boolean detailed) {
        String key = "item." + GeneratedMod.MOD_ID + ".bonus" + bonus
                + (detailed ? ".detail_long" : ".detail");
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GREEN));
    }

    static void appendGrowthDetails(List<Component> tooltip) {
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_header")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_unique",
                ClientGluttony.lastUniqueCount).withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_hp",
                format(ClientGluttony.lastMaxHpBonus)).withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_atk",
                format(ClientGluttony.lastAttackBonus)).withStyle(ChatFormatting.DARK_RED));
        if (ClientGluttony.lastCured) {
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_speed_cured")
                    .withStyle(ChatFormatting.DARK_GREEN));
        } else {
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_speed",
                    format(ClientGluttony.lastSpeedPenalty * 100.0)).withStyle(ChatFormatting.DARK_AQUA));
        }
        if (ClientGluttony.lastReversed[1]) {
            tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_strong_body",
                    formatFine(ClientGluttony.lastStrongAttack), formatFine(ClientGluttony.lastStrongHp),
                    formatFine(ClientGluttony.lastStrongSpeed), formatFine(ClientGluttony.lastStrongAttackSpeed),
                    format(ClientGluttony.lastStrongDuration / 20.0)).withStyle(ChatFormatting.GREEN));
        }
    }

    static String format(double value) {
        double rounded = Math.round(value * 100.0) / 100.0;
        if (rounded == Math.floor(rounded)) {
            return String.valueOf((long) rounded);
        }
        return String.valueOf(rounded);
    }

    static String formatFine(double value) {
        String text = String.format(Locale.ROOT, "%.5f", value);
        while (text.contains(".") && text.endsWith("0")) {
            text = text.substring(0, text.length() - 1);
        }
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
