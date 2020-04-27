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
    public String owner;
    public List<String> admins;
    public List<String> members;
    public boolean open;

    // TODO: Owners of chatgroups

    public ChatGroup(String name, String owner, List<String> admins, List<String> members, boolean open) {
        this.name = name;
        this.owner = owner;
        this.admins = admins;
        this.members = members;
        this.open = open;
    }

    public static Set<VLOfflinePlayer> getChatGroupMembers(ChatGroup chatGroup) {
        Set<VLOfflinePlayer> members = new HashSet<>();
        for (String member : chatGroup.members) {
            members.add(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(member.trim())));
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
        ChatGroup chatGroup = new ChatGroup(name.toLowerCase(), sender.getUniqueId().toString(), Collections.singletonList(sender.getUniqueId().toString()), Collections.singletonList(sender.getUniqueId().toString()), open);
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

    public static ChatGroupRole getRole(VLOfflinePlayer target, ChatGroup chatGroup) {
        if (chatGroup.owner.equals(target.getUniqueId().toString())) return ChatGroupRole.OWNER;
        else if (chatGroup.admins.contains(target.getUniqueId().toString())) return ChatGroupRole.ADMIN;
        else if (chatGroup.members.contains(target.getUniqueId().toString())) return ChatGroupRole.MEMBER;
        else return null;
    }

    public static boolean isOpen(ChatGroup chatGroup) {
        return chatGroup.open;
    }

    public static void sendMessage(ChatGroup chatGroup, VLPlayer sender, String message) {
        for (VLOfflinePlayer players : getChatGroupMembers(chatGroup)) {
            if (!players.isOnline()) continue;
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.format"), chatGroup.name, sender.getFormattedName(), message));
        }
    }

    private static void saveChatGroup(ChatGroup chatGroup) {
        VaultCore.getInstance().getChatGroupFile().set("chatgroups." + chatGroup.name, chatGroup);
        VaultCore.getInstance().saveConfig();
        VaultCore.getInstance().reloadConfig();
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.name);
        result.put("owner", this.owner);
        result.put("admins", this.admins);
        result.put("members", this.members);
        result.put("open", this.open);
        return result;
    }

    public static ChatGroup deserialize(Map<String, Object> args) {
        String name = null;
        String owner = null;
        List<String> admins = null;
        List<String> members = null;
        boolean open = false;

        name = args.get("name").toString();
        owner = args.get("owner").toString();
        admins = (List<String>) args.get("admins");
        members = (List<String>) args.get("members");
        open = (boolean) args.get("open");

        return new ChatGroup(name, owner, admins, members, open);
    }
}