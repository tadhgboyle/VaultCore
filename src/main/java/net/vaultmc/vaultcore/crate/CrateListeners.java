/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.crate;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CrateListeners extends ConstructorRegisterListener {
    private static final Map<UUID, Inventory> map = new HashMap<>();
    private static final Map<UUID, int[]> range = new HashMap<>();
    private static final Map<UUID, Integer> iteration = new HashMap<>();
    private static final Map<UUID, Integer> targetItems = new HashMap<>();
    private static final Map<UUID, Integer> rolls = new HashMap<>();
    private static final ItemStack yellow = new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static final ItemStack orange = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static final ItemStack lime = new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static final ItemStack red = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static int time = 1;
    public CrateListeners() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(VaultLoader.getInstance(), CrateListeners::updateAllInventories, 5L, 5L);
    }

    private static void updateAllInventories() {
        List<UUID> list = new ArrayList<>();
        map.forEach((uuid, inv) -> {
            VLPlayer player = VLPlayer.getPlayer(uuid);
            if (time % iteration.get(player.getUniqueId()) == 0) {
                rolls.put(player.getUniqueId(), rolls.get(player.getUniqueId()) + 1);
                for (int i = 0; i < range.get(player.getUniqueId()).length; i++) {
                    range.get(player.getUniqueId())[i]++;
                    range.get(player.getUniqueId())[i] %= CrateCommand.getItems().size() - 1;
                }
                int slot = 9;
                for (int index : range.get(player.getUniqueId())) {
                    inv.setItem(slot, CrateCommand.getItemsList().get(index));
                    slot++;
                }
                for (int i = 0; i < 9; i++) {
                    if (i == 4) continue;
                    if (rolls.get(player.getUniqueId()) % 2 == 0) {
                        inv.setItem(i, i % 2 == 0 ? yellow : orange);
                    } else {
                        inv.setItem(i, i % 2 == 0 ? orange : yellow);
                    }
                }
                for (int i = 18; i < 27; i++) {
                    if (i == 22) continue;
                    if (rolls.get(player.getUniqueId()) % 2 == 0) {
                        inv.setItem(i, i % 2 == 0 ? orange : yellow);
                    } else {
                        inv.setItem(i, i % 2 == 0 ? yellow : orange);
                    }
                }
                if (inv.getItem(13).hashCode() == targetItems.get(player.getUniqueId()) && iteration.get(player.getUniqueId()) == 3) {
                    ItemStack item = inv.getItem(13);
                    for (int i = 0; i < 27; i++) {
                        if (i != 13) {
                            inv.setItem(i, i % 2 == 0 ? yellow : lime);
                        }
                    }
                    player.getInventory().addItem(item);
                    player.sendMessageByKey("vaultcore.commands.crate.item-received");
                    list.add(player.getUniqueId());
                    player.getPlayer().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 100, 1F);
                }
            }
            time++;
        });
        for (UUID uid : list) {
            map.remove(uid);
            range.remove(uid);
            iteration.remove(uid);
            targetItems.remove(uid);
            rolls.remove(uid);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        map.remove(e.getPlayer().getUniqueId());
        range.remove(e.getPlayer().getUniqueId());
        iteration.remove(e.getPlayer().getUniqueId());
        targetItems.remove(e.getPlayer().getUniqueId());
        rolls.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        map.remove(e.getPlayer().getUniqueId());
        range.remove(e.getPlayer().getUniqueId());
        iteration.remove(e.getPlayer().getUniqueId());
        targetItems.remove(e.getPlayer().getUniqueId());
        rolls.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RESET + "Crate Rollin'")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND && e.getAction() == Action.RIGHT_CLICK_BLOCK && CrateCommand.getAccessPoints().contains(e.getClickedBlock().getLocation())) {
            e.setCancelled(true);
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (player.getDataConfig().getInt("crate-keys", 0) >= 1) {
                player.getDataConfig().set("crate-keys", player.getDataConfig().getInt("crate-keys") - 1);
                if (player.getWorld().getName().equalsIgnoreCase("lobby")) {
                    player.getInventory().setItem(2, new ItemStackBuilder(Material.TRIPWIRE_HOOK)
                            .amount(Math.min(127, Math.max(1, player.getDataConfig().getInt("crate-keys", 0))))
                            .name(ChatColor.GREEN + "Crate Keys " + ChatColor.YELLOW + "(" + player.getDataConfig().getInt("crate-keys", 0) + ")")
                            .build());
                }
                Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RESET + "Crate Rollin'");
                map.put(player.getUniqueId(), inv);
                for (int i = 0; i < 9; i++) {
                    inv.setItem(i, yellow);
                }
                for (int i = 18; i < 27; i++) {
                    inv.setItem(i, yellow);
                }
                inv.setItem(4, red);
                inv.setItem(22, red);
                range.put(player.getUniqueId(), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
                iteration.put(player.getUniqueId(), 1);
                rolls.put(player.getUniqueId(), 0);

                List<Map.Entry<ItemStack, Integer>> items = new ArrayList<>(CrateCommand.getItems().entrySet());
                items.sort(Map.Entry.comparingByValue());
                Collections.reverse(items);
                ItemStack target = items.get(ThreadLocalRandom.current().nextInt(items.size())).getKey();
                for (Map.Entry<ItemStack, Integer> entry : items) {
                    if (ThreadLocalRandom.current().nextInt(1, 101) < entry.getValue()) {
                        target = entry.getKey();
                        break;
                    }
                }
                targetItems.put(player.getUniqueId(), target.hashCode());

                Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                    if (iteration.containsKey(player.getUniqueId())) {
                        iteration.put(player.getUniqueId(), 2);
                        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                            if (iteration.containsKey(player.getUniqueId())) {
                                iteration.put(player.getUniqueId(), 3);
                            }
                        }, 100L);
                    }
                }, 400L);
                updateAllInventories();
                player.openInventory(inv);
            } else {
                player.sendMessageByKey("vaultcore.commands.crate.crate-key-required");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(2, new ItemStackBuilder(Material.TRIPWIRE_HOOK)
                            .amount(Math.min(127, Math.max(1, player.getDataConfig().getInt("crate-keys", 0))))
                            .name(ChatColor.GREEN + "Crate Keys " + ChatColor.YELLOW + "(" + player.getDataConfig().getInt("crate-keys", 0) + ")")
                            .build()));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(2, new ItemStackBuilder(Material.TRIPWIRE_HOOK)
                            .amount(Math.min(127, Math.max(1, player.getDataConfig().getInt("crate-keys", 0))))
                            .name(ChatColor.GREEN + "Crate Keys " + ChatColor.YELLOW + "(" + player.getDataConfig().getInt("crate-keys", 0) + ")")
                            .build()));
        } else {
            e.getPlayer().getInventory().clear(2);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Crate Keys"))
            e.setCancelled(true);
    }
}
