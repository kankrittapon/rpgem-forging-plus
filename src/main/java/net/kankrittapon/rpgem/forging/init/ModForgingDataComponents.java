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

        // Data components moved to rpgem-core: ModDataComponents
        // UPGRADE_LEVEL, ARMOR_PATH, ORIGINAL_NAME

        private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
                        String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
                return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
        }

        public static void register(IEventBus eventBus) {
                DATA_COMPONENTS.register(eventBus);
        }
}
