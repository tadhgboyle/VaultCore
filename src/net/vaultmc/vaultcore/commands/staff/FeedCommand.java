package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(
        literal = "feed",
        description = "Feed a player."
)
@Permission(Permissions.FeedCommand)
public class FeedCommand extends CommandExecutor {
    String string = Utilities.string;
    String variable1 = Utilities.variable1;

    public FeedCommand() {
        register("feedSelf", Collections.emptyList());
        register("feedOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("feedSelf")
    @PlayerOnly
    public void feedSelf(VLPlayer player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been " + variable1 + "fed."));
        player.feed();
    }

    @SubCommand("feedOthers")
    @Permission(Permissions.FeedCommandOther)
    public void feedOthers(VLCommandSender sender, VLPlayer target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have fed " + variable1 + target.getFormattedName()));
        target.feed();
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been fed by " + variable1 + sender.getFormattedName()));
    }
}