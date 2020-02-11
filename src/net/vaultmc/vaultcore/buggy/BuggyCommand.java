package net.vaultmc.vaultcore.buggy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
            pages = Bug.getBugs().stream().filter(b ->
                    (b.getStatus() == Bug.Status.OPEN || b.getStatus() == Bug.Status.REOPENED) && b.getStatus() != Bug.Status.CREATING)
                    .collect(Collectors.toSet()).size() / 7;
        } else {
            pages = Bug.getBugs().stream().filter(b -> b.getStatus() != Bug.Status.CREATING).collect(Collectors.toSet()).size() / 7;
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
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.reporter").replace("{REPORTER}", bug.getReporter().getFormattedName())),
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.assignees").replace("{ASSIGNEES}", listToString(bug.getAssignee()))),
                    new TextComponent(VaultLoader.getMessage("buggy.bugs.hover.status").replace("{STATUS}", VaultLoader.getMessage(bug.getStatus().getKey())))
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

        TextComponent arrows = new TextComponent(ChatColor.GOLD + "\u2190");
        arrows.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buggy bugs " + page + " " + openedOnly));
        arrows.addExtra(new TextComponent("                              "));
        TextComponent next = new TextComponent(ChatColor.GOLD + "\u2191");
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
