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
import java.util.UUID;

@RootCommand(literal = "ignore", description = "Stop seeing messages from a player.")
@Permission(Permissions.IgnoreCommand)
@PlayerOnly
public class IgnoreCommand extends CommandExecutor {

    public IgnoreCommand() {
        register("ignoreList", Collections.singletonList(Arguments.createLiteral("list")));
        register("ignore", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    static List<String> ignored;

    @SubCommand("ignore")
    public void ignore(VLPlayer sender, VLOfflinePlayer target) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        ignored = Arrays.asList(csvIgnored.split("\\s*,\\s*"));
        if (ignored.contains(target.getUniqueId().toString())) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.already_ignored"), target.getFormattedName()));
        } else {
            data.set("ignored", csvIgnored + (ignored.size() < 1 ? "" : ", ") + target.getUniqueId());
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.toggle_ignored"), ChatColor.GREEN + "started", target.getFormattedName()));
        }
        ignored.clear();
    }

    @SubCommand("ignoreList")
    public void ignoreList(VLPlayer sender) {
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        ignored = Arrays.asList(csvIgnored.split("\\s*,\\s*"));
        if (ignored.size() > 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.header"));
            int count = 1;
            for (String uuid : ignored) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.list"), count, VLOfflinePlayer.getOfflinePlayer(UUID.fromString(uuid)).getFormattedName()));
                count++;
            }
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring_anyone"));
        }
        ignored.clear();
    }

    public static boolean isIgnoring(VLOfflinePlayer ignorer, VLOfflinePlayer ignoredPlayer) {
        SQLPlayerData data = ignorer.getPlayerData();
        String csvIgnored = data.getString("ignored");
        ignored = Arrays.asList(csvIgnored.split("\\s*,\\s*"));
        if (ignored.contains(ignoredPlayer.getUniqueId().toString())) return true;
        return false;
    }
}
