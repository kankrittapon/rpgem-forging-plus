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

    /**
     * Fail Stack: tracks upgrade failures to boost future success rates.
     * Default: 0. Each failure increases by 1.
     */
    public static final Supplier<AttachmentType<Integer>> FAIL_STACK = ATTACHMENT_TYPES.register("fail_stack",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
