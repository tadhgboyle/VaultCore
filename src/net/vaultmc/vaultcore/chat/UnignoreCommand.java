package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unchecked")
@RootCommand(literal = "unignore", description = "Stop ignoring a player.")
@Permission(Permissions.IgnoreCommand)
@PlayerOnly
public class UnignoreCommand extends CommandExecutor {

    Collection ignored;

    public UnignoreCommand() {
        register("unignore", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("unignore")
    public void unignore(VLPlayer sender, VLOfflinePlayer target) {
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        if (csvIgnored != null) {
            if (csvIgnored.isEmpty()) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring_anyone"));
                return;
            }
            ignored = new ArrayList(Arrays.asList(csvIgnored.split("\\s*,\\s*")));
            if (ignored.contains(target.getUniqueId().toString())) {
                ignored.remove(target.getUniqueId().toString());
                if (ignored.size() == 1) data.set("ignored", Utilities.listToString(ignored, false).replace(", ", ""));
                else data.set("ignored", Utilities.listToString(ignored, false));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.toggle_ignore"), ChatColor.RED + "stopped", target.getFormattedName()));
            } else
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignored"), target.getFormattedName()));
        } else {
            data.set("ignored", "");
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring"), target.getFormattedName()));
        }
    }
}
