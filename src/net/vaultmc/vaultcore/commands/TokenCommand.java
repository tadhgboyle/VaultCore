package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class TokenCommand implements CommandExecutor {

    static String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    static String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("token")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(Utilities.consoleError());
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission(Permissions.TokenCommand)) {
                sender.sendMessage(Utilities.noPermission());
                return true;
            }

            if (args.length != 0) {
                player.sendMessage(Utilities.usageMessage(cmd.getName(), ""));
                return true;
            }

            try {
                String token = getToken(player.getUniqueId(), player);
                // if they are 1/308915776 make them run cmd again
                if (token == null) {
                    return true;
                }
                player.sendMessage(string + "Your token: " + variable2 + token);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    static String getToken(UUID uuid, Player player) throws SQLException {

        java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
        ResultSet getTokenRS = stmt.executeQuery("SELECT token FROM players WHERE uuid='" + uuid + "'");
        if (getTokenRS.next()) {
            String token = getTokenRS.getString("token");
            if (token != null) {
                return token;
            }
        }
        player.sendMessage(string + "Generating your token...");

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomLimitedInt = 97 + (int) (random.nextFloat() * (122 - 9 + 1));
            buffer.append((char) randomLimitedInt);
        }
        String new_token = buffer.toString();

        ResultSet generateTokenRS = stmt.executeQuery("SELECT username FROM players WHERE token='" + new_token + "'");

        if (!generateTokenRS.next()) {
            VaultCore.getInstance().connection.createStatement()
                    .executeUpdate("UPDATE players SET token='" + new_token + "' WHERE uuid='" + uuid + "'");
        } else {
            player.sendMessage(string + "You are one in " + variable1 + "308915776" + string
                    + "! The token we generated was already in our database.");
            player.sendMessage(string + "Please re-run this command.");
            return null;
        }
        return new_token;
    }
}