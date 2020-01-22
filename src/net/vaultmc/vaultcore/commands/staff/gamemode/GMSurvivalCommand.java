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

@RootCommand(literal = "gms", description = "Change your gamemode to survival.")
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({ "survival", "gmsurvival" })
public class GMSurvivalCommand extends CommandExecutor {

	public GMSurvivalCommand() {
		register("execute", Collections.emptyList());
	}

	@SubCommand("execute")
	public void execute(VLPlayer sender) {
		sender.setGameMode(GameMode.SURVIVAL);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Survival"));
	}
}