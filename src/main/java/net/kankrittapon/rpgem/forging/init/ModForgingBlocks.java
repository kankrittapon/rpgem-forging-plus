package net.kankrittapon.rpgem.forging.init;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.forging.block.AncientForgeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all Blocks that belong to the Forging system.
 */
public class ModForgingBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RPGEMForging.MODID);

    /** The main crafting station for the upgrade/repair system. */
    public static final DeferredBlock<AncientForgeBlock> ANCIENT_FORGE = BLOCKS.register("ancient_forge",
            () -> new AncientForgeBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.5F)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
