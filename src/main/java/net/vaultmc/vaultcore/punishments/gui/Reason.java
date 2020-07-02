/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.punishments.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum Reason {
    SENSITIVE_DISCUSSION("sensitive-discussion", PunishmentType.BAN, Material.IRON_AXE, "1 month", "permanent", "permanent"),
    EXPLOIT_BUG("exploit-bug", PunishmentType.BAN, Material.REDSTONE, "14 days", "30 days", "permanent"),
    DOXXING("doxxing", PunishmentType.BAN, Material.IRON_DOOR, "30 days", "permanent", "permanent"),
    CHEATING("cheating", PunishmentType.BAN, Material.STONE_SWORD, "30 days", "permanent", "permanent"),
    DISRESPECTFUL_TO_STAFF("disrespectful-to-staff", "Only when extremely disrespectful.",
            PunishmentType.KICK, Material.ACACIA_FENCE, "permanent", "permanent", "permanent"),  // I add this because I thought there isn't a kick punishment.
    INTOLERABLE_UNFRIENDLINESS("intolerable-unfriendliness", PunishmentType.MUTE, Material.STICK, "7 days", "14 days", "30 days"),
    DEATH_THREAT("death-threat", "Also serious attacks.",
            PunishmentType.BAN, Material.PLAYER_HEAD, "2 months", "permanent", "permanent"),
    BAN_EVASION("ban-evasion", "Also permanently ban the main account.",
            PunishmentType.BAN, Material.POTION, "permanent", "permanent", "permanent"),
    GRIEFING("griefing", "Not applicable in Factions and Kingdoms.",
            PunishmentType.BAN, Material.TNT, "1 day", "3 days", "14 days"),
    INAPPROPRIATE_BUILD("inappropriate-build", "The build should also be erased.",
            PunishmentType.BAN, Material.WOODEN_AXE, "1 day", "3 days", "7 days"),
    ADVERTISING("advertising", PunishmentType.MUTE, Material.GOLDEN_HOE, "1 day", "3 days", "7 days"),
    STAFF_IMPERSONATION("staff-impersonation", PunishmentType.KICK, Material.NAME_TAG, "permanent", "permanent", "permanent");


    private final String key;
    @Getter
    private final String additionalInfo;
    @Getter
    private final PunishmentType punishment;
    @Getter
    private final Material item;
    @Getter
    private final String offense1;
    @Getter
    private final String offense2;
    @Getter
    private final String offense3;

    Reason(String key, PunishmentType punishment, Material item, String offense1, String offense2, String offense3) {
        this.key = key;
        this.punishment = punishment;
        this.item = item;
        this.offense1 = offense1;
        this.offense2 = offense2;
        this.offense3 = offense3;
        this.additionalInfo = null;
    }

    public String getKey() {
        return "punishments.reasons." + key;
    }
}
