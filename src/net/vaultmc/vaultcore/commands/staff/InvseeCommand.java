package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;

import org.bukkit.event.Listener;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "invsee", description = "Look in a players inventory.")
@Permission(Permissions.InvseeCommand)
@PlayerOnly
@Aliases("openinv")
public class InvseeCommand extends CommandExecutor implements Listener {
	public InvseeCommand() {
		register("invsee", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
		VaultCore.getInstance().registerEvents(this);
	}

	@SubCommand("invsee")
	public void invsee(VLPlayer sender, VLPlayer target) {
		if (sender == target) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.self_error"));
			return;
		}

		if (target.hasPermission(Permissions.InvseeExempt)) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.excempt_error"));
			return;
		}

		sender.openInventory(target.getInventory());
	}
}