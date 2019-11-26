package net.vaultmc.vaultcore.commands.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("tpdeny")
    public void tpdeny(CommandSender sender) {
        String string = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("string"));
        String variable1 = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("variable-1"));

        Player player = (Player) sender;
        if (requests.containsKey(player.getUniqueId())) {

            player.sendMessage(string + "You denied the teleportation request.");
            Bukkit.getPlayer(requests.get(player.getUniqueId()))
                    .sendMessage(variable1 + player.getName() + string + " denied your teleportation request.");
            requests.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(ChatColor.RED + "You don't have a pending request!");
    }
}