package net.vaultmc.vaultcore.misc.commands;

import com.sun.management.OperatingSystemMXBean;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Collections;

@RootCommand(
        literal = "lag",
        description = "See if VaultMC is lagging."
)
@Permission(Permissions.LagCommand)
@Aliases("tps")
public class LagCommand extends CommandExecutor {
    private static final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static DecimalFormat numberFormat = new DecimalFormat("###.##");

    public LagCommand() {
        unregisterExisting();
        this.register("lag", Collections.emptyList());
    }

    @SubCommand("lag")
    public static void lag(VLCommandSender sender) {
        String osInfo = ChatColor.GOLD + operatingSystemMXBean.getArch() + ChatColor.YELLOW + " (" + operatingSystemMXBean.getName() + ", " + ChatColor.YELLOW + " Kernel: " + ChatColor.GOLD + operatingSystemMXBean.getVersion() + ChatColor.YELLOW + ")";
        String load = operatingSystemMXBean.getProcessCpuLoad() < 0 ? "Unavailable" : numberFormat.format(operatingSystemMXBean.getSystemCpuLoad() * 100) + "%";
        String cpuInfo = ChatColor.DARK_GREEN + load + ChatColor.YELLOW + " Cores: " + ChatColor.DARK_GREEN + Runtime.getRuntime().availableProcessors();
        String javaVersion = ChatColor.GOLD + System.getProperty("java.runtime.version");
        String uptime = Utilities.millisToTime(System.currentTimeMillis() - VaultCore.getStartTime(), false);
        String tps = "" + ChatColor.DARK_GREEN + Math.round(Bukkit.getTPS()[0]) + ChatColor.YELLOW + ", " + ChatColor.DARK_GREEN + Math.round(Bukkit.getTPS()[1]) + ChatColor.YELLOW + ", " + ChatColor.DARK_GREEN + Math.round(Bukkit.getTPS()[2]);

        long freeRam = operatingSystemMXBean.getFreePhysicalMemorySize();
        long maxRam = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long usedRam = maxRam - freeRam;
        double ramPercent = ((double) usedRam / maxRam) * 100;
        String ramInfo = "" + ChatColor.GOLD + numberFormat.format(ramPercent) + "% " + ChatColor.DARK_GREEN + FileUtils.byteCountToDisplaySize(usedRam) + ChatColor.YELLOW + "/" + ChatColor.DARK_GREEN + FileUtils.byteCountToDisplaySize(maxRam);

        long freeSpace = new File("/").getFreeSpace();
        long maxSpace = new File("/").getTotalSpace();
        long usedSpace = maxSpace - freeSpace;
        double diskPercent = ((double) usedSpace / maxSpace) * 100;

        String diskInfo = "" + ChatColor.GOLD + numberFormat.format(diskPercent) + "% " + ChatColor.DARK_GREEN + FileUtils.byteCountToDisplaySize(usedSpace) + ChatColor.YELLOW + "/" + ChatColor.DARK_GREEN + FileUtils.byteCountToDisplaySize(maxSpace);
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.lag.header"));
        sender.sendMessage(ChatColor.YELLOW + "Platform: " + osInfo);
        sender.sendMessage(ChatColor.YELLOW + "Java Version: " + javaVersion);
        sender.sendMessage(ChatColor.YELLOW + "CPU: " + cpuInfo);
        sender.sendMessage(ChatColor.YELLOW + "Uptime: " + uptime);
        sender.sendMessage(ChatColor.YELLOW + "TPS: " + tps);
        sender.sendMessage(ChatColor.YELLOW + "RAM Usage: " + ramInfo);
        sender.sendMessage(ChatColor.YELLOW + "Disk Usage: " + diskInfo);
    }
}
