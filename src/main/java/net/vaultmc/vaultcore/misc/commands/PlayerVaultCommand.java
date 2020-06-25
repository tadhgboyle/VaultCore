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

package net.vaultmc.vaultcore.misc.commands;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RootCommand(
        literal = "playervault",
        description = "Opens your player vault!"
)
@Permission(Permissions.PlayerVault)
@PlayerOnly
public class PlayerVaultCommand extends CommandExecutor implements Listener {
    private static final List<String> worlds = Arrays.asList("clans", "survival", "skyblock");
    private static final Inventory selectInv = Bukkit.createInventory(null, 9, ChatColor.RESET + "Select Player Vault");

    static {
        selectInv.setItem(1, new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .name(ChatColor.GOLD + "Player Vault 1")
                .build());
        selectInv.setItem(3, new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .name(ChatColor.GOLD + "Player Vault 2")
                .build());
        selectInv.setItem(4, new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .name(ChatColor.GOLD + "Player Vault 3")
                .build());
        selectInv.setItem(5, new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .name(ChatColor.GOLD + "Player Vault 4")
                .build());
        selectInv.setItem(7, new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .name(ChatColor.GOLD + "Player Vault 5")
                .build());
        ItemStack border = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build();
        for (int i = 0; i < 9; i++) {
            if (selectInv.getItem(i) == null) {
                selectInv.setItem(i, border);
            }
        }
    }

    public PlayerVaultCommand() {
        register("playerVault", Collections.emptyList());
        register("playerVaultId", Collections.singletonList(
                Arguments.createArgument("id", Arguments.integerArgument(1, 5))
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    public static int getSlots(VLPlayer player) {
        int max = 9;
        for (int i : new int[]{18, 27, 36, 45, 54}) {
            if (i > max && player.hasPermission(Permissions.PlayerVault + "." + i)) {
                max = i;
            }
        }
        return max;
    }

    @SubCommand("playerVault")
    public void playerVault(VLPlayer sender) {
        if (!worlds.contains(sender.getWorld().getName().toLowerCase().replace("_nether", "").replace("_the_end", ""))) {
            sender.sendMessageByKey("vaultcore.commands.player-vault.invalid-world");
            return;
        }
        sender.openInventory(selectInv);
    }

    @SubCommand("playerVaultId")
    public void playerVaultID(VLPlayer sender, int id) {
        String world = sender.getWorld().getName().toLowerCase().replace("_nether", "").replace("_the_end", "");
        if (!worlds.contains(world)) {
            sender.sendMessageByKey("vaultcore.commands.player-vault.invalid-world");
            return;
        }
        sender.setTemporaryData("player-vault-current-id", id);
        sender.setTemporaryData("player-vault-world", world);
        Inventory playerVault = Bukkit.createInventory(null, getSlots(sender), ChatColor.RESET + "Player Vault");
        for (int i = 0; i < getSlots(sender); i++) {
            if (sender.getDataConfig().contains("player-vault." + world + ".i" + id + ".s" + i)) {
                playerVault.setItem(i, sender.getDataConfig().getItemStack("player-vault." + world + ".i" + id + ".s" + i));
            }
        }
        sender.openInventory(playerVault);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Select Player Vault")) {
            e.setCancelled(true);
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            switch (e.getSlot()) {
                case 1:
                    player.closeInventory();
                    playerVaultID(player, 1);
                    break;
                case 3:
                    player.closeInventory();
                    playerVaultID(player, 2);
                    break;
                case 4:
                    player.closeInventory();
                    playerVaultID(player, 3);
                    break;
                case 5:
                    player.closeInventory();
                    playerVaultID(player, 4);
                    break;
                case 7:
                    player.closeInventory();
                    playerVaultID(player, 5);
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Player Vault")) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getPlayer());
            int id = (int) player.getTemporaryData("player-vault-current-id");
            String world = (String) player.getTemporaryData("player-vault-world");
            for (int i = 0; i < e.getInventory().getSize(); i++) {
                player.getDataConfig().set("player-vault." + world + ".i" + id + ".s" + i, e.getInventory().getItem(i));
            }
            player.saveData();
            player.removeTemporaryData("player-vault-current-id");
            player.removeTemporaryData("player-vault-world");
        }
    }
}
