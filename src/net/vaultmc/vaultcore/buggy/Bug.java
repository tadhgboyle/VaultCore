package net.vaultmc.vaultcore.buggy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Bug {
    private static final SimpleDateFormat ios8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    @Getter
    private static final List<Bug> bugs = new ArrayList<>();
    private static int currentId;

    static {
        ios8601.setTimeZone(TimeZone.getTimeZone("CET"));
    }

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

    @SneakyThrows
    public static void load() {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT id FROM bugs")) {
            while (rs.next()) {
                Bug bug = Bug.deserialize(rs.getString("id"));
                if (bug == null) return;
                bugs.add(bug);
            }
        }
        currentId = VaultCore.getInstance().getData().getInt("bugs-current-id", 0);
    }

    public static void save() {
        try {
            for (Bug bug : bugs) {
                bug.serialize();
            }
        } catch (ConcurrentModificationException ignored) {
        }
        VaultCore.getInstance().getData().set("bugs-current-id", currentId);
        VaultCore.getInstance().saveConfig();
    }

    public static Bug deserialize(ConfigurationSection map) {
        return new Bug((String) map.get("title"), (String) map.get("description"), (String) map.get("actual-behavior"),
                (String) map.get("expected-behavior"), (List<String>) map.get("steps-to-reproduce"),
                (String) map.get("additional-information"), (boolean) map.get("hidden"),
                VLOfflinePlayer.getOfflinePlayer(UUID.fromString((String) map.get("reporter"))),
                ((List<String>) map.get("assignee")).stream().map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toList()),
                (String) map.get("uuid"), Status.valueOf((String) map.get("status")));
    }

    @SneakyThrows
    public static Bug deserialize(String key) {
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT * FROM bugs WHERE id=?", key)) {
            if (rs.next()) {
                return new Bug(rs.getString("title"), rs.getString("description"), rs.getString("actualBehavior"),
                        rs.getString("expectedBehavior"), Arrays.asList(rs.getString("stepsToReproduce").split(VaultCore.SEPARATOR)),
                        rs.getString("additionalInformation"),
                        rs.getBoolean("hidden"), VLOfflinePlayer.getOfflinePlayer(UUID.fromString(rs.getString("reporter"))),
                        rs.getString("assignees").trim().equals("") ? new ArrayList<>()
                                : Arrays.stream(rs.getString("assignees").split(VaultCore.SEPARATOR)).map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s)))
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
    }

    @SneakyThrows
    public void sendWebhook() {
        JsonObject request = new JsonObject();
        request.addProperty("avatar_url", "https://crafatar.com/avatars/" + reporter.getUniqueId().toString());
        JsonArray embeds = new JsonArray();
        JsonObject embed = new JsonObject();
        embed.addProperty("title", "Buggy Bug Tracker");
        embed.addProperty("type", "rich");
        embed.addProperty("description", "A new bug is reported.");
        embed.addProperty("timestamp", ios8601.format(new Date()));
        embed.addProperty("color", 0xff0000);
        JsonArray fields = new JsonArray();
        JsonObject id = new JsonObject();
        id.addProperty("name", "ID");
        id.addProperty("value", uniqueId);
        fields.add(id);
        JsonObject title = new JsonObject();
        title.addProperty("name", "Title");
        title.addProperty("value", this.title);
        fields.add(title);
        JsonObject description = new JsonObject();
        description.addProperty("name", "Description");
        description.addProperty("value", this.description);
        fields.add(description);
        JsonObject expectedBehavior = new JsonObject();
        expectedBehavior.addProperty("name", "Expected Behavior");
        expectedBehavior.addProperty("value", this.expectedBehavior);
        fields.add(expectedBehavior);
        JsonObject actualBehavior = new JsonObject();
        actualBehavior.addProperty("name", "Actual Behavior");
        actualBehavior.addProperty("value", this.actualBehavior);
        fields.add(actualBehavior);
        JsonObject stepsToReproduce = new JsonObject();
        stepsToReproduce.addProperty("name", "Steps to Reproduce");
        stepsToReproduce.addProperty("value", String.join(", ", this.stepsToReproduce));
        fields.add(stepsToReproduce);
        JsonObject additionalInformation = new JsonObject();
        additionalInformation.addProperty("name", "Additional Information");
        additionalInformation.addProperty("value", this.additionalInformation);
        fields.add(additionalInformation);
        JsonObject isExploit = new JsonObject();
        isExploit.addProperty("name", "Is Exploit");
        isExploit.addProperty("value", String.valueOf(hidden));
        fields.add(isExploit);
        JsonObject reporter = new JsonObject();
        reporter.addProperty("name", "Reporter");
        reporter.addProperty("value", this.reporter.getName());
        fields.add(reporter);
        embed.add("fields", fields);
        embeds.add(embed);
        request.add("embeds", embeds);

        VaultCore.getInstance().getLogger().info("Sending request to Discord: " + request.toString());

        HttpURLConnection connection = (HttpURLConnection) new URL(VaultCore.getInstance().getConfig().getString("buggy-webhook-link") + "?wait=true").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.addRequestProperty("User-Agent", "VaultMC Buggy Bug Tracker: vaultmc.net");
        connection.setRequestProperty("Content-Type", "application/json");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        writer.write(request.toString());
        writer.close();
        connection.connect();

        Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
        VaultCore.getInstance().getLogger().info("Received " + scanner.next() + " from Discord");
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
