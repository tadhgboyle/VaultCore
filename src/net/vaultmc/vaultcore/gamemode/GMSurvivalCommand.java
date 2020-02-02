package net.vaultmc.vaultcore.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;

import java.util.Collections;

@RootCommand(literal = "gms", description = "Change your gamemode to survival.")
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({"survival", "gmsurvival"})
public class GMSurvivalCommand extends CommandExecutor {
    public GMSurvivalCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(VLPlayer sender) {
        sender.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Survival"));
    }
}