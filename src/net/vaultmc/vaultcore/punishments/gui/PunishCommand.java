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

package net.vaultmc.vaultcore.punishments.gui;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

@RootCommand(
        literal = "punish",
        description = "Punish a player. Shows the reason selector."
)
@Permission(Permissions.PunishCommand)
@PlayerOnly
public class PunishCommand extends CommandExecutor implements Listener {
    private static final Map<UUID, VLOfflinePlayer> targets = new HashMap<>();
    private static final Map<UUID, Reason> currentReasons = new HashMap<>();
    private static final Inventory select;
    private static final Inventory ban;
    private static final Inventory mute;
    private static final Inventory kick;

    static {
        select = Bukkit.createInventory(null, 27, ChatColor.RESET + "What do you want to do?");
        select.setItem(11, new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.RED + "Ban")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "Show all punishments that are applicable",
                        ChatColor.YELLOW + "to a ban."
                ))
                .build());
        select.setItem(13, new ItemStackBuilder(Material.OAK_FENCE)
                .name(ChatColor.GOLD + "Mute")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "Show all punishments that are applicable",
                        ChatColor.YELLOW + "to a mute."
                ))
                .build());
        select.setItem(15, new ItemStackBuilder(Material.IRON_DOOR)
                .name(ChatColor.YELLOW + "Kick")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "Show all punishments that are applicable",
                        ChatColor.YELLOW + "to a kick."
                ))
                .build());

        ban = Bukkit.createInventory(null, 27, ChatColor.RESET + "Ban");
        mute = Bukkit.createInventory(null, 27, ChatColor.RESET + "Mute");
        kick = Bukkit.createInventory(null, 27, ChatColor.RESET + "Kick");

        for (Reason reason : Reason.values()) {
            List<String> lore;
            if (reason.getAdditionalInfo() == null) {
                lore = new ArrayList<>();
            } else {
                lore = new ArrayList<>(Collections.singletonList(ChatColor.YELLOW + reason.getAdditionalInfo()));
            }
            lore.add(ChatColor.DARK_GRAY + reason.toString());
            if (reason.getPunishment() == PunishmentType.BAN) {
                ban.addItem(new ItemStackBuilder(reason.getItem())
                        .name(ChatColor.RESET + VaultLoader.getMessage(reason.getKey()))
                        .lore(lore)
                        .build());
            } else if (reason.getPunishment() == PunishmentType.MUTE) {
                mute.addItem(new ItemStackBuilder(reason.getItem())
                        .name(ChatColor.RESET + VaultLoader.getMessage(reason.getKey()))
                        .lore(lore)
                        .build());
            } else if (reason.getPunishment() == PunishmentType.KICK) {
                kick.addItem(new ItemStackBuilder(reason.getItem())
                        .name(ChatColor.RESET + VaultLoader.getMessage(reason.getKey()))
                        .lore(lore)
                        .build());
            }
        }
    }

    public PunishCommand() {
        register("punish", Collections.singletonList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void selectDuration(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Duration")) {
            e.setCancelled(true);
            Reason reason = currentReasons.remove(e.getWhoClicked().getUniqueId());
            String command = null;
            switch (reason.getPunishment()) {
                case BAN:
                    if ((e.getSlot() == 11 && reason.getOffense1().equals("permanent")) || (e.getSlot() == 13 && reason.getOffense2().equals("permanent")) ||
                            (e.getSlot() == 15 && reason.getOffense3().equals("permanent"))) {
                        command = "ban ";
                    } else {
                        command = "tempban ";
                    }
                    break;
                case MUTE:
                    if ((e.getSlot() == 11 && reason.getOffense1().equals("permanent")) || (e.getSlot() == 13 && reason.getOffense2().equals("permanent")) ||
                            (e.getSlot() == 15 && reason.getOffense3().equals("permanent"))) {
                        command = "mute ";
                    } else {
                        command = "tempmute ";
                    }
                    break;
                case KICK:
                    command = "kick ";
                    break;
            }
            command += targets.remove(e.getWhoClicked().getUniqueId()).getName() + " ";
            if (e.getSlot() == 11) {
                if (!reason.getOffense1().equals("permanent")) {
                    command += reason.getOffense1() + " ";
                }
                command += "true " + VaultLoader.getMessage(reason.getKey());
                ((Player) e.getWhoClicked()).performCommand(command);
                e.getWhoClicked().closeInventory();
            } else if (e.getSlot() == 13) {
                if (!reason.getOffense1().equals("permanent")) {
                    command += reason.getOffense1() + " ";
                }
                command += "true " + VaultLoader.getMessage(reason.getKey());
                ((Player) e.getWhoClicked()).performCommand(command);
                e.getWhoClicked().closeInventory();
            } else if (e.getSlot() == 15) {
                if (!reason.getOffense1().equals("permanent")) {
                    command += reason.getOffense1() + " ";
                }
                command += "true " + VaultLoader.getMessage(reason.getKey());
                ((Player) e.getWhoClicked()).performCommand(command);
                e.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void selectReason(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Ban") || e.getView().getTitle().equals(ChatColor.RESET + "Mute") ||
                e.getView().getTitle().equals(ChatColor.RESET + "Kick")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                String id = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size() - 1));
                Reason reason = Reason.valueOf(id);
                e.getWhoClicked().closeInventory();

                currentReasons.put(e.getWhoClicked().getUniqueId(), reason);
                Inventory duration = Bukkit.createInventory(null, 27, ChatColor.RESET + "Duration");
                duration.setItem(11, new ItemStackBuilder(Material.GREEN_WOOL)
                        .name(ChatColor.GREEN + "1st Offense")
                        .lore(Arrays.asList(
                                ChatColor.YELLOW + "Go to " + ChatColor.GOLD + "vaultmc.net" + ChatColor.YELLOW + " for a reference.",
                                ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + reason.getOffense1()
                        ))
                        .build());
                duration.setItem(13, new ItemStackBuilder(Material.YELLOW_WOOL)
                        .name(ChatColor.YELLOW + "2nd Offense")
                        .lore(Arrays.asList(
                                ChatColor.YELLOW + "Go to " + ChatColor.GOLD + "vaultmc.net" + ChatColor.YELLOW + " for a reference.",
                                ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + reason.getOffense2()
                        ))
                        .build());
                duration.setItem(15, new ItemStackBuilder(Material.RED_WOOL)
                        .name(ChatColor.RED + "3rd Offense (or more)")
                        .lore(Arrays.asList(
                                ChatColor.YELLOW + "Go to " + ChatColor.GOLD + "vaultmc.net" + ChatColor.YELLOW + " for a reference.",
                                ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + reason.getOffense3()
                        ))
                        .build());
                e.getWhoClicked().openInventory(duration);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "What do you want to do?")) {
            e.setCancelled(true);
            if (e.getSlot() == 11) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(ban);
            } else if (e.getSlot() == 13) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(mute);
            } else if (e.getSlot() == 15) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(kick);
            }
        }
    }

    @SubCommand("punish")
    public void punish(VLPlayer sender, VLOfflinePlayer player) {
        targets.put(sender.getUniqueId(), player);
        sender.openInventory(select);
    }
}
