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

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

@RootCommand(literal = "chatgroup", description = "Secret chats for you and your friends!")
@Permission(Permissions.ChatGroupsCommand)
@Aliases({"cg"})
@PlayerOnly
public class ChatGroupsCommand extends CommandExecutor {
    @Getter
    public static Set<UUID> toggled = new HashSet<>();
    @Getter
    public static HashMap<VLPlayer, ChatGroup> invites = new HashMap<>();
    Set<ChatGroup> chatGroupsList = new HashSet<>();

    public ChatGroupsCommand() {
        register("chatGroupInfo", Collections.emptyList());
        register("chatGroupChat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("chatGroupList", Collections.singletonList(Arguments.createLiteral("list")));
        register("chatGroupToggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        register("chatGroupCreate", Arrays.asList(Arguments.createLiteral("create"), Arguments.createArgument("name", Arguments.string()), Arguments.createArgument("open", Arguments.boolArgument())));
        register("chatGroupSettings", Collections.singletonList(Arguments.createLiteral("settings")));
        register("chatGroupJoin", Arrays.asList(Arguments.createLiteral("join"), Arguments.createArgument("name", Arguments.string())));
        // register("chatGroupDelete", Collections.singletonList(Arguments.createLiteral("delete")));
        register("chatGroupInvite", Arrays.asList(Arguments.createLiteral("invite"), Arguments.createArgument("target", Arguments.playerArgument())));
        register("chatGroupAccept", Collections.singletonList(Arguments.createLiteral("accept")));
        register("chatGroupDecline", Collections.singletonList(Arguments.createLiteral("decline")));
        register("chatGroupLeave", Collections.singletonList(Arguments.createLiteral("leave")));
        register("chatGroupPromote", Arrays.asList(Arguments.createLiteral("promote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupDemote", Arrays.asList(Arguments.createLiteral("demote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupKick", Arrays.asList(Arguments.createLiteral("kick"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("chatGroupInfo")
    public void chatGroupInfo(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.header"));
        if (chatGroup != null) {
            Bukkit.getLogger().info(chatGroup.name);
            Bukkit.getLogger().info(chatGroup.owner);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.in_group"), chatGroup.name));
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.not_in_group"));
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "list", "View all public ChatGroups"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "join <name>", "Join a public ChatGroup"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "create <name> <public>", "Create a new ChatGroup."));
    }

    @SubCommand("chatGroupChat")
    public void chatGroupChat(VLPlayer sender, String message) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup == null) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.leave.error"));
            return;
        }
        ChatGroup.sendMessage(chatGroup, sender, PlayerSettings.getSetting(sender, "settings.grammarly") ? Utilities.grammarly(message) : message);
    }

    @SubCommand("chatGroupList")
    public void chatGroupList(VLPlayer sender) {
        // TODO: Add pagination
        ConfigurationSection configurationSection = VaultCore.getInstance().getChatGroupFile().getConfigurationSection("chatgroups");
        try {
            for (String name : configurationSection.getValues(true).keySet()) {
                ChatGroup chatGroup = ChatGroup.getChatGroup(name);
                if (chatGroup.open) chatGroupsList.add(chatGroup);
            }
        } catch (NullPointerException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.list.no_chatgroups"));
            return;
        }
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.header"));
        if (chatGroupsList.size() == 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.list.no_chatgroups"));
            return;
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.list.count"), chatGroupsList.size()));
        for (ChatGroup chatGroup : chatGroupsList) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.list.layout"), chatGroup.name));
        }
        chatGroupsList.clear();
    }

    @SubCommand("chatGroupToggle")
    public void chatGroupToggle(VLPlayer sender) {
        // Simple
        if (toggled.contains(sender.getUniqueId())) {
            toggled.remove(sender.getUniqueId());
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.toggle"), "off"));
        } else {
            if (StaffChatCommand.checkToggled(sender, toggled)) return;
            toggled.add(sender.getUniqueId());
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.toggle"), "on"));
        }
    }

    @SubCommand("chatGroupCreate")
    public void chatGroupCreate(VLPlayer sender, String name, boolean open) {
        if (ChatGroup.getChatGroup(sender) == null && ChatGroup.createChatGroup(name, sender, open)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.creation.success"), name));
        } else {
            // Chatgroup name is taken, or they are already in a chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.creation.error"));
        }
    }

    @SubCommand("chatGroupSettings")
    public void chatGroupSettings(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup == null || !chatGroup.admins.contains(sender.getUniqueId().toString())) {
            // Command sender is not in a chatgroup or is not admin in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
            return;
        }
        CGSettingsInv cgSettingsInv = new CGSettingsInv();
        cgSettingsInv.openMainMenu(sender);
    }

    @SubCommand("chatGroupJoin")
    public void chatGroupJoin(VLPlayer sender, String cgName) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(cgName);
        // If they are in a chatgroup or the chatgroup given by the name doesnt exist
        if (ChatGroup.getChatGroup(sender) != null || chatGroup == null) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.join.error"));
            return;
        }
        // Success
        ChatGroup.addToGroup(chatGroup, sender);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.join.success"), chatGroup.name));
        for (VLOfflinePlayer member : ChatGroup.getChatGroupMembers(chatGroup)) {
            if (member.isOnline())
                member.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.join.members_message"), chatGroup.name, sender.getFormattedName(), chatGroup.name));
        }
    }

    @SubCommand("chatGroupDelete")
    public void chatGroupDelete(VLPlayer sender) {
        // TODO: This
    }

    @SubCommand("chatGroupInvite")
    public void chatGroupInvite(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.self_error"));
            return;
        }
        if (invites.containsKey(target)) {
            // Target has pending invite already
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.pending_error"));
        } else {
            ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
            if (chatGroup == null) {
                // Command sender is not in a chatgroup or is not admin in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
                return;
            }
            if (ChatGroup.getChatGroup(target) != null && ChatGroup.getChatGroup(target) == chatGroup) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.already_in_group"));
                return;
            }
            // Require admin or owner rank in their chatgroup
            if (ChatGroup.getRole(sender, chatGroup).getLevel() < 2) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.permission_error"));
                return;
            }
            // Success
            invites.put(target, chatGroup);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.sender"), target.getFormattedName()));
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.target"), sender.getFormattedName(), chatGroup.name));
        }
    }

    @SubCommand("chatGroupAccept")
    public void chatGroupAccept(VLPlayer sender) {
        if (invites.containsKey(sender)) {
            if (ChatGroup.addToGroup(invites.get(sender), sender)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.accepted"), invites.get(sender).name));
                for (VLOfflinePlayer member : ChatGroup.getChatGroupMembers(invites.get(sender))) {
                    if (member.isOnline())
                        member.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.join.members_message"), invites.get(sender), sender.getFormattedName(), invites.get(sender).name));
                }
                invites.remove(sender);
            } else {
                // Invitee is already in a chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.accept_error_target"));
            }
        } else {
            // Sender has no invites pending for them
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.pending_error_target"));
        }
    }

    @SubCommand("chatGroupDecline")
    public void chatGroupDecline(VLPlayer sender) {
        if (invites.containsKey(sender)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.declined_target"), invites.get(sender).name));
            invites.remove(sender);
        } else {
            // No invites pending for sender
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.pending_error_target"));
        }
    }

    @SubCommand("chatGroupLeave")
    public void chatGroupLeave(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null) {
            // Success
            String name = chatGroup.name;
            ChatGroup.removeFromGroup(chatGroup, VLOfflinePlayer.getOfflinePlayer(sender.getUniqueId()));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.leave.success"), name));
        } else {
            // Not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.leave.error"));
        }
    }

    @SubCommand("chatGroupPromote")
    public void chatGroupPromote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup == null) {
            // Command sender is not in a chatgroup or is not admin in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
            return;
        }
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.self_error"));
            return;
        }
        if (!ChatGroup.permissionCheck(sender, target)) return;
        if (ChatGroup.makeAdmin(chatGroup, target)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.promote"), target.getFormattedName()));
            target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.promoted_target"), sender.getFormattedName()));
        } else {
            // Target is not in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.not_in_chatgroup"));
        }
    }

    @SubCommand("chatGroupDemote")
    public void chatGroupDemote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup == null) {
            // Command sender is not in a chatgroup or is not admin in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
            return;
        }
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.self_error"));
            return;
        }
        if (!ChatGroup.permissionCheck(sender, target)) return;
        if (ChatGroup.makeMember(chatGroup, target)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.demote"), target.getFormattedName()));
            target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.demoted_target"), sender.getFormattedName()));
        } else {
            // Target is not in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.not_in_chatgroup"));
        }
    }

    @SubCommand("chatGroupKick")
    public void chatGroupKick(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup == null) {
            // Command sender is not in a chatgroup or is not admin in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
            return;
        }
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.self_error"));
            return;
        }
        if (!ChatGroup.permissionCheck(sender, target)) return;
        if (ChatGroup.removeFromGroup(chatGroup, target)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.kick.success"), target.getFormattedName()));
            target.sendOrScheduleMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.kick.kicked_target"));
        } else {
            // Target is not in chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.not_in_chatgroup"));
        }
    }
}