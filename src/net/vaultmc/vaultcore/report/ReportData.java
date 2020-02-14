package net.vaultmc.vaultcore.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.util.Set;

@AllArgsConstructor
@Data
public class ReportData {
    private VLOfflinePlayer target;
    private Set<Report.Reason> reasons;

    public void addReason(Report.Reason reason) {
        reasons.add(reason);
    }
}
