package net.kankrittapon.rpgem.forging.init;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.forging.block.entity.AncientForgeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

/**
 * Registers all Block Entities that belong to the Forging system.
 */
public class ModForgingBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, RPGEMForging.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AncientForgeBlockEntity>> ANCIENT_FORGE_BE = BLOCK_ENTITY_TYPES
            .register(
                    "ancient_forge_be",
                    () -> BlockEntityType.Builder
                            .of(AncientForgeBlockEntity::new,
                                    ModForgingBlocks.ANCIENT_FORGE.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
