/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.report.Report;
import net.vaultmc.vaultcore.vanish.VanishCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RootCommand(
        literal = "mod",
        description = "Toggles your mod mode status."
)
@Permission(Permissions.ModMode)
@Aliases("modmode")
@PlayerOnly

// TODO
//  Refactor
public class ModMode extends CommandExecutor implements Listener, Runnable {
    private static final Map<VLPlayer, Boolean> status = new HashMap<>();
    private static final Map<VLPlayer, ItemStack[]> inv = new HashMap<>();

    public ModMode() {
        VaultCore.getInstance().registerEvents(this);
        Bukkit.getScheduler().runTaskTimer(VaultCore.getInstance().getBukkitPlugin(), this,
                VaultCore.getInstance().getConfig().getLong("mod-mode.delay"),
                VaultCore.getInstance().getConfig().getLong("mod-mode.delay"));
        register("toggle", Collections.emptyList());
    }

    private void setModMode(VLPlayer player, boolean modMode) {
        if (modMode) {
            status.put(player, true);
            inv.put(player, player.getInventory().getContents());

            player.getInventory().clear();
            run();
            player.sendMessage(VaultLoader.getMessage("mod-mode.enabled"));
        } else {
            status.remove(player);
            player.getInventory().clear();
            player.getInventory().setContents(inv.get(player));

            player.sendMessage(VaultLoader.getMessage("mod-mode.disabled"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (status.getOrDefault(player, false)) {
            setModMode(player, false);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (status.getOrDefault(player, false)) {
                int slot = player.getInventory().getHeldItemSlot();
                if (slot == 1) {
                    Entity entity = Bukkit.selectEntities(Bukkit.getConsoleSender(), "@r").get(0);
                    player.sendMessage(VaultLoader.getMessage("mod-mode.teleportation").replace("{PLAYER}",
                            VLPlayer.getPlayer((Player) entity).getFormattedName()));
                    player.teleport(entity);
                } else if (slot == 3) {
                    player.performCommand("reports");
                } else if (slot == 5) {
                    player.performCommand("vanish");
                }
            }
        }
    }

    @Override
    public void run() {
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            if (status.getOrDefault(player, false)) {
                Inventory inv = player.getInventory();
                inv.setItem(1, new ItemStackBuilder(Material.CLOCK)
                        .name(ChatColor.GOLD + "Random teleportation")
                        .lore(Arrays.asList(
                                "",
                                ChatColor.GRAY + "Teleports you to a random",
                                ChatColor.GRAY + "player."
                        )).build());

                if (Report.getActiveReports() == 0) {
                    inv.setItem(3, new ItemStackBuilder(Material.BOOK)
                            .name(ChatColor.GOLD + "Active Reports: " + ChatColor.GREEN + "0")
                            .lore(Arrays.asList(
                                    "",
                                    ChatColor.GRAY + "View the statuses and perform",
                                    ChatColor.GRAY + "operation on all reports."
                            )).build());
                } else if (Report.getActiveReports() <= 64) {
                    inv.setItem(3, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                            .name(ChatColor.GOLD + "Active Reports: " + ChatColor.YELLOW + Report.getActiveReports())
                            .lore(Arrays.asList(
                                    "",
                                    ChatColor.GRAY + "View the statuses and perform",
                                    ChatColor.GRAY + "operation on all reports."
                            ))
                            .amount(Report.getActiveReports())
                            .build());
                } else {
                    inv.setItem(3, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                            .name(ChatColor.GOLD + "Active Reports: " + ChatColor.YELLOW + Report.getActiveReports())
                            .lore(Arrays.asList(
                                    "",
                                    ChatColor.GRAY + "View the statuses and perform",
                                    ChatColor.GRAY + "operation on all reports."
                            ))
                            .amount(64)
                            .build());
                }

                if (VanishCommand.vanished.getOrDefault(player.getUniqueId(), false)) {
                    inv.setItem(5, new ItemStackBuilder(Material.LIME_DYE)
                            .name(ChatColor.GREEN + "Invisible")
                            .lore(Arrays.asList(
                                    "",
                                    ChatColor.GRAY + "Toggles invisibility."
                            )).build());
                } else {
                    inv.setItem(5, new ItemStackBuilder(Material.GRAY_DYE)
                            .name(ChatColor.GOLD + "Visible")
                            .lore(Arrays.asList(
                                    "",
                                    ChatColor.GRAY + "Toggles invisibility."
                            )).build());
                }
            }
        }
    }

    @SubCommand("toggle")
    public void execute(VLPlayer player) {
        if (status.getOrDefault(player, false)) {
            setModMode(player, false);
        } else {
            setModMode(player, true);
        }
    }
}
