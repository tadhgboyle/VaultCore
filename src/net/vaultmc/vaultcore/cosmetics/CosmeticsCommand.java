package net.vaultmc.vaultcore.cosmetics;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
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
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Click to enable or disable particles."
                ))
                .build());
        mainMenu.setItem(4, new ItemStackBuilder(Material.EGG)
                .name(ChatColor.YELLOW + "Disguises")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Click to enable or disable disguises."
                ))
                .build());
        mainMenu.setItem(7, new ItemStackBuilder(Material.OBSIDIAN)
                .name(ChatColor.YELLOW + "?????")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "?????"
                ))
                .build());
        sender.openInventory(mainMenu);
    }
}
