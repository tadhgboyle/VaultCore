package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(literal = "customkeys", description = "View or change your custom chat prefixes.")
@Permission(Permissions.SettingsCommand)
@PlayerOnly
@Aliases({"ck"})
public class PlayerCustomKeys extends CommandExecutor {

    public PlayerCustomKeys() {
        register("pckList", Collections.emptyList());
        register("pckSet", Arrays.asList(Arguments.createLiteral("set"), Arguments.createArgument("context", Arguments.string()), Arguments.createArgument("key", Arguments.greedyString())));
    }

    Map<VLPlayer, Map.Entry<String, String>> cachedKeys = new HashMap<>();

    List<String> contexts = Arrays.asList("chatgroups", "staffchat", "adminchat");
    Map<String, String> contextDefaultKeys = new HashMap<String, String>() {{
        put("chatgroups", "!");
        put("staffchat", "#");
        put("adminchat", ",");
    }};

    @SubCommand("pckList")
    public void pckList(VLPlayer sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.header"));
        for (String context : contexts) {
            try {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.list_format"), context, getCustomKey(sender, context)));
            } catch (NullPointerException e) {
                setCustomKey(sender, context, contextDefaultKeys.get(context));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.list_format"), context, getCustomKey(sender, context)));
            }
        }
    }

    @SubCommand("pckSet")
    public void pckSet(VLPlayer sender, String context, String key) {
        context = context.toLowerCase().trim();
        key = key.trim();
        if (contexts.contains(context)) {
            if (key.length() != 1) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.invalid_key_length"));
                return;
            }
            for (String contextList : contexts) {
                if (getCustomKey(sender, contextList).equalsIgnoreCase(key)) {
                    sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.duplicate_key"), contextList, key));
                    return;
                }
            }
            setCustomKey(sender, context, key);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.set_key"), context, key));
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.settings.custom_keys.invalid_context"));
        }
    }

    public String getCustomKey(VLPlayer target, String context) {
        SQLPlayerData data = target.getPlayerData();
        String key = data.getString("custom_keys-" + context);
        if (key == null) {
            data.set("custom_keys-" + context, contextDefaultKeys.get(context));
            return contextDefaultKeys.get(context);
        }
        return key;
    }

    public void setCustomKey(VLPlayer target, String context, String key) {
        SQLPlayerData data = target.getPlayerData();
        data.set("custom_keys-" + context, key);
    }

    @TabCompleter(
            subCommand = "pckSet",
            argument = "context"
    )
    public List<WrappedSuggestion> suggestContexts(VLPlayer sender, String remaining) {
        return contexts.stream().map(WrappedSuggestion::new).collect(Collectors.toList());
    }
}
