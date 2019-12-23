package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RootCommand(
        literal = "gamemode",
        description = "Change your (or other's) game mode."
)
@Permission(Permissions.GamemodeCommand)
@Aliases({"gm", "gmode"})
public class GameModeCommand extends CommandExecutor {
    public GameModeCommand() {
        unregisterExisting();
        register("gmCrSelf", Collections.singletonList(Arguments.createLiteral("creative")), "vaultcore");
        register("gmSvSelf", Collections.singletonList(Arguments.createLiteral("survival")), "vaultcore");
        register("gmSpSelf", Collections.singletonList(Arguments.createLiteral("spectator")), "vaultcore");
        register("gmAdSelf", Collections.singletonList(Arguments.createLiteral("adventure")), "vaultcore");

        register("gmCrOthers", Arrays.asList(
                Arguments.createLiteral("creative"),
                Arguments.createArgument("target", Arguments.playersArgument())
        ));
        register("gmSvOthers", Arrays.asList(
                Arguments.createLiteral("survival"),
                Arguments.createArgument("target", Arguments.playersArgument())
        ));
        register("gmSpOthers", Arrays.asList(
                Arguments.createLiteral("spectator"),
                Arguments.createArgument("target", Arguments.playersArgument())
        ));
        register("gmAdOthers", Arrays.asList(
                Arguments.createLiteral("adventure"),
                Arguments.createArgument("target", Arguments.playersArgument())
        ));
    }

    @SubCommand("gmCrSelf")
    @PlayerOnly
    public void gamemodeCreative(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.CREATIVE);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "creative" + ChatColor.YELLOW + ".");
    }

    @SubCommand("gmSvSelf")
    @PlayerOnly
    public void gamemodeSurvival(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "survival" + ChatColor.YELLOW + ".");
    }

    @SubCommand("gmSpSelf")
    @PlayerOnly
    public void gamemodeSpectator(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SPECTATOR);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "spectator" + ChatColor.YELLOW + ".");
    }

    @SubCommand("gmAdSelf")
    @PlayerOnly
    public void gamemodeAdventure(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.ADVENTURE);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "adventure" + ChatColor.YELLOW + ".");
    }

    @SubCommand("gmCrOthers")
    @Permission(Permissions.GamemodeCommandOther)
    public void gamemodeCreativeOthers(CommandSender sender, Collection<Player> target) {
        for (Player player : target) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + ChatColor.YELLOW + " set your game mode to " + ChatColor.GOLD + "creative" + ChatColor.YELLOW + ".");
            sender.sendMessage(ChatColor.YELLOW + "Set " + VaultCoreAPI.getName(player) + ChatColor.YELLOW + "'s game mode to " + ChatColor.GOLD + "creative" + ChatColor.YELLOW + ".");
        }
    }

    @SubCommand("gmSvOthers")
    @Permission(Permissions.GamemodeCommandOther)
    public void gamemodeSurvivalOthers(CommandSender sender, Collection<Player> target) {
        for (Player player : target) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + ChatColor.YELLOW + " set your game mode to " + ChatColor.GOLD + "survival" + ChatColor.YELLOW + ".");
            sender.sendMessage(ChatColor.YELLOW + "Set " + VaultCoreAPI.getName(player) + ChatColor.YELLOW + "'s game mode to " + ChatColor.GOLD + "survival" + ChatColor.YELLOW + ".");
        }
    }

    @SubCommand("gmSpOthers")
    @Permission(Permissions.GamemodeCommandOther)
    public void gamemodeSpectatorOthers(CommandSender sender, Collection<Player> target) {
        for (Player player : target) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + ChatColor.YELLOW + " set your game mode to " + ChatColor.GOLD + "spectator" + ChatColor.YELLOW + ".");
            sender.sendMessage(ChatColor.YELLOW + "Set " + VaultCoreAPI.getName(player) + ChatColor.YELLOW + "'s game mode to " + ChatColor.GOLD + "spectator" + ChatColor.YELLOW + ".");
        }
    }

    @SubCommand("gmAdOthers")
    @Permission(Permissions.GamemodeCommandOther)
    public void gamemodeAdventureOthers(CommandSender sender, Collection<Player> target) {
        for (Player player : target) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage((sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) + ChatColor.YELLOW + " set your game mode to " + ChatColor.GOLD + "adventure" + ChatColor.YELLOW + ".");
            sender.sendMessage(ChatColor.YELLOW + "Set " + VaultCoreAPI.getName(player) + ChatColor.YELLOW + "'s game mode to " + ChatColor.GOLD + "adventure" + ChatColor.YELLOW + ".");
        }
    }
}
