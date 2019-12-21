package net.vaultmc.vaultcore.commands.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(
        literal = "tpahere",
        description = "Request for a player to teleport you."
)
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor {
    private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

    public TPAHereCommand() {
        register("tpahere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())), "vaultcore");
    }

    @SubCommand("tpahere")
    public void tpaHere(CommandSender sender, Player target) {
        String string = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("string"));
        String variable1 = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("variable-1"));

        Player player = (Player) sender;
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
            return;
        }
        if (VaultCore.getInstance().getPlayerData()
                .getBoolean("players." + target.getUniqueId() + ".settings.tpa") == false) {
            player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
            return;
        }
        if (VaultCore.getInstance().getPlayerData()
                .getBoolean("players." + target.getUniqueId() + ".settings.autotpa") == true) {
            player.teleport(target);
            player.sendMessage(string + "Teleported to " + variable1 + VaultCoreAPI.getName(target) + string + ".");
            target.sendMessage(variable1 + VaultCoreAPI.getName(player) + string + " has teleported to you.");
        } else {
            requestsHere.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(
                    string + "You requested that " + variable1 + VaultCoreAPI.getName(target) + string + " teleports to you.");
            target.sendMessage(variable1 + VaultCoreAPI.getName(player) + string + " asked you to teleport to them, type "
                    + variable1 + "/tpaccept " + string + "to accept it.");
        }
    }
}