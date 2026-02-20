package net.kankrittapon.rpgem.forging.network;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers all network packets for the Forging module.
 * Called during the mod bus {@link RegisterPayloadHandlersEvent}.
 */
public class ModForgingMessages {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(RPGEMForging.MODID);

        // Client → Server: player pressed Upgrade/Repair button
        registrar.playToServer(
                PacketUpgradeItem.TYPE,
                PacketUpgradeItem.STREAM_CODEC,
                PacketUpgradeItem::handle);

        // Server → Client: sync Fail Stack value after each attempt
        registrar.playToClient(
                PacketSyncFailStack.TYPE,
                PacketSyncFailStack.STREAM_CODEC,
                PacketSyncFailStack::handle);
    }
}
