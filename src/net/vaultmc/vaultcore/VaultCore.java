package net.vaultmc.vaultcore;

import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.vaultmc.vaultcore.brand.BrandListener;
import net.vaultmc.vaultcore.buggy.Bug;
import net.vaultmc.vaultcore.buggy.BuggyListener;
import net.vaultmc.vaultcore.chat.ChatUtils;
import net.vaultmc.vaultcore.chat.groups.CGSettingsInvListener;
import net.vaultmc.vaultcore.chat.groups.ChatGroup;
import net.vaultmc.vaultcore.combat.LegacyCombat;
import net.vaultmc.vaultcore.cosmetics.CosmeticsInvListener;
import net.vaultmc.vaultcore.cosmetics.ParticleRunnable;
import net.vaultmc.vaultcore.creative.CycleListener;
import net.vaultmc.vaultcore.creative.EntityUpperBound;
import net.vaultmc.vaultcore.creative.ItemDrops;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultcore.economy.EconomyImpl;
import net.vaultmc.vaultcore.economy.EconomyListener;
import net.vaultmc.vaultcore.inventory.InventoryStorageListeners;
import net.vaultmc.vaultcore.lobby.PlayerHider;
import net.vaultmc.vaultcore.lobby.ServerNavigator;
import net.vaultmc.vaultcore.misc.commands.AFKCommand;
import net.vaultmc.vaultcore.misc.commands.NightvisionCommand;
import net.vaultmc.vaultcore.misc.commands.SuicideCommand;
import net.vaultmc.vaultcore.misc.commands.staff.FlyCommand;
import net.vaultmc.vaultcore.misc.commands.staff.ModMode;
import net.vaultmc.vaultcore.misc.commands.staff.grant.GrantCommandListener;
import net.vaultmc.vaultcore.misc.listeners.GameModeListeners;
import net.vaultmc.vaultcore.misc.listeners.LobbyPortals;
import net.vaultmc.vaultcore.misc.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.misc.listeners.SignHandler;
import net.vaultmc.vaultcore.misc.runnables.AFKListener;
import net.vaultmc.vaultcore.misc.runnables.RankPromotions;
import net.vaultmc.vaultcore.nametags.Nametags;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ReasonSelector;
import net.vaultmc.vaultcore.punishments.ban.BannedListener;
import net.vaultmc.vaultcore.punishments.mute.MutedListener;
import net.vaultmc.vaultcore.report.Report;
import net.vaultmc.vaultcore.settings.SettingsCommand;
import net.vaultmc.vaultcore.stats.Statistics;
import net.vaultmc.vaultcore.survival.NetherWarningMessage;
import net.vaultmc.vaultcore.survival.SleepHandler;
import net.vaultmc.vaultcore.survival.StarterGearExperience;
import net.vaultmc.vaultcore.survival.item.ItemListeners;
import net.vaultmc.vaultcore.survival.item.ItemRegistry;
import net.vaultmc.vaultcore.teleport.PlayerTPListener;
import net.vaultmc.vaultcore.teleport.tpa.TPACommand;
import net.vaultmc.vaultcore.teleport.tpa.TPAHereCommand;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultcore.vanish.VanishListeners;
import net.vaultmc.vaultloader.components.Component;
import net.vaultmc.vaultloader.components.annotations.ComponentInfo;
import net.vaultmc.vaultloader.components.annotations.Version;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.configuration.Configuration;
import net.vaultmc.vaultloader.utils.configuration.ConfigurationManager;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.text.DecimalFormat;
import java.util.Map;

@ComponentInfo(name = "VaultCore", description = "The suite of tools created for the VaultMC server.", authors = {
        "Aberdeener", "yangyang200", "2xjtn"})
@Version(major = 2, minor = 0, revision = 0)
public final class VaultCore extends Component implements Listener {
    public static final DecimalFormat numberFormat = new DecimalFormat("###,###.###");
    public static final String SEPARATOR = "\u00a7â";  // I hope nobody will type this using another client
    public static boolean isReloaded = false;
    @Getter
    private static VaultCore instance;
    private static Chat chat = null;
    @Getter
    private static DBConnection database;
    @Getter
    private static DBConnection pDatabase;
    @Getter
    private static long startTime;
    private Configuration config;
    private Configuration locations;
    private Configuration data;
    private Configuration inv;
    private Configuration chatgroups;

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

    @Override
    public void onStartingReloaded() {
        isReloaded = true;
    }

    static {
        ConfigurationSerialization.registerClass(ChatGroup.class);
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

    public FileConfiguration getChatGroupFile() {
        return chatgroups.getConfig();
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
        locations.save();
        chatgroups.save();
    }

    public void reloadConfig() {
        chatgroups.reload();
        config.reload();
        data.reload();
        inv.reload();
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        instance = this;

        config = ConfigurationManager.loadConfiguration("config.yml", this);
        data = ConfigurationManager.loadConfiguration("data.yml", this);
        inv = ConfigurationManager.loadConfiguration("inventory.yml", this);
        locations = ConfigurationManager.loadConfiguration("locations.yml", this);
        chatgroups = ConfigurationManager.loadConfiguration("chatgroups.yml", this);

        database = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"), getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));
        pDatabase = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
                "VaultMC_Punishments", getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));


        getServer().getScheduler().runTaskLater(this.getBukkitPlugin(), () -> registerEvents(new Nametags()), 1);
        getServer().getServicesManager().register(Economy.class, new EconomyImpl(), this.getBukkitPlugin(), ServicePriority.Highest);
        getServer().getMessenger().registerIncomingPluginChannel(this.getBukkitPlugin(), "minecraft:brand", new BrandListener());

        setupChat();
        Report.dbInit();
        startTime = System.currentTimeMillis();
        VaultMCBot.startVaultMCBot();
        Bug.dbInit();
        Bug.load();
        ItemRegistry.load();
        SettingsCommand.init();
        Registry.registerCommands();
        PunishmentsDB.createTables();
        Report.load();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), () -> {
            RankPromotions.memberPromotion();
            RankPromotions.patreonPromotion();
            Statistics.statistics();
            AFKListener.afkUpdater();
        }, 0L, 2400L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), ParticleRunnable::particleHandler, 0L, 10L);

        // TODO: Organize these
        registerEvents(new StarterGearExperience());
        registerEvents(new NetherWarningMessage());
        registerEvents(new ServerNavigator());
        registerEvents(new Tour());
        registerEvents(new EntityUpperBound());
        registerEvents(new BuggyListener());
        registerEvents(new ItemListeners());
        registerEvents(new CycleListener());
        registerEvents(new SleepHandler());
        registerEvents(new ItemDrops());
        registerEvents(new PlayerJoinQuitListener());
        registerEvents(new CGSettingsInvListener());
        registerEvents(new CosmeticsInvListener());
        registerEvents(new NightvisionCommand());
        registerEvents(new FlyCommand());
        registerEvents(new TPACommand());
        registerEvents(new TPAHereCommand());
        registerEvents(new PlayerHider());
        registerEvents(new LegacyCombat());
        registerEvents(new ChatUtils());
        registerEvents(new VanishListeners());
        registerEvents(new GameModeListeners());
        registerEvents(new ReasonSelector());
        registerEvents(new InventoryStorageListeners());
        registerEvents(new LobbyPortals());
        registerEvents(new EconomyListener());
        registerEvents(new GrantCommandListener());
        registerEvents(new SignHandler());
        registerEvents(new PlayerTPListener());
        registerEvents(new BannedListener());
        registerEvents(new MutedListener());
        registerEvents(new SuicideCommand());

        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{
                ChatColor.YELLOW + "                   _ _     " + ChatColor.GOLD + "___               ",
                ChatColor.YELLOW + " /\\   /\\__ _ _   _| | |_  " + ChatColor.GOLD + "/ __\\___  _ __ ___ ",
                ChatColor.YELLOW + " \\ \\ / / _` | | | | | __|" + ChatColor.GOLD + "/ /  / _ \\| '__/ _ \\",
                ChatColor.YELLOW + "  \\ V / (_| | |_| | | |_" + ChatColor.GOLD + "/ /__| (_) | | |  __/",
                ChatColor.YELLOW + "   \\_/ \\__,_|\\__,_|_|\\__" + ChatColor.GOLD + "\\____/\\___/|_|  \\___|", "",
                ChatColor.GREEN + "Successfully enabled. Maintained by " + ChatColor.YELLOW + "Aberdeener"
                        + ChatColor.GREEN + ", " + "running on " + ChatColor.YELLOW + "Bukkit - " + getServerName()
                        + ChatColor.GREEN + "."});
    }

    @Override
    public void onReload() {
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    @Override
    @SneakyThrows
    public void onDisable() {
        for (Map.Entry<VLPlayer, Location> entry : AFKCommand.getAfk().entrySet()) {
            entry.getKey().teleport(entry.getValue());
        }
        Bug.save();
        Report.save();
        ModMode.save();
        database.close();
        pDatabase.close();
        inv.save();
        locations.save();
        data.save();
        chatgroups.save();
    }
}