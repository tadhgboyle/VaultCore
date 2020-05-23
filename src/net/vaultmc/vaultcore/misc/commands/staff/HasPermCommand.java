package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "hasperm", description = "Check if a player has a permission.")
@Permission(Permissions.HasPermCommand)
public class HasPermCommand extends CommandExecutor {
    public HasPermCommand() {
        register("hasPermSelf",
                Collections.singletonList(Arguments.createArgument("permission", Arguments.string())));
        register("hasPermOther", Arrays.asList(Arguments.createArgument("permission", Arguments.string()),
                Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("hasPermSelf")
    @PlayerOnly
    public void hasPermSelf(VLPlayer sender, String permission) {
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.hasperm.self"),
                (sender.hasPermission(permission)) ? ChatColor.GREEN + "have" : ChatColor.RED + "dont' have",
                permission));
    }

    @SubCommand("hasPermOther")
    @Permission(Permissions.HasPermCommandOther)
    public void hasPermOther(VLCommandSender sender, String permission, VLOfflinePlayer target) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.hasperm.other"),
                target.getFormattedName(),
                (target.hasPermission(permission)) ? ChatColor.GREEN + "has" : ChatColor.RED + "doesn't have",
                permission));
    }
}
