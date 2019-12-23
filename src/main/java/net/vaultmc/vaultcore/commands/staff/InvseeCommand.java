package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
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

    private Player getByValue(Inventory inv) {
        for (Map.Entry<Player, Inventory> entry : hook.entrySet()) {
            if (entry.getValue() == inv) {
                return entry.getKey();
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (hook.containsKey(e.getPlayer())) {
            hook.get(e.getPlayer()).getViewers().forEach(player -> {
                player.sendMessage(ChatColor.YELLOW + "The owner of the inventory you are viewing just left the game!");
                player.closeInventory(InventoryCloseEvent.Reason.DISCONNECT);
            });
            hook.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Inventory") && e.getViewers().size() <= 1) {
            hook.remove(getByValue(e.getInventory()));
        }
    }

    @EventHandler
    public void onPlayerChangesSelfInventory(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory && hook.containsKey(e.getWhoClicked())) {
            Inventory inv = hook.get(e.getWhoClicked());
            PlayerInventory targetInv = (PlayerInventory) e.getClickedInventory();

            for (int i = 0; i <= 35; i++) {
                inv.setItem(i, targetInv.getItem(i));
            }

            inv.setItem(45, targetInv.getHelmet());
            inv.setItem(46, targetInv.getChestplate());
            inv.setItem(47, targetInv.getLeggings());
            inv.setItem(48, targetInv.getBoots());

            inv.setItem(51, targetInv.getItemInMainHand());

            inv.setItem(53, targetInv.getItemInOffHand());
        }
    }

    @EventHandler
    public void onViewerChangesVictimInventory(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;

        if (e.getView().getTitle().equals(ChatColor.RESET + "Inventory")) {
            if (!e.getWhoClicked().hasPermission(Permissions.InvseeModify)) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(ChatColor.RED + "You couldn't modify this player's inventory!");
                return;
            }

            Player victim = getByValue(e.getInventory());
            for (int i = 0; i <= 35; i++) {
                victim.getInventory().setItem(i, e.getInventory().getItem(i));
            }

            victim.getInventory().setHelmet(e.getInventory().getItem(45));
            victim.getInventory().setChestplate(e.getInventory().getItem(46));
            victim.getInventory().setLeggings(e.getInventory().getItem(47));
            victim.getInventory().setBoots(e.getInventory().getItem(48));
            victim.getInventory().setItemInOffHand(e.getInventory().getItem(53));

            victim.getInventory().setItem(victim.getInventory().getHeldItemSlot(), e.getInventory().getItem(51));
        }
    }

    @SubCommand("invsee")
    public void invsee(Player sender, Player target) {
        if (sender == target && !sender.getName().equals("yangyang200") /* Debugging */) {
            sender.sendMessage(ChatColor.RED + "You couldn't open your own inventory!");
            return;
        }

        if (target.hasPermission(Permissions.InvseeExempt) && !sender.getName().equals("yangyang200") && !target.getName().equals("yangyang200")) {
            sender.sendMessage(ChatColor.RED + "You couldn't open this player's inventory!");
            return;
        }

        if (hook.containsKey(target)) {
            sender.openInventory(hook.get(target));
        } else {
            Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RESET + "Inventory");

            for (int i = 0; i <= 35; i++) {
                inv.setItem(i, target.getInventory().getItem(i));
            }

            inv.setItem(45, target.getInventory().getHelmet());
            inv.setItem(46, target.getInventory().getChestplate());
            inv.setItem(47, target.getInventory().getLeggings());
            inv.setItem(48, target.getInventory().getBoots());

            inv.setItem(51, target.getInventory().getItemInMainHand());

            inv.setItem(53, target.getInventory().getItemInOffHand());

            hook.put(target, inv);
            sender.openInventory(inv);
        }
    }
}