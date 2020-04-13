package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CGSettingsInv {

    public Set<VLPlayer> settingPlayers;

    public void openMainMenu(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);

        Inventory mainMenu = Bukkit.createInventory(null, 27, ChatColor.RESET + "ChatGroup Settings: " + chatGroup.name);
        mainMenu.setItem(11, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Public/Private")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Toggle the visibility of your ChatGroup.",
                        "",
                        chatGroup.open ? ChatColor.GREEN + "Public" : ChatColor.RED + "Private"
                ))
                .build());
        mainMenu.setItem(15, new ItemStackBuilder(Material.PLAYER_HEAD)
                .name(ChatColor.YELLOW + "Manage Group Members")
                .skullOwner(VLOfflinePlayer.getOfflinePlayer("606e2ff0-ed77-4842-9d6c-e1d3321c7838"))
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Edit the members of your ChatGroup."
                ))
                .build());
        sender.openInventory(mainMenu);
    }

    public static void openMembersMenu(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);

        List<String> members = chatGroup.members;

        int memberCount = members.size();
        int slotCount;

        if (memberCount < 27) slotCount = 27;
        else if (memberCount > 27 && memberCount < 54) slotCount = 54;
        else return; // ERROR

        int currentSlot = 0;

        Inventory membersMenu = Bukkit.createInventory(null, slotCount, ChatColor.RESET + "ChatGroup Members: " + chatGroup.name);

        for (String member : members) {
            UUID uuid = UUID.fromString(member);
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(uuid);
            membersMenu.setItem(currentSlot, new ItemStackBuilder(Material.PLAYER_HEAD)
                    .name(ChatColor.YELLOW + "Edit " + player.getFormattedName())
                    .skullOwner(player)
                    .lore(Arrays.asList(
                            ChatColor.GRAY + "Click to kick, promote or demote " + player.getName() + ".",
                            "",
                            ChatColor.GRAY + "UUID: " + player.getUniqueId().toString(),
                            "",
                            ChatColor.GRAY + "Role: " + ChatColor.GOLD + (chatGroup.admins.contains(player) ? "Admin" : "Member")
                    ))
                    .build());
            currentSlot++;
        }
        sender.openInventory(membersMenu);
    }

    private void openMemberSettingsMenu(VLPlayer sender, VLOfflinePlayer target) {

    }

}
