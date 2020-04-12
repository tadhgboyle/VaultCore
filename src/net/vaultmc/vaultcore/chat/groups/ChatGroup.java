package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatGroup implements ConfigurationSerializable {

    public String name;
    public List<String> admins;
    public List<String> members;
    public boolean open;

    public ChatGroup(String name, List<String> admins, List<String> members, boolean open) {
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
        return new ChatGroup(cgName, chatGroupsFile.getStringList(path + ".admins"), chatGroupsFile.getStringList(path + ".members"), chatGroupsFile.getBoolean(path + ".open"));
    }

    public static boolean createChatGroup(String name, VLPlayer sender, boolean open) {
        Object cg = chatGroupsFile.get("chatgroups." + name);
        if (cg != null) return false;
        ChatGroup chatGroup = new ChatGroup(name.toLowerCase(), Collections.singletonList(sender.getUniqueId().toString()), Collections.singletonList(sender.getUniqueId().toString()), open);
        chatGroupsFile.set("players." + sender.getUniqueId().toString(), chatGroup.name);
        return true;
    }

    public static boolean addToGroup(ChatGroup chatGroup, VLPlayer target) {
        if (getChatGroup(target) != null || chatGroup.members.contains(target.getUniqueId().toString())) return false;
        else {
            chatGroup.members.add(target.getUniqueId().toString());
            chatGroupsFile.set("players." + target.getUniqueId().toString(), chatGroup.name);
            return true;
        }
    }

    public static boolean removeFromGroup(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (chatGroup.members.contains(target.getUniqueId().toString())) {
            chatGroup.admins.remove(target.getUniqueId().toString());
            chatGroup.members.remove(target.getUniqueId().toString());
            if (chatGroup.members.size() <= 1) deleteGroup(chatGroup);
            return true;
        } else return false;
    }

    public static void deleteGroup(ChatGroup chatGroup) {
        chatGroupsFile.set("chatgroups." + chatGroup.name, null);
    }

    public static boolean makeAdmin(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.members.contains(target.getUniqueId().toString())) return false;
        else {
            chatGroup.admins.add(target.getUniqueId().toString());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean makeMember(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.admins.contains(target.getUniqueId().toString())) return false;
        else {
            chatGroup.admins.remove(target.getUniqueId().toString());
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
        VaultCore.getInstance().reloadConfig();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("admins", this.admins);
        map.put("members", this.members);
        map.put("name", this.name);
        map.put("open", this.open);
        return map;
    }
}
