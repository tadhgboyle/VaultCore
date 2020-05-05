package net.vaultmc.vaultcore.misc.commands.mail;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

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
            if (first)
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.header"));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.count"), mailList.size()));
            first = false;
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.list"), mail.id, VLOfflinePlayer.getOfflinePlayer(mail.sender).getFormattedName(), Utilities.millisToLongDate(mail.sent)));
        }
    }

    @SubCommand("read")
    public void read(VLPlayer sender, int id) {
        // TODO
    }

    @SubCommand("send")
    public void send(VLPlayer sender, VLOfflinePlayer target, String message) {
        if (message.length() > 255) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.max_length"), message.length()));
            return;
        }

        VaultCore.getDatabase().executeUpdateStatement("INSERT INTO mail (recipient, sender, sent, message) VALUES (?, ?, ?, ?)", target.getUniqueId().toString(), sender.getUniqueId().toString(), System.currentTimeMillis(), message);
        try {
            ResultSet resultSet = VaultCore.getDatabase().executeQueryStatement("SELECT LAST_INSERT_ID() AS id FROM mail");
            String id = "";
            while (resultSet.next()) {
                id = resultSet.getString("id");
            }
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mail.new_mail"), sender.getFormattedName(), id));
        } catch (SQLException e) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.error_alerting"));
            target.sendMessage(VaultLoader.getMessage("vaultcore.commands.mail.error_alerting"));
            e.printStackTrace();
        }
    }
}
