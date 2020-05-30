/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.chat.groups;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

@SuppressWarnings("unchecked")
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

    public static boolean permissionCheck(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = getChatGroup(sender);
        if (chatGroup != getChatGroup(target)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.different_chatgroup"));
            return false;
        }
        ChatGroupRole senderRole = ChatGroup.getRole(sender, chatGroup);
        ChatGroupRole targetRole = ChatGroup.getRole(target, chatGroup);
        if (chatGroup.admins.contains(sender.getUniqueId().toString())) {
            // Owner editing admin, or admin editing member - ALLOWED
            if (senderRole.getLevel() > targetRole.getLevel()) return true;
                // Admin editing Admin, or Admin editing Owner - NOT ALLOWED
            else {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.permission_error"));
                return false;
            }
        }
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.permission_error"));
        return false;
    }

    private static void saveChatGroup(ChatGroup chatGroup) {
        VaultCore.getInstance().getChatGroupFile().set("chatgroups." + chatGroup.name, chatGroup);
        VaultCore.getInstance().saveConfig();
        VaultCore.getInstance().reloadConfig();
    }

    public static ChatGroup deserialize(Map<String, Object> args) {
        String name;
        String owner;
        List<String> admins;
        List<String> members;
        boolean open;

        name = args.get("name").toString();
        owner = args.get("owner").toString();
        admins = (List<String>) args.get("admins");
        members = (List<String>) args.get("members");
        open = (boolean) args.get("open");

        return new ChatGroup(name, owner, admins, members, open);
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
}