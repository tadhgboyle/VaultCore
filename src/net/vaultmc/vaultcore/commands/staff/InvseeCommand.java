package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class InvseeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (commandLabel.equalsIgnoreCase("invsee")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(Utilities.consoleError());
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission(Permissions.InvseeCommand)) {
                player.sendMessage(Utilities.noPermission());
                return true;
            }
            if (args.length != 1) {
                player.sendMessage(Utilities.usageMessage(cmd.getName(), "<player>"));
                return true;
            }
            if (args.length == 1) {

                Player target = Bukkit.getServer().getPlayer(args[0]);

                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "That player is offline!");
                    return true;
                }
                if (target == sender) {
                    sender.sendMessage(ChatColor.RED + "Press e to open your inventory, silly.");
                    return true;
                }
                Inventory targetInv = target.getInventory();
                player.openInventory(targetInv);
            }
        }

        return true;
    }
}