package net.vaultmc.vaultcore.chat.groups;

import net.milkbowl.vault.chat.Chat;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChatGroup {

    // Note: I havent done anything about chat groups yet other than make this. I am focussing on making simpler things like ignore and suicide commands work first

    public String name;
    public List<UUID> admins;
    public List<UUID> members;
    public boolean open;

    public ChatGroup(String name, List<UUID> admins, List<UUID> members, boolean open) {
        this.name = name;
        this.admins = admins;
        this.members = members;
        this.open = open;
    }

    static FileConfiguration chatGroupsFile = VaultCore.getInstance().getChatGroupFile();

    public static ChatGroup getChatGroup(VLPlayer player) {
        String cgName = chatGroupsFile.getString("players." + player.getUniqueId().toString());
        if (cgName == null) return null;
        String path = "chatgroups." + cgName;
        return new ChatGroup(cgName, chatGroupsFile.getStringList(path + ".admins").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList()), chatGroupsFile.getStringList(path + ".members").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList()), chatGroupsFile.getBoolean(path + ".open"));
    }

    public static boolean createChatGroup(String name, VLPlayer sender, boolean open) {
        Object cg = chatGroupsFile.get("chatgroups." + name);
        if (cg != null) return false;
        ChatGroup chatGroup = new ChatGroup(name, Arrays.asList(sender.getUniqueId()), Arrays.asList(sender.getUniqueId()), open);
        saveChatGroup(chatGroup);
        return true;
    }

    public static boolean addToGroup(ChatGroup chatGroup, VLPlayer target) {
        if (getChatGroup(target) != null ||chatGroup.members.contains(target.getUniqueId())) return false;
        else {
            chatGroup.members.add(target.getUniqueId());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean removeFromGroup(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (chatGroup.members.contains(target.getUniqueId())) {
            // TODO: catch exception if target is not an admin
            chatGroup.admins.remove(target.getUniqueId());
            chatGroup.members.remove(target.getUniqueId());
            saveChatGroup(chatGroup);
            return true;
        } else return false;
    }

    public static boolean makeAdmin(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.members.contains(target.getUniqueId())) return false;
        else {
            chatGroup.admins.add(target.getUniqueId());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean makeMember(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.admins.contains(target.getUniqueId())) return false;
        else {
            chatGroup.admins.remove(target.getUniqueId());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean isOpen(ChatGroup chatGroup) {
        return chatGroup.open;
    }

    private static void saveChatGroup(ChatGroup chatGroup) {
        chatGroupsFile.set("chatgroups." + chatGroup.name, chatGroup);
        VaultCore.getInstance().saveConfig();
    }
}
