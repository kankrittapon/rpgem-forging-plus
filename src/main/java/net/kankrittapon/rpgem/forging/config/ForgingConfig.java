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

    /** The built ModConfigSpec that must be registered with the mod container. */
    public static final ModConfigSpec SPEC = BUILDER.build();
}
