package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "heal", description = "Heal a player.")
@Permission(Permissions.HealCommand)
public class HealCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public HealCommand() {
        register("healSelf", Collections.emptyList());
        register("healOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("healSelf")
    @PlayerOnly
    public void healSelf(VLPlayer player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have been " + variable1 + "healed"));
        player.heal();
    }

    @SubCommand("healOthers")
    @Permission(Permissions.HealCommandOther)
    public void healOthers(VLCommandSender sender, VLPlayer target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have healed " + variable1 + target.getFormattedName()));
        target.heal();
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been healed by " + variable1 + sender.getFormattedName()));
    }
}