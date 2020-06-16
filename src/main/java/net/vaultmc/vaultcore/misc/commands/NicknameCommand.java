/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

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

    public static String getNickname(VLOfflinePlayer player) {
        String nickname = player.getPlayerData().getString("nickname");
        if (nickname == null) {
            player.getPlayerData().set("nickname", "0, 0");
            return null;
        }
        if (nickname.isEmpty() || nickname.equals("0, 0")) return null;
        return nickname;
    }

    @SubCommand("self")
    public void self(VLPlayer sender, String nickname) {
        // If `/nick Aberdeener` is run by Aberdeener
        if (nickname.equals(sender.getName())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.invalid_nick"));
            return;
        }
        setNickname(sender, sender, nickname);
    }

    @SubCommand("other")
    @Permission(Permissions.NicknameCommandOther)
    public void other(VLPlayer sender, VLPlayer target, String nickname) {
        if (nickname.equals(target.getName())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.invalid_nick"));
            return;
        }
        setNickname(sender, target, nickname);
    }

    private void setNickname(VLPlayer sender, VLPlayer target, String nickname) {
        boolean self = false;
        if (sender == target) self = true;
        String originalName = target.getName();
        if (nickname.equals("off")) {
            Bukkit.getPlayer(target.getUniqueId()).setDisplayName(target.getName());
            if (self) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.disabled_self"));
            } else {
                target.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.disabled_target"));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.disabled_sender"), target.getFormattedName()));
            }
            target.getPlayerData().set("nickname", "0, 0");
            return;
        }
        if (nickname.length() < 3) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.too_short"));
            return;
        }
        if (originalName.toLowerCase().startsWith(nickname.substring(0, 3).toLowerCase())) {
            String newName = ChatColor.ITALIC + nickname;
            target.getPlayer().setDisplayName(newName);
            if (self) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success"), sender.getFormattedName()));
            } else {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success_other"), target.getFormattedName()));
                target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success"), target.getFormattedName()));
            }
            target.getPlayerData().set("nickname", nickname);
        } else {
            if (sender.hasPermission(Permissions.NicknameLimitBypass)) {
                String newName = nickname;
                target.getPlayer().setDisplayName(newName);
                if (self) {
                    sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success"), sender.getFormattedName()));
                } else {
                    sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success_other"), target.getFormattedName()));
                    target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nickname.success"), target.getFormattedName()));
                }
                target.getPlayerData().set("nickname", nickname);
                return;
            }
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.nickname.invalid_starting"));
        }
    }
}
