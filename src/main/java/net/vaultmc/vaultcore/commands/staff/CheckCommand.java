package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

@RootCommand(
        literal = "check",
        description = "Get info about a player."
)
@Permission(Permissions.CheckCommand)
public class CheckCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    private String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    public CheckCommand() {
        register("check", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())), "vaultcore");
    }

    @SubCommand("check")
    public void check(CommandSender sender, OfflinePlayer target) {
        try {
            java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username='"
                            + target.getName() + "'");
            if (!rs.next()) {
                sender.sendMessage(ChatColor.RED + "That player has never joined the server.");
                return;
            }
            String uuid = rs.getString("uuid");
            String username = rs.getString("username");
            long firstseen = rs.getLong("firstseen");
            long lastseen = rs.getLong("lastseen");
            String rank = rs.getString("rank");
            String ip = rs.getString("ip");

            GregorianCalendar firstCal = new GregorianCalendar();
            firstCal.setTimeInMillis(firstseen);

            int fdate = firstCal.get(Calendar.DAY_OF_MONTH);
            int fmonth = firstCal.get(Calendar.MONTH) + 1;
            int fyear = firstCal.get(Calendar.YEAR);

            GregorianCalendar lastCal = new GregorianCalendar();
            lastCal.setTimeInMillis(lastseen);

            int ldate = lastCal.get(Calendar.DAY_OF_MONTH);
            int lmonth = lastCal.get(Calendar.MONTH) + 1;
            int lyear = lastCal.get(Calendar.YEAR);

            sender.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");
            sender.sendMessage("");

            if (target.isOnline()) {
                sender.sendMessage(string + "Checking: " + variable1 + username);
            } else {
                sender.sendMessage(string + "Checking: " + variable1 + username + ChatColor.GRAY + " "
                        + ChatColor.ITALIC + "[OFFLINE]");
            }
            sender.sendMessage(string + "UUID: " + variable2 + uuid);
            sender.sendMessage(string + "First Seen (D/M/Y): " + variable2 + fdate + "/" + variable2
                    + fmonth + "/" + variable2 + fyear);
            sender.sendMessage(string + "Last Seen (D/M/Y): " + variable2 + ldate + "/" + variable2 + lmonth
                    + "/" + variable2 + lyear);
            sender.sendMessage(string + "Last IP: " + variable2 + ip);
            sender.sendMessage(string + "Rank: " + variable2 + rank);
            sender.sendMessage(
                    string + "Database: " + variable2 + "https://database.vaultmc.net/?user=" + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}