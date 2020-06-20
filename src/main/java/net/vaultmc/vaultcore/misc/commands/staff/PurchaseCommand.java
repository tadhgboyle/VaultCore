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

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;

@RootCommand(
        literal = "purchase",
        description = "Handles VaultMC purchases. No you couldn't hack this to get free ranks."
)
@Permission(Permissions.PurchaseCommand)
public class PurchaseCommand extends CommandExecutor implements Listener {
    public PurchaseCommand() {
        register("hero", Arrays.asList(
                Arguments.createLiteral("hero"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("titan", Arrays.asList(
                Arguments.createLiteral("titan"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("god", Arrays.asList(
                Arguments.createLiteral("god"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("lord", Arrays.asList(
                Arguments.createLiteral("lord"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("overlord", Arrays.asList(
                Arguments.createLiteral("overlord"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("chargeback", Arrays.asList(
                Arguments.createLiteral("chargeback"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (player.getDataConfig().contains("hero-experience")) {
            player.getDataConfig().set("hero-experience", null);
            player.saveData();
            player.getPlayer().openBook(VaultCore.getInstance().getData().getItemStack("hero-book"));
        } else if (player.getDataConfig().contains("titan-experience")) {
            player.getDataConfig().set("titan-experience", null);
            player.saveData();
            player.getPlayer().openBook(VaultCore.getInstance().getData().getItemStack("titan-book"));
        } else if (player.getDataConfig().contains("god-experience")) {
            player.getDataConfig().set("god-experience", null);
            player.saveData();
            player.getPlayer().openBook(VaultCore.getInstance().getData().getItemStack("god-book"));
        } else if (player.getDataConfig().contains("lord-experience")) {
            player.getDataConfig().set("lord-experience", null);
            player.saveData();
            player.getPlayer().openBook(VaultCore.getInstance().getData().getItemStack("lord-book"));
        } else if (player.getDataConfig().contains("overlord-experience")) {
            player.getDataConfig().set("overlord-experience", null);
            player.saveData();
            player.getPlayer().openBook(VaultCore.getInstance().getData().getItemStack("overlord-book"));
        }
    }

    @SubCommand("hero")
    public void hero(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set hero");
        if (target.isOnline()) {
            VLPlayer player = target.getOnlinePlayer();
            player.kick(ChatColor.YELLOW + "Please reconnect to continue.");
        } else {
            target.getDataConfig().set("hero-experience", true);
            target.saveData();
        }
    }

    @SubCommand("titan")
    public void titan(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set titan");
        if (target.isOnline()) {
            VLPlayer player = target.getOnlinePlayer();
            player.kick(ChatColor.YELLOW + "Please reconnect to continue.");
        } else {
            target.getDataConfig().set("titan-experience", true);
            target.saveData();
        }
    }

    @SubCommand("god")
    public void god(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set god");
        if (target.isOnline()) {
            VLPlayer player = target.getOnlinePlayer();
            player.kick(ChatColor.YELLOW + "Please reconnect to continue.");
        } else {
            target.getDataConfig().set("god-experience", true);
            target.saveData();
        }
    }

    @SubCommand("lord")
    public void lord(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set lord");
        if (target.isOnline()) {
            VLPlayer player = target.getOnlinePlayer();
            player.kick(ChatColor.YELLOW + "Please reconnect to continue.");
        } else {
            target.getDataConfig().set("lord-experience", true);
            target.saveData();
        }
    }

    @SubCommand("overlord")
    public void overlord(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set overlord");
        if (target.isOnline()) {
            VLPlayer player = target.getOnlinePlayer();
            player.kick(ChatColor.YELLOW + "Please reconnect to continue.");
        } else {
            target.getDataConfig().set("overlord-experience", true);
            target.saveData();
        }
    }

    @SubCommand("chargeback")
    public void chargeback(VLCommandSender sender, VLOfflinePlayer target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + target.getName() + " true Charging back is not allowed on VaultMC. This couldn't be appealed.");
    }
}
