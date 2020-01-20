package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "staffchat", description = "Use staff chat.")
@Permission(Permissions.StaffChatCommand)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor {
	public static final Set<UUID> toggled = new HashSet<>();

	public StaffChatCommand() {
		register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
		register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
	}

	@SubCommand("chat")
	public void chat(VLCommandSender sender, String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("vc.sc")) {
				player.sendMessage(
						Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.format"),
								sender.getFormattedName(), message));
			}
		}
	}

	@SubCommand("toggle")
	@PlayerOnly
	public void toggle(VLPlayer player) {
		if (toggled.contains(player.getUniqueId())) {
			toggled.remove(player.getUniqueId());
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "disabled"));
		} else {
			toggled.add(player.getUniqueId());
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "enabled"));
		}
	}
}