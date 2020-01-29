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

package net.vaultmc.vaultcore.commands.report;

import lombok.Getter;
import lombok.Setter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {
    public static final List<Report> reports = new ArrayList<>();
    @Getter
    private VLOfflinePlayer reporter;
    @Getter
    private VLOfflinePlayer victim;
    @Getter
    private List<String> reasons;
    @Getter
    @Setter
    private boolean open;
    @Getter
    @Setter
    private VLOfflinePlayer assignee;

    public Report(VLOfflinePlayer reporter, VLOfflinePlayer victim, List<String> reasons) {
        this.reporter = reporter;
        this.victim = victim;
        this.reasons = reasons;
        this.open = true;

        reports.add(this);
    }

    public static int getActiveReports() {
        int output = 0;
        for (Report report : reports) if (report.open) output++;
        return output;
    }

    public static Report getReport(VLOfflinePlayer reporter, VLOfflinePlayer victim) {
        for (Report report : reports) {
            if (report.getReporter() == reporter && report.getVictim() == victim) {
                return report;
            }
        }
        return null;
    }

    public static Report deserialize(FileConfiguration conf, String path) {
        Report report = new Report(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(conf.getString(path + ".reporter"))),
                VLOfflinePlayer.getOfflinePlayer(UUID.fromString(conf.getString(path + ".victim"))),
                conf.getStringList(path + ".reasons"));
        report.setOpen(conf.getBoolean(path + ".open"));
        report.setAssignee(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(conf.getString(path + ".assignee"))));
        return report;
    }

    public void serialize(FileConfiguration conf, String path) {
        conf.set(path + ".reporter", reporter.getUniqueId().toString());
        conf.set(path + ".victim", victim.getUniqueId().toString());
        conf.set(path + ".reasons", reasons);
        conf.set(path + ".open", open);
        conf.set(path + ".assignee", assignee.getUniqueId().toString());
        VaultCore.getInstance().saveConfig();
    }
}
