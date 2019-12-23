package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@RootCommand(
        literal = "seen",
        description = "See how long a player has been online/offline for."
)
@Permission(Permissions.SeenCommand)
public class SeenCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    private String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    public SeenCommand() {
        register("seen", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("seen")
    public void seen(CommandSender sender, OfflinePlayer player) {
        try {
            java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT lastseen FROM players WHERE username='" + player.getName() + "'");
            if (!rs.next()) {
                sender.sendMessage(ChatColor.RED + "This player has never joined before!");
                return;
            }
            long lastseen = rs.getLong("lastseen");
            long currenttime = System.currentTimeMillis();
            long duration = currenttime - lastseen;

            long[] time = Utilities.formatDuration(duration);

            String status;

            if (player.isOnline()) {
                status = ChatColor.GREEN + " online ";
            } else {
                status = ChatColor.RED + " offline ";
            }

            String message = String.format(variable1 + VaultCoreAPI.getName(player) + string + " has been" + status + string + "for "
                    + variable2 + "%d" + string + " days, " + variable2 + "%d" + string + " hours and "
                    + variable2 + "%d" + string + "  minutes.", time[0], time[1], time[2]);
            sender.sendMessage(message);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}