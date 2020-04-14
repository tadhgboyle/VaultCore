package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CGSettingInvListener implements Listener {

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        VLPlayer sender = VLPlayer.getPlayer((Player) e.getWhoClicked());
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);

        CGSettingsInv cgSettingsInv = new CGSettingsInv();
        if (cgSettingsInv.settingPlayers.contains(sender)) {
            String title = e.getView().getTitle();
            if (title.startsWith("ChatGroup Settings:")) {
                if (e.getSlot() == 11) {
                    e.setCancelled(true);
                    chatGroup.open = !chatGroup.open;
                    VaultCore.getInstance().getChatGroupFile().set("chatgroups." + chatGroup.name, chatGroup);
                    VaultCore.getInstance().saveConfig();
                    VaultCore.getInstance().reloadConfig();
                } else if (e.getSlot() == 15) {
                    e.setCancelled(true);
                    cgSettingsInv.openMembersMenu(sender);
                    sender.closeInventory();
                }
            } else if (title.startsWith("ChatGroup Members:")) {
                ItemStack item = e.getCurrentItem();
                VLOfflinePlayer member = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(item.getItemMeta().getLore().get(2).replace(ChatColor.GRAY + "UUID: ", "")));
                Bukkit.getLogger().info(member.getName());
            }
        }
    }
}