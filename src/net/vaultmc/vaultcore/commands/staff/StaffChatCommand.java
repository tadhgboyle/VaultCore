package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.*;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(
        literal = "staffchat",
        description = "Use staff chat."
)
@Permission(Permissions.StaffChatCommand)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor {
    public static final Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
    }

    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    @SubCommand("chat")
    public void chat(CommandSender sender, String message) {
        String cprefix = (ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
        String cstaffchat = String.format("%s" + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                ChatColor.BOLD + "CONSOLE" + ChatColor.RESET) +
                ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + "%s", cprefix, message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("vc.sc")) {
                player.sendMessage(cstaffchat);
            }
        }
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(CommandSender sender) {
        Player player = (Player) sender;
        if (toggled.contains(player.getUniqueId())) {
            toggled.remove(player.getUniqueId());
            player.sendMessage(string + "You have " + variable1 + "disabled" + string + " staff chat.");
        } else {
            toggled.add(player.getUniqueId());
            player.sendMessage(string + "You have " + variable1 + "enabled" + string + " staff chat.");
        }
    }
}