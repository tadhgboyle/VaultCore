package net.vaultmc.vaultcore.lobby;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Collections;

public class ServerNavigator extends ConstructorRegisterListener {
    private static final ItemStack paper = new ItemStackBuilder(Material.PAPER)
            .name(ChatColor.GREEN + "Server Navigator")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Easily navigate through worlds",
                    ChatColor.GRAY + "and games on VaultMC."
            ))
            .build();
    private static final Inventory inv = Bukkit.createInventory(null, 9, "Server Navigator");

    static {
        inv.addItem(new ItemStackBuilder(Material.GRASS_BLOCK)
                .name(ChatColor.GOLD + "Survival")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Enjoy a classic vanilla survival experience",
                        ChatColor.GRAY + "with not much additions."
                ))
                .build());
        inv.addItem(new ItemStackBuilder(Material.WOODEN_AXE)
                .name(ChatColor.GOLD + "Creative")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Expand your creativity by building in an ",
                        ChatColor.GRAY + "endless plot-based world."
                ))
                .build());
        inv.addItem(new ItemStackBuilder(Material.RED_BANNER)
                .name(ChatColor.GOLD + "Clans")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "It is Factions, but simplified."
                ))
                .build());
        inv.addItem(new ItemStackBuilder(Material.PHANTOM_MEMBRANE)
                .name(ChatColor.GOLD + "SkyBlock")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Survive on a tiny island and build up",
                        ChatColor.GRAY + "your own world."
                ))
                .build());
        inv.addItem(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.GOLD + "PvP")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "Simple KitPvP gameplay."
                ))
                .build());
    }

    @EventHandler
    public void onNavigatorInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (e.getInventory() == inv) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            if (!Tour.getTouringPlayers().contains(player.getUniqueId())) {
                switch (e.getSlot()) {
                    case 0:
                        player.getPlayer().performCommand("sv");
                        break;
                    case 1:
                        player.getPlayer().performCommand("cr");
                        break;
                    case 2:
                        SQLPlayerData data = player.getPlayerData();
                        if (!data.contains("locations.clans")) {
                            player.teleport(Bukkit.getWorld("clans").getSpawnLocation());
                        } else {
                            player.teleport(Utilities.deserializeLocation(data.getString("locations.clans")));
                        }
                        break;
                    case 3:
                        player.getPlayer().performCommand("is");
                        break;
                    case 4:
                        ByteArrayDataOutput output = ByteStreams.newDataOutput();
                        output.writeUTF("Connect");
                        output.writeUTF("backup");
                        player.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", output.toByteArray());
                        break;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(3, paper));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(3, paper));
        } else {
            e.getPlayer().getInventory().clear(3);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Server Navigator"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onClickCompass(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (e.getClickedInventory() instanceof PlayerInventory && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Server Navigator"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                (ChatColor.GREEN + "Server Navigator").equals(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) &&
                e.getHand() == EquipmentSlot.HAND) {
            e.getPlayer().openInventory(inv);
        }
    }
}
