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

package net.vaultmc.vaultcore.settings;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(
        literal = "settings",
        description = "Manage your player settings."
)
@Permission(Permissions.SettingsCommand)
@PlayerOnly
@Aliases("setting")
public class SettingsCommand extends CommandExecutor implements Listener {
    private static final ItemStack item = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.GREEN + "Settings")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Customize your experience on",
                    ChatColor.GRAY + "VaultMC."
            ))
            .build();
    static List<String> settingNames = new ArrayList<>();
    static HashMap<String, String> settingMap = new HashMap<>();

    public SettingsCommand() {
        register("settingsMenu", Collections.emptyList());
        register("settingsSpecific", Collections.singletonList(Arguments.createArgument("setting", Arguments.word())));
        VaultCore.getInstance().registerEvents(this);
    }

    public static void init() {
        for (Settings setting : Settings.values()) {
            settingNames.add(
                    setting.vc_name);
            settingMap.put(
                    setting.vc_name,
                    setting.name);

        }
    }

    @SubCommand("settingsSpecific")
    public void settingsSpecific(VLPlayer sender, String setting) {
        if (settingNames.contains(setting.toLowerCase())) {
            SQLPlayerData data = sender.getPlayerData();
            boolean oldValue = data.getBoolean("settings." + setting);
            data.set("settings." + setting, !oldValue);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.settings.specific.success"), settingMap.get(setting), !oldValue));
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.settings.specific.invalid_setting"));
        }
    }

    @TabCompleter(
            subCommand = "settingsSpecific",
            argument = "setting"
    )
    public List<WrappedSuggestion> suggestSetting(VLPlayer sender, String remaining) {
        return settingNames.stream().map(WrappedSuggestion::new).collect(Collectors.toList());
    }

    @SubCommand("settingsMenu")
    public void settingsMenu(VLPlayer sender) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.RESET + "Settings");
        inv.setItem(10, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Messaging")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Allow other players to send you",
                        ChatColor.GRAY + "private messages.",
                        "",
                        sender.getPlayerData().getBoolean("settings.msg") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(11, new ItemStackBuilder(Material.BLAZE_ROD)
                .name(ChatColor.YELLOW + "Inventory Cycling")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Cycle through rows in your inventory when",
                        ChatColor.GRAY + "you cycle through your hot bar from first slot",
                        ChatColor.GRAY + "to last slot (or reversed).",
                        "",
                        sender.getPlayerData().getBoolean("settings.cycle") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(12, new ItemStackBuilder(Material.COMPASS)
                .name(ChatColor.YELLOW + "Allow TPA's")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Allow other players to request to ",
                        ChatColor.GRAY + "teleport to you (or teleport to them).",
                        "",
                        sender.getPlayerData().getBoolean("settings.tpa") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(13, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "Minimal Messages")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Only see chat messages from ",
                        ChatColor.GRAY + "other players.",
                        ChatColor.GRAY + "Disables join messages etc.",
                        "",
                        sender.getPlayerData().getBoolean("settings.minimal_messages") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(14, new ItemStackBuilder(Material.IRON_BARS)
                .name(ChatColor.YELLOW + "Minimal Caps")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Reduce the amount of caps ",
                        ChatColor.GRAY + "in player messages.",
                        "",
                        sender.getPlayerData().getBoolean("settings.minimal_caps") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(15, new ItemStackBuilder(Material.BELL)
                .name(ChatColor.YELLOW + "Notifications")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Receive sound notifications ",
                        ChatColor.GRAY + "when you receive a message or ",
                        ChatColor.GRAY + "TPA request.",
                        "",
                        sender.getPlayerData().getBoolean("settings.notifications") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(16, new ItemStackBuilder(Material.ENDER_EYE)
                .name(ChatColor.YELLOW + "Auto Accept TPA's")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Auto accept TPA requests. ",
                        "",
                        sender.getPlayerData().getBoolean("settings.autotpa") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(21, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Grammarly")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Transform your messages ",
                        ChatColor.GRAY + "to have grammer gud. ",
                        "",
                        sender.getPlayerData().getBoolean("settings.grammarly") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(23, new ItemStackBuilder(Material.DIRT)
                .name(ChatColor.YELLOW + "Item Drops")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Toggle item drops on or off. ",
                        ChatColor.GRAY + "Only applies in Creative world.",
                        "",
                        sender.getPlayerData().getBoolean("settings.item_drops") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Settings")) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            SQLPlayerData data = player.getPlayerData();
            switch (e.getSlot()) {
                case 10:
                    data.set("settings.msg", !data.getBoolean("settings.msg"));
                    break;
                case 11:
                    data.set("settings.cycle", !data.getBoolean("settings.cycle"));
                    break;
                case 12:
                    data.set("settings.tpa", !data.getBoolean("settings.tpa"));
                    break;
                case 13:
                    data.set("settings.minimal_messages", !data.getBoolean("settings.minimal_messages"));
                    break;
                case 14:
                    data.set("settings.minimal_caps", !data.getBoolean("settings.minimal_caps"));
                    break;
                case 15:
                    data.set("settings.notifications", !data.getBoolean("settings.notifications"));
                    break;
                case 26:
                    data.set("settings.autotpa", !data.getBoolean("settings.autotpa"));
                    break;
                case 21:
                    data.set("settings.grammarly", !data.getBoolean("settings.grammarly"));
                    break;
                case 23:
                    data.set("settings.item_drops", !data.getBoolean("settings.item_drops"));
                    break;
                default:
                    return;
            }
            e.setCancelled(true);
            player.closeInventory();
            settingsMenu(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(5, item));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(5, item));
        } else {
            e.getPlayer().getInventory().clear(5);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Settings"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                (ChatColor.GREEN + "Settings").equals(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) &&
                e.getHand() == EquipmentSlot.HAND) {
            settingsMenu(VLPlayer.getPlayer(e.getPlayer()));
        }
    }
}
