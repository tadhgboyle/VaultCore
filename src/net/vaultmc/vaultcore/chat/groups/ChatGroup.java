package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

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

    public static Set<VLPlayer> getChatGroupMembers(ChatGroup chatGroup) {
        Set<VLPlayer> members = null;
        for (String member : chatGroup.members) {
            VLPlayer player = VLPlayer.getPlayer(UUID.fromString(member));
            if (player.isOnline()) members.add(VLPlayer.getPlayer(UUID.fromString(member)));
        }
        return members;
    }

    public static ChatGroup getChatGroup(VLPlayer player) {
        String cgName = chatGroupsFile.getString("players." + player.getUniqueId().toString());
        if (cgName == null) return null;
        String path = "chatgroups." + cgName;
        /*
         This is returning a chatgroup, but the admins and members lists are empty... bukkit javadocs say it will return empty if the list does not exist, but they do exist?
         To recreate: /cg create <name> <public>, then /cg settings, then check console (see lines 102-112 ChatGroupsCommand)
         */
        return new ChatGroup(cgName, chatGroupsFile.getStringList(path + ".admins"), chatGroupsFile.getStringList(path + ".members"), chatGroupsFile.getBoolean(path + ".open"));
    }

    public static boolean createChatGroup(String name, VLPlayer sender, boolean open) {
        Object cg = chatGroupsFile.get("chatgroups." + name);
        if (cg != null) return false;
        ChatGroup chatGroup = new ChatGroup(name.toLowerCase(), Collections.singletonList(sender.getUniqueId().toString()), Collections.singletonList(sender.getUniqueId().toString()), open);
        chatGroupsFile.set("players." + sender.getUniqueId().toString(), chatGroup.name);
        saveChatGroup(chatGroup);
        return true;
    }

    public static boolean addToGroup(ChatGroup chatGroup, VLPlayer target) {
        if (getChatGroup(target) != null || chatGroup.members.contains(target.getUniqueId().toString())) return false;
        else {
            chatGroup.members.add(target.getUniqueId().toString());
            chatGroupsFile.set("players." + target.getUniqueId().toString(), chatGroup.name);
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean removeFromGroup(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (chatGroup.members.contains(target.getUniqueId().toString())) {
            chatGroup.admins.remove(target.getUniqueId().toString());
            chatGroup.members.remove(target.getUniqueId().toString());
            if (chatGroup.members.size() == 0) chatGroupsFile.set("chatgroups." + chatGroup.name, null);
            saveChatGroup(chatGroup);
            return true;
        } else return false;
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

    public static void sendMessage(ChatGroup chatGroup, VLPlayer sender, String message) {
        for (VLPlayer players : getChatGroupMembers(chatGroup)) {
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.format"), sender.getFormattedName(), message));
        }
    }

    private static void saveChatGroup(ChatGroup chatGroup) {
        chatGroupsFile.set("chatgroups." + chatGroup.name, chatGroup);
        VaultCore.getInstance().saveConfig();
        VaultCore.getInstance().reloadConfig();
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("admins", this.admins);
        result.put("members", this.members);
        result.put("name", this.name);
        result.put("open", this.open);
        return result;
    }
}
