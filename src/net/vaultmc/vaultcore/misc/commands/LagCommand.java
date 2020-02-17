package net.vaultmc.vaultcore.misc.commands;

import com.sun.management.OperatingSystemMXBean;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Set;

@RootCommand(
        literal = "lag",
        description = "See if VaultMC is lagging."
)
@Permission(Permissions.LagCommand)
@Aliases("tps")
public class LagCommand extends CommandExecutor {
    private static final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

    public LagCommand() {
        unregisterExisting();
        this.register("lag", Collections.emptyList());
    }

    @SubCommand("lag")
    public static void lag(VLCommandSender sender) {
        String osInfo = operatingSystemMXBean.getArch() + " " + operatingSystemMXBean.getName() + ", " + operatingSystemMXBean.getVersion();
        String load = operatingSystemMXBean.getProcessCpuLoad() < 0 ? "Unavailable" : operatingSystemMXBean.getProcessCpuLoad() * 100 + "%";
        String cpuInfo = "Load: " + load + " Processors: " + Runtime.getRuntime().availableProcessors() + " Threads: " + threadSet.size();
        String uptime = Utilities.millisToTime(System.currentTimeMillis() - VaultCore.getStartTime());
        String tps = Math.round(Bukkit.getTPS()[0]) + ", " + Math.round(Bukkit.getTPS()[1]) + ", " + Math.round(Bukkit.getTPS()[2]);
        String ramInfo = (operatingSystemMXBean.getFreePhysicalMemorySize() / 1048576) + " MB / " + (operatingSystemMXBean.getTotalPhysicalMemorySize() / 1048576) + " MB";
        String javaVersion = System.getProperty("java.version");
        long maxSpace = new File("/").getTotalSpace();
        long freeSpace = new File("/").getFreeSpace();
        String diskInfo = (freeSpace / 1048576) + " MB / " + (maxSpace / 1048576) + " MB";
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.lag.message")
                .replace("{OS}", osInfo)
                .replace("{CPU}", cpuInfo)
                .replace("{UPTIME}", uptime)
                .replace("{TPS}", tps)
                .replace("{RAM}", ramInfo)
                .replace("{JAVA}", javaVersion)
                .replace("{DISK}", diskInfo));
    }
}
