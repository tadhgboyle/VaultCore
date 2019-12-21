package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Collections;

@RootCommand(
        literal = "discord",
        description = "Get a link to our Discord Guild."
)
@Permission(Permissions.DiscordCommand)
@PlayerOnly
public class DiscordCommand extends CommandExecutor {

    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;
    private String variable2 = Utilities.variable2;
    public DiscordCommand() {
        this.register("discord", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("discord")
    public void execute(CommandSender sender) {

        Player player = (Player) sender;

        try {
            String token = TokenCommand.getToken(player.getUniqueId(), player);

            player.sendMessage(string + "Your token: " + variable2 + token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        player.sendMessage(string + "Click here to join our guild: " + variable1 + "https://discord.vaultmc.net");
    }
}
