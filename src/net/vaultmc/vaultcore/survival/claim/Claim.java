package net.vaultmc.vaultcore.survival.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
