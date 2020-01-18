package net.vaultmc.vaultcore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandInv;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.runnables.RankPromotions;
import net.vaultmc.vaultcore.runnables.Statistics;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.components.Component;
import net.vaultmc.vaultloader.components.annotations.ComponentInfo;
import net.vaultmc.vaultloader.components.annotations.Version;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.ReloadPersistentStorage;
import net.vaultmc.vaultloader.utils.configuration.Configuration;
import net.vaultmc.vaultloader.utils.configuration.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

@ComponentInfo(name = "VaultCore", description = "The suite of tools created for the VaultMC server.", authors = {
        "Aberdeener", "yangyang200", "2xjtn"})
@Version(major = 3, minor = 0, revision = 4)
public class VaultCore extends Component implements Listener {
    @Getter
    public static VaultCore instance;
    public static boolean isReloaded = false;
    private static Chat chat = null;
    private static Permission perms = null;
    @Getter
    private static DBConnection database;
    private Configuration config;
    private Configuration locations;

    private static String getServerName() {
        String name = "CraftBukkit";

        try {
            Class.forName("org.spigotmc.event.entity.EntityDismountEvent");
            name = "Spigot";
        } catch (ClassNotFoundException ignored) {
        }

        try {
            Class.forName("com.destroystokyo.paper.NamespacedTag");
            name = "Paper";
        } catch (ClassNotFoundException ignored) {
        }

        return name;
    }

    public static Chat getChat() {
        return chat;
    }

    public static Permission getPermissions() {
        return perms;
    }

    @Override
    public void onStartingReloaded() {
        isReloaded = true;
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        instance = this;

        config = ConfigurationManager.loadConfiguration("config.yml", this);

        database = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"), getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));

        VaultLoader.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(VaultLoader.getInstance(),
                "BungeeCord");

        setupChat();
        setupPermissions();
        Registry.registerCommands();
        Registry.registerListeners();
        GrantCommandInv.initAdmin();
        GrantCommandInv.initMod();
        int minute = (int) 1200L;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), () -> {
            RankPromotions.memberPromotion();
            RankPromotions.patreonPromotion();
            Statistics.statistics();
        }, 0L, minute * 2);

        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{
                ChatColor.YELLOW + "                   _ _     " + ChatColor.GOLD + "___               ",
                ChatColor.YELLOW + " /\\   /\\__ _ _   _| | |_  " + ChatColor.GOLD + "/ __\\___  _ __ ___ ",
                ChatColor.YELLOW + " \\ \\ / / _` | | | | | __|" + ChatColor.GOLD + "/ /  / _ \\| '__/ _ \\",
                ChatColor.YELLOW + "  \\ V / (_| | |_| | | |_" + ChatColor.GOLD + "/ /__| (_) | | |  __/",
                ChatColor.YELLOW + "   \\_/ \\__,_|\\__,_|_|\\__" + ChatColor.GOLD + "\\____/\\___/|_|  \\___|", "",
                ChatColor.GREEN + "Successfully enabled. Maintained by " + ChatColor.YELLOW + "Aberdeener"
                        + ChatColor.GREEN + ", " + "running on " + ChatColor.YELLOW + "Bukkit - " + getServerName()
                        + ChatColor.GREEN + "."});

        this.getServer().getMessenger().registerOutgoingPluginChannel(VaultLoader.getInstance(), "BungeeCord");

    }

    public FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    public FileConfiguration getLocationFile() {
        return this.locations.getConfig();
    }

    @Override
    public void onServerFinishedLoading() {
        locations = ConfigurationManager.loadConfiguration("locations.yml", this);
    }

    public void saveLocations() {
        locations.save();
    }

    public void saveConfig() {
        config.save();
    }

    public void reloadConfig() {
        config.reload();
    }

    @Override
    public void onReload() {
        ReloadPersistentStorage.put("sessionIds", PlayerJoinQuitListener.getSession_ids(), this);
        ReloadPersistentStorage.put("sessionDuration", PlayerJoinQuitListener.getSession_duration(), this);
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
    }

    public void sendToBackup() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.sendMessage(
                    ChatColor.RED + "VaultMC is shutting down for maintenance... Sending you to the backup server...");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("backup");
            players.sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void onDisable() {
    }
}