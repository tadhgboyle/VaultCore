package net.vaultmc.vaultcore.commands.teleport;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor {
	@Getter
	private static HashMap<UUID, UUID> requests = new HashMap<>();
	@Getter
	private static HashMap<UUID, UUID> requestsHere = new HashMap<>();

	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public TPACommand() {
		register("tpa", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

    @SubCommand("tpa")
    public void tpa(VLPlayer player, VLPlayer target) {
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
            return;
        }
        if (!player.getDataConfig().getBoolean("settings.tpa")) {
            player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
        } else if (player.getDataConfig()
                .getBoolean("settings.autotpa")) {
            player.teleport(target);
            player.sendMessage(string + "Teleported to " + target.getFormattedName() + string + ".");
            target.sendMessage(player.getFormattedName() + string + " has teleported to you.");
        } else {
            requests.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(
                    string + "You sent a teleport request to " + target.getFormattedName() + string + ".");
            target.sendMessage(player.getFormattedName() + string + " sent you a teleport request, type " + variable1
                    + "/tpaccept " + string + "to accept it.");
        }
	}
}