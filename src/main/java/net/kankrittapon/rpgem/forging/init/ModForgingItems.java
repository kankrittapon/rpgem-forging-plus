package net.kankrittapon.rpgem.forging.init;

import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all Items that belong to the Forging system:
 * - Upgrade Stones (Tier 1, 2, 3)
 * - Forged Stones (Fortitude, Agility, Destruction + Ultimate variants)
 * - Repair/Support Items (Memory Fragment, Protection Stone, Artisan's Memory)
 */
public class ModForgingItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RPGEMForging.MODID);

    // ============================
    // Repair / Support Items
    // ============================

    /** Used to repair equipment without downgrade risk. */
    public static final DeferredItem<Item> MEMORY_FRAGMENT = ITEMS.registerSimpleItem("memory_fragment",
            new Item.Properties());

    /** Prevents item downgrade on upgrade failure (Tier 2/3 attempts). */
    public static final DeferredItem<Item> PROTECTION_STONE = ITEMS.registerSimpleItem("protection_stone",
            new Item.Properties());

    /** Multiplies repair amount by ×5 when placed in the support slot. */
    public static final DeferredItem<Item> ARTISANS_MEMORY = ITEMS.registerSimpleItem("artisans_memory",
            new Item.Properties());

    // ============================
    // Upgrade Stones
    // ============================

    /** Tier 1 Upgrade Stone: used for +1 → +15. */
    public static final DeferredItem<Item> UPGRADE_STONE_TIER_1 = ITEMS.registerSimpleItem("upgrade_stone_tier_1",
            new Item.Properties());

    /** Tier 2 Upgrade Stone: used for +15 → +25. */
    public static final DeferredItem<Item> UPGRADE_STONE_TIER_2 = ITEMS.registerSimpleItem("upgrade_stone_tier_2",
            new Item.Properties());

    /** Tier 3 Upgrade Stone: used for +25 → +28 (Ultimate). */
    public static final DeferredItem<Item> UPGRADE_STONE_TIER_3 = ITEMS.registerSimpleItem("upgrade_stone_tier_3",
            new Item.Properties());

    // ============================
    // Forged Stones — Tier 2
    // ============================

    public static final DeferredItem<Item> FORGED_STONE_FORTITUDE = ITEMS.registerSimpleItem("forged_stone_fortitude",
            new Item.Properties());
    public static final DeferredItem<Item> FORGED_STONE_AGILITY = ITEMS.registerSimpleItem("forged_stone_agility",
            new Item.Properties());
    public static final DeferredItem<Item> FORGED_STONE_DESTRUCTION = ITEMS
            .registerSimpleItem("forged_stone_destruction", new Item.Properties());

    // ============================
    // Forged Stones — Ultimate
    // ============================

    public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_FORTITUDE = ITEMS
            .registerSimpleItem("forged_stone_ultimate_fortitude", new Item.Properties());
    public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_AGILITY = ITEMS
            .registerSimpleItem("forged_stone_ultimate_agility", new Item.Properties());
    public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_DESTRUCTION = ITEMS
            .registerSimpleItem("forged_stone_ultimate_destruction", new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
