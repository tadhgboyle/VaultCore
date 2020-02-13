package net.vaultmc.vaultcore.teleport;

import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "back", description = "Teleport to your previous location.")
@Permission(Permissions.BackCommand)
@PlayerOnly
public class BackCommand extends CommandExecutor {
	public BackCommand() {
		this.register("back", Collections.emptyList());
	}

	@SubCommand("back")
	public void back(VLPlayer player) {
		if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {
			Location before = PlayerTPListener.teleports.get(player.getUniqueId());
			player.teleport(before);
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.success"));
			PlayerTPListener.teleports.remove(player.getUniqueId());
		} else {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.no_teleport_location"));
		}
	}

	@EventHandler
	public void deathRemove(PlayerDeathEvent event) {
		if (PlayerTPListener.teleports.containsKey(event.getEntity().getUniqueId())) {
			PlayerTPListener.teleports.remove(event.getEntity().getUniqueId());
		}
	}
}