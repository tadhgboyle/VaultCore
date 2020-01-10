package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.*;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collections;

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
	public void invsee(Player sender, Player target) {
		if (sender == target) {
			sender.sendMessage(ChatColor.RED + "You can't open your own inventory!");
			return;
		}

		if (target.hasPermission(Permissions.InvseeExempt)) {
			sender.sendMessage(ChatColor.RED + "You can't open this player's inventory!");
			return;
		}

		sender.openInventory(target.getInventory());
	}
}