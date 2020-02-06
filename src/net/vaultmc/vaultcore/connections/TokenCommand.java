package net.vaultmc.vaultcore.connections;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang.RandomStringUtils;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.UUID;

@RootCommand(literal = "token", description = "Get your universal token for VaultMC services.")
@Permission(Permissions.TokenCommand)
@PlayerOnly
public class TokenCommand extends CommandExecutor {
    public TokenCommand() {
        register("getToken", Collections.emptyList());
    }

    @SneakyThrows
    static String getToken(UUID uuid, VLPlayer player) {
        DBConnection database = VaultCore.getDatabase();

        ResultSet getToken = database.executeQueryStatement("SELECT token FROM players WHERE uuid='" + uuid + "'");
        if (getToken.next()) {
            String token = getToken.getString("token");
            if (token != null) {
                return token;
            }
        }
        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.token.generating"));

        String new_token = RandomStringUtils.random(8, true, true);

        ResultSet duplicateCheck = database
                .executeQueryStatement("SELECT username FROM players WHERE token='" + new_token + "'");

        if (!duplicateCheck.next()) {
            database.executeUpdateStatement("UPDATE players SET token='" + new_token + "' WHERE uuid='" + uuid + "'");
            database.executeUpdateStatement("INSERT INTO web_accounts (uuid, token) VALUES ('" + player.getUniqueId()
                    + "', '" + new_token + "')");
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.token.please_rerun"));
            // line 61 checks for this null
            return null;
        }
        return new_token;
    }

    @SneakyThrows
    @SubCommand("getToken")
    public void getToken(VLPlayer sender) {
        String token = getToken(sender.getUniqueId(), sender);
        // if they are 1/308915776 make them run cmd again
        if (token == null) {
            return;
        }
        sender.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.token.your_token"), token));
    }
}