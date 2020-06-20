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

package net.vaultmc.vaultcore.afk;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RootCommand(
        literal = "afk",
        description = "Toggles your (or a player's) AFK state."
)
@Permission(Permissions.AFKCommand)
public class AFKCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Map<VLPlayer, Location> afk = new HashMap<>();  // I have no plans of saving this in data.yml
    @Getter
    private static final Map<VLPlayer, Integer> pt = new HashMap<>();
    private static final Location afkHub = new Location(Bukkit.getWorld("Lobby"), 121.5, 103, -5.5, 0F, 0F);

    public AFKCommand() {
        VaultCore.getInstance().registerEvents(this);
        register("afkSelf", Collections.emptyList());  // Multiple registration for different possible usages.
        register("afkOthers", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (afk.containsKey(player)) {
            player.teleport(afk.remove(player));
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));

            for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (afk.containsKey(player)) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                player.teleport(afk.remove(player));
                player.getPlayer().setStatistic(Statistic.PLAY_ONE_MINUTE, pt.remove(player));
            });
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));

            for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @SubCommand("afkSelf")
    public void afkSelf(VLPlayer player) {
        boolean newValue = !afk.containsKey(player);
        Location loc = null;
        if (newValue) afk.put(player, player.getLocation());
        else loc = afk.remove(player);
        player.setTemporaryData("afk", newValue);

        if (newValue) {
            player.sendMessage(VaultLoader.getMessage("you-afk"));
            pt.put(player, player.getStatistic(Statistic.PLAY_ONE_MINUTE));
            player.teleport(afkHub);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));
            player.getPlayer().setStatistic(Statistic.PLAY_ONE_MINUTE, pt.remove(player));
            if (loc != null) player.teleport(loc);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @SubCommand("afkOthers")
    @Permission(Permissions.AFKCommandOther)
    public void afkOthers(VLCommandSender sender, VLPlayer player) {
        boolean newValue = !afk.containsKey(player);
        Location loc = null;
        if (newValue) afk.put(player, player.getLocation());
        else loc = afk.remove(player);
        player.setTemporaryData("afk", newValue);

        if (newValue) {
            player.sendMessage(VaultLoader.getMessage("you-afk"));
            pt.put(player, player.getStatistic(Statistic.PLAY_ONE_MINUTE));
            player.teleport(afkHub);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));
            player.getPlayer().setStatistic(Statistic.PLAY_ONE_MINUTE, pt.remove(player));
            if (loc != null) player.teleport(loc);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        afk.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}
