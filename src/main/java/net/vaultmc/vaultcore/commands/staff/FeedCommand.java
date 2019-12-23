package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "feed",
        description = "Feed a player."
)
@Permission(Permissions.FeedCommand)
public class FeedCommand extends CommandExecutor {
    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
    public FeedCommand() {
        register("feedSelf", Collections.emptyList(), "vaultcore");
        register("feedOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())), "vaultcore");
    }

    @SubCommand("feedSelf")
    @PlayerOnly
    public void feedSelf(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been " + variable1 + "fed."));
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    @SubCommand("feedOthers")
    @Permission(Permissions.FeedCommandOther)
    public void feedOthers(CommandSender sender, Player target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have fed " + variable1 + VaultCoreAPI.getName(target)));
        target.setFoodLevel(20);
        target.setSaturation(20);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been fed by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                        ChatColor.BOLD + "CONSOLE" + ChatColor.RESET)));
    }
}