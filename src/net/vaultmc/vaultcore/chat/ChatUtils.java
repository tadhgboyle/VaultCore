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
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.misc.commands.AFKCommand;
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
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        e.setFormat(player.getFormattedName() + ChatColor.DARK_GRAY + ":" + ChatColor.RESET + " %2$s");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatShouldFormat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().getWorld().getName().contains("clans")) {
            return;  // Let clans handle this itself
        }

        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());

        if (player.hasPermission(Permissions.ChatColor)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        // Staff chat
        if ((e.getMessage().startsWith("#") || StaffChatCommand.toggled.contains(player.getUniqueId())) && player.hasPermission(Permissions.StaffChatCommand)) {
            StaffChatCommand.chat(player, e.getMessage().replaceFirst("#", ""));
            e.setCancelled(true);
            return;
        }

        if (MuteChatCommand.chatMuted && !player.hasPermission(Permissions.MuteChatCommandOverride)) {
            player.sendMessage(VaultLoader.getMessage("chat.muted"));
            e.setCancelled(true);
            return;
        }

        formatChat(e);
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
                    if (AFKCommand.getAfk().getOrDefault(referred, false)) {
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
        e.setMessage(sb.toString());
    }
}
