package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@RootCommand(
        literal = "token",
        description = "Get your universal token for VaultMC services."
)
@Permission(Permissions.TokenCommand)
@PlayerOnly
public class TokenCommand extends CommandExecutor {
    private static String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private static String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    private static String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    public TokenCommand() {
        register("getToken", Collections.emptyList());
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

    @SubCommand("getToken")
    public void getToken(CommandSender sender) throws SQLException {
        String token = getToken(((Player) sender).getUniqueId(), (Player) sender);
        // if they are 1/308915776 make them run cmd again

        // FIXME/QUESTION from yangyang200:
        //  If they are 1/308915776 and the token is already in the database, doesn't it
        //  means that the player won't be able to get a token anymore since all the
        //  random numbers and used, and this is extremely impossible? Not extremely,
        //  but just, impossible.

        if (token == null) {
            return;
        }
        sender.sendMessage(string + "Your token: " + variable2 + token);
    }
}