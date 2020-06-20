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

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@AllArgsConstructor
public enum Reward {
    SURVIVAL_DIAMOND_WEEKLY(player -> {
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), new ItemStack(Material.DIAMOND, 5));
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-diamond");
    }, "Survival Diamond (Weekly)", null, 604800000),
    SURVIVAL_ENCHANTMENT_BOOK_MONTHLY(player -> {
        Enchantment enchantment = Enchantment.values()[ThreadLocalRandom.current().nextInt(Enchantment.values().length)];
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), new ItemStackBuilder(Material.ENCHANTED_BOOK)
                .enchant(enchantment, enchantment.getMaxLevel() != 1 ? ThreadLocalRandom.current().nextInt(1, enchantment.getMaxLevel()) : 1)
                .build());
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-enchantment-book");
    }, "Survival Enchantment Book (Monthly)", "vaultcore.reward.enchantmentbook", 2592000000L),
    SURVIVAL_SHULKER_BOX_MONTHLY(player -> {
        ItemStack box = new ItemStack(Material.RED_SHULKER_BOX);
        BlockStateMeta meta = (BlockStateMeta) box.getItemMeta();
        ShulkerBox state = (ShulkerBox) meta.getBlockState();
        for (int i = 0; i < 10; i++) {
            state.getInventory().addItem(new ItemStack(RewardsCommand.mat[ThreadLocalRandom.current().nextInt(RewardsCommand.mat.length)]));
        }
        meta.setBlockState(state);
        box.setItemMeta(meta);
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), box);
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-loot-box");
    }, "Survival Loot Box (Monthly)", "vaultcore.reward.shulkerbox", 2592000000L),
    SURVIVAL_TROPHY_MONTHLY(player -> {
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), new ItemStackBuilder(Material.DRAGON_EGG)
                .name(ChatColor.GOLD + "Survival Trophy")
                .build());
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-trophy");
    }, "Survival Trophy (Monthly)", "vaultcore.reward.trophy", 2592000000L),
    SKYBLOCK_BALANCE_MONTHLY(player -> {
        player.deposit(Bukkit.getWorld("skyblock"), 100);
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-balance");
    }, "SkyBlock Balance (Monthly)", "vaultcore.reward.balance", 2592000000L),
    VOTE_REWARD(player -> {
        player.deposit(Bukkit.getWorld("skyblock"), 10);
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), new ItemStack(Material.DIAMOND, 3));
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-vote");
    }, "Vote Reward", null, -1),
    DAILY_REWARD(player -> {
        player.deposit(Bukkit.getWorld("skyblock"), 10);
        RewardsCommand.getSurvivalGrant().put(player.getUniqueId(), new ItemStack(Material.DIAMOND, 3));
        player.sendMessageByKey("vaultcore.commands.rewards.claimed-daily");
    }, "Daily Reward", null, 86400000);

    @Getter
    private final Consumer<VLPlayer> reward;
    @Getter
    private final String name;
    @Getter
    private final String permission;
    @Getter
    private final long delay;
}
