package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.Listener;

import java.util.Collections;

@RootCommand(literal = "invsee", description = "Look in a players inventory.")
@Permission(Permissions.InvseeCommand)
@PlayerOnly
@Aliases("openinv")
public class InvseeCommand extends CommandExecutor implements Listener {
    public InvseeCommand() {
        register("invsee", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("invsee")
    public void invsee(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.self_error"));
            return;
        }

        if (target.hasPermission(Permissions.InvseeExempt) && !sender.hasPermission(Permissions.InvSeeAdmin)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.excempt_error"));
            return;
        }

        sender.openInventory(target.getInventory());
    }
}