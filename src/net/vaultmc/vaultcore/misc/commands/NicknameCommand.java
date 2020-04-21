package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "nickname", description = "Change yourself or anothers nickname.")
@Permission(Permissions.NicknameCommand)
@Aliases("nick")
@PlayerOnly
public class NicknameCommand extends CommandExecutor {

    public NicknameCommand() {
        register("self", Collections.singletonList(Arguments.createArgument("nickname", Arguments.word())));
        register("other", Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()), Arguments.createArgument("nickname", Arguments.word())));
    }

    @SubCommand("self")
    public void self(VLPlayer sender, String nickname) {
        String senderName = sender.getName();
        if (nickname.equals("off")) {
            Bukkit.getPlayer(sender.getUniqueId()).setDisplayName(sender.getName());
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.disabled_self"));
            sender.getPlayerData().set("nickname", "0, 0");
            return;
        }
        if (nickname.length() < 3) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.too_short"));
            return;
        }
        if (senderName.startsWith(nickname.substring(0, 3))) {
            String newName = ChatColor.ITALIC + nickname;
            Bukkit.getPlayer(sender.getUniqueId()).setDisplayName(newName);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success_self"), sender.getFormattedName()));
            sender.getPlayerData().set("nickname", nickname);
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.invalid_starting"));
        }
    }

    @SubCommand("other")
    @Permission(Permissions.NicknameCommandOther)
    public void other(VLPlayer sender, VLPlayer target, String nickname) {
    }

    public static String getNickname(VLOfflinePlayer player) {
        String nickname = player.getPlayerData().getString("nickname");
        if (nickname == null) player.getPlayerData().set("nickname", "0, 0");
        if (nickname.isEmpty()) return null;
        return nickname;
    }
}
