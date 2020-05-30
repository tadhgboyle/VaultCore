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

package net.vaultmc.vaultcore.misc.commands.staff.grant;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GrantCommandListener extends ConstructorRegisterListener {
    private static final String INVTITLE = ChatColor.DARK_GRAY + "Grant Rank to ";

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());

        if (e.getView().getTitle().startsWith(INVTITLE)) {
            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
            } else {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String rank = "Patreon";
                    String itemName = e.getCurrentItem().getItemMeta().getDisplayName();

                    if (itemName.equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Member"))
                        rank = "Member";
                    else if (itemName.equals(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon"))
                        rank = "Patreon";
                    else if (itemName.equals(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted"))
                        rank = "Trusted";
                    else if (itemName.equals(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Moderator"))
                        rank = "Moderator";
                    else if (itemName.equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Admin"))
                        rank = "Admin";
                    else if (itemName.equals(ChatColor.RED + "No Permission")) {
                        player.closeInventory();
                        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.grant.no_permission"));
                        return;
                    }
                    VLPlayer target = VLPlayer.getPlayer(Bukkit.getPlayer(e.getView().getTitle().substring(INVTITLE.length())));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "lp user " + target.getName() + " parent set " + rank);

                    player.closeInventory();

                    player.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.grant.updated_sender"),
                                    target.getFormattedName(), rank));
                    target.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.grant.updated_player"),
                                    rank, player.getFormattedName()));
                }
            }
        }
    }
}