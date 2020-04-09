package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChatGroup {

    public String name;
    public List<UUID> admins;
    public List<UUID> members;

    public ChatGroup(String name, List<UUID> admins, List<UUID> members) {
        this.name = name;
        this.admins = admins;
        this.members = members;
    }

    static FileConfiguration chatGroupsFile = VaultCore.getInstance().getChatGroupFile();

    public static ChatGroup getChatGroup(VLPlayer player) {
        String cgName = chatGroupsFile.getString("players." + player.getUniqueId().toString());
        if (cgName == null) return null;
        return new ChatGroup(cgName, chatGroupsFile.getStringList("chatgroups." + cgName + ".admins").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList()), chatGroupsFile.getStringList("chatgroups." + cgName + ".members").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList()));
    }

    public static boolean createChatGroup(String name, VLPlayer sender) {
        Object cg = chatGroupsFile.get("chatgroups." + name);
        if (cg != null) return false;
        ChatGroup chatGroup = new ChatGroup(name, Arrays.asList(sender.getUniqueId()), Arrays.asList(sender.getUniqueId()));
        saveChatGroup(chatGroup);
        return true;
    }

    public static boolean addToGroup(ChatGroup chatGroup, VLPlayer target) {
        if (chatGroup.members.contains(target.getUniqueId())) return false;
        else {
            chatGroup.members.add(target.getUniqueId());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean kickFromGroup(ChatGroup chatGroup, VLOfflinePlayer target) {
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

    private static void saveChatGroup(ChatGroup chatGroup) {
        chatGroupsFile.set("chatgroups.", chatGroup);
        VaultCore.getInstance().saveConfig();
    }
}
