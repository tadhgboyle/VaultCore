package me.aberdeener.vaultcore;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.aberdeener.vaultcore.commands.DiscordCommand;
import me.aberdeener.vaultcore.commands.ECCommand;
import me.aberdeener.vaultcore.commands.HelpCommand;
import me.aberdeener.vaultcore.commands.MsgCommand;
import me.aberdeener.vaultcore.commands.PingCommand;
import me.aberdeener.vaultcore.commands.PlayTime;
import me.aberdeener.vaultcore.commands.RanksCommand;
import me.aberdeener.vaultcore.commands.TPACommand;
import me.aberdeener.vaultcore.commands.WildTeleport;
import me.aberdeener.vaultcore.commands.WorkbenchCommand;
import me.aberdeener.vaultcore.commands.WorldTPCommand;
import me.aberdeener.vaultcore.commands.staff.CheckCommand;
import me.aberdeener.vaultcore.commands.staff.ClearChat;
import me.aberdeener.vaultcore.commands.staff.ConsoleSay;
import me.aberdeener.vaultcore.commands.staff.FeedCommand;
import me.aberdeener.vaultcore.commands.staff.FlyCommand;
import me.aberdeener.vaultcore.commands.staff.GamemodeCommand;
import me.aberdeener.vaultcore.commands.staff.GrantCommand;
import me.aberdeener.vaultcore.commands.staff.GrantCommandInv;
import me.aberdeener.vaultcore.commands.staff.HealCommand;
import me.aberdeener.vaultcore.commands.staff.InvseeCommand;
import me.aberdeener.vaultcore.commands.staff.StaffChat;
import me.aberdeener.vaultcore.commands.staff.TeleportCommand;
import me.aberdeener.vaultcore.listeners.GrantCommandListener;
import me.aberdeener.vaultcore.listeners.MuteChat;
import me.aberdeener.vaultcore.listeners.PlayerJoinListener;
import me.aberdeener.vaultcore.listeners.PlayerQuitListener;
import me.aberdeener.vaultcore.listeners.PlayerTPListener;
import me.aberdeener.vaultcore.listeners.SetDisplayName;
import me.aberdeener.vaultcore.listeners.SignColours;
import me.aberdeener.vaultcore.listeners.SpawnCommand;
import me.aberdeener.vaultcore.listeners.VaultSuiteChat;
import me.aberdeener.vaultcore.runnables.RankPromotions;
import net.milkbowl.vault.chat.Chat;

public class VaultCore extends JavaPlugin implements Listener {

	// vault chat
	private static Chat chat = null;

	// main instance
	public static VaultCore instance;

	// data file setup
	private File playerDataFile;
	private FileConfiguration playerData;

	@Override
	public void onEnable() {

		// save config.yml if not exist
		getConfig().options().copyDefaults(true);
		saveConfig();

		// access Main from other classes
		instance = this;

		// setup vault chat
		setupChat();

		// register listeners (below)
		registerListeners();

		// create player data file
		createPlayerData();

		// initiate /grant.admin
		GrantCommandInv.initAdmin();

		// initiate /grant.mod
		GrantCommandInv.initMod();

		// register commands and their classes
		this.getCommand("help").setExecutor(new HelpCommand());

		this.getCommand("clearchat").setExecutor(new ClearChat());
		this.getCommand("cc").setExecutor(new ClearChat());

		this.getCommand("playtime").setExecutor(new PlayTime());
		this.getCommand("pt").setExecutor(new PlayTime());

		this.getCommand("staffchat").setExecutor(new StaffChat());
		this.getCommand("sc").setExecutor(new StaffChat());

		this.getCommand("wild").setExecutor(new WildTeleport());

		this.getCommand("ping").setExecutor(new PingCommand());

		this.getCommand("mutechat").setExecutor(new MuteChat());

		this.getCommand("check").setExecutor(new CheckCommand());

		this.getCommand("say").setExecutor(new ConsoleSay());
		this.getCommand("chat").setExecutor(new ConsoleSay());

		this.getCommand("setspawn").setExecutor(new SpawnCommand());

		this.getCommand("spawn").setExecutor(new SpawnCommand());

		this.getCommand("grant").setExecutor(new GrantCommand());

		this.getCommand("gamemode").setExecutor(new GamemodeCommand());
		this.getCommand("gmc").setExecutor(new GamemodeCommand());
		this.getCommand("gms").setExecutor(new GamemodeCommand());
		this.getCommand("gmsp").setExecutor(new GamemodeCommand());

		this.getCommand("teleport").setExecutor(new TeleportCommand());
		this.getCommand("tp").setExecutor(new TeleportCommand());

		this.getCommand("tphere").setExecutor(new TeleportCommand());

		this.getCommand("tpa").setExecutor(new TPACommand());

		this.getCommand("tpahere").setExecutor(new TPACommand());

		this.getCommand("tpaccept").setExecutor(new TPACommand());

		this.getCommand("tpdeny").setExecutor(new TPACommand());

		this.getCommand("invsee").setExecutor(new InvseeCommand());

		this.getCommand("fly").setExecutor(new FlyCommand());

		this.getCommand("workbench").setExecutor(new WorkbenchCommand());

		this.getCommand("enderchest").setExecutor(new ECCommand());

		this.getCommand("msg").setExecutor(new MsgCommand());
		this.getCommand("r").setExecutor(new MsgCommand());

		this.getCommand("feed").setExecutor(new FeedCommand());

		this.getCommand("heal").setExecutor(new HealCommand());

		this.getCommand("ranks").setExecutor(new RanksCommand());

		this.getCommand("back").setExecutor(new PlayerTPListener());

		this.getCommand("discord").setExecutor(new DiscordCommand());
		
		this.getCommand("sv").setExecutor(new WorldTPCommand());
		
		this.getCommand("tsv").setExecutor(new WorldTPCommand());

		this.getCommand("cr").setExecutor(new WorldTPCommand());

		// run rank promotions task every 5 minutes
		// define minute
		int minute1 = (int) 1200L;
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				// run method in class
				RankPromotions.memberPromotion();
				RankPromotions.patreonPromotion();
			}
			// use minute variable to see when to run
		}, 0L, minute1 * 5);
	}

	// call data file from other class
	public FileConfiguration getPlayerData() {
		return this.playerData;
	}

	// method to make data file
	private void createPlayerData() {
		playerDataFile = new File(getDataFolder(), "data.yml");
		if (!playerDataFile.exists()) {
			playerDataFile.getParentFile().mkdirs();
			saveResource("data.yml", false);
		}

		playerData = new YamlConfiguration();
		try {
			playerData.load(playerDataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}
	
	// save data file
	public void savePlayerData() {
		try {
			playerData.save(playerDataFile);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// lets us use this instance to load coords, reload config etc
	public static VaultCore getInstance() {
		return instance;
	}

	// vault stuff
	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	public static Chat getChat() {
		return chat;
	}

	public void registerListeners() {

		// creates the plugin manager
		PluginManager pm = Bukkit.getServer().getPluginManager();

		// listens for the main class
		pm.registerEvents(this, this);

		// Listens for mutechat class
		pm.registerEvents(new MuteChat(), this);

		// Listens for chat class
		pm.registerEvents(new VaultSuiteChat(), this);

		// Listens for grantcommand class
		pm.registerEvents(new GrantCommandListener(), this);

		// Listens for setdisplayname
		pm.registerEvents(new SetDisplayName(), this);

		// Listens for signs
		pm.registerEvents(new SignColours(), this);

		// Listens for join
		pm.registerEvents(new PlayerJoinListener(), this);

		// Listens for leave
		pm.registerEvents(new PlayerQuitListener(), this);

		// Listens for /back messages
		pm.registerEvents(new PlayerTPListener(), this);
	}

	@Override
	public void onDisable() {
		this.saveConfig();
		this.savePlayerData();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("vcreload")) {

			if (!sender.hasPermission("vc.reload")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-permission")));
				return true;
			}

			reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
			return true;
		}
		return true;
	}
}