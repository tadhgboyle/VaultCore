package net.vaultmc.vaultcore.commands.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(
        literal = "tpdeny",
        description = "Deny a teleport request from a player."
)
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
    private static HashMap<UUID, UUID> requests = TPACommand.getRequests();
    String string = Utilities.string;

    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    public void tpdeny(VLPlayer player) {
        if (requests.containsKey(player.getUniqueId())) {
            player.sendMessage(string + "You denied the teleportation request.");
            Bukkit.getPlayer(requests.get(player.getUniqueId()))
                    .sendMessage(player.getFormattedName() + string + " denied your teleportation request.");
            requests.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(ChatColor.RED + "You don't have a pending request!");
    }
}