/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;

import java.util.Collections;

@RootCommand(literal = "gmc", description = "Change your gamemode to creative.")
@Permission(Permissions.GameModeCommand)
@PlayerOnly
@Aliases({"creative", "gmcreative"})
public class GMCreativeCommand extends CommandExecutor {
    public GMCreativeCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(VLPlayer sender) {
        sender.setGameMode(GameMode.CREATIVE);
        sender.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.gamemode.self"), "Creative"));
    }
}
