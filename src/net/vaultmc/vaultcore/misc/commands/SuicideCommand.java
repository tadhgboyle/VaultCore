package net.vaultmc.vaultcore.misc.commands;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.*;

@RootCommand(literal = "suicide", description = "Sometimes, you just dont want to be alive :(")
@Permission(Permissions.SuicideCommand)
@PlayerOnly
public class SuicideCommand extends CommandExecutor {

    public SuicideCommand() {
        unregisterExisting();
        register("suicide", Collections.emptyList());
    }

    @Getter
    public static HashMap<VLPlayer, VLPlayer> suicidalPlayers = new HashMap<>();

    List<String> messages = Arrays.asList(
            "{SENDER}&e fell of a cliff while fighting a pigman was exploded by a ghast and burned to a crisp.",
            "{SENDER}&e committed alive'nt.",
            "{SENDER}&e found a tall ladder and short rope.",
            // Thanks to @psrcek
            "{SENDER}&e stepped on Lego.",
            "{SENDER}&e stubbed their toe.",
            // Thanks to @Clikz_
            "{SENDER}&e fell and couldn't get up."
    );

    @SubCommand("suicide")
    public void suicide(VLPlayer sender) {
        sender.setHealth(0.0D);
        String player = ((VLPlayer) VLPlayer.getOnlinePlayers().toArray()[new Random().nextInt(VLPlayer.getOnlinePlayers().size())]).getName();
        String message = ChatColor.translateAlternateColorCodes('&', messages.toArray()[new Random().nextInt(messages.size())].toString().replace("{SENDER}", sender.getFormattedName()));
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(players, "minimal_chat")) continue;
            players.sendMessage(message);
        }
        suicidalPlayers.put(sender, sender);
    }
}
