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

package net.vaultmc.vaultcore.combat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RootCommand(
        literal = "shield",
        description = "Customize the shield that is shown when you are blocking."
)
@Permission(Permissions.ShieldCommand)
@PlayerOnly
public class ShieldCommand extends CommandExecutor implements Listener {
    private static final Map<Integer, Material> bannerIds = new HashMap<>();
    private static final ItemStack shield = new ItemStackBuilder(Material.SHIELD)
            .lore(Arrays.asList(
                    ChatColor.YELLOW + "Click with a banner on",
                    ChatColor.YELLOW + "your cursor to customize!"
            ))
            .build();

    static {
        bannerIds.put(0, Material.WHITE_BANNER);
        bannerIds.put(1, Material.ORANGE_BANNER);
        bannerIds.put(2, Material.MAGENTA_BANNER);
        bannerIds.put(3, Material.LIGHT_BLUE_BANNER);
        bannerIds.put(4, Material.YELLOW_BANNER);
        bannerIds.put(5, Material.LIME_BANNER);
        bannerIds.put(6, Material.PINK_BANNER);
        bannerIds.put(7, Material.GRAY_BANNER);
        bannerIds.put(8, Material.LIGHT_GRAY_BANNER);
        bannerIds.put(9, Material.CYAN_BANNER);
        bannerIds.put(10, Material.PURPLE_BANNER);
        bannerIds.put(11, Material.BLUE_BANNER);
        bannerIds.put(12, Material.BROWN_BANNER);
        bannerIds.put(13, Material.GREEN_BANNER);
        bannerIds.put(14, Material.RED_BANNER);
        bannerIds.put(15, Material.BLACK_BANNER);
    }

    public ShieldCommand() {
        register("shield", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("shield")
    public void shield(VLPlayer sender) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RESET + "Shield");
        if (sender.getDataConfig().contains("shield")) {
            inv.setItem(4, new ItemStackBuilder(Material.SHIELD)
                    .shieldPattern(sender.getDataConfig().getItemStack("shield"))
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Click with a banner on",
                            ChatColor.YELLOW + "your cursor to customize!",
                            "",
                            ChatColor.YELLOW + "Right click to reset."
                    ))
                    .build());
        } else {
            inv.setItem(4, shield);
        }
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (e.getView().getTitle().equals(ChatColor.RESET + "Shield")) {
            e.setCancelled(true);
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            if (e.getSlot() == 4 && e.getCursor() != null) {
                if (e.isLeftClick()) {
                    player.getDataConfig().set("shield", e.getCursor());
                    player.getPlayer().setItemOnCursor(null);
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getInventory().setItem(4, new ItemStackBuilder(Material.SHIELD)
                            .shieldPattern(player.getDataConfig().getItemStack("shield"))
                            .lore(Arrays.asList(
                                    ChatColor.YELLOW + "Click with a banner on",
                                    ChatColor.YELLOW + "your cursor to customize!",
                                    "",
                                    ChatColor.YELLOW + "Right click to reset."
                            ))
                            .build()));
                    player.saveData();
                } else if (e.isRightClick()) {
                    player.getPlayer().setItemOnCursor(player.getDataConfig().getItemStack("shield"));
                    player.getDataConfig().set("shield", null);
                    player.saveData();
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getInventory().setItem(4, shield));
                }
            }
        }
    }
}
