package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Chunk;

import java.util.Collections;
import java.util.List;

import static net.vaultmc.vaultcore.survival.claim.ClaimCommand.deserializeChunks;
import static net.vaultmc.vaultcore.survival.claim.ClaimCommand.serializeChunks;

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
        List<Chunk> chunks = deserializeChunks(sender.getPlayerData().getStringList("claim.chunks"));
        if (!chunks.contains(sender.getLocation().getChunk())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.not-claimed"));
            return;
        }
        chunks.remove(sender.getLocation().getChunk());
        sender.getPlayerData().set("claim.chunks", serializeChunks(chunks));
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.unclaimed").replace("{X}",
                String.valueOf(sender.getLocation().getChunk().getX())).replace("{Z}",
                String.valueOf(sender.getLocation().getChunk().getZ())));
        sender.getPlayer().setLevel(sender.getPlayer().getLevel() + 1);
    }
}