package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "heal", description = "Heal a player.")
@Permission(Permissions.HealCommand)
public class HealCommand extends CommandExecutor {
    public HealCommand() {
        register("healSelf", Collections.emptyList());
        register("healOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("healSelf")
    @PlayerOnly
    public void healSelf(VLPlayer player) {
        player.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.self"), "healed"));
        player.heal();
    }

    @SubCommand("healOthers")
    @Permission(Permissions.HealCommandOther)
    public void healOthers(VLCommandSender sender, VLPlayer target) {
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.other"),
                "healed", target.getFormattedName()));
        target.heal();
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.receiver"),
                "healed", sender.getFormattedName()));
    }
}