package net.vaultmc.vaultcore.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Report {
    @Getter
    private static final List<Report> reports = new ArrayList<>();
    private VLOfflinePlayer target;
    private List<VLOfflinePlayer> assignees;

    private VLOfflinePlayer reporter;
    private List<Reason> reasons;
    private Status status;
    private String id;

    public Report(VLOfflinePlayer reporter, VLOfflinePlayer target, List<VLOfflinePlayer> assignees, List<Reason> reasons, Status status, String id) {
        this.id = id;
        this.reporter = reporter;
        this.target = target;
        this.assignees = assignees;
        this.reasons = reasons;
        this.status = status;
    }

    public Report(VLOfflinePlayer reporter, VLOfflinePlayer target, List<VLOfflinePlayer> assignees, List<Reason> reasons, Status status) {
        int currentId = VaultCore.getInstance().getData().getInt("report-current-id", 0);
        currentId++;
        VaultCore.getInstance().getData().set("report-current-id", currentId);
        VaultCore.getInstance().saveConfig();

        this.reporter = reporter;
        this.target = target;
        this.assignees = assignees;
        this.reasons = reasons;
        this.status = status;
        this.id = "REPORT-" + currentId;
    }

    public static List<Report> getActiveReports() {
        return reports.stream().filter(report -> report.status == Status.OPEN).collect(Collectors.toList());
    }

    public static Report getReport(String id) {
        for (Report report : reports) {
            if (report.getId().equalsIgnoreCase(id)) {
                return report;
            }
        }
        return null;
    }

    public static void load() {
        if (!VaultCore.getInstance().getData().contains("reports"))
            VaultCore.getInstance().getData().createSection("reports");
        for (String key : VaultCore.getInstance().getData().getConfigurationSection("reports").getKeys(false)) {
            Report report = deserialize(key);
            if (report == null) {
                report = deserialize(VaultCore.getInstance().getData().getConfigurationSection("reports." + key));
            }
            reports.add(report);
        }
    }

    public static void save() {
        for (Report report : reports) {
            report.serialize();
        }
    }

    public static Report deserialize(ConfigurationSection section) {
        return new Report(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(section.getString("reporter"))),
                VLOfflinePlayer.getOfflinePlayer(UUID.fromString(section.getString("reporter"))),
                section.getStringList("assignees").stream().map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toList()),
                section.getStringList("reasons").stream().map(Reason::valueOf).collect(Collectors.toList()),
                Status.valueOf(section.getString("status")), section.getString("id"));
    }

    @SneakyThrows
    public static Report deserialize(String id) {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT * FROM reports WHERE id=?", id)) {
            if (rs.next()) {
                return new Report(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(rs.getString("reporter"))),
                        VLOfflinePlayer.getOfflinePlayer(UUID.fromString(rs.getString("target"))),
                        rs.getString("assignees").trim().equals("") ? new ArrayList<>()
                                : Arrays.stream(rs.getString("assignees").split(VaultCore.SEPARATOR)).map(p -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(p))).collect(Collectors.toList()),
                        Arrays.stream(rs.getString("reasons").split(VaultCore.SEPARATOR)).map(Reason::valueOf).collect(Collectors.toList()),
                        Status.valueOf(rs.getString("status")), rs.getString("id"));
            }
        }
        return null;
    }

    public static void dbInit() {
        VaultCore.getDatabase().executeUpdateStatement("CREATE TABLE IF NOT EXISTS reports (" +
                "id TEXT NOT NULL," +
                "reporter CHAR(36) NOT NULL," +
                "target CHAR(36) NOT NULL," +
                "assignees TEXT NOT NULL," +
                "reasons TEXT NOT NULL," +
                "status VARCHAR(10) NOT NULL" +
                ")");
    }

    @SneakyThrows
    public void serialize() {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT reporter FROM reports WHERE id=?", id)) {
            if (rs.next()) {
                VaultCore.getDatabase().executeUpdateStatement("UPDATE reports SET reporter=?, target=?, assignees=?, reasons=?, status=? WHERE id=?",
                        reporter.getUniqueId().toString(), target.getUniqueId().toString(),
                        assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)),
                        reasons.stream().map(Reason::toString).collect(Collectors.joining(VaultCore.SEPARATOR)), status.toString(), id);
            } else {
                VaultCore.getDatabase().executeUpdateStatement("INSERT INTO reports (id, reporter, target, assignees, reasons, status) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        id, reporter.getUniqueId().toString(), target.getUniqueId().toString(),
                        assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)),
                        reasons.stream().map(Reason::toString).collect(Collectors.joining(VaultCore.SEPARATOR)), status.toString());
            }
        }
    }

    @AllArgsConstructor
    public enum Reason {
        KILL_AURA("report.reasons.kill-aura", Material.IRON_SWORD),
        SPEED("report.reasons.speed", Material.RABBIT_FOOT),
        WATER_WALKING("report.reasons.water-walking", Material.WATER_BUCKET),
        FLY("report.reasons.fly", Material.ELYTRA),
        CHEATS_OTHER("report.reasons.cheats-other", Material.ARROW),
        INAPPROPRIATE_SKIN("report.reasons.inappropriate-skin", Material.PLAYER_HEAD),
        CHAT_VIOLATION("report.reasons.chat-violation", Material.REDSTONE);

        @Getter
        private String key;
        @Getter
        private Material item;
    }

    @AllArgsConstructor
    public enum Status {
        OPEN("report.status.open"),
        CLOSED("report.status.closed"),
        FALSE("report.status.false"),
        RESOLVED("report.status.resolved");

        @Getter
        private String key;
    }
}
