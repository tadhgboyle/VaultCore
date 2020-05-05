package net.vaultmc.vaultcore.misc.commands.mail;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.IgnoreCommand;
import net.vaultmc.vaultcore.chat.msg.SocialSpyCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RootCommand(literal = "mail", description = "Check or send mail.")
@Permission(Permissions.MailCommand)
@PlayerOnly
public class MailCommand extends CommandExecutor {

    public MailCommand() {
        register("check", Collections.singletonList(Arguments.createLiteral("check")));
        register("read", Arrays.asList(Arguments.createLiteral("read"), Arguments.createArgument("id", Arguments.integerArgument(0))));
        register("delete", Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("id", Arguments.integerArgument(0))));
        register("clear", Collections.singletonList(Arguments.createLiteral("clear")));
        register("clearConfirm", Arrays.asList(Arguments.createLiteral("clear"), Arguments.createArgument("confirm", Arguments.greedyString())));
        register("send", Arrays.asList(Arguments.createLiteral("send"), Arguments.createArgument("target", Arguments.offlinePlayerArgument()), Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("check")
    public static void check(VLPlayer sender) {
        List<Mail> mailList = new ArrayList<>();
        try {
            ResultSet resultSet = VaultCore.getDatabase().executeQueryStatement("SELECT id, recipient, sender, sent, message FROM mail WHERE (recipient = ? AND status = '0')", sender.getUniqueId().toString());
            while (resultSet.next()) {
                mailList.add(new Mail(resultSet.getInt("id"), UUID.fromString(resultSet.getString("recipient")), UUID.fromString(resultSet.getString("sender")), resultSet.getLong("sent"), resultSet.getString("message")));
            }
        } catch (SQLException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.error_checking"));
            e.printStackTrace();
            return;
        }
        if (mailList.size() == 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail"));
            return;
        }
        boolean first = true;
        for (Mail mail : mailList) {
            if (first) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.header"));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.count"), mailList.size()));
            }
            first = false;
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.list"), mail.id, VLOfflinePlayer.getOfflinePlayer(mail.sender).getFormattedName(), Utilities.millisToLongDate(mail.sent)));
        }
    }

    @SubCommand("read")
    public void read(VLPlayer sender, int id) {
        try {
            ResultSet resultSet = VaultCore.getDatabase().executeQueryStatement("SELECT id, recipient, sender, sent, message, status FROM mail WHERE id = ?", id);
            if (resultSet.next()) {
                UUID recipient = UUID.fromString(resultSet.getString("recipient"));
                if (!recipient.equals(sender.getUniqueId())) {
                    sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.not_their_mail"));
                    return;
                }
                int status = resultSet.getInt("status");
                if (status == 1) {
                    sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail_id"));
                    return;
                }
                Mail mail = new Mail(resultSet.getInt("id"), recipient, UUID.fromString(resultSet.getString("sender")), resultSet.getLong("sent"), resultSet.getString("message"));
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.header"));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.layout"), "From", VLOfflinePlayer.getOfflinePlayer(mail.sender).getFormattedName()));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.layout"), "Time", Utilities.millisToLongDate(mail.sent)));
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.layout"), "Content", mail.message));
            } else {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail_id"));
            }
        } catch (SQLException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail_id"));
            e.printStackTrace();
        }
    }

    @SubCommand("delete")
    public void delete(VLPlayer sender, int id) {
        try {
            ResultSet resultSet = VaultCore.getDatabase().executeQueryStatement("SELECT id, recipient, sender, sent, message FROM mail WHERE id = ?", id);
            if (resultSet.next()) {
                UUID recipient = UUID.fromString(resultSet.getString("recipient"));
                if (!recipient.equals(sender.getUniqueId())) {
                    sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.not_their_mail"));
                    return;
                }
                VaultCore.getDatabase().executeUpdateStatement("UPDATE mail SET status = 1 WHERE id = ?", id);
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.marked_read"), id));
            } else {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail_id"));
            }
        } catch (SQLException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.no_mail_id"));
            e.printStackTrace();
        }
    }

    @SubCommand("clear")
    public void clear(VLPlayer sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.clear.confirm"));
    }

    @SubCommand("clearConfirm")
    public void clearConfirm(VLPlayer sender, String confirm) {
        if (confirm.equals("confirm")) {
            VaultCore.getDatabase().executeLargeUpdateStatement("UPDATE mail SET status = '1' WHERE recipient = ?", sender.getUniqueId().toString());
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.clear.cleared"));
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.clear.confirm"));
        }
    }

    @SubCommand("send")
    public void send(VLPlayer sender, VLOfflinePlayer target, String message) {
        if (message.length() > 255) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.max_length"), message.length()));
            return;
        }
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.self_error"));
            return;
        }
        if (IgnoreCommand.isIgnoring(target, sender)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.you_are_ignored"));
            return;
        }
        VaultCore.getDatabase().executeUpdateStatement("INSERT INTO mail (recipient, sender, sent, message) VALUES (?, ?, ?, ?)", target.getUniqueId().toString(), sender.getUniqueId().toString(), System.currentTimeMillis(), message);
        try {
            ResultSet resultSet = VaultCore.getDatabase().executeQueryStatement("SELECT LAST_INSERT_ID() AS id FROM mail");
            String id = "";
            while (resultSet.next()) {
                id = resultSet.getString("id");
            }
            if (target.isOnline() && (PlayerSettings.getSetting(target.getOnlinePlayer(), "settings.notifications")))
                target.getOnlinePlayer().getPlayer().playSound(target.getOnlinePlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 100, (float) Math.pow(2F, (-6F / 12F)) /* High C */);
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.new_mail"), sender.getFormattedName(), id));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.sent"), target.getFormattedName()));
            SocialSpyCommand.sendSS("MAIL", sender, target, message);
        } catch (SQLException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.error_alerting"));
            target.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.error_alerting"));
            e.printStackTrace();
        }
    }
}
