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

package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.chat.groups.ChatGroup;
import net.vaultmc.vaultcore.chat.groups.ChatGroupsCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.misc.commands.AFKCommand;
import net.vaultmc.vaultcore.settings.ChatContext;
import net.vaultmc.vaultcore.settings.PlayerCustomKeys;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatUtils extends ConstructorRegisterListener {
    public static void formatChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (player.hasPermission(Permissions.ChatColor)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        // Staff + Admin chat
        if ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.STAFF_CHAT)) ||
                StaffChatCommand.toggled.contains(player.getUniqueId())) && player.hasPermission(Permissions.StaffChatCommand)) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.STAFF_CHAT), "");
            if (message.length() > 0) {
                StaffChatCommand.chat(player, message);
                Bukkit.getLogger().info("[SC] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }
        if ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.ADMIN_CHAT)) ||
                AdminChatCommand.getToggled().contains(player.getUniqueId())) && player.hasPermission(Permissions.AdminChatCommand)) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.ADMIN_CHAT), "");
            if (message.length() > 0) {
                AdminChatCommand.chat(player, message);
                Bukkit.getLogger().info("[AC] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }
        // Chat Groups
        if (ChatGroup.getChatGroup(player) != null && ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.CHAT_GROUP)) ||
                ChatGroupsCommand.getToggled().contains(player.getUniqueId())))) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.CHAT_GROUP), "");
            if (message.length() > 0) {
                ChatGroup.sendMessage(ChatGroup.getChatGroup(player), player, PlayerSettings.getSetting(player, "settings.grammarly") ? Utilities.grammarly(message) : message);
                Bukkit.getLogger().info("[CG] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }

        // MuteChat
        if (MuteChatCommand.chatMuted && !player.hasPermission(Permissions.MuteChatCommandOverride)) {
            player.sendMessage(VaultLoader.getMessage("chat.muted"));
            e.setCancelled(true);
            return;
        }

        // Grammarly
        if (PlayerSettings.getSetting(VLPlayer.getPlayer(e.getPlayer()), "settings.grammarly")) {
            e.setMessage(Utilities.grammarly(e.getMessage()));
        }

        e.setFormat(player.getExtraFormattedName() + ChatColor.DARK_GRAY + ":" + ChatColor.RESET + " %2$s");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatShouldFormat(AsyncPlayerChatEvent e) {
        // Let clans handle itself
        if (e.getPlayer().getWorld().getName().contains("clans")) return;
        formatChat(e);
        e.getRecipients().removeIf(player -> IgnoreCommand.isIgnoring(VLPlayer.getPlayer(player), VLPlayer.getPlayer(e.getPlayer())));
        e.getRecipients().removeIf(p -> Tour.getTouringPlayers().contains(p.getUniqueId()));
    }

    // Handle @mentions in chat
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        String[] words = e.getMessage().split(" ");

        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("@") && !words[i].equals("@")) {
                Player referred = Bukkit.getPlayer(words[i].replace("@", ""));
                if (referred == null) {
                    words[i] = ChatColor.WHITE + "@" + words[i].replace("@", "") + ChatColor.RESET;
                    e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-offline"));
                } else {
                    words[i] = ChatColor.YELLOW + "@" + referred.getName() + ChatColor.RESET;
                    if (AFKCommand.getAfk().containsKey(referred)) {
                        e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-afk"));
                    }
                    if (PlayerSettings.getSetting(VLPlayer.getPlayer(referred), "settings.mention_notifications"))
                        referred.playSound(referred.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 100, (float) Math.pow(2F, (-6F / 12F)) /* High C */);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : words) {
            sb.append(s).append(" ");
        }
        e.setMessage(sb.toString().trim());
    }
}
