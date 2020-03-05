package net.vaultmc.vaultcore.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.NoDupeArrayList;
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
    private static final NoDupeArrayList<Report> reports = new NoDupeArrayList<>();
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

    @SneakyThrows
    public static synchronized void load() {
        if (!VaultCore.getInstance().getData().contains("reports"))
            VaultCore.getInstance().getData().createSection("reports");
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT id FROM reports")) {
            while (rs.next()) {
                reports.add(deserialize(rs.getString("id")));
            }
        }
    }

    public static synchronized void save() {
        for (Report report : (ArrayList<Report>) reports.clone()) {
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
    public synchronized void serialize() {
        VaultCore.getDatabase().executeUpdateStatement("DELETE FROM reports WHERE id=?", id);
        VaultCore.getDatabase().executeUpdateStatement("INSERT INTO reports (id, reporter, target, assignees, reasons, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                id, reporter.getUniqueId().toString(), target.getUniqueId().toString(),
                assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)),
                reasons.stream().map(Reason::toString).collect(Collectors.joining(VaultCore.SEPARATOR)), status.toString());
    }

    @AllArgsConstructor
    public enum Reason {
        POLITICAL_DISCUSSION("report.reasons.political", Material.DIAMOND_AXE),
        EXPLOITING_BUGS("report.reasons.bug-exploit", Material.RED_STAINED_GLASS),
        INVADING_PRIVACY("report.reasons.invading-privacy", Material.GLASS_PANE),
        CLIENTSIDE_MODIFICATION("report.reasons.clientside-modification", Material.ELYTRA),
        INTENTIONAL_DISRESPECT("report.reasons.intentional-disrespect", Material.EGG),
        SLOWING_SERVER_DOWN("report.reasons.slowing-server-down", Material.SPLASH_POTION),
        NSFW_SHOCKING("report.reasons.nsfw-shocking", Material.PLAYER_HEAD),
        ADVERTISING_ON_FOR("report.reasons.advertising", Material.OAK_SIGN),
        INAPPROPRIATE_BUILDS("report.reasons.inappropriate-builds", Material.BROWN_CONCRETE),
        OTHER("report.reasons.other", Material.ITEM_FRAME);

        @Getter
        private String key;
        @Getter
        private Material item;
    }

    @AllArgsConstructor
    public enum Status {
        OPEN("report.status.open"),
        CLOSED("report.status.closed"),
        RESOLVED("report.status.resolved");

        @Getter
        private String key;
    }
}
