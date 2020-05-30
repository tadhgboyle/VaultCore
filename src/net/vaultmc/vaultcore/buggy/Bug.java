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

package net.vaultmc.vaultcore.buggy;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.WebhookCluster;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.NoDupeArrayList;
import net.vaultmc.vaultloader.utils.PersistentKeyValue;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import okhttp3.OkHttpClient;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Bug {
    private static final SimpleDateFormat ios8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    @Getter
    private static final NoDupeArrayList<Bug> bugs = new NoDupeArrayList<>();

    public static WebhookCluster cluster;
    private static WebhookClient webhook;

    static {
        ios8601.setTimeZone(TimeZone.getTimeZone("CET"));
        WebhookClientBuilder builder = new WebhookClientBuilder(VaultCore.getInstance().getConfig().getString("buggy-webhook-link"));
        builder.setThreadFactory(job -> {
            Thread thread = new Thread(job);
            thread.setName("Webhook Job Thread");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        webhook = builder.build();

        cluster = new WebhookCluster(5);
        cluster.setDefaultHttpClient(new OkHttpClient());
        cluster.setDefaultDaemon(true);
        cluster.addWebhooks(webhook);
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
                "VAULTMC-" + getAndIncreaseCurrentId(), Status.CREATING);
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

    public static int getCurrentId() {
        return PersistentKeyValue.contains("bug-current-id") ? Integer.parseInt(PersistentKeyValue.get("bug-current-id")) : 0;
    }

    public static int getAndIncreaseCurrentId() {
        int id = getCurrentId();
        id++;
        PersistentKeyValue.set("bug-current-id", String.valueOf(id));
        return id - 1;
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
    }

    public static void save() {
        try {
            for (Bug bug : (ArrayList<Bug>) bugs.clone()) {
                bug.serialize();
            }
        } catch (ConcurrentModificationException ignored) {
        }
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
        VaultCore.getDatabase().executeUpdateStatement("DELETE FROM bugs WHERE id=?", getUniqueId());

        VaultCore.getDatabase().executeUpdateStatement("INSERT INTO bugs (id, title, description, actualBehavior, expectedBehavior, " +
                        "stepsToReproduce, additionalInformation, status, hidden, reporter, assignees) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                uniqueId, title, description, actualBehavior, expectedBehavior, String.join(VaultCore.SEPARATOR, stepsToReproduce),
                additionalInformation, status.toString(), hidden, reporter.getUniqueId().toString(),
                assignees.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.joining(VaultCore.SEPARATOR)));
    }

    @SneakyThrows
    public void sendWebhook() {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0x2ff20d)
                .setTitle(new WebhookEmbed.EmbedTitle(title, ""))
                .setDescription("**A new bug has been reported!**\n" +
                        "**Description**: " + description + "\n" +
                        "**Expected Behavior**: " + expectedBehavior + "\n" +
                        "**Actual Behavior**: " + actualBehavior + "\n" +
                        "**Steps to Reproduce**: " + String.join("\n", stepsToReproduce) + "\n" +
                        "**Additional Information**: " + additionalInformation + "\n" +
                        "**Reported by**: " + reporter.getName())
                .build();
        webhook.send(embed);
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
        private final String key;

        Status(String key) {
            this.key = key;
        }
    }
}
