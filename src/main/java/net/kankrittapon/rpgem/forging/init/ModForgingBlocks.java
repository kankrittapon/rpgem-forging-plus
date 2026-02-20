package net.kankrittapon.rpgem.forging.init;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.forging.block.AncientForgeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all Blocks that belong to the Forging system.
 */
public class ModForgingBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RPGEMForging.MODID);

    /** The main crafting station for the upgrade/repair system. */
    public static final DeferredBlock<AncientForgeBlock> ANCIENT_FORGE = registerBlock("ancient_forge",
            () -> new AncientForgeBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.5F)));

    // ====== BlockItem helpers ======

    private static <T extends Block> DeferredBlock<T> registerBlock(
            String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(
            String name, DeferredBlock<T> block) {
        ModForgingItems.ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
