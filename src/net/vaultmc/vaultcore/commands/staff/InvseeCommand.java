package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
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
    public void invsee(VLPlayer sender, VLPlayer target) {
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