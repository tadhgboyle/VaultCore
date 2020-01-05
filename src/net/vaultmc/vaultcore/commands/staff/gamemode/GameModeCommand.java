package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RootCommand(literal = "gamemode", description = "Change your (or other's) game mode.")
@Permission(Permissions.GamemodeCommand)
@Aliases({ "gm", "gmode" })
public class GameModeCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

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
	public void gamemodeCreative(CommandSender sender) {
		((Player) sender).setGameMode(GameMode.CREATIVE);
		sender.sendMessage(string + "Your game mode is now " + variable1 + "Creative" + string + ".");
	}

	@SubCommand("gmSvSelf")
	@PlayerOnly
	public void gamemodeSurvival(CommandSender sender) {
		((Player) sender).setGameMode(GameMode.SURVIVAL);
		sender.sendMessage(string + "Your game mode is now " + variable1 + "Survival" + string + ".");
	}

	@SubCommand("gmSpSelf")
	@PlayerOnly
	public void gamemodeSpectator(CommandSender sender) {
		((Player) sender).setGameMode(GameMode.SPECTATOR);
		sender.sendMessage(string + "Your game mode is now " + variable1 + "Spectator" + string + ".");
	}

	@SubCommand("gmAdSelf")
	@PlayerOnly
	public void gamemodeAdventure(CommandSender sender) {
		((Player) sender).setGameMode(GameMode.ADVENTURE);
		sender.sendMessage(string + "Your game mode is now " + variable1 + "Adventure" + string + ".");
	}

	@SubCommand("gmCrOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeCreativeOthers(CommandSender sender, Collection<Player> target) {
		for (Player player : target) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
					: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + string
					+ " set your game mode to " + variable1 + "Creative" + string + ".");
			sender.sendMessage(string + "Set " + VaultCoreAPI.getName(player) + string + "'s game mode to " + variable1
					+ "creative" + string + ".");
		}
	}

	@SubCommand("gmSvOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeSurvivalOthers(CommandSender sender, Collection<Player> target) {
		for (Player player : target) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
					: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + string
					+ " set your game mode to " + variable1 + "Survival" + string + ".");
			sender.sendMessage(string + "Set " + VaultCoreAPI.getName(player) + string + "'s game mode to " + variable1
					+ "survival" + string + ".");
		}
	}

	@SubCommand("gmSpOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeSpectatorOthers(CommandSender sender, Collection<Player> target) {
		for (Player player : target) {
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
					: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + string
					+ " set your game mode to " + variable1 + "Spectator" + string + ".");
			sender.sendMessage(string + "Set " + VaultCoreAPI.getName(player) + string + "'s game mode to " + variable1
					+ "spectator" + string + ".");
		}
	}

	@SubCommand("gmAdOthers")
	@Permission(Permissions.GamemodeCommandOther)
	public void gamemodeAdventureOthers(CommandSender sender, Collection<Player> target) {
		for (Player player : target) {
			player.setGameMode(GameMode.ADVENTURE);
			player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
					: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + string
					+ " set your game mode to " + variable1 + "Adventure" + string + ".");
			sender.sendMessage(string + "Set " + VaultCoreAPI.getName(player) + string + "'s game mode to " + variable1
					+ "adventure" + string + ".");
		}
	}
}
