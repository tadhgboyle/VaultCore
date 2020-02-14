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
