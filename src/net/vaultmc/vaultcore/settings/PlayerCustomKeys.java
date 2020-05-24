package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "customkeys", description = "View or change your custom chat prefixes.")
@Permission(Permissions.SettingsCommand)
@PlayerOnly
@Aliases({"ck"})
public class PlayerCustomKeys extends CommandExecutor {
    public PlayerCustomKeys() {
        register("list", Collections.emptyList());
        register("setChatGroup", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createLiteral("chatgroups"),
                Arguments.createArgument("key", Arguments.character())
        ));
        register("setStaffChat", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createLiteral("staffchat"),
                Arguments.createArgument("key", Arguments.character())
        ));
        register("setAdminChat", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createLiteral("adminchat"),
                Arguments.createArgument("key", Arguments.character())
        ));
    }

    public static void setCustomKey(VLPlayer player, ChatContext context, String key) {
        player.getDataConfig().set("custom-keys." + context.internalName, key);
        player.saveData();
    }

    public static String getCustomKey(VLPlayer player, ChatContext context) {
        return player.getDataConfig().getString("custom-keys." + context.internalName, context.defaultKey);
    }

    public static void execute(VLPlayer sender, ChatContext context, String key) {
        for (ChatContext c : ChatContext.values()) {
            if (getCustomKey(sender, c).equalsIgnoreCase(key)) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.duplicate_key"), c.name, key));
                return;
            }
        }
        setCustomKey(sender, context, key.toLowerCase());
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.set_key"), context.name, key));
    }

    @SubCommand("list")
    public void list(VLPlayer sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.header"));
        for (ChatContext context : ChatContext.values()) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.list_format"), context.name,
                    getCustomKey(sender, context)));
        }
    }

    @SubCommand("setChatGroup")
    @Permission(Permissions.ChatGroupsCommand)
    public void setChatGroup(VLPlayer sender, String key) {
        execute(sender, ChatContext.CHAT_GROUP, key);
    }

    @SubCommand("setStaffChat")
    @Permission(Permissions.StaffChatCommand)
    public void setStaffChat(VLPlayer sender, String key) {
        execute(sender, ChatContext.STAFF_CHAT, key);
    }

    @SubCommand("setAdminChat")
    @Permission(Permissions.AdminChatCommand)
    public void setAdminChat(VLPlayer sender, String key) {
        execute(sender, ChatContext.ADMIN_CHAT, key);
    }
}
