package net.vaultmc.vaultcore.commands.tpa;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;

@RootCommand(
        literal = "tpdeny",
        description = "Deny a teleport request from a player."
)
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
	
    String string = Utilities.string;
	
    private static HashMap<UUID, UUID> requests = TPACommand.getRequests();

    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    public void tpdeny(CommandSender sender) {
        Player player = (Player) sender;
        if (requests.containsKey(player.getUniqueId())) {

            player.sendMessage(string + "You denied the teleportation request.");
            Bukkit.getPlayer(requests.get(player.getUniqueId()))
                    .sendMessage(VaultCoreAPI.getName(player) + string + " denied your teleportation request.");
            requests.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(ChatColor.RED + "You don't have a pending request!");
    }
}