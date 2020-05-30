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

package net.vaultmc.vaultcore.misc.commands.donation;

import lombok.Getter;

public enum DonationRanks {

    DONOR("Donor", "donor", 5f),
    DONOR_PLUS("Donor+", "donorplus", 15f),
    DONOR_PLUS_PLUS("Donor++", "donorplusplus", 30f);

    @Getter
    private final String name;
    @Getter
    private final String luckPermsRole;
    @Getter
    private final float donationRequirement;

    DonationRanks(String name, String luckPermsRole, float donationRequirement) {
        this.name = name;
        this.luckPermsRole = luckPermsRole;
        this.donationRequirement = donationRequirement;
    }
}
