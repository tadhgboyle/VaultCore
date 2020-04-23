/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.groups.ChatGroup;
import net.vaultmc.vaultcore.chat.groups.ChatGroupsCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.misc.commands.AFKCommand;
import net.vaultmc.vaultcore.settings.PlayerCustomKeys;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class ChatUtils extends ConstructorRegisterListener {
    public static void formatChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (player.hasPermission(Permissions.ChatColor)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        // Grammarly
        List<String> apostrophe = Arrays.asList("dont", "wont", "cant");
        List<String> punctuation = Arrays.asList(".", "!", "?");
        if (PlayerSettings.getSetting(VLPlayer.getPlayer(e.getPlayer()), "settings.grammarly")) {
            String message = e.getMessage();
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String word : message.split(" ")) {
                // Add apostrophe
                if (apostrophe.contains(word)) {
                    // TODO
                }
                // Uppercase if first word
                if (first)
                    word = Utilities.capitalizeMessage(word);
                first = false;
                sb.append(word).append(" ");
            }
            message = sb.toString().trim();
            // Punctuation for last word
            if (!punctuation.contains(Character.toString(message.charAt(message.length() - 1)))) {
                message = message + ".";
            }
            e.setMessage(message);
        }

        PlayerCustomKeys playerCustomKeys = new PlayerCustomKeys();
        String chatGroupsKey = playerCustomKeys.getCustomKey(player, "chatgroups");
        String staffChatKey = playerCustomKeys.getCustomKey(player, "staffchat");
        String adminChatKey = playerCustomKeys.getCustomKey(player, "adminchat");

        // Staff + Admin chat
        if ((e.getMessage().startsWith(staffChatKey) || StaffChatCommand.toggled.contains(player.getUniqueId())) && player.hasPermission(Permissions.StaffChatCommand)) {
            StaffChatCommand.chat(player, e.getMessage().replaceFirst(staffChatKey, ""));
            e.setCancelled(true);
            return;
        }
        if ((e.getMessage().startsWith(adminChatKey) || AdminChatCommand.getToggled().contains(player.getUniqueId())) && player.hasPermission(Permissions.AdminChatCommand)) {
            SQLMessenger.sendGlobalMessage("513ACChat" + VaultCore.SEPARATOR + player.getFormattedName() + VaultCore.SEPARATOR + e.getMessage().replaceFirst(adminChatKey, ""));
            e.setCancelled(true);
            return;
        }

        // ChatGroups
        if (ChatGroup.getChatGroup(player) != null && ((e.getMessage().startsWith(chatGroupsKey) || ChatGroupsCommand.getToggled().contains(player)))) {
            ChatGroup.sendMessage(ChatGroup.getChatGroup(player), player, e.getMessage().replaceFirst(chatGroupsKey, ""));
            e.setCancelled(true);
            return;
        }

        // MuteChat
        if (MuteChatCommand.chatMuted && !player.hasPermission(Permissions.MuteChatCommandOverride)) {
            player.sendMessage(VaultLoader.getMessage("chat.muted"));
            e.setCancelled(true);
            return;
        }
        e.setFormat(player.getFormattedName() + ChatColor.DARK_GRAY + ":" + ChatColor.RESET + " %2$s");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatShouldFormat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().getWorld().getName().contains("clans")) {
            return;  // Let clans handle this itself
        }
        formatChat(e);
        e.getRecipients().removeIf(player -> IgnoreCommand.isIgnoring(VLPlayer.getPlayer(player), VLPlayer.getPlayer(e.getPlayer())));
        if (VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc"))
            e.getRecipients().removeIf(p -> Tour.getTouringPlayers().contains(p.getUniqueId()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        String[] parts = e.getMessage().split(" ");

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("@") && !parts[i].equals("@")) {
                Player referred = Bukkit.getPlayer(parts[i].replace("@", ""));
                if (referred == null) {
                    parts[i] = ChatColor.WHITE + "@" + parts[i].replace("@", "") + ChatColor.RESET;
                    e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-offline"));
                } else {
                    parts[i] = ChatColor.YELLOW + "@" + referred.getName() + ChatColor.RESET;
                    if (AFKCommand.getAfk().containsKey(referred)) {
                        e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-afk"));
                    }
                    referred.playSound(referred.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 100, (float) Math.pow(2F, (-6F / 12F)) /* High C */);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s : parts) {
            sb.append(s).append(" ");
        }
        e.setMessage(sb.toString().trim());
    }
}
