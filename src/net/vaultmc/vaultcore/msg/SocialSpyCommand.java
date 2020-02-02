package net.vaultmc.vaultcore.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RootCommand(literal = "socialspy", description = "View messages players are sending server-wide.")
@Permission(Permissions.SocialSpyCommand)
@PlayerOnly
public class SocialSpyCommand extends CommandExecutor {
    public static final Set<VLPlayer> toggled = new HashSet<>();

    public SocialSpyCommand() {
        register("toggle", Collections.emptyList());
    }

    @SubCommand("toggle")
    public void toggle(VLPlayer player) {
        if (toggled.contains(player)) {
            toggled.remove(player);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "off"));
        } else {
            toggled.add(player);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "on"));
        }
    }
}
