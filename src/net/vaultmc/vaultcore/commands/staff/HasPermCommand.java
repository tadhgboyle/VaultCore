package net.vaultmc.vaultcore.commands.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "hasperm", description = "Check if a player has a permission.")
@Permission(Permissions.HasPermCommand)
public class HasPermCommand extends CommandExecutor {
	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public HasPermCommand() {
		this.register("hasPermSelf", Collections.singletonList(Arguments.createArgument("permission", Arguments.string())));
		this.register("hasPermOther", Arrays.asList(Arguments.createArgument("permission", Arguments.string()),
				Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

    @SubCommand("hasPermSelf")
    @PlayerOnly
    public void hasPermSelf(VLPlayer sender, String permission) {
        if (sender.hasPermission(permission)) {
            sender.sendMessage(string + "You " + ChatColor.GREEN + "have" + string + " the permission " + variable1
                    + permission + string + ".");
        } else {
            sender.sendMessage(string + "You " + ChatColor.RED + "don't have" + string + " the permission " + variable1
                    + permission + string + ".");
        }
    }

    @SubCommand("hasPermOther")
    @Permission(Permissions.HasPermCommandOther)
    public void hasPermOther(VLCommandSender sender, String permission, VLOfflinePlayer target) {
        if (target.isOnline()) {
            hasPermOtherOnline(target.getOnlinePlayer(), sender, permission);
            return;
        }
        hasPermOtherOffline(sender, target, permission);
    }

    private void hasPermOtherOnline(VLPlayer target, VLCommandSender sender, String permission) {
        if (target.hasPermission(permission)) {
            sender.sendMessage(variable1 + target.getFormattedName() + ChatColor.GREEN + " has " + string
                    + "the permission " + variable1 + permission + string + ".");
        } else {
            sender.sendMessage(variable1 + target.getFormattedName() + ChatColor.RED + " doesn't have " + string
                    + "the permission " + variable1 + permission + string + ".");
        }
    }

    @SneakyThrows
    private void hasPermOtherOffline(VLCommandSender sender, VLOfflinePlayer target, String permission) {
        DBConnection database = VaultCore.getDatabase();
        ResultSet rs = database.executeQueryStatement("SELECT username FROM players WHERE username=?", target.getName());
        if (!rs.next()) {
            sender.sendMessage(ChatColor.RED + "That player has never joined before!");
            return;
        }

        if (target.hasPermission(permission)) {
            sender.sendMessage(variable1 + target.getFormattedName() + ChatColor.GREEN + " has " + string
                    + "the permission " + variable1 + permission + string + ".");
        } else {
            sender.sendMessage(variable1 + target.getFormattedName() + ChatColor.RED + " doesn't have " + string
                    + "the permission " + variable1 + permission + string + ".");
        }
    }
}
