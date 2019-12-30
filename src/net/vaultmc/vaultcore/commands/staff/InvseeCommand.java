package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.ItemStackBuilder;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RootCommand(
        literal = "invsee",
        description = "Look in a players inventory."
)
@Permission(Permissions.InvseeCommand)
@PlayerOnly
@Aliases("openinv")
public class InvseeCommand extends CommandExecutor implements Listener {
    private static final Map<Player, Inventory> hook = new HashMap<>();

    public InvseeCommand() {
        register("invsee", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
        Bukkit.getPluginManager().registerEvents(this, VaultCore.getInstance());
    }

    @SubCommand("invsee")
    public void invsee(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You couldn't open your own inventory!");
            return;
        }

        if (target.hasPermission(Permissions.InvseeExempt))) {
            sender.sendMessage(ChatColor.RED + "You couldn't open this player's inventory!");
            return;
        }

        sender.openInventory(target.getInventory());
    }
}