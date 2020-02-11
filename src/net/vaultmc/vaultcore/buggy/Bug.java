package net.vaultmc.vaultcore.buggy;

import lombok.Data;
import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Bug {
    @Getter
    private static final List<Bug> bugs = new ArrayList<>();
    private String title;
    private String description;
    private String actualBehavior;
    private String expectedBehavior;
    private List<String> stepsToReproduce;
    private String additionalInformation;
    private Status status;
    private boolean hidden;
    private UUID uniqueId;
    private VLOfflinePlayer reporter;
    private List<VLOfflinePlayer> assignee;

    public Bug() {
        this(null, null, null, null, new ArrayList<>(), null, false, null, new ArrayList<>(), UUID.randomUUID(), Status.CREATING);
    }

    Bug(String title, String description, String actualBehavior, String expectedBehavior, List<String> stepsToReproduce,
        String additionalInformation, boolean hidden, VLOfflinePlayer reporter, List<VLOfflinePlayer> assignee, UUID uuid) {
        this(title, description, actualBehavior, expectedBehavior, stepsToReproduce,
                additionalInformation, hidden, reporter, assignee, uuid, Status.OPEN);
    }

    Bug(String title, String description, String actualBehavior, String expectedBehavior, List<String> stepsToReproduce,
        String additionalInformation, boolean hidden, VLOfflinePlayer reporter, List<VLOfflinePlayer> assignee, UUID uuid, Status status) {
        this.title = title;
        this.description = description;
        this.actualBehavior = actualBehavior;
        this.expectedBehavior = expectedBehavior;
        this.stepsToReproduce = stepsToReproduce;
        this.additionalInformation = additionalInformation;
        this.hidden = hidden;
        this.status = status;
        this.reporter = reporter;
        this.assignee = assignee;
        uniqueId = uuid;
    }

    public static void load() {
        for (String key : VaultCore.getInstance().getData().getConfigurationSection("bugs").getKeys(false)) {
            bugs.add(Bug.deserialize(VaultCore.getInstance().getData().getConfigurationSection("bugs." + key)));
        }
    }

    public static void save() {
        for (Bug bug : bugs) {
            bug.serialize(VaultCore.getInstance().getData().createSection("bugs." + bug.getUniqueId().toString()));
        }
        VaultCore.getInstance().saveConfig();
    }

    public static Bug deserialize(ConfigurationSection map) {
        return new Bug((String) map.get("title"), (String) map.get("description"), (String) map.get("actual-behavior"),
                (String) map.get("expected-behavior"), (List<String>) map.get("steps-to-reproduce"),
                (String) map.get("additional-information"), (boolean) map.get("hidden"),
                VLOfflinePlayer.getOfflinePlayer(UUID.fromString((String) map.get("reporter"))),
                ((List<String>) map.get("assignee")).stream().map(s -> VLOfflinePlayer.getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toList()),
                UUID.fromString((String) map.get("uuid")), Status.valueOf((String) map.get("status")));
    }

    public void serialize(ConfigurationSection section) {
        section.set("title", title);
        section.set("description", description);
        section.set("actual-behavior", actualBehavior);
        section.set("expected-behavior", expectedBehavior);
        section.set("steps-to-reproduce", stepsToReproduce);
        section.set("additional-information", additionalInformation);
        section.set("hidden", hidden);
        section.set("status", status.toString());
        section.set("reporter", reporter.getUniqueId().toString());
        section.set("assignee", assignee.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList()));
        section.set("uuid", uniqueId.toString());
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
