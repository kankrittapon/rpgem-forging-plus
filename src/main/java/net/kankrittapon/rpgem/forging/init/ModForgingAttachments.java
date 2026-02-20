package net.kankrittapon.rpgem.forging.init;

import com.mojang.serialization.Codec;
import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Registers Entity Attachments specific to the Forging system.
 */
public class ModForgingAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, RPGEMForging.MODID);

    // FAIL_STACK moved to rpgem-core: ModAttachments.FAIL_STACK

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
