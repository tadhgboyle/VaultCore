package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "clearchat",
        description = "Clears all messages."
)
@Permission(Permissions.ClearChatCommand)
@Aliases("cc")
public class ClearChatCommand extends CommandExecutor {
    public ClearChatCommand() {
        register("clear", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("clear")
    public void clearChat(CommandSender sender) {
        String string = VaultCore.getInstance().getConfig().getString("string");
        String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

        if (!(sender instanceof Player)) {

            for (int i = 0; i < 200; i++) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(" ");
                }
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    variable1 + "CONSOLE " + string + "has cleared chat!"));
            return;
        }

        Player player = (Player) sender;

        for (int i = 0; i < 200; i++) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(" ");
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                string + "The chat has been cleared by " + variable1 + player.getName() + string + "!"));
    }
}