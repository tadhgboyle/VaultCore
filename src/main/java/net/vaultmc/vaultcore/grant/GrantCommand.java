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

package net.vaultmc.vaultcore.grant;

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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RootCommand(
        literal = "grant",
        description = "Easily set a player's rank."
)
@Permission(Permissions.GrantCommand)
@PlayerOnly
public class GrantCommand extends CommandExecutor implements Listener {
    private static final Map<String, ItemStack> items = new LinkedHashMap<>();
    private static final ItemStack noPermission = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
            .name(ChatColor.RED + "No Permission")
            .build();

    static {
        items.put("default", new ItemStackBuilder(Material.GRAY_WOOL)
                .name(ChatColor.DARK_GRAY + "Default")
                .build());
        items.put("member", new ItemStackBuilder(Material.LIGHT_GRAY_WOOL)
                .name(ChatColor.GRAY + "Member")
                .build());
        items.put("patreon", new ItemStackBuilder(Material.WHITE_WOOL)
                .name(ChatColor.WHITE + "Patreon")
                .build());
        items.put("hero", new ItemStackBuilder(Material.LIGHT_BLUE_WOOL)
                .name(ChatColor.AQUA + "Hero")
                .build());
        items.put("titan", new ItemStackBuilder(Material.GREEN_WOOL)
                .name(ChatColor.GREEN + "Titan")
                .build());
        items.put("god", new ItemStackBuilder(Material.ORANGE_WOOL)
                .name(ChatColor.GOLD + "God")
                .build());
        items.put("lord", new ItemStackBuilder(Material.RED_WOOL)
                .name(ChatColor.RED + "" + ChatColor.BOLD + "Lord")
                .build());
        items.put("overlord", new ItemStackBuilder(Material.BROWN_WOOL)
                .name(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Overlord")
                .build());
        items.put("trusted", new ItemStackBuilder(Material.PURPLE_WOOL)
                .name(ChatColor.DARK_PURPLE + "Trusted")
                .build());
        items.put("helper", new ItemStackBuilder(Material.YELLOW_WOOL)
                .name(ChatColor.YELLOW + "Helper")
                .build());
        items.put("moderator", new ItemStackBuilder(Material.CYAN_WOOL)
                .name(ChatColor.DARK_AQUA + "Moderator")
                .build());
        items.put("admin", new ItemStackBuilder(Material.BLUE_WOOL)
                .name(ChatColor.BLUE + "Administrator")
                .build());
    }

    public GrantCommand() {
        register("grant", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("grant")
    public void grant(VLPlayer sender, VLPlayer player) {
        sender.setTemporaryData("vc-grant", player);
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.RESET + "Grant: " + player.getFormattedName());
        items.forEach((rank, item) -> {
            if (sender.hasPermission(Permissions.GrantCommand + "." + rank)) {
                inv.addItem(item);
            } else {
                inv.addItem(noPermission);
            }
        });
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith(ChatColor.RESET + "Grant: ")) {
            e.setCancelled(true);
            VLPlayer sender = VLPlayer.getPlayer((Player) e.getWhoClicked());
            VLPlayer player = (VLPlayer) sender.getTemporaryData("vc-grant");
            if (player == null) {
                return;
            }
            sender.removeTemporaryData("vc-grant");
            if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                return;
            }
            switch (e.getSlot()) {
                case 0:
                    player.setGroup("default");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "default");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "default", "target", sender.getFormattedName());
                    break;
                case 1:
                    player.setGroup("member");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "member");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "member", "target", sender.getFormattedName());
                    break;
                case 2:
                    player.setGroup("patreon");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "patreon");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "patreon", "target", sender.getFormattedName());
                    break;
                case 3:
                    player.setGroup("hero");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "hero");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "hero", "target", sender.getFormattedName());
                    break;
                case 4:
                    player.setGroup("titan");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "titan");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "titan", "target", sender.getFormattedName());
                    break;
                case 5:
                    player.setGroup("god");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "god");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "god", "target", sender.getFormattedName());
                    break;
                case 6:
                    player.setGroup("lord");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "lord");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "lord", "target", sender.getFormattedName());
                    break;
                case 7:
                    player.setGroup("overlord");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "overlord");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "overlord", "target", sender.getFormattedName());
                    break;
                case 8:
                    player.setGroup("trusted");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "trusted");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "trusted", "target", sender.getFormattedName());
                    break;
                case 9:
                    player.setGroup("helper");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "helper");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "helper", "target", sender.getFormattedName());
                    break;
                case 10:
                    player.setGroup("moderator");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "moderator");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "moderator", "target", sender.getFormattedName());
                    break;
                case 11:
                    player.setGroup("admin");
                    sender.sendMessageByKey("vaultcore.commands.grant.updated_sender", "target", player.getFormattedName(), "rank", "administrator");
                    player.sendMessageByKey("vaultcore.commands.grant.updated_player", "rank", "administrator", "target", sender.getFormattedName());
                    break;
            }
        }
    }
}