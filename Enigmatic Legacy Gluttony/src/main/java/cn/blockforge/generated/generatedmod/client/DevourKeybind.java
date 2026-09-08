package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.compat.CuriosCompat;
import cn.blockforge.generated.generatedmod.net.DevourPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DevourKeybind {
    public static final KeyMapping DEVOUR = new KeyMapping(
            "key." + GeneratedMod.MOD_ID + ".devour",
            InputConstants.Type.MOUSE,
            1,
            "key.categories." + GeneratedMod.MOD_ID);

    private DevourKeybind() {
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(DEVOUR);
    }

    @Mod.EventBusSubscriber(modid = GeneratedMod.MOD_ID, value = Dist.CLIENT)
    public static final class InputHandler {
        private InputHandler() {
        }

        @SubscribeEvent
        public static void onKey(InputEvent.Key event) {
            if (isGluttonyEquipped() && event.getAction() == GLFW.GLFW_PRESS
                    && DEVOUR.matches(event.getKey(), event.getScanCode())) {
                sendTargetRequest(false);
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onMouse(InputEvent.MouseButton.Pre event) {
            if (event.getAction() != GLFW.GLFW_PRESS
                    || DEVOUR.getKey().getType() != InputConstants.Type.MOUSE) {
                return;
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (!isGluttonyEquipped() || minecraft.player == null || minecraft.level == null
                    || minecraft.screen != null || minecraft.hitResult == null
                    || minecraft.hitResult.getType() != HitResult.Type.ENTITY) {
                return;
            }
            if (DEVOUR.getKey().getValue() == event.getButton()) {
                sendTargetRequest(isDefaultRightClick());
                event.setCanceled(true);
                return;
            }
            // 改绑到其他按键后，普通右键只取消实体交互，不再触发吞噬。
            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && !isDefaultRightClick()) {
                event.setCanceled(true);
            }
        }

        private static boolean isDefaultRightClick() {
            return DEVOUR.getKey().getType() == InputConstants.Type.MOUSE
                    && DEVOUR.getKey().getValue() == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
        }

        private static boolean isGluttonyEquipped() {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft.player != null
                    && (CuriosCompat.isEquipped(minecraft.player,
                            cn.blockforge.generated.generatedmod.registry.ModItems.GLUTTONY_SIN.get())
                    || CuriosCompat.isEquipped(minecraft.player,
                            cn.blockforge.generated.generatedmod.registry.ModItems.GLUTTONY_SIN_CREATIVE.get()));
        }

        private static void sendTargetRequest(boolean requireEmptyHand) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null || minecraft.level == null || minecraft.screen != null
                    || minecraft.hitResult == null || minecraft.hitResult.getType() != HitResult.Type.ENTITY) {
                return;
            }
            Entity target = ((EntityHitResult) minecraft.hitResult).getEntity();
            if (target != null && target != minecraft.player) {
                DevourPacket.sendToServer(new DevourPacket.DevourRequestPacket(target.getId(), requireEmptyHand));
            }
        }
    }
}
