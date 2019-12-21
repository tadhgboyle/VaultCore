package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

@RootCommand(
        literal = "invsee",
        description = "Look in a players inventory."
)
@Permission(Permissions.InvseeCommand)
@PlayerOnly
@Aliases("openinv")
public class InvseeCommand extends CommandExecutor {
    public InvseeCommand() {
        register("invsee", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())), "vaultcore");
    }

    @SubCommand("invsee")
    public void invsee(CommandSender sender, Player target) {
        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You couldn't open your own inventory!");
            return;
        }
        Inventory targetInv = target.getInventory();
        ((Player) sender).openInventory(targetInv);
    }
}