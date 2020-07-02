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

package net.vaultmc.vaultcore.economy;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.ResultSet;
import java.util.*;

@RootCommand(
        literal = "moneytop",
        description = "Balance leaderboard"
)
@Permission(Permissions.MoneyTopCommand)
@Aliases("baltop")
public class MoneyTopCommand extends CommandExecutor {
    private static Map<UUID, Double> cache;

    public MoneyTopCommand() {
        register("total", Collections.emptyList());
        Bukkit.getScheduler().runTaskTimerAsynchronously(VaultLoader.getInstance(), this::updateCache, 0, 72000);
    }

    @SneakyThrows
    public void updateCache() {
        // We are not getting a VLOfflinePlayer object right here because it will take a while to update VLOfflinePlayer's cache
        // Instead we are doing something a little bit more hacky
        Map<UUID, Double> money = new HashMap<>();
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT uuid FROM players")) {
            while (rs.next()) {
                for (World world : Bukkit.getWorlds()) {
                    try (ResultSet rs2 = VaultCore.getDatabase().executeQueryStatement("SELECT someValue FROM playerData WHERE uuid=? AND someKey=?",
                            rs.getString("uuid"), "economy." + world.getName())) {
                        double balance = money.getOrDefault(UUID.fromString(rs.getString("uuid")), 0D);
                        balance += Double.parseDouble(rs2.getString("someValue"));
                        money.put(UUID.fromString(rs.getString("uuid")), balance);
                    }
                }
            }
        }
        List<Map.Entry<UUID, Double>> list = new ArrayList<>(money.entrySet());
        list.sort(Map.Entry.comparingByValue());
        cache = new LinkedHashMap<>();
        for (Map.Entry<UUID, Double> entry : list.subList(0, Math.min(10, list.size()))) {
            cache.put(entry.getKey(), entry.getValue());
        }
    }

    @SubCommand("total")
    public void total(VLCommandSender sender) {
        sender.sendMessageByKey("economy.balance-top-header");
        for (Map.Entry<UUID, Double> entry : cache.entrySet()) {
            sender.sendMessageByKey("economy.balance-top", "player", VLOfflinePlayer.getOfflinePlayer(entry.getKey()).getFormattedName(),
                    "amount", String.valueOf(entry.getValue()));
        }
    }
}
