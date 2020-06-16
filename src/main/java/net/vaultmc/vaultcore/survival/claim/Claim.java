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

package net.vaultmc.vaultcore.survival.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ds.ConfigurationSerializable;
import net.vaultmc.vaultloader.utils.ds.SerializableField;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationSerializable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Claim {
    @Getter
    private static final Map<Long, Claim> claims = new HashMap<>();

    static {
        List<Claim> claimList = VaultCore.getInstance().getVLData().deserializeList("claims", Claim.class);
        for (Claim claim : claimList) {
            claims.put(claim.chunk.getChunkKey(), claim);
        }
    }

    @SerializableField
    public VLOfflinePlayer owner;

    @SerializableField
    public Chunk chunk;
}
