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

package net.vaultmc.vaultcore.cosmetics;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RootCommand(
        literal = "cosmetics",
        description = "View available cosmetics."
)
@Permission(Permissions.CosmeticsCommand)
@PlayerOnly
public class CosmeticsCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Multimap<UUID, Cosmetic> appliedCosmetics = HashMultimap.create();
    private static final ItemStack item = new ItemStackBuilder(Material.CHEST)
            .name(ChatColor.GREEN + "Cosmetics")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Ever wanted to make your friend jealous",
                    ChatColor.GRAY + "at you? Then use these cosmetics to make",
                    ChatColor.GRAY + "you stand out!"
            ))
            .build();

    public CosmeticsCommand() {
        register("cosmetics", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    public static void save() {
        for (UUID uuid : appliedCosmetics.keySet()) {
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(uuid);
            player.getDataConfig().set("cosmetics", appliedCosmetics.get(player.getUniqueId()).stream().map(Cosmetic::toString).collect(Collectors.toList()));
            player.saveData();
        }
    }

    @SubCommand("cosmetics")
    public static /* Yep you can use static */ void cosmetics(VLPlayer sender) {
        List<Cosmetic> available = Arrays.stream(Cosmetic.values()).filter(c -> sender.hasPermission(c.getPermission())).collect(Collectors.toList());
        if (available.isEmpty()) {
            sender.sendMessageByKey("vaultcore.commands.cosmetics.none-available");
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RESET + "Cosmetics");
        for (Cosmetic cosmetic : available) {
            if (appliedCosmetics.get(sender.getUniqueId()).contains(cosmetic)) {
                inv.addItem(new ItemStackBuilder(cosmetic.getItem())
                        .name(ChatColor.YELLOW + cosmetic.getName())
                        .lore(Collections.singletonList(ChatColor.GREEN + "Click to disable"))
                        .enchant(Enchantment.DURABILITY, 5)
                        .hideFlags(ItemFlag.HIDE_ENCHANTS)
                        .identifier(cosmetic.toString())
                        .build());
            } else {
                inv.addItem(new ItemStackBuilder(cosmetic.getItem())
                        .name(ChatColor.YELLOW + cosmetic.getName())
                        .lore(Collections.singletonList(ChatColor.GREEN + "Click to enable"))
                        .identifier(cosmetic.toString())
                        .build());
            }
        }
        sender.openInventory(inv);
    }

    @EventHandler
    public void onPlayerJoin2(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(4, item));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if (e.getPlayer().getWorld().getName().equals("Lobby") && (e.getAction() == Action.RIGHT_CLICK_AIR ||
                e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getHeldItemSlot() == 4) {
            e.setCancelled(true);
            cosmetics(VLPlayer.getPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Cosmetics"))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(4, item));
        } else {
            e.getPlayer().getInventory().clear(4);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        for (String s : player.getDataConfig().getStringList("cosmetics")) {
            try {
                appliedCosmetics.put(player.getUniqueId(), Cosmetic.valueOf(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Cosmetics")) {
            e.setCancelled(true);
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() == null) {
                return;
            }
            Cosmetic cosmetic = Cosmetic.valueOf(ItemStackBuilder.getIdentifier(e.getCurrentItem()));
            if (appliedCosmetics.get(player.getUniqueId()).contains(cosmetic)) {
                appliedCosmetics.remove(player.getUniqueId(), cosmetic);
                if (appliedCosmetics.get(player.getUniqueId()).isEmpty()) {
                    player.getDataConfig().set("cosmetics", null);
                    player.saveData();
                }
            } else {
                if (appliedCosmetics.get(player.getUniqueId()).size() > 5) {
                    player.sendMessageByKey("vaultcore.commands.cosmetics.limitation");
                    return;
                }
                appliedCosmetics.put(player.getUniqueId(), cosmetic);
            }
            player.closeInventory();
            cosmetics(player);
        }
    }
}
