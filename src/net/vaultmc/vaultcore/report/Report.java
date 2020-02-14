package net.vaultmc.vaultcore.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Report {
    @Getter
    private static final Set<Report> reports = new HashSet<>();
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

    public static Set<Report> getActiveReports() {
        return reports.stream().filter(report -> report.status == Status.OPEN).collect(Collectors.toSet());
    }

    public static void load() {
        if (!VaultCore.getInstance().getData().contains("reports"))
            VaultCore.getInstance().getData().createSection("reports");
        for (String key : VaultCore.getInstance().getData().getConfigurationSection("reports").getKeys(false)) {
            reports.add(deserialize(VaultCore.getInstance().getData().getConfigurationSection("reports." + key)));
        }
    }

    public static void save() {
        VaultCore.getInstance().getData().set("reports", null);
        for (Report report : reports) {
            report.serialize(VaultCore.getInstance().getData().getConfigurationSection("reports." + report.getId()));
        }
    }

    public static Report deserialize(ConfigurationSection section) {
        if (section.contains("::serializeType") && !section.getString("::serializeType").equals("net.vaultmc.vaultcore.report.Report")) {
            throw new IllegalArgumentException("Invalid ConfigurationSection");
        }
        return new Report(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(section.getString("reporter"))),
                VLOfflinePlayer.getOfflinePlayer(UUID.fromString(section.getString("reporter"))),
                section.getStringList("assignees").stream().map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toList()),
                section.getStringList("reasons").stream().map(Reason::valueOf).collect(Collectors.toList()),
                Status.valueOf(section.getString("status")), section.getString("id"));
    }

    public void serialize(ConfigurationSection section) {
        section.set("::serializeType", "net.vaultmc.vaultcore.report.Report");
        section.set("reporter", reporter.getUniqueId().toString());
        section.set("target", target.getUniqueId().toString());
        section.set("assignees", assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList()));
        section.set("reasons", reasons.stream().map(Reason::toString).collect(Collectors.toList()));
        section.set("status", status.toString());
        section.set("id", id);
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
