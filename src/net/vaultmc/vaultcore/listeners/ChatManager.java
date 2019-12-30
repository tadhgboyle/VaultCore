package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.commands.staff.MuteChatCommand;
import net.vaultmc.vaultcore.commands.staff.StaffChatCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatManager implements Listener {
    private static final String[][] worldGroups = new String[][]{ // Messages will be split within these worlds.
            new String[]{"Lobby"},
            new String[]{
                    "Survival", // E.g. I sent a message in Survival.
                    "Survival_Nether", // Player in Survival_Nether receives it, player in Survival_End receives it.
                    "Survival_End" // But player in Lobby won't receive it.
            },
            new String[]{
                    "clans",
                    "clans_nether",
                    "clans_the_end"
            },
            new String[]{
                    "skyblock",
                    "skyblock_nether"
            }};

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled())
            return;

        Player player = e.getPlayer();

        if (StaffChatCommand.toggled.contains(player.getUniqueId()) || e.getMessage().charAt(0) == ',') {

            String message = (ChatColor.translateAlternateColorCodes('&',
                    VaultCore.getInstance().getConfig().getString("staffchat-prefix")))
                    + (ChatColor.translateAlternateColorCodes('&', VaultCore.getChat().getPlayerPrefix(player))
                    + player.getDisplayName()
                    + ChatColor.translateAlternateColorCodes('&', VaultCore.getChat().getPlayerSuffix(player))
                    + ChatColor.DARK_GRAY + " \\u1f80a " + ChatColor.AQUA
                    + ChatColor.translateAlternateColorCodes('&',
                    e.getMessage().charAt(0) == ',' ? e.getMessage().replaceFirst(",", "")
                            : e.getMessage()
                    /*
                     * If e. (...) charAt (...) Then use e.get (...) replaceFirst else use
                     * e.getMessage()
                     */));
            Bukkit.getConsoleSender().sendMessage(message);

            Bukkit.getOnlinePlayers().forEach(x -> {
                if (x.hasPermission(Permissions.StaffChat)) {
                    x.sendMessage(message);
                }
            });

            e.setCancelled(true);
            return;
        }

        if (MuteChatCommand.chatMuted && !player.hasPermission(Permissions.MuteChatCommandOverride)) {
            player.sendMessage(ChatColor.RED + "The chat is currently muted!");
            e.setCancelled(true);
            return;
        }

        if (player.hasPermission("vc.chat.color")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        e.setFormat(ChatColor.translateAlternateColorCodes('&', VaultCore.getChat().getPlayerPrefix(player))
                + player.getDisplayName()
                + ChatColor.translateAlternateColorCodes('&', VaultCore.getChat().getPlayerSuffix(player)) + " "
                + ChatColor.DARK_GRAY + "\\u1f80a" + ChatColor.RESET + " %2$s");

        // Find the world group index of the player
        int group = -1;
        for (int i = 0; i < worldGroups.length; i++) {
            String[] worlds = worldGroups[i];
            for (String world : worlds) {
                if (world.equalsIgnoreCase(player.getWorld().getName())) {
                    group = i;
                    break;
                }
            }
        }

        if (group == -1) {
            // The player's world is not in the world group, treat as if the player hadn't
            // enabled per world chat.
            return;
        }

        List<Player> toRemove = new ArrayList<>();
        for (Player x : e.getRecipients()) {
            if (VaultCore.getInstance().getPlayerData().getBoolean("players." + x.getUniqueId() + ".settings.pwc")) {
                // The player enabled pwc.

                // Find the world group index of this player
                int thisGroup = -1;
                for (int j = 0; j < worldGroups.length; j++) {
                    String[] worlds = worldGroups[j];
                    for (String world : worlds) {
                        if (world.equalsIgnoreCase(x.getWorld().getName())) {
                            thisGroup = j;
                            break;
                        }
                    }
                }

                if (thisGroup == -1) {
                    continue;
                }

                if (group != thisGroup) {
                    // The player should not receive this message.
                    // Directly removing using for loop with counter is acting extremely weirdly.
                    toRemove.add(x);
                }
            }
        }

        // Remove all the players that should not receive this message.
        e.getRecipients().removeIf(toRemove::contains);
    }
}
