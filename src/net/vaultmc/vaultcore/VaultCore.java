package net.vaultmc.vaultcore;

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
import net.vaultmc.vaultcore.chat.groups.CGSettingsInvListener;
import net.vaultmc.vaultcore.chat.groups.ChatGroup;
import net.vaultmc.vaultcore.chat.groups.ChatGroupsCommand;
import net.vaultmc.vaultcore.chat.msg.MsgCommand;
import net.vaultmc.vaultcore.chat.msg.ReplyCommand;
import net.vaultmc.vaultcore.chat.msg.SocialSpyCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.combat.CombatLog;
import net.vaultmc.vaultcore.combat.LegacyCombat;
import net.vaultmc.vaultcore.connections.DiscordCommand;
import net.vaultmc.vaultcore.connections.TokenCommand;
import net.vaultmc.vaultcore.cosmetics.CosmeticsCommand;
import net.vaultmc.vaultcore.cosmetics.CosmeticsInvListener;
import net.vaultmc.vaultcore.cosmetics.ParticleRunnable;
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
import net.vaultmc.vaultcore.inventory.InventoryStorageListeners;
import net.vaultmc.vaultcore.lobby.PlayerHider;
import net.vaultmc.vaultcore.lobby.ServerNavigator;
import net.vaultmc.vaultcore.misc.commands.*;
import net.vaultmc.vaultcore.misc.commands.donation.DonationCommand;
import net.vaultmc.vaultcore.misc.commands.staff.*;
import net.vaultmc.vaultcore.misc.commands.staff.grant.GrantCommand;
import net.vaultmc.vaultcore.misc.commands.staff.grant.GrantCommandListener;
import net.vaultmc.vaultcore.misc.commands.staff.logs.LogsCommand;
import net.vaultmc.vaultcore.misc.listeners.*;
import net.vaultmc.vaultcore.misc.runnables.AFKListener;
import net.vaultmc.vaultcore.misc.runnables.RankPromotions;
import net.vaultmc.vaultcore.nametags.Nametags;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ban.*;
import net.vaultmc.vaultcore.punishments.gui.PunishCommand;
import net.vaultmc.vaultcore.punishments.kick.KickCommand;
import net.vaultmc.vaultcore.punishments.mute.*;
import net.vaultmc.vaultcore.punishments.warn.WarnCommand;
import net.vaultmc.vaultcore.pvp.*;
import net.vaultmc.vaultcore.report.Report;
import net.vaultmc.vaultcore.report.ReportCommand;
import net.vaultmc.vaultcore.report.ReportsCommand;
import net.vaultmc.vaultcore.rewards.ReferralCommand;
import net.vaultmc.vaultcore.rewards.RewardsCommand;
import net.vaultmc.vaultcore.settings.PlayerCustomKeys;
import net.vaultmc.vaultcore.settings.SettingsCommand;
import net.vaultmc.vaultcore.stats.CheckCommand;
import net.vaultmc.vaultcore.stats.PlayTimeCommand;
import net.vaultmc.vaultcore.stats.SeenCommand;
import net.vaultmc.vaultcore.stats.Statistics;
import net.vaultmc.vaultcore.survival.*;
import net.vaultmc.vaultcore.survival.claim.Claim;
import net.vaultmc.vaultcore.survival.claim.ClaimCommand;
import net.vaultmc.vaultcore.survival.claim.ClaimListeners;
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

    static {
        ConfigurationSerialization.registerClass(ChatGroup.class);
    }

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

    public FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    public Configuration getVLConfig() {
        return this.config;
    }

    public Configuration getVLData() {
        return data;
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

        setupChat();
        Report.dbInit();
        getServer().getScheduler().runTaskLater(this.getBukkitPlugin(), () -> registerEvents(new Nametags()), 1);
        getServer().getServicesManager().register(Economy.class, new EconomyImpl(), this.getBukkitPlugin(), ServicePriority.Highest);
        getServer().getMessenger().registerIncomingPluginChannel(this.getBukkitPlugin(), "minecraft:brand", new BrandListener());

        startTime = System.currentTimeMillis();

        VaultMCBot.startVaultMCBot();
        new ManageBotCommand();
        new ReferralCommand();
        new ChatGroupsCommand();
        new CRCommand();
        new SVCommand();
        new WildTeleportCommand();
        new TourCommand();
        new TourStageCommand();
        new TourMusic();
        new Tour();
        new SchemCommand();
        new BackCommand();
        new ServerNavigator();
        new LolCommand();
        new AFKCommand();
        new WarpCommand();
        new TheEndReset();
        new NetherWarningMessage();
        new StarterGearExperience();
        new ReportsCommand();
        new NearCommand();
        new RewardsCommand();
        Bug.dbInit();
        Bug.load();
        new BuggyCommand();
        new BuggyListener();
        new EntityUpperBound();
        ItemRegistry.load();
        new NicknameCommand();
        new ItemListeners();
        new CraftingCommand();
        new CosmeticsCommand();
        new NightvisionCommand();
        registerEvents(new CycleListener());
        registerEvents(new SleepHandler());
        registerEvents(new ItemDrops());
        registerEvents(new PlayerJoinQuitListener());
        registerEvents(new CGSettingsInvListener());
        registerEvents(new CosmeticsInvListener());
        registerEvents(new NightvisionCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), () -> {
            RankPromotions.memberPromotion();
            RankPromotions.patreonPromotion();
            Statistics.statistics();
            AFKListener.afkUpdater();
        }, 0L, 2400L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getBukkitPlugin(), ParticleRunnable::particleHandler, 0L, 10L);
        new EconomyCommand();
        new MoneyCommand();
        new TransferCommand();
        new PlayerHider();
        new SkullCommand();
        registerEvents(new FlyCommand());
        registerEvents(new TPACommand());
        registerEvents(new TPAHereCommand());
        new PlayerCustomKeys();
        new SuicideCommand();
        new IgnoreCommand();
        new UnignoreCommand();
        new SecLogCommand();
        new LegacyCombat();
        new LogsCommand();
        new CombatLog();
        new SettingsCommand();
        SettingsCommand.init();
        new MsgCommand();
        new ReplyCommand();
        new TPACommand();
        new TPAcceptCommand();
        new TPAHereCommand();
        new TPHereCommand();
        new TPDenyCommand();
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
        new ListCommand();
        new SocialSpyCommand();
        new ModMode();
        new DonationCommand();
        new ChatUtils();
        new TimeCommand();
        new VanishCommand();
        new VanishListeners();
        new GameModeListeners();
        new WeatherCommand();
        new HelpCommand();
        new BrandCommand();
        new InventoryStorageListeners();
        new LagCommand();
        new HubCommand();
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
        new StatsCommand();
        new KitCommand();
        new Scoreboards();
        new KitGuis();
        new GoldenAppleDelay();
        new ExploitsListener();
        Claim.getClaims();  // Load the class
        new ClaimCommand();
        new UnclaimCommand();
        new ClaimListeners();
        new PlayerCustomKeys();
        Kit.getKits();  // Load the class
        KitInit.init();
        registerEvents(new PlayerDeathListener());
        registerEvents(new GrantCommandListener());
        registerEvents(new SignHandler());
        registerEvents(new PlayerTPListener());
        registerEvents(new SuicideCommand());

        new BanCommand();
        new BannedListener();
        new IpBanCommand();
        new IpTempBanCommand();
        new TempBanCommand();
        new UnbanCommand();
        new KickCommand();
        new IpMuteCommand();
        new IpTempMuteCommand();
        new MuteCommand();
        new MutedListener();
        new TempMuteCommand();
        new UnmuteCommand();
        new WarnCommand();
        new PunishCommand();

        PunishmentsDB.createTables();
        Report.load();
        new ReportCommand();

        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.YELLOW + "VaultCore" + ChatColor.GREEN + " successfully enabled. Running on " + ChatColor.YELLOW + "Bukkit - " + getServerName() + ChatColor.GREEN + ".");
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
        Bug.cluster.close();
        Report.save();
        ModMode.save();
        KitGuis.save();
        database.close();
        pDatabase.close();
        inv.save();
        locations.save();
        data.save();
        chatgroups.save();
    }
}