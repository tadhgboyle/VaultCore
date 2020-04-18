package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class ChatGroup implements ConfigurationSerializable {

    public String name;
    public List<String> admins;
    public List<String> members;
    public boolean open;

    // TODO: Owners of chatgroups

    public ChatGroup(String name, List<String> admins, List<String> members, boolean open) {
        this.name = name;
        this.admins = admins;
        this.members = members;
        this.open = open;
    }

    public static Set<VLPlayer> getChatGroupMembers(ChatGroup chatGroup) {
        Set<VLPlayer> members = new HashSet<>();
        for (String member : chatGroup.members) {
            members.add(VLPlayer.getPlayer(UUID.fromString(member)));
        }
        return members;
    }

    public static ChatGroup getChatGroup(VLOfflinePlayer player) {
        String cgName = VaultCore.getInstance().getChatGroupFile().getString("players." + player.getUniqueId().toString());
        if (cgName == null) return null;
        ChatGroup chatGroup = (ChatGroup) VaultCore.getInstance().getChatGroupFile().get("chatgroups." + cgName);
        return chatGroup;
    }

    public static ChatGroup getChatGroup(String cgName) {
        ConfigurationSection configurationSection = VaultCore.getInstance().getChatGroupFile().getConfigurationSection("chatgroups");
        if (configurationSection.getValues(true).containsKey(cgName)) {
            ChatGroup chatGroup = (ChatGroup) VaultCore.getInstance().getChatGroupFile().get("chatgroups." + cgName);
            return chatGroup;
        } else return null;
    }

    public static boolean createChatGroup(String name, VLPlayer sender, boolean open) {
        Object cg = VaultCore.getInstance().getChatGroupFile().get("chatgroups." + name);
        if (cg != null) return false;
        ChatGroup chatGroup = new ChatGroup(name.toLowerCase(), Collections.singletonList(sender.getUniqueId().toString()), Collections.singletonList(sender.getUniqueId().toString()), open);
        VaultCore.getInstance().getChatGroupFile().set("players." + sender.getUniqueId().toString(), chatGroup.name);
        saveChatGroup(chatGroup);
        return true;
    }

    public static boolean addToGroup(ChatGroup chatGroup, VLPlayer target) {
        // TODO: Check if chatgroup is private. If so, check if the invites hashmap contains the correct chatgroup for the target
        if (getChatGroup(target) != null || chatGroup.members.contains(target.getUniqueId().toString())) return false;
        else {
            chatGroup.members.add(target.getUniqueId().toString());
            VaultCore.getInstance().getChatGroupFile().set("players." + target.getUniqueId().toString(), chatGroup.name);
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean removeFromGroup(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (chatGroup.members.contains(target.getUniqueId().toString())) {
            VaultCore.getInstance().getChatGroupFile().set("players." + target.getUniqueId().toString(), null);
            chatGroup.admins.remove(target.getUniqueId().toString());
            chatGroup.members.remove(target.getUniqueId().toString());
            if (chatGroup.members.size() <= 1)
                VaultCore.getInstance().getChatGroupFile().set("chatgroups." + chatGroup.name, null);
            saveChatGroup(chatGroup);
            return true;
        } else return false;
    }

    public static boolean makeAdmin(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.members.contains(target.getUniqueId().toString()) || chatGroup.admins.contains(target.getUniqueId().toString()))
            return false;
        else {
            chatGroup.admins.add(target.getUniqueId().toString());
            saveChatGroup(chatGroup);
            return true;
        }
    }

    public static boolean makeMember(ChatGroup chatGroup, VLOfflinePlayer target) {
        if (!chatGroup.members.contains(target.getUniqueId().toString()) || !chatGroup.admins.contains(target.getUniqueId().toString()))
            return false;
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
        Bukkit.getLogger().severe(chatGroup.name);
        for (VLPlayer players : getChatGroupMembers(chatGroup)) {
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.format"), chatGroup.name, sender.getFormattedName(), message));
        }
        Bukkit.getLogger().info(sender.getFormattedName() + ": CG: " + message);
    }

    private static void saveChatGroup(ChatGroup chatGroup) {
        VaultCore.getInstance().getChatGroupFile().set("chatgroups." + chatGroup.name, chatGroup);
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

    public static ChatGroup deserialize(Map<String, Object> args) {
        String name = null;
        List<String> admins = null;
        List<String> members = null;
        boolean open = false;

        name = args.get("name").toString();
        admins = (List<String>) args.get("admins");
        members = (List<String>) args.get("members");
        open = (boolean) args.get("open");

        return new ChatGroup(name, admins, members, open);
    }
}