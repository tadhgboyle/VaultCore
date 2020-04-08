package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RootCommand(literal = "suicide", description = "Sometimes, you just dont want to be alive :(")
@Permission(Permissions.SuicideCommand)
@PlayerOnly
public class SuicideCommand extends CommandExecutor {

    public SuicideCommand() {
        unregisterExisting();
        register("suicide", Collections.emptyList());
    }

    List<String> messages = Arrays.asList(
            "{SENDER}&e didn't want to be around {PLAYER}&e anymore.",
            "{SENDER}&e fell of a cliff while fighting a pigman was exploded by a ghast and burned to a crisp.",
            "{SENDER}&e committed liv'nt.",
            "{SENDER}&e had enough of {PLAYER}&e and decided to end it all.",
            "{SENDER}&e found a tall ladder and short rope."
    );

    @SubCommand("suicide")
    public void suicide(VLPlayer sender) {
        sender.setHealth(0.0D);
        String player = ((VLPlayer) VLPlayer.getOnlinePlayers().toArray()[new Random().nextInt(VLPlayer.getOnlinePlayers().size())]).getName();
        String message = ChatColor.translateAlternateColorCodes('&', messages.toArray()[new Random().nextInt(messages.size())].toString().replace("{SENDER}", sender.getFormattedName()).replace("{PLAYER}", player));
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(players, "minimal_chat")) continue;
            players.sendMessage(message);
        }
    }
}
