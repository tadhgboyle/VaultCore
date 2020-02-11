package net.vaultmc.vaultcore.buggy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(
        literal = "buggy",
        description = "Report a bug to us."
)
@Permission(Permissions.BuggyCommand)
@PlayerOnly
public class BuggyCommand extends CommandExecutor {
    public BuggyCommand() {
        register("bugs", Collections.singletonList(
                Arguments.createLiteral("bugs")
        ));
        register("bugsPaged", Arrays.asList(
                Arguments.createLiteral("bugs"),
                Arguments.createArgument("page", Arguments.integerArgument(1))
        ));
        register("bugsPagedWhat", Arrays.asList(
                Arguments.createLiteral("bugs"),
                Arguments.createArgument("page", Arguments.integerArgument(1)),
                Arguments.createArgument("openedOnly", Arguments.boolArgument())
        ));
        register("bug", Arrays.asList(
                Arguments.createLiteral("bug"),
                Arguments.createArgument("uid", Arguments.greedyString())
        ));
        register("status", Arrays.asList(
                Arguments.createLiteral("bug"),
                Arguments.createLiteral("status"),
                Arguments.createArgument("status", Arguments.word()),
                Arguments.createArgument("uid", Arguments.greedyString())
        ));
        register("assign", Arrays.asList(
                Arguments.createLiteral("bug"),
                Arguments.createLiteral("assign"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("uid", Arguments.greedyString())
        ));
        register("unassign", Arrays.asList(
                Arguments.createLiteral("bug"),
                Arguments.createLiteral("unassign"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("uid", Arguments.greedyString())
        ));
        register("report", Collections.singletonList(
                Arguments.createLiteral("report")
        ));
    }

    private static String listToString(List<VLOfflinePlayer> players) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (VLOfflinePlayer player : players) {
            if (first) {
                sb.append(player.getFormattedName());
                first = false;
                continue;
            }
            sb.append(ChatColor.YELLOW.toString()).append(", ").append(player.getFormattedName());
        }
        return sb.toString();
    }

    @TabCompleter(
            subCommand = "status",
            argument = "status"
    )
    public List<WrappedSuggestion> suggestStatus(VLPlayer sender, String remaining) {
        return Arrays.stream(Bug.Status.values()).map(s -> new WrappedSuggestion(s.toString().toLowerCase())).collect(Collectors.toList());
    }

    @SubCommand("status")
    @Permission(Permissions.BuggyAdmin)
    public void status(VLPlayer sender, String name, String uid) {
        Bug.Status status;

        try {
            status = Bug.Status.valueOf(name.toUpperCase());
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Bad status.");
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Bad UUID");
            return;
        }
        Bug bug = null;
        for (Bug x : Bug.getBugs()) {
            if (x.getUniqueId() == uuid) {
                bug = x;
                break;
            }
        }
        if (bug == null) {
            sender.sendMessage(VaultLoader.getMessage("buggy.no-bug-found"));
            return;
        }

        bug.setStatus(status);
        sender.sendMessage(VaultLoader.getMessage("buggy.status-set")
                .replace("{BUG}", bug.getTitle())
                .replace("{STATUS}", name));
    }

    @SubCommand("assign")
    @Permission(Permissions.BuggyAdmin)
    public void assign(VLPlayer sender, VLOfflinePlayer player, String uid) {
        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Bad UUID");
            return;
        }
        Bug bug = null;
        for (Bug x : Bug.getBugs()) {
            if (x.getUniqueId() == uuid) {
                bug = x;
                break;
            }
        }
        if (bug == null) {
            sender.sendMessage(VaultLoader.getMessage("buggy.no-bug-found"));
            return;
        }

        bug.getAssignee().add(player);
        player.sendOrScheduleMessage(VaultLoader.getMessage("buggy.you-assigned")
                .replace("{BUG}", bug.getTitle())
                .replace("{UID}", bug.getUniqueId().toString()));
        sender.sendMessage(VaultLoader.getMessage("buggy.assigned")
                .replace("{PLAYER}", player.getFormattedName())
                .replace("{BUG}", bug.getTitle()));
    }

    @SubCommand("unassign")
    @Permission(Permissions.BuggyAdmin)
    public void unassign(VLPlayer sender, VLOfflinePlayer player, String uid) {
        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Bad UUID");
            return;
        }
        Bug bug = null;
        for (Bug x : Bug.getBugs()) {
            if (x.getUniqueId() == uuid) {
                bug = x;
                break;
            }
        }
        if (bug == null) {
            sender.sendMessage(VaultLoader.getMessage("buggy.no-bug-found"));
            return;
        }
        bug.getAssignee().remove(player);
        player.sendOrScheduleMessage(VaultLoader.getMessage("buggy.you-unassigned")
                .replace("{BUG}", bug.getTitle())
                .replace("{UID}", bug.getUniqueId().toString()));
        sender.sendMessage(VaultLoader.getMessage("buggy.unassigned")
                .replace("{PLAYER}", player.getFormattedName())
                .replace("{BUG}", bug.getTitle()));
    }

    @SubCommand("bug")
    public void bug(VLPlayer sender, String uid) {
        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Bad UUID");
            return;
        }
        Bug bug = null;
        for (Bug x : Bug.getBugs()) {
            if (x.getUniqueId() == uuid) {
                bug = x;
                break;
            }
        }
        if (bug == null) {
            sender.sendMessage(VaultLoader.getMessage("buggy.no-bug-found"));
            return;
        }
        sender.sendMessage(bug.getTitle());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage(BuggyListener.Stage.DESCRIPTION.getKey()) + ":");
        sender.sendMessage(bug.getDescription());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage(BuggyListener.Stage.EXPECTED_BEHAVIOR.getKey()) + ":");
        sender.sendMessage(bug.getExpectedBehavior());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage(BuggyListener.Stage.ACTUAL_BEHAVIOR.getKey()) + ":");
        sender.sendMessage(bug.getActualBehavior());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage(BuggyListener.Stage.STEPS_TO_REPRODUCE.getKey()) + ":");
        for (int i = 0; i < bug.getStepsToReproduce().size(); i++) {
            sender.sendMessage(ChatColor.YELLOW.toString() + (i + 1) + ". " + bug.getStepsToReproduce().get(i));
        }
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage(BuggyListener.Stage.ADDITIONAL_INFORMATION.getKey()) + ":");
        sender.sendMessage(bug.getAdditionalInformation());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage("buggy.reporter") + ":");
        sender.sendMessage(bug.getReporter().getFormattedName());
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage("buggy.assignees") + ":");
        sender.sendMessage(bug.getAssignee().size() == 0 ? "-" : listToString(bug.getAssignee()));
        sender.sendMessage(ChatColor.YELLOW + VaultLoader.getMessage("buggy.uid") + ":");
        sender.sendMessage(ChatColor.GOLD + bug.getUniqueId().toString());
    }

    @SubCommand("bugs")
    public void bugs(VLPlayer sender) {
        bugsPaged(sender, 1);
    }

    @SubCommand("bugsPaged")
    public void bugsPaged(VLPlayer sender, int page) {
        bugsPagedWhat(sender, page, true);
    }

    @SubCommand("bugsPagedWhat")
    public void bugsPagedWhat(VLPlayer sender, int page, boolean openedOnly) {
        for (int i = 0; i < 100; i++) {
            sender.sendMessage("\n");
        }

        int pages;
        if (openedOnly) {
            int size = Bug.getBugs().stream().filter(b ->
                    (b.getStatus() == Bug.Status.OPEN || b.getStatus() == Bug.Status.REOPENED) && b.getStatus() != Bug.Status.CREATING)
                    .collect(Collectors.toSet()).size();
            pages = size / 7;
            if (size % 7 != 0) {
                pages++;
            }
        } else {
            int size = Bug.getBugs().stream().filter(b -> b.getStatus() != Bug.Status.CREATING).collect(Collectors.toSet()).size();
            pages = size / 7;
            if (size % 7 != 0) {
                pages++;
            }
        }

        if (page > pages) {
            sender.sendMessage(VaultLoader.getMessage("buggy.only-pages").replace("{PAGE}", String.valueOf(pages)));
            return;
        }
        page--;
        List<Bug> toShow = new ArrayList<>();
        for (int i = page * 7; i < page * 7 + 6; i++) {
            try {
                toShow.add(Bug.getBugs().get(i));
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }

        sender.sendMessage(VaultLoader.getMessage("buggy.bugs.header")
                .replace("{PAGE}", String.valueOf(page + 1))
                .replace("{MAX_PAGE}", String.valueOf(pages)));
        for (Bug bug : toShow) {
            TextComponent component = new TextComponent(ChatColor.GREEN + bug.getTitle());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.reporter").replace("{REPORTER}", bug.getReporter().getFormattedName()) + "\n"),
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.assignees").replace("{ASSIGNEES}", listToString(bug.getAssignee())) + "\n"),
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.status").replace("{STATUS}", VaultLoader.getMessage(bug.getStatus().getKey())) + "\n")
            }));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buggy bug " + bug.getUniqueId().toString()));
            sender.sendMessage(component);
        }

        sender.sendMessage(" ");
        TextComponent showing = new TextComponent(VaultLoader.getMessage("buggy.bugs.showing")
                .replace("{TYPE}", openedOnly ? VaultLoader.getMessage("buggy.bugs.opened-only") :
                        VaultLoader.getMessage("buggy.bugs.all")));
        showing.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buggy bugs " + page + " " + !openedOnly));
        sender.sendMessage(showing);

        TextComponent arrows = page != 1 ? new TextComponent(ChatColor.GOLD + "\u2190") : new TextComponent(" ");
        arrows.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buggy bugs " + page + " " + openedOnly));
        arrows.addExtra(new TextComponent("                              "));
        TextComponent next = (page + 1 != pages) ? new TextComponent(ChatColor.GOLD + "\u2192") : new TextComponent(" ");
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buggy bugs " + (page + 2) + " " + openedOnly));
        arrows.addExtra(next);
        sender.sendMessage(arrows);
    }

    @SubCommand("report")
    public void report(VLPlayer sender) {
        Bug bug = new Bug();
        bug.setReporter(sender);
        BuggyListener.bugs.put(sender.getUniqueId(), bug);
        BuggyListener.stages.put(sender.getUniqueId(), BuggyListener.Stage.TITLE);
        sender.sendMessage(VaultLoader.getMessage("buggy.notice"));
        sender.sendMessage(VaultLoader.getMessage("buggy.title"));
    }
}
