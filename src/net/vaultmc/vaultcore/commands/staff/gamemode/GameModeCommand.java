package net.vaultmc.vaultcore.commands.staff.gamemode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.GameMode;

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

@RootCommand(literal = "gamemode", description = "Change your (or other's) game mode.")
@Permission(Permissions.GamemodeCommand)
@Aliases({ "gm", "gmode" })
public class GameModeCommand extends CommandExecutor {

	public GameModeCommand() {
		unregisterExisting();
		register("gmCrSelf", Collections.singletonList(Arguments.createLiteral("creative")));
		register("gmSvSelf", Collections.singletonList(Arguments.createLiteral("survival")));
		register("gmSpSelf", Collections.singletonList(Arguments.createLiteral("spectator")));
		register("gmAdSelf", Collections.singletonList(Arguments.createLiteral("adventure")));

		register("gmCrOthers", Arrays.asList(Arguments.createLiteral("creative"),
				Arguments.createArgument("target", Arguments.playersArgument())));
		register("gmSvOthers", Arrays.asList(Arguments.createLiteral("survival"),
				Arguments.createArgument("target", Arguments.playersArgument())));
		register("gmSpOthers", Arrays.asList(Arguments.createLiteral("spectator"),
				Arguments.createArgument("target", Arguments.playersArgument())));
		register("gmAdOthers", Arrays.asList(Arguments.createLiteral("adventure"),
				Arguments.createArgument("target", Arguments.playersArgument())));
	}

	@SubCommand("gmCrSelf")
	@PlayerOnly
	public void gamemodeCreative(VLPlayer sender) {
		sender.setGameMode(GameMode.CREATIVE);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Creative"));
	}

	@SubCommand("gmSvSelf")
	@PlayerOnly
	public void gamemodeSurvival(VLPlayer sender) {
		sender.setGameMode(GameMode.SURVIVAL);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Survival"));
	}

	@SubCommand("gmSpSelf")
	@PlayerOnly
	public void gamemodeSpectator(VLPlayer sender) {
		sender.setGameMode(GameMode.SPECTATOR);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Spectator"));
	}

	@SubCommand("gmAdSelf")
	@PlayerOnly
	public void gamemodeAdventure(VLPlayer sender) {
		sender.setGameMode(GameMode.ADVENTURE);
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Adventure"));
	}

	@SubCommand("gmCrOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeCreativeOthers(VLCommandSender sender, Collection<VLPlayer> target) {
		for (VLPlayer player : target) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_receiver"),
							sender.getFormattedName(), "Creative"));
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_sender"),
							player.getFormattedName(), "Creative"));
		}
	}

	@SubCommand("gmSvOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeSurvivalOthers(VLCommandSender sender, Collection<VLPlayer> target) {
		for (VLPlayer player : target) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_receiver"),
							sender.getFormattedName(), "Survival"));
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_sender"),
							player.getFormattedName(), "Survival"));

		}
	}

	@SubCommand("gmSpOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeSpectatorOthers(VLCommandSender sender, Collection<VLPlayer> target) {
		for (VLPlayer player : target) {
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_receiver"),
							sender.getFormattedName(), "Spectator"));
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_sender"),
							player.getFormattedName(), "Spectator"));

		}
	}

	@SubCommand("gmAdOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeAdventureOthers(VLCommandSender sender, Collection<VLPlayer> target) {
		for (VLPlayer player : target) {
			player.setGameMode(GameMode.ADVENTURE);
			player.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_receiver"),
							sender.getFormattedName(), "Adventure"));
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.other_sender"),
							player.getFormattedName(), "Adventure"));

		}
	}
}
