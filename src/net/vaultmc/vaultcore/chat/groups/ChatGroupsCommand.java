package net.vaultmc.vaultcore.chat.groups;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

@RootCommand(literal = "chatgroup", description = "Secret chats for you and your friends!")
@Permission(Permissions.ChatGroupsCommand)
@Aliases({"cg"})
@PlayerOnly
public class ChatGroupsCommand extends CommandExecutor {

    @Getter
    public static Set<VLPlayer> toggled;

    static HashMap<VLPlayer, ChatGroup> invites = new HashMap<>();

    public ChatGroupsCommand() {
        register("chatGroupInfo", Collections.emptyList());
        // register("chatGroupList", Collections.emptyList()); TODO: Add listing + allow chatgroups to be private
        register("chatGroupToggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        register("chatGroupCreate", Arrays.asList(Arguments.createLiteral("create"), Arguments.createArgument("name", Arguments.string()), Arguments.createArgument("open", Arguments.boolArgument())));
        register("chatGroupJoin", Arrays.asList(Arguments.createLiteral("join"), Arguments.createArgument("name", Arguments.string())));
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
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.in_group"), chatGroup.name));
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.not_in_group"));
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "list", "View all public ChatGroups"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "join <name>", "Join a public ChatGroup"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.info.layout"), "create <name> <public>", "Create a new ChatGroup."));
    }

    @SubCommand("chatGroupToggle")
    public void chatGroupToggle(VLPlayer sender) {
        // Simple
        if (toggled != null && toggled.contains(sender)) {
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
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.creation.error"));
        }
    }

    @SubCommand("chatGroupSettings")
    public void chatGroupSettings(VLPlayer sender) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId().toString())) {
            CGSettingsInv cgSettingsInv = new CGSettingsInv();
            cgSettingsInv.openMainMenu(sender);
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
        }
    }

    @SubCommand("chatGroupInvite")
    public void chatGroupInvite(VLPlayer sender, VLPlayer target) {
        if (invites.containsKey(target)) {
            // Target has pending invite already
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.invites.pending_error"));
        } else {
            ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
            if (chatGroup == null) {
                sender.sendMessage("not in cg");
                return;
            } else if (chatGroup.admins.contains(sender.getUniqueId().toString())) {
                sender.sendMessage("not admin");
                return;
            }
            /*
            if (chatGroup == null || !chatGroup.admins.contains(sender.getUniqueId().toString())) {
                // Command sender is not in a chatgroup or is not admin in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
                return;
            }
             */
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
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null) {
            // Success
            String name = chatGroup.name;
            ChatGroup.chatGroupsFile.set("players." + sender.getUniqueId().toString(), null);
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
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId().toString())) {
            if (ChatGroup.makeAdmin(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.promote"), target.getFormattedName()));
                target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.promoted_target"), sender.getFormattedName()));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
        }
    }

    @SubCommand("chatGroupDemote")
    public void chatGroupDemote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId().toString())) {
            if (ChatGroup.makeMember(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.demote"), target.getFormattedName()));
                target.sendOrScheduleMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.roles.demoted_target"), sender.getFormattedName()));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
        }
    }

    @SubCommand("chatGroupKick")
    public void chatGroupKick(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup chatGroup = ChatGroup.getChatGroup(sender);
        if (chatGroup != null && chatGroup.admins.contains(sender.getUniqueId().toString())) {
            if (ChatGroup.removeFromGroup(chatGroup, target)) {
                // Success
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.kick.success"), target.getFormattedName()));
                target.sendOrScheduleMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.kick.kicked_target"));
            } else {
                // Target is not in chatgroup
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.not_in_chatgroup"));
            }
        } else {
            // Dont have admin perms or not in chatGroup
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.chatgroups.misc_error_sender"));
        }
    }
}
