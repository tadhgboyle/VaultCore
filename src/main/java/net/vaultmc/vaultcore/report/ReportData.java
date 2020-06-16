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

package net.vaultmc.vaultcore.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.util.List;

@AllArgsConstructor
@Data
public class ReportData {
    private VLOfflinePlayer target;
    private List<Report.Reason> reasons;
}
