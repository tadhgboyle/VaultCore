package net.vaultmc.vaultcore.buggy;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Bug {
    @Getter
    private static final List<Bug> bugs = new ArrayList<>();
    private static int currentId;
    private String title;
    private String description;
    private String actualBehavior;
    private String expectedBehavior;
    private List<String> stepsToReproduce;
    private String additionalInformation;
    private Status status;
    private boolean hidden;
    private VLOfflinePlayer reporter;
    private List<VLOfflinePlayer> assignees;
    private String uniqueId;

    public Bug() {
        this(null, null, null, null, new ArrayList<>(), null, false, null, new ArrayList<>(),
                "VAULTMC-" + currentId++, Status.CREATING);
    }

    Bug(String title, String description, String actualBehavior, String expectedBehavior, List<String> stepsToReproduce,
        String additionalInformation, boolean hidden, VLOfflinePlayer reporter, List<VLOfflinePlayer> assignees, String uuid) {
        this(title, description, actualBehavior, expectedBehavior, stepsToReproduce,
                additionalInformation, hidden, reporter, assignees, uuid, Status.OPEN);
    }

    Bug(String title, String description, String actualBehavior, String expectedBehavior, List<String> stepsToReproduce,
        String additionalInformation, boolean hidden, VLOfflinePlayer reporter, List<VLOfflinePlayer> assignees, String uuid, Status status) {
        this.title = title;
        this.description = description;
        this.actualBehavior = actualBehavior;
        this.expectedBehavior = expectedBehavior;
        this.stepsToReproduce = stepsToReproduce;
        this.additionalInformation = additionalInformation;
        this.hidden = hidden;
        this.status = status;
        this.reporter = reporter;
        this.assignees = assignees;
        uniqueId = uuid;
    }

    public static Bug getBug(String uuid) {
        for (Bug bug : bugs) {
            if (bug.getUniqueId().equals(uuid)) {
                return bug;
            }
        }
        return null;
    }

    public static void load() {
        for (String key : VaultCore.getInstance().getData().getConfigurationSection("bugs").getKeys(false)) {
            bugs.add(Bug.deserialize(key));
        }
        currentId = VaultCore.getInstance().getData().getInt("bugs-current-id", 0);
    }

    public static void save() {
        for (Bug bug : bugs) {
            bug.serialize();
        }
        VaultCore.getInstance().getData().set("bugs-current-id", currentId);
        VaultCore.getInstance().saveConfig();
    }

    @SneakyThrows
    public static Bug deserialize(String key) {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT * FROM bugs WHERE id=?", key)) {
            if (rs.next()) {
                return new Bug(rs.getString("title"), rs.getString("description"), rs.getString("actualBehavior"),
                        rs.getString("expectedBehavior"), Arrays.asList(rs.getString("stepsToReproduce").split(VaultCore.SEPARATOR)),
                        rs.getString("additionalInformation"),
                        rs.getBoolean("hidden"), VLOfflinePlayer.getOfflinePlayer(rs.getString("reporter")),
                        Arrays.stream(rs.getString("assignees").split(VaultCore.SEPARATOR)).map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s)))
                                .collect(Collectors.toList()), rs.getString("id"), Status.valueOf(rs.getString("status")));
            }
        }
        return null;
    }

    public static void dbInit() {
        VaultCore.getDatabase().executeUpdateStatement("CREATE TABLE IF NOT EXISTS bugs (" +
                "id TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "actualBehavior TEXT NOT NULL," +
                "expectedBehavior TEXT NOT NULL," +
                "stepsToReproduce TEXT NOT NULL," +
                "additionalInformation TEXT NOT NULL," +
                "status VARCHAR(10) NOT NULL," +
                "hidden BOOLEAN NOT NULL," +
                "reporter CHAR(36) NOT NULL," +
                "assignees TEXT NOT NULL" +
                ")");
    }

    @SneakyThrows
    public void serialize() {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT title FROM bugs WHERE id=?", getUniqueId())) {
            if (!rs.next()) {
                VaultCore.getDatabase().executeUpdateStatement("INSERT INTO bugs (id, title, description, actualBehavior, expectedBehavior, " +
                                "stepsToReproduce, additionalInformation, status, hidden, reporter, assignees) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        uniqueId, title, description, actualBehavior, expectedBehavior, String.join(VaultCore.SEPARATOR, stepsToReproduce),
                        additionalInformation, status.toString(), hidden, reporter.getUniqueId().toString(),
                        assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)));
            } else {
                VaultCore.getDatabase().executeUpdateStatement("UPDATE bugs SET title=?, description=?, actualBehavior=?, expectedBehavior=?," +
                                "stepsToReproduce=?, additionalInformation=?, status=?, hidden=?, reporter=?, assignees=? WHERE id=?",
                        title, description, actualBehavior, expectedBehavior, String.join(VaultCore.SEPARATOR, stepsToReproduce),
                        additionalInformation, status.toString(), hidden, reporter.getUniqueId().toString(),
                        assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)), uniqueId);
            }
        }

        ConfigurationSection section = VaultCore.getInstance().getData().createSection("bugs." + getUniqueId());
        section.set("title", title);
        section.set("description", description);
        section.set("actual-behavior", actualBehavior);
        section.set("expected-behavior", expectedBehavior);
        section.set("steps-to-reproduce", stepsToReproduce);
        section.set("additional-information", additionalInformation);
        section.set("hidden", hidden);
        section.set("status", status.toString());
        section.set("reporter", reporter.getUniqueId().toString());
        section.set("assignee", assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList()));
        section.set("uuid", uniqueId);
        VaultCore.getInstance().saveConfig();
    }

    public enum Status {
        CREATING(null),
        OPEN("buggy.bugs.status.open"),
        CLOSED("buggy.bugs.status.closed"),
        RESOLVED("buggy.bugs.status.resolved"),
        REOPENED("buggy.bugs.status.reopened"),
        POSTPONED("buggy.bugs.status.postponed"),
        DUPLICATE("buggy.bugs.status.duplicate"),
        INTENDED("buggy.bugs.status.intended");

        @Getter
        private String key;

        Status(String key) {
            this.key = key;
        }
    }
}
