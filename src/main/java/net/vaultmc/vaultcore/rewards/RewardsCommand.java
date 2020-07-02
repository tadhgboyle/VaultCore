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

package net.vaultmc.vaultcore.rewards;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.*;
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
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RootCommand(
        literal = "rewards",
        description = "Open the rewards inventory"
)
@PlayerOnly
public class RewardsCommand extends CommandExecutor implements Listener {
    public static final Material[] mat = {
            Material.TORCH,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_AXE,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_HOE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.LILY_PAD,
            Material.STONE,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.OAK_PLANKS,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.LILY_PAD,
            Material.STONE,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.LILY_PAD,
            Material.STONE,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.STICK,
            Material.LILY_PAD,
            Material.STONE,
            Material.COD,
            Material.COD
    };
    @Getter
    private static final Multimap<UUID, ItemStack> survivalGrant = HashMultimap.create();
    @Getter
    private static final Map<UUID, Map<Reward, Long>> availableRewards = new HashMap<>();
    private static final Reward[] firstRow = {Reward.SURVIVAL_DIAMOND_WEEKLY,
            Reward.SURVIVAL_ENCHANTMENT_BOOK_MONTHLY, Reward.SURVIVAL_SHULKER_BOX_MONTHLY, Reward.SURVIVAL_TROPHY_MONTHLY,
            Reward.SKYBLOCK_BALANCE_MONTHLY};
    private static final ItemStack item = new ItemStackBuilder(Material.DIAMOND)
            .name(ChatColor.GREEN + "Rewards")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Rewards make you more powerful.",
                    ChatColor.GRAY + "We have free diamonds and free items",
                    ChatColor.GRAY + "that you can easily get with a click."
            ))
            .build();

    public RewardsCommand() {
        register("rewards", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    public static void save() {
        for (UUID uuid : availableRewards.keySet()) {
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(uuid);
            player.getDataConfig().set("rewards", null);
            if (availableRewards.containsKey(player.getUniqueId())) {
                for (Map.Entry<Reward, Long> reward : availableRewards.get(player.getUniqueId()).entrySet()) {
                    player.getDataConfig().set("rewards." + reward.getKey().toString(), reward.getValue());
                }
            }
            player.saveData();
        }
    }

    public static boolean available(VLPlayer player, Reward reward) {
        if (!availableRewards.containsKey(player.getUniqueId())) return false;
        if (reward.getDelay() == -1) {
            return availableRewards.get(player.getUniqueId()).containsKey(reward);
        }
        if (!availableRewards.get(player.getUniqueId()).containsKey(reward)) {
            return true;
        }
        return System.currentTimeMillis() >= availableRewards.get(player.getUniqueId()).get(reward);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("survival")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                if (survivalGrant.containsKey(e.getPlayer().getUniqueId())) {
                    for (ItemStack item : survivalGrant.get(e.getPlayer().getUniqueId())) {
                        if (e.getPlayer().getInventory().firstEmpty() == -1) {
                            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
                        } else {
                            e.getPlayer().getInventory().addItem(item);
                        }
                    }
                    survivalGrant.removeAll(e.getPlayer().getUniqueId());
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)  // We want the message to show up last
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (!player.getDataConfig().contains("rewards-granted")) {
            player.getDataConfig().set("rewards-granted", true);
            player.saveData();
            Map<Reward, Long> map = new HashMap<>();
            for (Reward reward : Reward.values()) {
                if (reward.getDelay() != -1) {
                    map.put(reward, 0L);
                }
            }
            availableRewards.put(player.getUniqueId(), map);
            return;
        }
        Map<Reward, Long> map = new HashMap<>();
        if (player.getDataConfig().contains("rewards")) {
            for (String s : player.getDataConfig().getConfigurationSection("rewards").getKeys(false)) {
                try {
                    map.put(Reward.valueOf(s), player.getDataConfig().getLong("rewards." + s));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        availableRewards.put(player.getUniqueId(), map);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Rewards")) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            Reward reward = Reward.valueOf(ItemStackBuilder.getIdentifier(e.getCurrentItem()));
            if (!available(player, reward)) {
                return;
            }
            if (reward.getPermission() != null && !player.hasPermission(reward.getPermission())) {
                player.sendMessageByKey("vaultcore.commands.rewards.couldnt-claim");
                return;
            }
            player.getPlayer().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 100, 1);
            reward.getReward().accept(player);
            if (reward.getDelay() == -1) {
                availableRewards.get(player.getUniqueId()).remove(reward);
            } else {
                availableRewards.get(player.getUniqueId()).replace(reward, System.currentTimeMillis() + reward.getDelay());
            }
            player.closeInventory();
            rewards(player);
        }
    }

    @SubCommand("rewards")
    public void rewards(VLPlayer sender) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RESET + "Rewards");
        for (int i = 0; i < firstRow.length; i++) {
            if (available(sender, firstRow[i])) {
                inv.setItem(i + 20, new ItemStackBuilder(Material.CHEST_MINECART)
                        .name(ChatColor.YELLOW + firstRow[i].getName())
                        .lore(Arrays.asList(
                                ChatColor.GRAY + "There is an available",
                                ChatColor.GRAY + "reward! Click to claim",
                                ChatColor.GRAY + "it right now."
                        ))
                        .identifier(firstRow[i].toString())
                        .build());
            } else {
                inv.setItem(i + 20, new ItemStackBuilder(Material.MINECART)
                        .name(ChatColor.YELLOW + firstRow[i].getName())
                        .lore(Arrays.asList(
                                ChatColor.GRAY + "You already claimed this",
                                ChatColor.GRAY + "reward! Check back later."
                        ))
                        .identifier(firstRow[i].toString())
                        .build());
            }
        }
        if (available(sender, Reward.DAILY_REWARD)) {
            inv.setItem(30, new ItemStackBuilder(Material.CHEST_MINECART)
                    .name(ChatColor.YELLOW + Reward.DAILY_REWARD.getName())
                    .lore(Arrays.asList(
                            ChatColor.GRAY + "There is an available",
                            ChatColor.GRAY + "reward! Click to claim",
                            ChatColor.GRAY + "it right now."
                    ))
                    .identifier("DAILY_REWARD")
                    .build());
        } else {
            inv.setItem(30, new ItemStackBuilder(Material.MINECART)
                    .name(ChatColor.YELLOW + Reward.DAILY_REWARD.getName())
                    .lore(Arrays.asList(
                            ChatColor.GRAY + "You already claimed this",
                            ChatColor.GRAY + "reward! Check back later."
                    ))
                    .identifier("DAILY_REWARD")
                    .build());
        }
        if (available(sender, Reward.VOTE_REWARD)) {
            inv.setItem(32, new ItemStackBuilder(Material.CHEST_MINECART)
                    .name(ChatColor.YELLOW + Reward.VOTE_REWARD.getName())
                    .lore(Arrays.asList(
                            ChatColor.GRAY + "There is an available",
                            ChatColor.GRAY + "reward! Click to claim",
                            ChatColor.GRAY + "it right now."
                    ))
                    .identifier("VOTE_REWARD")
                    .build());
        } else {
            inv.setItem(32, new ItemStackBuilder(Material.MINECART)
                    .name(ChatColor.YELLOW + Reward.VOTE_REWARD.getName())
                    .lore(Arrays.asList(
                            ChatColor.GRAY + "Vote for us using /vote to",
                            ChatColor.GRAY + "receive this reward!"
                    ))
                    .identifier("VOTE_REWARD")
                    .build());
        }
        sender.openInventory(inv);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Rewards"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin2(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(3, item));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld2(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().getInventory().setItem(3, item));
        } else {
            e.getPlayer().getInventory().clear(3);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                (ChatColor.GREEN + "Rewards").equals(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) &&
                e.getHand() == EquipmentSlot.HAND) {
            e.setCancelled(true);
            rewards(VLPlayer.getPlayer(e.getPlayer()));
        }
    }
}
