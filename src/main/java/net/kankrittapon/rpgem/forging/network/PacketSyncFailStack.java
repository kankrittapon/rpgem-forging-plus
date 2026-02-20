package net.kankrittapon.rpgem.forging.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.core.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent by the server to update the client-side Fail Stack display
 * after every upgrade attempt (success resets to 0, failure increments).
 */
public record PacketSyncFailStack(int failStack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketSyncFailStack> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEMForging.MODID, "sync_fail_stack"));

    public static final StreamCodec<ByteBuf, PacketSyncFailStack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PacketSyncFailStack::failStack,
            PacketSyncFailStack::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PacketSyncFailStack message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Only runs on client side
            if (context.flow().isClientbound() && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player
                        .setData(ModAttachments.FAIL_STACK, message.failStack());
            }
        });
    }
}
