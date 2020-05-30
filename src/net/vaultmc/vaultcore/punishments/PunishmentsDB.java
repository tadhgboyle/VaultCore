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

package net.vaultmc.vaultcore.punishments;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PunishmentsDB {
    public static void createTables() {
        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS bans (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS tempbans (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "expiry BIGINT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS mutes (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS tempmutes (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "expiry BIGINT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS ipbans (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "ip VARCHAR(256) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS iptempbans (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "ip VARCHAR(256) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "expiry BIGINT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS ipmutes (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "ip VARCHAR(256) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS iptempmutes (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "ip VARCHAR(256) NOT NULL," +
                        "status BOOLEAN NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "expiry BIGINT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS kicks (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);");

        VaultCore.getPDatabase().executeUpdateStatement(
                "CREATE TABLE IF NOT EXISTS warnings (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "uuid CHAR(36) NOT NULL," +
                        "reason TEXT NOT NULL," +
                        "actor CHAR(36) NOT NULL," +
                        "executionTime BIGINT NOT NULL);"
        );
    }

    public static void registerData(String table, PunishmentData data) {
        Bukkit.getScheduler().runTaskAsynchronously(VaultCore.getInstance().getBukkitPlugin(), () -> {
            if (table.equals("kicks") || table.equals("warnings")) {
                VaultCore.getPDatabase().executeUpdateStatement(
                        "INSERT INTO " + table + " (uuid, reason, actor, executionTime)" +
                                "VALUES (?, ?, ?, ?);",
                        data.getVictim(), data.getReason(), data.getActor().toString(), Utilities.currentTime()
                );
            } else if (table.contains("temp")) {
                VaultCore.getPDatabase().executeUpdateStatement(
                        "INSERT INTO " + table + " (" + (table.contains("ip") ? "ip" : "uuid") + ", status, reason, expiry, actor, executionTime)" +
                                "VALUES (?, ?, ?, ?, ?, ?);",
                        data.getVictim(), data.isStatus(), data.getReason(), data.getExpiry(), data.getActor().toString(), Utilities.currentTime());
            } else {
                VaultCore.getPDatabase().executeUpdateStatement(
                        "INSERT INTO " + table + " (" + (table.contains("ip") ? "ip" : "uuid") + ", status, reason, actor, executionTime)" +
                                "VALUES (?, ?, ?, ?, ?);",
                        data.getVictim(), data.isStatus(), data.getReason(), data.getActor().toString(), Utilities.currentTime());
            }
        });
    }

    public static void unregisterData(String table, String victim) {
        Bukkit.getScheduler().runTaskAsynchronously(VaultCore.getInstance().getBukkitPlugin(), () -> VaultCore.getPDatabase().executeUpdateStatement(
                "UPDATE " + table + " SET status = false WHERE " + (table.contains("ip") ? "ip" : "uuid") +
                        " = ?;", victim));
    }

    public static PunishmentData retrieveData(String table, String victim) {
        return retrieveData(table, victim, false);
    }

    public static PunishmentData retrieveData(String table, String victim, boolean bypass) {
        if (Bukkit.isPrimaryThread() && !bypass) {
            throw new RuntimeException("[VaultUtils] THIS IS NOT A BUG.\n" +
                    "A retrieve data check occurred at primary thread, in which it might cause lags to occur.\n" +
                    "If this is the intended behaviour of the plugin author, please use retrieveData(String, UUID, boolean) to " +
                    "bypass this check. Remember this might cause lag spike!");
        }
        try {
            if (table.equalsIgnoreCase("kicks")) {
                throw new IllegalArgumentException("Must not be kicks table.");  // It's just not designed for this purpose.
            }
            ResultSet result = VaultCore.getPDatabase().executeQueryStatement(
                    "SELECT * FROM " + table + " WHERE " + (table.contains("ip") ? "ip" : "uuid") + " = ?;", victim);

            PunishmentData data;
            Map<Long, PunishmentData> map = new HashMap<>();
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                if (!result.next()) break;
                long executionTime = result.getLong("executionTime");
                if (table.contains("temp")) {
                    data = new PunishmentData(victim, result.getBoolean("status"),
                            result.getString("reason"), result.getLong("expiry"), UUID.fromString(result.getString("actor")));
                } else {
                    data = new PunishmentData(victim, result.getBoolean("status"),
                            result.getString("reason"), -1, UUID.fromString(result.getString("actor")));
                }
                map.put(executionTime, data);
            }

            long max = 0;
            for (long i : map.keySet()) {
                if (i > max) max = i;
            }

            result.close();
            return map.get(max);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class PunishmentData {
        @Getter
        private final String victim;
        @Getter
        private final boolean status;
        @Getter
        private final String reason;
        @Getter
        private final long expiry;
        @Getter
        private final UUID actor;
    }
}
