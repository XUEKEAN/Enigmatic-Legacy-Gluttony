package cn.blockforge.generated.generatedmod.net;

import cn.blockforge.generated.generatedmod.GeneratedMod;
import cn.blockforge.generated.generatedmod.client.ClientGluttony;
import cn.blockforge.generated.generatedmod.config.ModConfig;
import cn.blockforge.generated.generatedmod.devour.DevourData;
import cn.blockforge.generated.generatedmod.devour.DevourEvents;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class DevourPacket {
    public static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(GeneratedMod.MOD_ID, "devour"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static int nextId = 0;

    private DevourPacket() {
    }

    public static void register() {
        CHANNEL.registerMessage(nextId++, EffectPacket.class,
                EffectPacket::encode,
                EffectPacket::decode,
                EffectPacket::handle);
        CHANNEL.registerMessage(nextId++, StatsPacket.class,
                StatsPacket::encode,
                StatsPacket::decode,
                StatsPacket::handle);
        CHANNEL.registerMessage(nextId++, DevourRequestPacket.class,
                DevourRequestPacket::encode,
                DevourRequestPacket::decode,
                DevourRequestPacket::handle);
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToClient(ServerPlayer player, Object message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendStats(ServerPlayer player) {
        CompoundTag data = DevourData.data(player).copy();
        data.putDouble(DevourData.TAG_STRONG_ATTACK_PER_RISE, ModConfig.STRONG_BODY_ATTACK_PER_RISE.get());
        data.putDouble(DevourData.TAG_STRONG_HP_PER_RISE, ModConfig.STRONG_BODY_HEALTH_PER_RISE.get());
        data.putDouble(DevourData.TAG_STRONG_SPEED_PER_RISE, ModConfig.STRONG_BODY_SPEED_PER_RISE.get());
        data.putDouble(DevourData.TAG_STRONG_ATTACK_SPEED_PER_RISE,
                ModConfig.STRONG_BODY_ATTACK_SPEED_PER_RISE.get());
        data.putDouble(DevourData.TAG_STRONG_DURATION_PER_RISE,
                ModConfig.STRONG_BODY_DURATION_SECONDS_PER_RISE.get());
        data.putInt(DevourData.TAG_STRONG_REVERSE_CROUCHES, ModConfig.STRONG_BODY_REVERSE_CROUCHES.get());
        data.putInt(DevourData.TAG_PICKY_REVERSE_FOODS, ModConfig.PICKY_REVERSE_FOOD_COUNT.get());
        data.putInt(DevourData.TAG_FULL_HUNGER_MINUTES, ModConfig.GLUTTONY_FULL_HUNGER_MINUTES.get());
        sendToClient(player, new StatsPacket(data));
    }

    public static class DevourRequestPacket {
        private final int targetId;
        private final boolean requireEmptyHand;

        public DevourRequestPacket(int targetId, boolean requireEmptyHand) {
            this.targetId = targetId;
            this.requireEmptyHand = requireEmptyHand;
        }

        public DevourRequestPacket(FriendlyByteBuf buf) {
            this(buf.readVarInt(), buf.readBoolean());
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeVarInt(this.targetId);
            buf.writeBoolean(this.requireEmptyHand);
        }

        public static DevourRequestPacket decode(FriendlyByteBuf buf) {
            return new DevourRequestPacket(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    DevourEvents.handleKeyRequest(player, this.targetId, this.requireEmptyHand);
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static class EffectPacket {
        private final double x;
        private final double y;
        private final double z;
        private final int strength;

        public EffectPacket(double x, double y, double z, int strength) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.strength = strength;
        }

        public EffectPacket(FriendlyByteBuf buf) {
            this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeInt(this.strength);
        }

        public static EffectPacket decode(FriendlyByteBuf buf) {
            return new EffectPacket(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                if (context.getDirection().getReceptionSide().isClient()) {
                    ClientGluttony.trigger(this.x, this.y, this.z, this.strength);
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static class StatsPacket {
        private final CompoundTag data;

        public StatsPacket(CompoundTag data) {
            this.data = data;
        }

        public StatsPacket(FriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeNbt(this.data);
        }

        public static StatsPacket decode(FriendlyByteBuf buf) {
            return new StatsPacket(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                if (context.getDirection().getReceptionSide().isClient()) {
                    ClientGluttony.updateStats(this.data);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
