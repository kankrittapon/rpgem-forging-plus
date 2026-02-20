package net.kankrittapon.rpgem.forging.block.entity;

import net.kankrittapon.rpgem.core.init.ModAttachments;
import net.kankrittapon.rpgem.core.init.ModDataComponents;
import net.kankrittapon.rpgem.core.init.ModItems;
import net.kankrittapon.rpgem.forging.init.ModForgingItems;
import net.kankrittapon.rpgem.forging.init.ModForgingBlockEntities;
import net.kankrittapon.rpgem.forging.config.ForgingConfig;
import net.kankrittapon.rpgem.forging.menu.AncientForgeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class AncientForgeBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;

    public AncientForgeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModForgingBlockEntities.ANCIENT_FORGE_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.rpgem_forging.ancient_forge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AncientForgeMenu(containerId, playerInventory, this, this.data);
    }

    // ========================
    // NBT Save / Load
    // ========================

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            CompoundTag invTag = tag.getCompound("inventory");
            invTag.putInt("Size", 3); // ensure 3 slots
            itemHandler.deserializeNBT(registries, invTag);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    // ========================
    // Public Entry Point
    // ========================

    public void handleForgeAction(Level level, Player player) {
        ItemStack stone = this.itemHandler.getStackInSlot(1);
        if (stone.is(ModForgingItems.MEMORY_FRAGMENT.get())) {
            performRepair(level, player);
        } else {
            performUpgrade(level, player);
        }
    }

    // ========================
    // Repair Logic
    // ========================

    private void performRepair(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack material = this.itemHandler.getStackInSlot(1);
        ItemStack support = this.itemHandler.getStackInSlot(2);

        if (equipment.isEmpty() || material.isEmpty())
            return;

        if (!equipment.isDamageableItem()) {
            player.sendSystemMessage(Component.literal("§cThis item cannot be repaired!"));
            return;
        }
        if (equipment.getDamageValue() == 0) {
            player.sendSystemMessage(Component.literal("§aItem is already at full durability!"));
            return;
        }

        int upgradeLevel = equipment.getOrDefault(ModDataComponents.UPGRADE_LEVEL.get(), 0);
        int repairAmount;

        if (upgradeLevel > 0) {
            repairAmount = ForgingConfig.REPAIR_AMOUNT_EPIC_LEGENDARY.get();
        } else {
            net.minecraft.world.item.Rarity rarity = equipment.getRarity();
            if (rarity == net.minecraft.world.item.Rarity.COMMON)
                repairAmount = ForgingConfig.REPAIR_AMOUNT_COMMON.get();
            else if (rarity == net.minecraft.world.item.Rarity.UNCOMMON)
                repairAmount = ForgingConfig.REPAIR_AMOUNT_UNCOMMON.get();
            else if (rarity == net.minecraft.world.item.Rarity.RARE)
                repairAmount = ForgingConfig.REPAIR_AMOUNT_RARE.get();
            else
                repairAmount = ForgingConfig.REPAIR_AMOUNT_EPIC_LEGENDARY.get();
        }

        // Artisan's Memory boost
        boolean useArtisan = !support.isEmpty() && support.is(ModForgingItems.ARTISANS_MEMORY.get());
        if (useArtisan) {
            repairAmount *= ForgingConfig.ARTISAN_MEMORY_MULTIPLIER.get();
        }

        int newDamage = Math.max(0, equipment.getDamageValue() - repairAmount);
        equipment.setDamageValue(newDamage);
        material.shrink(1);

        if (useArtisan) {
            support.shrink(1);
            player.sendSystemMessage(Component.literal("§6[Artisan's Memory] Repair Boost Activated! (×"
                    + ForgingConfig.ARTISAN_MEMORY_MULTIPLIER.get() + ")"));
        }

        level.playSound(null, this.getBlockPos(),
                net.minecraft.sounds.SoundEvents.ANVIL_USE,
                net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        player.sendSystemMessage(Component.literal("§aItem repaired! (+" + repairAmount + " Durability)"));
    }

    // ========================
    // Upgrade Logic
    // ========================

    private void performUpgrade(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack stone = this.itemHandler.getStackInSlot(1);
        ItemStack support = this.itemHandler.getStackInSlot(2);

        if (equipment.isEmpty() || stone.isEmpty())
            return;

        int currentLevel = equipment.getOrDefault(ModDataComponents.UPGRADE_LEVEL.get(), 0);
        int nextLevel = currentLevel + 1;

        // ===== Tier determination =====
        int tier = 0;
        boolean isForgedStone = false;
        String forgedType = "";

        if (stone.is(ModForgingItems.UPGRADE_STONE_TIER_1.get()))
            tier = 1;
        else if (stone.is(ModForgingItems.UPGRADE_STONE_TIER_2.get()))
            tier = 2;
        else if (stone.is(ModForgingItems.UPGRADE_STONE_TIER_3.get()))
            tier = 3;
        else {
            forgedType = getForgedStoneType(stone);
            if (!forgedType.isEmpty()) {
                isForgedStone = true;
                tier = isUltimateForgedStone(stone) ? 3 : 2;
            }
        }

        if (tier == 0) {
            player.sendSystemMessage(Component.literal("§cInvalid upgrade material!"));
            return;
        }

        // ===== Durability validation =====
        if (equipment.isDamageableItem()) {
            int durability = equipment.getMaxDamage() - equipment.getDamageValue();
            if (currentLevel <= 15 && durability <= 20) {
                player.sendSystemMessage(Component.literal("§cDurability too low! Repair required (>20)."));
                player.playNotifySound(net.minecraft.sounds.SoundEvents.ANVIL_HIT,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            if (currentLevel > 15 && durability <= 0) {
                player.sendSystemMessage(Component.literal("§cItem is broken! Repair required."));
                player.playNotifySound(net.minecraft.sounds.SoundEvents.ANVIL_HIT,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
        }

        // ===== Level range validation =====
        if (tier == 1 && currentLevel >= 15) {
            player.sendSystemMessage(Component.literal("§cNeeds Tier 2 or Forged Stone!"));
            return;
        }
        if (tier == 2 && (currentLevel < 15 || currentLevel >= 25)) {
            player.sendSystemMessage(Component.literal(
                    currentLevel < 15 ? "§cItem level too low for Tier 2!" : "§cNeeds Tier 3 / Ultimate stone!"));
            return;
        }
        if (tier == 3 && currentLevel < 25) {
            player.sendSystemMessage(Component.literal("§cItem level too low for Tier 3!"));
            return;
        }
        if (tier == 3 && currentLevel >= 28) {
            player.sendSystemMessage(Component.literal("§6Item is already at Max Level!"));
            return;
        }

        // ===== Fail Stack & success rate =====
        double baseChance;
        if (tier == 1)
            baseChance = ForgingConfig.UPGRADE_SUCCESS_RATE_TIER_1.get();
        else if (tier == 2)
            baseChance = ForgingConfig.UPGRADE_SUCCESS_RATE_TIER_2.get();
        else
            baseChance = ForgingConfig.UPGRADE_SUCCESS_RATE_TIER_3.get();

        int failStack = player.getData(ModAttachments.FAIL_STACK);
        double successRate = Math.min(1.0, baseChance + failStack * ForgingConfig.FAILSTACK_BONUS_PER_STACK.get());

        player.displayClientMessage(Component.literal(
                "§eChance: " + (int) (successRate * 100) + "% (Base: "
                        + (int) (baseChance * 100) + "% + FS: " + failStack + ")"),
                true);

        boolean success = level.random.nextDouble() < successRate;
        stone.shrink(1);

        if (success) {
            equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), nextLevel);
            updateItemName(equipment, nextLevel);

            // Set armor path on first Forged Stone use
            if (isForgedStone && isArmor(equipment)) {
                String currentPath = equipment.getOrDefault(ModDataComponents.ARMOR_PATH.get(), "none");
                if (currentPath.equals("none")) {
                    String newPath = forgedType.equals("fortitude") ? "reduction" : "evasion";
                    equipment.set(ModDataComponents.ARMOR_PATH.get(), newPath);
                }
            }

            // Reset fail stack
            player.setData(ModAttachments.FAIL_STACK, 0);
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(sp,
                        new net.kankrittapon.rpgem.forging.network.PacketSyncFailStack(0));
            }

            player.sendSystemMessage(Component.literal(
                    "§a✦ Upgrade Successful! New Level: " + getFormattedLevel(nextLevel)));
            level.playSound(null, this.getBlockPos(),
                    net.minecraft.sounds.SoundEvents.ANVIL_USE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);

        } else {
            // Increment fail stack
            int newFs = failStack + 1;
            player.setData(ModAttachments.FAIL_STACK, newFs);
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(sp,
                        new net.kankrittapon.rpgem.forging.network.PacketSyncFailStack(newFs));
            }

            // Durability penalty
            if (equipment.isDamageableItem()) {
                int newDamage = Math.min(equipment.getMaxDamage(),
                        equipment.getDamageValue() + ForgingConfig.DURABILITY_PENALTY_ON_FAILURE.get());
                equipment.setDamageValue(newDamage);
            }

            // Downgrade logic (Tier 2/3)
            if (tier >= 2 || currentLevel >= 15) {
                boolean hasProtection = !support.isEmpty()
                        && support.is(ModForgingItems.PROTECTION_STONE.get());
                if (hasProtection) {
                    player.sendSystemMessage(Component.literal("§b[Protection Stone] Item saved from Downgrade!"));
                    support.shrink(1);
                } else {
                    int downgradedLevel = Math.max(15, currentLevel - 1);
                    if (downgradedLevel < currentLevel) {
                        equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), downgradedLevel);
                        updateItemName(equipment, downgradedLevel);
                        player.sendSystemMessage(Component.literal(
                                "§c⚠ Upgrade Failed! Item downgraded to " + getFormattedLevel(downgradedLevel)));
                    } else {
                        player.sendSystemMessage(Component.literal(
                                "§c✦ Upgrade Failed! (Fail Stack: " + newFs + ")"));
                    }
                }
            } else {
                player.sendSystemMessage(Component.literal(
                        "§c✦ Upgrade Failed! (Fail Stack: " + newFs + ")"));
            }

            level.playSound(null, this.getBlockPos(),
                    net.minecraft.sounds.SoundEvents.ANVIL_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    // ========================
    // Helper Methods
    // ========================

    private boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.world.item.ArmorItem;
    }

    private boolean isWeapon(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.world.item.SwordItem
                || stack.getItem() instanceof net.minecraft.world.item.AxeItem;
    }

    private String getForgedStoneType(ItemStack stone) {
        if (stone.is(ModForgingItems.FORGED_STONE_FORTITUDE.get())
                || stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_FORTITUDE.get()))
            return "fortitude";
        if (stone.is(ModForgingItems.FORGED_STONE_AGILITY.get())
                || stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_AGILITY.get()))
            return "agility";
        if (stone.is(ModForgingItems.FORGED_STONE_DESTRUCTION.get())
                || stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_DESTRUCTION.get()))
            return "destruction";
        return "";
    }

    private boolean isUltimateForgedStone(ItemStack stone) {
        return stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_FORTITUDE.get())
                || stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_AGILITY.get())
                || stone.is(ModForgingItems.FORGED_STONE_ULTIMATE_DESTRUCTION.get());
    }

    private void updateItemName(ItemStack stack, int upgradeLevel) {
        Component originalName;
        String originalJson;

        if (stack.has(ModDataComponents.ORIGINAL_NAME.get())) {
            originalJson = stack.get(ModDataComponents.ORIGINAL_NAME.get());
            originalName = Component.Serializer.fromJson(originalJson,
                    this.level != null ? this.level.registryAccess() : null);
            if (originalName == null)
                originalName = stack.getHoverName();
        } else {
            originalName = stack.getHoverName();
            net.minecraft.core.RegistryAccess ra = this.level != null ? this.level.registryAccess() : null;
            originalJson = Component.Serializer.toJson(originalName, ra);
            stack.set(ModDataComponents.ORIGINAL_NAME.get(), originalJson);
        }

        String prefix = getUpgradePrefix(upgradeLevel);
        if (!prefix.isEmpty()) {
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME,
                    Component.literal(prefix + ": ").append(originalName));
        }
    }

    private String getUpgradePrefix(int level) {
        if (level <= 0)
            return "";
        if (level <= 15)
            return "§7[+" + level + "]";
        return switch (level) {
            case 16 -> "§a[PRI]";
            case 17 -> "§b[DUO]";
            case 18 -> "§e[TRI]";
            case 19 -> "§6[TET]";
            case 20 -> "§c[PEN]";
            case 21 -> "§4[HEX]";
            case 22 -> "§5[SEP]";
            case 23 -> "§d[OCT]";
            case 24 -> "§9[NOV]";
            case 25 -> "§5§k[DEC]";
            default -> "§5[Ultimate]";
        };
    }

    private String getFormattedLevel(int level) {
        return getUpgradePrefix(level);
    }
}
