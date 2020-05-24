package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.Collections;

@RootCommand(
        literal = "unclaim",
        description = "Claim a chunk for you."
)
@Permission(Permissions.ClaimCommand)
@PlayerOnly
public class UnclaimCommand extends CommandExecutor {
    public UnclaimCommand() {
        register("unclaim", Collections.emptyList());
    }

    @SubCommand("unclaim")
    public void unclaim(VLPlayer sender) {
        if (!sender.getWorld().getName().contains("Survival")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.must-be-in-survival"));
            return;
        }
        if (sender.getGameMode() != GameMode.SURVIVAL) {
            return;  // I don't know what this means and what I was thinking about when I wrote this
            //          but it seems important so I just copied it here. (Magic code time)
        }
        Claim claim = Claim.getClaims().get(sender.getLocation().getChunk().getChunkKey());
        if (claim == null) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.not-claimed"));
            return;
        }
        if (!claim.owner.getUniqueId().toString().equals(sender.getUniqueId().toString())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.not-claimed"));
            return;
        }
        Claim.getClaims().remove(sender.getLocation().getChunk().getChunkKey());
        VaultCore.getInstance().getVLData().serializeList("claims", new ArrayList<>(Claim.getClaims().values()));
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.unclaimed").replace("{X}",
                String.valueOf(sender.getLocation().getChunk().getX())).replace("{Z}",
                String.valueOf(sender.getLocation().getChunk().getZ())));
    }
}
