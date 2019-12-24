package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "mutechat",
        description = "Mutes the chat."
)
@Permission(Permissions.MuteChatCommand)
public class MuteChatCommand extends CommandExecutor {
    public static boolean chatMuted = false;

    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

    public MuteChatCommand() {
        register("mutechat", Collections.emptyList());
    }

    @SubCommand("mutechat")
    public void muteChat(CommandSender sender) {
        if (chatMuted) {
            chatMuted = false;
            Bukkit.broadcastMessage(
                    ChatColor.translateAlternateColorCodes('&', string + "The chat is no longer muted!"));

        } else {
            chatMuted = true;
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "The chat has been muted by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                            ChatColor.BOLD + "CONSOLE" + ChatColor.RESET)));
        }
    }
}
