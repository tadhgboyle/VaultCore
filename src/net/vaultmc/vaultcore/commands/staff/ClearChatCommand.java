package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "clearchat", description = "Clears all messages.")
@Permission(Permissions.ClearChatCommand)
@Aliases("cc")
public class ClearChatCommand extends CommandExecutor {
    public ClearChatCommand() {
        register("clear", Collections.emptyList());
    }

    @SubCommand("clear")
    public void clearChat(VLCommandSender sender) {
        String string = Utilities.string;
        String variable1 = Utilities.variable1;

        if (!(sender instanceof VLPlayer)) {
            for (int i = 0; i < 200; i++) {
                for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                    player.sendMessage(" ");
                }
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "CONSOLE " + string + "has cleared chat!"));
            return;
        }

        VLPlayer player = (VLPlayer) sender;

        for (int i = 0; i < 200; i++) {
            for (VLPlayer x : VLPlayer.getOnlinePlayers()) {
                x.sendMessage(" ");
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                string + "The chat has been cleared by " + variable1 + player.getFormattedName() + string + "!"));
    }
}