package net.vaultmc.vaultcore.misc.commands.staff.logs;

import lombok.Setter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.util.Collections;

@RootCommand(
        literal = "logs",
        description = "Search the logs for a phrase."
)
@Permission(Permissions.LogsCommand)
public class LogsCommand extends CommandExecutor {
    @Setter
    public static Boolean searching = false;

    public LogsCommand() {
        register("logs", Collections.singletonList(Arguments.createArgument("search", Arguments.greedyString())));
    }

    @SubCommand("logs")
    public void log(VLCommandSender sender, String search) {
        if (searching) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.already_searching"));
            return;
        }
        if (search.length() < 3) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.minimum_length"));
            return;
        }
        LogsHandler logsHandler = new LogsHandler(sender, search);
        Thread thread = new Thread(logsHandler);
        thread.start();
    }
}
