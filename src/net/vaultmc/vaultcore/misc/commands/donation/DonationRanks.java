package net.vaultmc.vaultcore.misc.commands.donation;

import lombok.Data;
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
