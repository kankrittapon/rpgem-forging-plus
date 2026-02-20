package net.kankrittapon.rpgem.forging.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.forging.block.entity.AncientForgeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent by the client when the player clicks the Upgrade/Repair button
 * in the Ancient Forge GUI. The server validates distance then delegates
 * the action to {@link AncientForgeBlockEntity#handleForgeAction}.
 */
public record PacketUpgradeItem(BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketUpgradeItem> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEMForging.MODID, "upgrade_item"));

    public static final StreamCodec<ByteBuf, PacketUpgradeItem> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketUpgradeItem::pos,
            PacketUpgradeItem::new);

    public static void handle(final PacketUpgradeItem payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level() instanceof Level level) {
                // Security: check player is close enough to the block
                if (context.player().distanceToSqr(
                        payload.pos.getX() + 0.5,
                        payload.pos.getY() + 0.5,
                        payload.pos.getZ() + 0.5) > 64) {
                    return;
                }
                BlockEntity be = level.getBlockEntity(payload.pos);
                if (be instanceof AncientForgeBlockEntity forge) {
                    forge.handleForgeAction(level, context.player());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
