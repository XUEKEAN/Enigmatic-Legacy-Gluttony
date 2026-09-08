package cn.blockforge.generated.generatedmod.devour;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.net.DevourPacket;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 暴食原罪的管理员辅助指令。 */
@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID)
public final class GluttonyCommands {
    private GluttonyCommands() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("gluttony_complete_all")
                .requires(source -> source.hasPermission(2))
                .executes(context -> complete(context.getSource())));
    }

    private static int complete(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        DevourData.completeAllReversals(player);
        DevourPacket.sendStats(player);
        source.sendSuccess(() -> Component.translatable("message.generated_mod.all_reversed")
                .withStyle(ChatFormatting.GREEN), true);
        return 1;
    }
}
