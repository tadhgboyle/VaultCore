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

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

@RootCommand(literal = "cosmetics", description = "Open the donor cosmetics GUI.")
@Permission(Permissions.CosmeticsCommand)
@PlayerOnly
public class CosmeticsCommand extends CommandExecutor {

    public CosmeticsCommand() {
        register("mainMenu", Collections.emptyList());
    }

    @SubCommand("mainMenu")
    public void mainMenu(VLPlayer sender) {
        // Flamboyant Flame
        // Wishful Water
        // Ridiculus Rainbow
        // Bouncy Bubbles
        // Smoggy Smoke
        Inventory mainMenu = Bukkit.createInventory(null, 9, "Cosmetics");
        mainMenu.setItem(1, new ItemStackBuilder(Material.NETHER_STAR)
                .name(ChatColor.YELLOW + "Particles")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "Click to enable or disable particles."
                ))
                .build());
        mainMenu.setItem(4, new ItemStackBuilder(Material.EGG)
                .name(ChatColor.YELLOW + "Disguises")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "Click to enable or disable disguises."
                ))
                .build());
        mainMenu.setItem(7, new ItemStackBuilder(Material.OBSIDIAN)
                .name(ChatColor.YELLOW + "?????")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "?????"
                ))
                .build());
        sender.openInventory(mainMenu);
    }
}
