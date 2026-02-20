package net.kankrittapon.rpgem.forging.init;

import com.mojang.serialization.Codec;
import net.kankrittapon.rpgem.forging.RPGEMForging;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

/**
 * Registers Item Data Components used by the Forging system.
 * These are stored per-ItemStack and survive persistence/network sync.
 */
public class ModForgingDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(RPGEMForging.MODID);

    /** Current upgrade level of a piece of equipment (+1 to +28/Ultimate). */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UPGRADE_LEVEL = register(
            "upgrade_level",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    /**
     * Armor upgrade path chosen with first Forged Stone application.
     * Values: "none" | "reduction" | "evasion"
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ARMOR_PATH = register(
            "armor_path",
            builder -> builder.persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    /**
     * Serialised JSON of the item's original display name (before upgrade prefix).
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ORIGINAL_NAME = register(
            "original_name",
            builder -> builder.persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
            String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
