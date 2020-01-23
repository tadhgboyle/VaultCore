/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.ported.report;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "report",
        description = "Report a rule-breaker."
)
@Permission("vaultutils.report")
@PlayerOnly
public class ReportCommand extends CommandExecutor {
    public ReportCommand() {
        register("reportPlayer", Collections.singletonList(
                Arguments.createArgument("victim", Arguments.offlinePlayerArgument())
        ));
    }

    @SubCommand("reportPlayer")
    public void execute(VLPlayer reporter, VLOfflinePlayer victim) {
        new ReportMainGUI(reporter, victim, new ReportMainGUI.MainGUIOptions()).open(reporter);
    }
}
