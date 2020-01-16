package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import java.util.Collections;

@RootCommand(literal = "fly", description = "Enable fly for a player.")
@Permission(Permissions.FlyCommand)
public class FlyCommand extends CommandExecutor {

    String string = Utilities.string;
    String variable1 = Utilities.variable1;

    public FlyCommand() {
        register("flySelf", Collections.emptyList());
        register("flyOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("flySelf")
    @PlayerOnly
    public void flySelf(VLPlayer player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(string + "You're in " + WordUtils.capitalize(player.getGameMode().toString().toLowerCase()) + " mode silly.");
        } else if (!player.getAllowFlight()) {
            player.sendMessage(string + "You have " + variable1 + "enabled" + string + " fly.");
            player.setAllowFlight(true);
        } else {
            player.sendMessage(string + "You have " + variable1 + "disabled" + string + " fly.");
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @SubCommand("flyOthers")
    @Permission(Permissions.FlyCommandOther)
    public void flyOthers(VLCommandSender sender, VLPlayer target) {
        if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) {
            sender.sendMessage(string + "They're in " + WordUtils.capitalize(target.getGameMode().toString().toLowerCase()) + " mode silly.");
        } else if (target.getAllowFlight()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1 + "disabled"
                    + string + " fly for " + variable1 + target.getFormattedName() + string + "."));
            target.setFlying(false);
            target.setAllowFlight(false);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "Your fly has been " + variable1 + "disabled" + string + " by " + variable1
                            + sender.getFormattedName()));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "You have " + variable1 + "enabled" + string + " fly for " + variable1
                            + target.getFormattedName()));
            target.setAllowFlight(true);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "Your fly has been " + variable1 + "enabled" + string + " by " + variable1
                            + sender.getFormattedName()));
        }
    }
}