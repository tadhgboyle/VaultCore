package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;

import java.util.Collections;

@RootCommand(literal = "gmc", description = "Change your gamemode to creative.")
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({ "creative", "gmcreative" })
public class GMCreativeCommand extends CommandExecutor {

	public GMCreativeCommand() {
		register("execute", Collections.emptyList());
	}

	@SubCommand("execute")
	public void execute(VLPlayer sender) {
		sender.setGameMode(GameMode.CREATIVE);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Creative"));
	}
}
