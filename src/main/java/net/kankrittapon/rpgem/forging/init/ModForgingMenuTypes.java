package net.kankrittapon.rpgem.forging.init;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.kankrittapon.rpgem.forging.menu.AncientForgeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all Menu (Container) Types that belong to the Forging system.
 */
public class ModForgingMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
            RPGEMForging.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AncientForgeMenu>> ANCIENT_FORGE_MENU = MENUS.register(
            "ancient_forge_menu",
            () -> IMenuTypeExtension.create(AncientForgeMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
