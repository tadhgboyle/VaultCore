package net.vaultmc.vaultcore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandInv;
import net.vaultmc.vaultcore.ported.brand.BrandCommand;
import net.vaultmc.vaultcore.ported.brand.BrandListener;
import net.vaultmc.vaultcore.ported.economy.EconomyCommand;
import net.vaultmc.vaultcore.ported.economy.EconomyImpl;
import net.vaultmc.vaultcore.ported.economy.MoneyCommand;
import net.vaultmc.vaultcore.ported.economy.TransferCommand;
import net.vaultmc.vaultcore.ported.help.HelpCommand;
import net.vaultmc.vaultcore.ported.inventory.InventoryStorageListeners;
import net.vaultmc.vaultcore.ported.misc.*;
import net.vaultmc.vaultcore.ported.modmode.CPSTest;
import net.vaultmc.vaultcore.ported.modmode.ModMode;
import net.vaultmc.vaultcore.ported.nametags.Nametags;
import net.vaultmc.vaultcore.ported.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.ported.punishments.ban.*;
import net.vaultmc.vaultcore.ported.punishments.kick.KickCommand;
import net.vaultmc.vaultcore.ported.punishments.mute.*;
import net.vaultmc.vaultcore.ported.report.Report;
import net.vaultmc.vaultcore.ported.report.ReportCommand;
import net.vaultmc.vaultcore.ported.report.ReportsCommand;
import net.vaultmc.vaultcore.ported.vanish.VanishCommand;
import net.vaultmc.vaultcore.ported.vanish.VanishListeners;
import net.vaultmc.vaultcore.runnables.PlayerNames;
import net.vaultmc.vaultcore.runnables.RankPromotions;
import net.vaultmc.vaultcore.runnables.Statistics;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.components.Component;
import net.vaultmc.vaultloader.components.annotations.ComponentInfo;
import net.vaultmc.vaultloader.components.annotations.Version;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.configuration.Configuration;
import net.vaultmc.vaultloader.utils.configuration.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.text.DecimalFormat;

@ComponentInfo(name = "VaultCore", description = "The suite of tools created for the VaultMC server.", authors = {
        "Aberdeener", "yangyang200", "2xjtn"})
@Version(major = 3, minor = 0, revision = 4)
public class VaultCore extends Component implements Listener {
    public static final DecimalFormat numberFormat = new DecimalFormat("###,###.###");
    @Getter
    public static VaultCore instance;
    public static boolean isReloaded = false;
    private static Chat chat = null;
    private static Permission perms = null;
    @Getter
    private static DBConnection database;
    @Getter
    private static DBConnection pDatabase;
    private Configuration config;
    private Configuration locations;
    private Configuration data;
    private Configuration inv;

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
        data = ConfigurationManager.loadConfiguration("data.yml", this);
        inv = ConfigurationManager.loadConfiguration("inventory.yml", this);

        database = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"), getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));
        pDatabase = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                "VaultMC_Punishments", getConfig().getString("mysql.user"),
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), PlayerNames::updatePlayerNames, 0L, minute / 6);

        // VaultUtils start

        PunishmentsDB.createTables();

        for (String s : data.getConfig().getConfigurationSection("reports").getKeys(false)) {
            Report.reports.add(Report.deserialize(data.getConfig(), s));
            data.getConfig().set("reports." + s, null);
        }
        data.save();

        new ModMode();
        new ReportCommand();
        new ReportsCommand();
        registerEvents(new CPSTest());
        new AFKCommand();
        new ChatUtils();
        new EconomyCommand();
        new MoneyCommand();
        new TimeCommand();
        new TransferCommand();
        new VanishCommand();
        new VanishListeners();
        new InventoryStorageListeners();
        new GameModeListeners();
        new WeatherCommand();
        new HelpCommand();
        new BrandCommand();

        new KickCommand();
        new BanCommand();
        new MuteCommand();
        new UnbanCommand();
        new IpBanCommand();
        new IpTempBanCommand();
        new IpMuteCommand();
        new IpTempMuteCommand();
        new UnmuteCommand();
        new TempBanCommand();
        new TempMuteCommand();
        registerEvents(new BannedListener());
        registerEvents(new MutedListener());

        getServer().getScheduler().runTaskLater(this.getBukkitPlugin(), () -> registerEvents(new Nametags()), 1);

        getServer().getServicesManager().register(Economy.class, new EconomyImpl(), this.getBukkitPlugin(), ServicePriority.Highest);
        getServer().getMessenger().registerIncomingPluginChannel(this.getBukkitPlugin(), "minecraft:brand", new BrandListener());


        // VaultUtils end

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

    public FileConfiguration getInventoryData() {
        return inv.getConfig();
    }

    public FileConfiguration getData() {
        return data.getConfig();
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
        data.save();
        inv.save();
    }

    public void reloadConfig() {
        config.reload();
        data.reload();
        inv.reload();
    }

    @Override
    public void onReload() {
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
            players.sendMessage(VaultLoader.getMessage("vaultcore.sendingtobackup"));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("backup");
            players.sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void onDisable() {
        database.close();
        pDatabase.close();
    }
}