package net.vaultmc.vaultcore.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultutils.VaultUtils;

@RootCommand(
        literal = "afk",
        description = "Toggles your (or a player's) AFK state."
)
@Permission(Permissions.AFKCommand)
public class AFKCommand extends CommandExecutor implements Listener {
	
	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	
    @Getter
    private static final Map<Player, Boolean> afk = new HashMap<>();  // I have no plans of saving this in data.yml

    public AFKCommand() {
        Bukkit.getPluginManager().registerEvents(this, VaultUtils.getInstance());
        register("afkSelf", Collections.emptyList());  // Multiple registration for different possible usages.
        register("afkOthers", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (afk.getOrDefault(e.getPlayer(), false)) {
            afk.put(e.getPlayer(), false);
            e.getPlayer().sendMessage(string + "You are no longer AFK.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultUtils.getName(e.getPlayer()) + string + " is no longer AFK.");
            }
        }
    }

    @SubCommand("afkSelf")
    public void afkSelf(Player player) {
        boolean newValue = !afk.getOrDefault(player, false);
        afk.put(player, newValue);

        if (newValue) {
            player.sendMessage(string + "You are now AFK.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultUtils.getName(player) + string + " is now AFK.");
            }
        } else {
            player.sendMessage(string + "You are no longer AFK.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultUtils.getName(player) + string + " is no longer AFK.");
            }
        }
    }

    @SubCommand("afkOthers")
    @Permission(Permissions.AFKCommandOther)
    public void afkOthers(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(VaultUtils.getMessage("player-isnt-online"));
            return;
        }

        boolean newValue = !afk.getOrDefault(player, false);
        afk.put(player, newValue);

        if (newValue) {
            player.sendMessage(string + "You are now AFK.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultUtils.getName(player) + string + " is now AFK.");
            }
        } else {
            player.sendMessage(string + "You are no longer AFK.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultUtils.getName(player) + string + " is no longer AFK.");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        afk.remove(e.getPlayer());
    }
}
