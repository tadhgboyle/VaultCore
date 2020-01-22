package net.vaultmc.vaultcore.commands.staff.gamemode;

import java.util.Collections;

import org.bukkit.GameMode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "gmsp", description = "Change your gamemode to spectator.")
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({ "spec", "gmspec", "spectator", "gmspectator" })
public class GMSpectatorCommand extends CommandExecutor {

	public GMSpectatorCommand() {
		register("execute", Collections.emptyList());
	}

	@SubCommand("execute")
	public void execute(VLPlayer sender) {
		sender.setGameMode(GameMode.SPECTATOR);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Spectator"));
	}
}
