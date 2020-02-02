package net.vaultmc.vaultcore.connections;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "discord", description = "Get a link to our Discord Guild.")
@Permission(Permissions.DiscordCommand)
@PlayerOnly
public class DiscordCommand extends CommandExecutor {
    public DiscordCommand() {
        this.register("discord", Collections.emptyList());
    }

    @SneakyThrows
    @SubCommand("discord")
    public void execute(VLPlayer player) {
        String token = TokenCommand.getToken(player.getUniqueId(), player);
        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.token.your_token"),
                token));
        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.discord.guild"));
    }
}
