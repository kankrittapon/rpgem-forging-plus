package net.kankrittapon.rpgem.forging;

import com.mojang.logging.LogUtils;
import net.kankrittapon.rpgem.forging.config.ForgingConfig;
import net.kankrittapon.rpgem.forging.init.ModForgingAttachments;
import net.kankrittapon.rpgem.forging.init.ModForgingBlockEntities;
import net.kankrittapon.rpgem.forging.init.ModForgingBlocks;
import net.kankrittapon.rpgem.forging.init.ModForgingDataComponents;
import net.kankrittapon.rpgem.forging.init.ModForgingItems;
import net.kankrittapon.rpgem.forging.init.ModForgingMenuTypes;
import net.kankrittapon.rpgem.forging.network.ModForgingMessages;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(RPGEMForging.MODID)
public class RPGEMForging {

    public static final String MODID = "rpgem_forging";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RPGEMForging(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("RPGEM Forging+ Initializing...");

        // --- Registries ---
        ModForgingAttachments.register(modEventBus);
        ModForgingDataComponents.register(modEventBus);
        ModForgingItems.register(modEventBus);
        ModForgingBlocks.register(modEventBus);
        ModForgingBlockEntities.register(modEventBus);
        ModForgingMenuTypes.register(modEventBus);

        // --- Networking ---
        modEventBus.addListener(ModForgingMessages::register);

        // --- Config ---
        modContainer.registerConfig(ModConfig.Type.COMMON, ForgingConfig.SPEC);

        // --- Creative Tab ---
        modEventBus.addListener(this::addCreative);

        LOGGER.info("RPGEM Forging+ Initialized successfully.");
    }

    private void addCreative(net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == net.kankrittapon.rpgem.core.init.ModCreativeModeTabs.RPGEM_TAB_KEY) {
            event.accept(ModForgingBlocks.ANCIENT_FORGE);
            event.accept(ModForgingItems.UPGRADE_STONE_TIER_1);
            event.accept(ModForgingItems.UPGRADE_STONE_TIER_2);
            event.accept(ModForgingItems.UPGRADE_STONE_TIER_3);
            event.accept(ModForgingItems.MEMORY_FRAGMENT);
            event.accept(ModForgingItems.PROTECTION_STONE);
            event.accept(ModForgingItems.ARTISANS_MEMORY);
            event.accept(ModForgingItems.FORGED_STONE_FORTITUDE);
            event.accept(ModForgingItems.FORGED_STONE_AGILITY);
            event.accept(ModForgingItems.FORGED_STONE_DESTRUCTION);
            event.accept(ModForgingItems.FORGED_STONE_ULTIMATE_FORTITUDE);
            event.accept(ModForgingItems.FORGED_STONE_ULTIMATE_AGILITY);
            event.accept(ModForgingItems.FORGED_STONE_ULTIMATE_DESTRUCTION);
        }
    }
}
