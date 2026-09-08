package cn.blockforge.generated.generatedmod.item;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.client.ClientGluttony;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CreativeGluttonySinItem extends Item {
    public CreativeGluttonySinItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(ChatFormatting.DARK_RED);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        GluttonySinItem.appendRelicHeader(tooltip);
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin_creative.story")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin_creative.tooltip1")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin_creative.tooltip2")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin_creative.tooltip3")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("按住 ").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal("Shift").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" 查看吞噬成长。").withStyle(ChatFormatting.DARK_PURPLE)));
        if (Screen.hasShiftDown()) {
            appendGrowthDetails(tooltip);
        }
    }

    private static void appendGrowthDetails(List<Component> tooltip) {
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_header")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_unique",
                ClientGluttony.lastUniqueCount).withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_hp",
                GluttonySinItem.format(ClientGluttony.lastMaxHpBonus)).withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin.growth_atk",
                GluttonySinItem.format(ClientGluttony.lastAttackBonus)).withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable("item." + GeneratedMod.MOD_ID + ".gluttony_sin_creative.growth_nocurses")
                .withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
