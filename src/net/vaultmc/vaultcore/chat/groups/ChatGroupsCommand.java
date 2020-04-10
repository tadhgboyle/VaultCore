package net.vaultmc.vaultcore.chat.groups;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import sun.nio.ch.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RootCommand(literal = "chatgroup", description = "Secret chats for you and your friends!")
@Permission(Permissions.ChatGroupsCommand)
@Aliases({"chatGroup"})
@PlayerOnly
public class ChatGroupsCommand extends CommandExecutor {
    
    public ChatGroupsCommand() {
        register("chatGroupHelp", Collections.emptyList());
        // register("chatGroupList", Collections.emptyList()); TODO: Add listing + allow chatgroups to be private
        register("chatGroupToggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        register("chatGroupCreate", Arrays.asList(Arguments.createLiteral("create"), Arguments.createArgument("name", Arguments.string()), Arguments.createArgument("open", Arguments.boolArgument())));
        register("chatGroupInvite", Arrays.asList(Arguments.createLiteral("invite"), Arguments.createArgument("target", Arguments.playerArgument())));
        register("chatGroupAccept", Collections.singletonList(Arguments.createLiteral("accept")));
        register("chatGroupDecline", Collections.singletonList(Arguments.createLiteral("decline")));
        register("chatGroupLeave", Collections.singletonList(Arguments.createLiteral("leave")));
        register("chatGroupPromote", Arrays.asList(Arguments.createLiteral("promote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupDemote", Arrays.asList(Arguments.createLiteral("demote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupKick", Arrays.asList(Arguments.createLiteral("kick"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    static HashMap<VLPlayer, ChatGroup> invites = new HashMap<>();
    @Getter
    public static List<VLPlayer> toggled;

    @SubCommand("chatGroupHelp")
    public void chatGroupHelp(VLPlayer sender) {
        // TODO : This
    }

    @SubCommand("chatGroupToggle")
    public void chatGroupToggle(VLPlayer sender) {
        // Simple
        if (toggled.contains(sender)) {
            toggled.remove(sender);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.toggle"), ChatColor.RED + "off"));
        } else {
            toggled.add(sender);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.toggle"), ChatColor.GREEN + "on"));
        }
    }

    @SubCommand("chatGroupCreate")
    public void chatGroupCreate(VLPlayer sender, String name, boolean open) {
        if (ChatGroup.getChatGroup(sender) == null && ChatGroup.createChatGroup(name, sender, open)) {
            // Success
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.creation.success"), name));
        } else {
            // Chatgroup name is taken, or they are already in a chatgroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.creation.error"));
        }
    }

    @SubCommand("chatGroupInvite")
    public void chatGroupInvite(VLPlayer sender, VLPlayer target) {
        if (invites.containsKey(target)) {
            // Target has pending invite already
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.invites.pending_error"));
        } else {
            ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
            if (chatGroup == null || !chatGroup.admins.contains(sender.getUniqueId())) {
                // Command sender is not in a chatgroup or is not admin in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
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
        // TODO - This
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null) {
            // Success
            String name = chatGroup.name;
            ChatGroup.removeFromGroup(chatGroup, sender);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.leave.success"), name));
        } else {
            // Not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.leave.error"));
        }
    }

    @SubCommand("chatGroupPromote")
    public void chatGroupPromote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.makeAdmin(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.roles.promote"), target.getFormattedName()));
                target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.roles.promoted_target"), sender.getFormattedName()));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.roles.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.misc_sender_error"));
        }
    }

    @SubCommand("chatGroupDemote")
    public void chatGroupDemote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.makeMember(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.roles.demote"), target.getFormattedName()));
                target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.roles.demoted_target"), sender.getFormattedName()));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.misc_sender_error"));
        }
    }

    @SubCommand("chatGroupKick")
    public void chatGroupKick(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.removeFromGroup(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.kick.success"), target.getFormattedName()));
                target.sendOrScheduleMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.kick.kicked_target"));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroup.misc_sender_error"));
        }
    }
}
