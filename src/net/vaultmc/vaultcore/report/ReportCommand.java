package net.vaultmc.vaultcore.report;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

@RootCommand(
        literal = "report",
        description = "Report a rule breaker."
)
@Permission(Permissions.ReportCommand)
@PlayerOnly
public class ReportCommand extends CommandExecutor implements Listener {
    private static final Map<UUID, ReportData> data = new HashMap<>();

    public ReportCommand() {
        register("report", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("addReason", Collections.singletonList(Arguments.createArgument("reason", Arguments.word())));
        register("next", Collections.singletonList(Arguments.createLiteral("next")));
        register("cancel", Collections.singletonList(Arguments.createLiteral("cancel")));
        VaultCore.getInstance().registerEvents(this);
    }

    private static net.minecraft.world.item.ItemStack generateBook(ReportData data) {
        net.minecraft.world.item.ItemStack item = new net.minecraft.world.item.ItemStack(Items.WRITTEN_BOOK);
        CompoundTag tag = item.getOrCreateTag();
        ListTag pages = new ListTag();
        TextComponent root = new TextComponent("Reporting " + data.getTarget().getFormattedName() + ":\n\n");
        for (Report.Reason reason : Report.Reason.values()) {
            if (data.getReasons().contains(reason)) {
                root.addSibling(Component.Serializer.fromJson(
                        "{\"text\":\"\\u2713 " + VaultLoader.getMessage(reason.getKey()) + "\"," +
                                "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/report " + reason.toString() + "\"}," +
                                "\"hoverEvent\":{\"action\":\"show_text\",   \"value\":{\"text\":\"" + VaultLoader.getMessage(
                                "report.deselect").replace("{REASON}", VaultLoader.getMessage(reason.getKey())) + "\"}}}"
                ));
            } else {
                root.addSibling(Component.Serializer.fromJson(
                        "{\"text\":\"\\u25CB " + VaultLoader.getMessage(reason.getKey()) + "\"," +
                                "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/report " + reason.toString() + "\"}," +
                                "\"hoverEvent\":{\"action\":\"show_text\",   \"value\":{\"text\":\"" + VaultLoader.getMessage(
                                "report.select").replace("{REASON}", VaultLoader.getMessage(reason.getKey())) + "\"}}}"
                ));
            }
        }
        root.addSibling(new TextComponent("\n\n"));
        root.addSibling(Component.Serializer.fromJson(
                "{\"text\":\"" + VaultLoader.getMessage("report.next") + "\n\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/report next\"}}"
        ));
        root.addSibling(Component.Serializer.fromJson(
                "{\"text\":\"" + VaultLoader.getMessage("report.cancel") + "\n\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/report cancel\"}}"
        ));
        pages.add(8, StringTag.valueOf(Component.Serializer.toJson(root)));
        tag.set("pages", pages);
        item.setTag(tag);
        return item;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        data.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("report")
    public void report(VLPlayer reporter, VLOfflinePlayer target) {
        if (!data.containsKey(reporter.getUniqueId())) {
            data.put(reporter.getUniqueId(), new ReportData(target, new HashSet<>()));
        }
        ItemStack oldHand = reporter.getInventory().getItemInMainHand().clone();
        net.minecraft.world.item.ItemStack book = generateBook(data.get(reporter.getUniqueId()));
        reporter.getInventory().setItemInMainHand(book.getBukkitStack());
        ((CraftPlayer) reporter.getPlayer()).getHandle().openBook(book, InteractionHand.MAIN_HAND);
        reporter.getInventory().setItemInMainHand(oldHand);
    }

    @SubCommand("addReason")
    public void addReason(VLPlayer reporter, String reason) {
        if (!data.containsKey(reporter.getUniqueId()))
            return;
        data.get(reporter.getUniqueId()).addReason(Report.Reason.valueOf(reason));
    }

    @SubCommand("next")
    public void next(VLPlayer player) {
        if (!data.containsKey(player.getUniqueId())) return;

    }

    @SubCommand("cancel")
    public void cancel(VLPlayer player) {
        if (!data.containsKey(player.getUniqueId())) return;
        data.remove(player.getUniqueId());
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("Cancel");
        meta.addPage(VaultLoader.getMessage("report.cancel-page"));
        book.setItemMeta(meta);
        player.getPlayer().openBook(book);

    }
}
