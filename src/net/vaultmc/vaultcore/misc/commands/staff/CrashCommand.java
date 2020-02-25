package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Particle;

import java.util.Collections;

@RootCommand(
        literal = "crash",
        description = "Useful command."
)
@Permission(Permissions.Crash)
public class CrashCommand extends CommandExecutor {
    public CrashCommand() {
        register("crash", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("crash")
    public void crash(VLCommandSender sender, VLPlayer target) {
        target.getPlayer().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 1000000000);
        sender.sendMessage(VaultLoader.getMessage("useful"));
    }
}
