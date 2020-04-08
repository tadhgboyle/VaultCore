package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RootCommand(literal = "unignore", description = "Stop ignoring a player.")
@Permission(Permissions.IgnoreCommand)
@PlayerOnly
public class UnignoreCommand extends CommandExecutor {

    public UnignoreCommand() {
        register("unignore", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    List<String> ignored;

    @SubCommand("unignore")
    public void unignore(VLPlayer sender, VLOfflinePlayer target) {
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        ignored = Arrays.asList(csvIgnored.split("\\s*,\\s*"));
        if (ignored.contains(target.getUniqueId().toString())) {
            ignored.remove(target.getUniqueId().toString());
            data.set("ignored", Utilities.listToString(ignored, false));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.toggle_ignore"), ChatColor.RED + "stopped", target.getFormattedName()));
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring"), target.getFormattedName()));
        }
        ignored.clear();
    }
}
