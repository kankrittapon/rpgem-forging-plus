package net.kankrittapon.rpgem.forging;

import net.kankrittapon.rpgem.forging.init.ModForgingMenuTypes;
import net.kankrittapon.rpgem.forging.screen.AncientForgeScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only initialisation for RPGEM Forging+.
 * Registers GUI screens for each menu type on the client side.
 */
@EventBusSubscriber(modid = RPGEMForging.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RPGEMForgingClient {

    @SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(ModForgingMenuTypes.ANCIENT_FORGE_MENU.get(), AncientForgeScreen::new);
    }
}
