package net.kankrittapon.rpgem.forging.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Common (server-side) configuration for the Forging system.
 * Controls base upgrade success rates used by
 * {@link net.kankrittapon.rpgem.forging.block.entity.AncientForgeBlockEntity}.
 */
public class ForgingConfig {

        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        // ==========================================
        // Upgrade System Settings (Ancient Forge)
        // ==========================================

        /** Base success rate for Tier 1 upgrades (+1 to +15). Default: 70% */
        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_1 = BUILDER
                        .comment("Base success rate for Tier 1 upgrades (+1 to +15). Default: 0.7 (70%)")
                        .defineInRange("upgradeSuccessRateTier1", 0.7, 0.0, 1.0);

        /** Base success rate for Tier 2 upgrades (PRI to DEC). Default: 40% */
        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_2 = BUILDER
                        .comment("Base success rate for Tier 2 upgrades (I to X). Default: 0.4 (40%)")
                        .defineInRange("upgradeSuccessRateTier2", 0.4, 0.0, 1.0);

        /** Base success rate for Tier 3 upgrades (Ultimate). Default: 15% */
        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_3 = BUILDER
                        .comment("Base success rate for Tier 3 upgrades (Ultimate+). Default: 0.15 (15%)")
                        .defineInRange("upgradeSuccessRateTier3", 0.15, 0.0, 1.0);

        // ==========================================
        // Repair System Settings
        // ==========================================

        public static final ModConfigSpec.IntValue REPAIR_AMOUNT_COMMON = BUILDER
                        .comment("Durability restored for Common items per Memory Fragment. Default: 10")
                        .defineInRange("repairAmountCommon", 10, 1, 100);

        public static final ModConfigSpec.IntValue REPAIR_AMOUNT_UNCOMMON = BUILDER
                        .comment("Durability restored for Uncommon items per Memory Fragment. Default: 5")
                        .defineInRange("repairAmountUncommon", 5, 1, 100);

        public static final ModConfigSpec.IntValue REPAIR_AMOUNT_RARE = BUILDER
                        .comment("Durability restored for Rare items per Memory Fragment. Default: 2")
                        .defineInRange("repairAmountRare", 2, 1, 100);

        public static final ModConfigSpec.IntValue REPAIR_AMOUNT_EPIC_LEGENDARY = BUILDER
                        .comment("Durability restored for Epic/Legendary/Upgraded items per Memory Fragment. Default: 1")
                        .defineInRange("repairAmountEpicLegendary", 1, 1, 100);

        public static final ModConfigSpec.IntValue ARTISAN_MEMORY_MULTIPLIER = BUILDER
                        .comment("Multiplier for repair amount when using Artisan's Memory. Default: 5")
                        .defineInRange("artisanMemoryMultiplier", 5, 1, 20);

        // ==========================================
        // Failure & Penalty Settings
        // ==========================================

        public static final ModConfigSpec.DoubleValue FAILSTACK_BONUS_PER_STACK = BUILDER
                        .comment("Success chance bonus added per fail stack (0.01 = 1%). Default: 0.01 (1%)")
                        .defineInRange("failstackBonusPerStack", 0.01, 0.0, 0.1);

        public static final ModConfigSpec.IntValue DURABILITY_PENALTY_ON_FAILURE = BUILDER
                        .comment("Durability lost on failed upgrade attempt. Default: 10")
                        .defineInRange("durabilityPenaltyOnFailure", 10, 0, 100);

        /** The built ModConfigSpec that must be registered with the mod container. */
        public static final ModConfigSpec SPEC = BUILDER.build();
}
