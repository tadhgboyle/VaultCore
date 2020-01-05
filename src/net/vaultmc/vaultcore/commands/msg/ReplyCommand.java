package net.vaultmc.vaultcore.commands.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RootCommand(literal = "r", description = "Reply to a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases("reply")
public class ReplyCommand extends CommandExecutor {
	public ReplyCommand() {
		this.register("r", Arrays.asList(Arguments.createArgument("message", Arguments.greedyString())));
	}

	@SubCommand("r")
	public void reply(CommandSender sender, String message) {
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(MsgCommand.getReplies().get(player.getUniqueId()));

		if (target == null) {
			player.sendMessage(ChatColor.RED + "You have no one to reply to!");
			return;
		}
		if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.msg")) {
			player.sendMessage(ChatColor.RED + "That player has disabled messaging!");
			return;
		}
		if (MsgCommand.getReplies().containsKey(player.getUniqueId())) {
			String meTo = VaultCoreAPI.getName(player) + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ VaultCoreAPI.getName(target) + ChatColor.YELLOW + ":";
			String toMe = VaultCoreAPI.getName(player) + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ VaultCoreAPI.getName(target) + ChatColor.YELLOW + ":";
			player.sendMessage(meTo + " " + ChatColor.RESET + message);
			target.sendMessage(toMe + " " + ChatColor.RESET + message);
			MsgCommand.getReplies().put(target.getUniqueId(), player.getUniqueId());
		} else {
			player.sendMessage(ChatColor.RED + "You have no one to reply to!");
		}
	}
}
