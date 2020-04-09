package net.vaultmc.vaultcore.chat.groups;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RootCommand(literal = "chatgroup", description = "Secret chats for you and your friends!")
@Permission(Permissions.ChatGroupsCommand)
@Aliases({"cg"})
@PlayerOnly
public class ChatGroupsCommand extends CommandExecutor {

    public ChatGroupsCommand() {
        // register("chatGroupHelp", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("chatGroupToggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        register("chatGroupCreate", Arrays.asList(Arguments.createLiteral("create"), Arguments.createArgument("name", Arguments.string())));
        register("chatGroupInvite", Arrays.asList(Arguments.createLiteral("invite"), Arguments.createArgument("target", Arguments.playerArgument())));
        register("chatGroupAccept", Collections.singletonList(Arguments.createLiteral("accept")));
        register("chatGroupPromote", Arrays.asList(Arguments.createLiteral("promote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupDemote", Arrays.asList(Arguments.createLiteral("demote"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("chatGroupKick", Arrays.asList(Arguments.createLiteral("kick"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    static HashMap<VLPlayer, ChatGroup> invites = new HashMap<>();
    @Getter
    public static List<VLPlayer> toggled;
    /*
    @SubCommand("chatGroupHelp")
    public void chatGroupHelp(VLPlayer sender) {
    }
    */

    @SubCommand("chatGroupToggle")
    public void chatGroupToggle(VLPlayer sender) {
        if (toggled.contains(sender)) {
            toggled.remove(sender);
        } else {
            toggled.add(sender);
        }
    }

    @SubCommand("chatGroupCreate")
    public void chatGroupCreate(VLPlayer sender, String name) {
        if (ChatGroup.createChatGroup(name, sender)) {
            // Success
        } else {
            // Chatgroup with same name already exists
        }
    }

    @SubCommand("chatGroupInvite")
    public void chatGroupInvite(VLPlayer sender, VLPlayer target) {
        if (invites.containsKey(target)) {
            // Error - Invite pending
        } else {
            invites.put(target, ChatGroup.getChatGroup(sender));
            // Success
        }
    }

    @SubCommand("chatGroupAccept")
    public void chatGroupAccept(VLPlayer sender) {
        if (invites.containsKey(sender)) {
            if (ChatGroup.addToGroup(invites.get(sender), sender)) {
                // Success
            } else {
                // Already in group??
            }
        } else {
            // No invite pending
        }
    }

    @SubCommand("chatGroupPromote")
    public void chatGroupPromote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup cg = ChatGroup.getChatGroup(sender);
        if (cg != null && cg.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.makeAdmin(cg, target)) {
                // Success
            } else {
                // Target is not in chatgroup
            }
        } else {
            // Dont have admin perms
        }
    }

    @SubCommand("chatGroupDemote")
    public void chatGroupDemote(VLPlayer sender, VLOfflinePlayer target) {
        ChatGroup cg = ChatGroup.getChatGroup(sender);
        if (cg != null && cg.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.makeMember(cg, target)) {
                // Success
            } else {
                // Target is not in chatgroup
            }
        } else {
            // Dont have admin perms
        }
    }

    @SubCommand("chatGroupKick")
    public void chatGroupKick(VLPlayer sender, VLPlayer target) {
        ChatGroup cg = ChatGroup.getChatGroup(sender);
        if (cg != null && cg.admins.contains(sender.getUniqueId())) {
            if (ChatGroup.kickFromGroup(cg, target)) {
                // Success
            } else {
                // Target is not in chatgroup
            }
        } else {
            // Dont have admin perms
        }
    }
}
