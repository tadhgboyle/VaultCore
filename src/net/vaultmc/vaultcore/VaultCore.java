package net.vaultmc.vaultcore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.vaultmc.vaultcore.brand.BrandCommand;
import net.vaultmc.vaultcore.brand.BrandListener;
import net.vaultmc.vaultcore.buggy.Bug;
import net.vaultmc.vaultcore.buggy.BuggyCommand;
import net.vaultmc.vaultcore.buggy.BuggyListener;
import net.vaultmc.vaultcore.chat.*;
import net.vaultmc.vaultcore.chat.msg.MsgCommand;
import net.vaultmc.vaultcore.chat.msg.MsgMessageListener;
import net.vaultmc.vaultcore.chat.msg.ReplyCommand;
import net.vaultmc.vaultcore.chat.msg.SocialSpyCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.combat.CombatLog;
import net.vaultmc.vaultcore.combat.LegacyCombat;
import net.vaultmc.vaultcore.connections.DiscordCommand;
import net.vaultmc.vaultcore.connections.TokenCommand;
import net.vaultmc.vaultcore.creative.CycleListener;
import net.vaultmc.vaultcore.creative.EntityUpperBound;
import net.vaultmc.vaultcore.creative.ItemDrops;
import net.vaultmc.vaultcore.creative.SchemCommand;
import net.vaultmc.vaultcore.discordbot.ManageBotCommand;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultcore.economy.*;
import net.vaultmc.vaultcore.gamemode.GMCreativeCommand;
import net.vaultmc.vaultcore.gamemode.GMSpectatorCommand;
import net.vaultmc.vaultcore.gamemode.GMSurvivalCommand;
import net.vaultmc.vaultcore.gamemode.GameModeCommand;
import net.vaultmc.vaultcore.grant.GrantCommand;
import net.vaultmc.vaultcore.grant.GrantCommandListener;
import net.vaultmc.vaultcore.inventory.InventoryStorageListeners;
import net.vaultmc.vaultcore.lobby.PlayerHider;
import net.vaultmc.vaultcore.lobby.ServerNavigator;
import net.vaultmc.vaultcore.messenger.GetServerService;
import net.vaultmc.vaultcore.misc.commands.*;
import net.vaultmc.vaultcore.misc.commands.donation.DonationCommand;
import net.vaultmc.vaultcore.misc.commands.staff.*;
import net.vaultmc.vaultcore.misc.commands.staff.logs.LogsCommand;
import net.vaultmc.vaultcore.misc.listeners.*;
import net.vaultmc.vaultcore.misc.runnables.AFKListener;
import net.vaultmc.vaultcore.misc.runnables.RankPromotions;
import net.vaultmc.vaultcore.nametags.Nametags;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ReasonSelector;
import net.vaultmc.vaultcore.punishments.ban.BanCommand;
import net.vaultmc.vaultcore.punishments.ban.BannedListener;
import net.vaultmc.vaultcore.punishments.ban.IpBanCommand;
import net.vaultmc.vaultcore.punishments.ban.UnbanCommand;
import net.vaultmc.vaultcore.punishments.mute.MutedListener;
import net.vaultmc.vaultcore.punishments.mute.UnmuteCommand;
import net.vaultmc.vaultcore.report.Report;
import net.vaultmc.vaultcore.report.ReportCommand;
import net.vaultmc.vaultcore.report.ReportsCommand;
import net.vaultmc.vaultcore.settings.SettingsCommand;
import net.vaultmc.vaultcore.stats.*;
import net.vaultmc.vaultcore.survival.*;
import net.vaultmc.vaultcore.survival.claim.ClaimCommand;
import net.vaultmc.vaultcore.survival.claim.UnclaimCommand;
import net.vaultmc.vaultcore.survival.home.DelHomeCommand;
import net.vaultmc.vaultcore.survival.home.HomeCommand;
import net.vaultmc.vaultcore.survival.home.SetHomeCommand;
import net.vaultmc.vaultcore.survival.item.ItemListeners;
import net.vaultmc.vaultcore.survival.item.ItemRegistry;
import net.vaultmc.vaultcore.teleport.*;
import net.vaultmc.vaultcore.teleport.tpa.TPACommand;
import net.vaultmc.vaultcore.teleport.tpa.TPAHereCommand;
import net.vaultmc.vaultcore.teleport.tpa.TPAcceptCommand;
import net.vaultmc.vaultcore.teleport.tpa.TPDenyCommand;
import net.vaultmc.vaultcore.teleport.worldtp.CRCommand;
import net.vaultmc.vaultcore.teleport.worldtp.SVCommand;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultcore.tour.TourCommand;
import net.vaultmc.vaultcore.tour.TourMusic;
import net.vaultmc.vaultcore.tour.TourStageCommand;
import net.vaultmc.vaultcore.vanish.VanishCommand;
import net.vaultmc.vaultcore.vanish.VanishListeners;
import net.vaultmc.vaultloader.VaultLoader;
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
import org.bukkit.entity.Player;
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
    public static final int TOTAL_SERVERS = 3;
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

        setupChat();
        Report.dbInit();
        getServer().getScheduler().runTaskLater(this.getBukkitPlugin(), () -> registerEvents(new Nametags()), 1);
        getServer().getServicesManager().register(Economy.class, new EconomyImpl(), this.getBukkitPlugin(), ServicePriority.Highest);
        getServer().getMessenger().registerIncomingPluginChannel(this.getBukkitPlugin(), "minecraft:brand", new BrandListener());
        getServer().getMessenger().registerOutgoingPluginChannel(VaultLoader.getInstance(), "BungeeCord");

        startTime = System.currentTimeMillis();

        if (getConfig().getString("server").trim().equalsIgnoreCase("vaultmc")) {
            VaultMCBot.startVaultMCBot();
            new ManageBotCommand();
            new CRCommand();
            new SVCommand();
            new WildTeleportCommand();
            new TourCommand();
            new TourStageCommand();
            new TourMusic();
            new Tour();
            new ClaimCommand();
            new UnclaimCommand();
            new SchemCommand();
            new ServerNavigator();
            new LolCommand();
            new AFKCommand();
            new WarpCommand();
            new BookIntroExperience();
            new TheEndReset();
            new MessageExperience();
            new StarterGearExperience();
            new ReportsCommand();
            new NearCommand();
            Bug.dbInit();
            Bug.load();
            new BuggyCommand();
            new BuggyListener();
            new EntityUpperBound();
            ItemRegistry.load();
            new ItemListeners();
            new CraftingCommand();
            registerEvents(new ShutDownListener());
            registerEvents(new CycleListener());
            registerEvents(new SleepHandler());
            registerEvents(new ItemDrops());
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), () -> {
                RankPromotions.memberPromotion();
                RankPromotions.patreonPromotion();
                Statistics.statistics();
                AFKListener.afkUpdater();
            }, 0L, 2400L);
        }

        if (!getConfig().getString("server").trim().equalsIgnoreCase("backup")) {
            new EconomyCommand();
            new MoneyCommand();
            new TransferCommand();
            new PlayerHider();
        }
        new SuicideCommand();
        new IgnoreCommand();
        new UnignoreCommand();
        new SecLogCommand();
        new LegacyCombat();
        new LogsCommand();
        new CombatLog();
        new SettingsCommand();
        new MsgCommand();
        new MsgMessageListener();
        new ReplyCommand();
        new TPACommand();
        new TPAcceptCommand();
        new TPAHereCommand();
        new TPHereCommand();
        new TPDenyCommand();
        new BackCommand();
        new DiscordCommand();
        new PingCommand();
        new PlayTimeCommand();
        new RanksCommand();
        new SpeedCommand();
        new SeenCommand();
        new TokenCommand();
        new GrantCommand();
        new GameModeCommand();
        new GMCreativeCommand();
        new GMSurvivalCommand();
        new GMSpectatorCommand();
        new CheckCommand();
        new ClearChatCommand();
        new ConsoleSay();
        new FeedCommand();
        new FlyCommand();
        new HealCommand();
        new InvseeCommand();
        new MuteChatCommand();
        new StaffChatCommand();
        new TPCommand();
        new ReloadCommand();
        new HasPermCommand();
        new TagCommand();
        new CrashCommand();
        new StatsCommand();
        new ListCommand();
        new SocialSpyCommand();
        new ModMode();
        new DonationCommand();
        new ChatManager();
        new TimeCommand();
        new VanishCommand();
        new VanishListeners();
        new GameModeListeners();
        new WeatherCommand();
        new HelpCommand();
        new BrandCommand();
        new BanCommand();
        new UnbanCommand();
        new IpBanCommand();
        new ReasonSelector();
        new UnmuteCommand();
        new InventoryStorageListeners();
        new LagCommand();
        new HubCommand();
        new GetServerService();
        new AdminChatCommand();
        new ClearCommand();
        new HomeCommand();
        new SetHomeCommand();
        new DelHomeCommand();
        new EconomyListener();
        new LobbyPortals();
        new SpawnCommand();
        new ForceFieldCommand();
        new SudoCommand();
        registerEvents(new GrantCommandListener());
        registerEvents(new SignHandler());
        registerEvents(new PlayerJoinQuitListener());
        registerEvents(new PlayerTPListener());
        registerEvents(new BannedListener());
        registerEvents(new MutedListener());

        PunishmentsDB.createTables();
        Report.load();
        new ReportCommand();

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