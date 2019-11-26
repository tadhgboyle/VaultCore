package net.vaultmc.vaultcore.commands.staff;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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
@Permission(Permissions.StaffChat)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor {
    public static final Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", StringArgumentType.greedyString())), "vaultcore");
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")), "vaultcore");
    }

    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    @SubCommand("chat")
    public void chat(CommandSender sender, String message) {
        String cprefix = (ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
        String cstaffchat = String.format("%s" + VaultCoreAPI.getName(sender) +
                ChatColor.DARK_GRAY + " � " + ChatColor.AQUA + "%s", cprefix, message);

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