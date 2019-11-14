package net.vaultmc.vaultcore;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.chat.Chat;
import net.vaultmc.vaultcore.commands.BackCommand;
import net.vaultmc.vaultcore.commands.DiscordCommand;
import net.vaultmc.vaultcore.commands.ECCommand;
import net.vaultmc.vaultcore.commands.HelpCommand;
import net.vaultmc.vaultcore.commands.MsgCommand;
import net.vaultmc.vaultcore.commands.PingCommand;
import net.vaultmc.vaultcore.commands.PlayTime;
import net.vaultmc.vaultcore.commands.RanksCommand;
import net.vaultmc.vaultcore.commands.TPACommand;
import net.vaultmc.vaultcore.commands.TokenCommand;
import net.vaultmc.vaultcore.commands.WarpCommand;
import net.vaultmc.vaultcore.commands.WildTeleport;
import net.vaultmc.vaultcore.commands.WorldTPCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsListener;
import net.vaultmc.vaultcore.commands.staff.CheckCommand;
import net.vaultmc.vaultcore.commands.staff.ClearChat;
import net.vaultmc.vaultcore.commands.staff.ConsoleSay;
import net.vaultmc.vaultcore.commands.staff.FeedCommand;
import net.vaultmc.vaultcore.commands.staff.FlyCommand;
import net.vaultmc.vaultcore.commands.staff.GamemodeCommand;
import net.vaultmc.vaultcore.commands.staff.GrantCommand;
import net.vaultmc.vaultcore.commands.staff.GrantCommandInv;
import net.vaultmc.vaultcore.commands.staff.HealCommand;
import net.vaultmc.vaultcore.commands.staff.InvseeCommand;
import net.vaultmc.vaultcore.commands.staff.MuteChatCommand;
import net.vaultmc.vaultcore.commands.staff.StaffChat;
import net.vaultmc.vaultcore.commands.staff.TeleportCommand;
import net.vaultmc.vaultcore.listeners.ChatManager;
import net.vaultmc.vaultcore.listeners.GrantCommandListener;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultcore.listeners.SignColours;
import net.vaultmc.vaultcore.runnables.RankPromotions;
import net.vaultmc.vaultcore.tabcompletion.TabCompletion;

public class VaultCore extends JavaPlugin implements Listener {
	public static VaultCore instance;
	private static Chat chat = null;
	// data file setup
	private File playerDataFile;
	private FileConfiguration playerData;
	// mysql info
	public Connection connection;
	private String url = "jdbc:mysql://localhost/VaultMC_Data?useSSL=false&autoReconnect=true";
	private String username = "root";
	private String password = "Stjames123b!!";

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		instance = this;
		BukkitRunnable r = new BukkitRunnable() {
			@Override
			public void run() {
				try {
					openConnection();
					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultCore connected to Database");
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "VaultCore could not connect to Database");
				}
			}
		};
		r.runTaskAsynchronously(VaultCore.getInstance());
		setupChat();
		registerCommands();
		registerListeners();
		createPlayerData();
		GrantCommandInv.initAdmin();
		GrantCommandInv.initMod();
		int minute = (int) 1200L;
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				RankPromotions.memberPromotion();
				RankPromotions.patreonPromotion();
			}
		}, 0L, minute * 5);
	}

	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}
		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		}
	}

	public FileConfiguration getPlayerData() {
		return this.playerData;
	}

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

	public void savePlayerData() {
		try {
			playerData.save(playerDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static VaultCore getInstance() {
		return instance;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	public static Chat getChat() {
		return chat;
	}

	public void registerCommands() {
		this.getCommand("help").setExecutor(new HelpCommand());
		this.getCommand("help").setTabCompleter(new TabCompletion());
		this.getCommand("clearchat").setExecutor(new ClearChat());
		this.getCommand("cc").setExecutor(new ClearChat());
		this.getCommand("playtime").setExecutor(new PlayTime());
		this.getCommand("pt").setExecutor(new PlayTime());
		this.getCommand("staffchat").setExecutor(new StaffChat());
		this.getCommand("sc").setExecutor(new StaffChat());
		this.getCommand("wild").setExecutor(new WildTeleport());
		this.getCommand("ping").setExecutor(new PingCommand());
		this.getCommand("mutechat").setExecutor(new MuteChatCommand());
		this.getCommand("check").setExecutor(new CheckCommand());
		this.getCommand("say").setExecutor(new ConsoleSay());
		this.getCommand("chat").setExecutor(new ConsoleSay());
		this.getCommand("grant").setExecutor(new GrantCommand());
		this.getCommand("gamemode").setExecutor(new GamemodeCommand());
		this.getCommand("gamemode").setTabCompleter(new TabCompletion());
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
		this.getCommand("enderchest").setExecutor(new ECCommand());
		this.getCommand("msg").setExecutor(new MsgCommand());
		this.getCommand("r").setExecutor(new MsgCommand());
		this.getCommand("feed").setExecutor(new FeedCommand());
		this.getCommand("heal").setExecutor(new HealCommand());
		this.getCommand("ranks").setExecutor(new RanksCommand());
		this.getCommand("back").setExecutor(new BackCommand());
		this.getCommand("discord").setExecutor(new DiscordCommand());
		this.getCommand("sv").setExecutor(new WorldTPCommand());
		this.getCommand("cr").setExecutor(new WorldTPCommand());
		this.getCommand("warp").setExecutor(new WarpCommand());
		this.getCommand("setwarp").setExecutor(new WarpCommand());
		this.getCommand("delwarp").setExecutor(new WarpCommand());
		this.getCommand("sctoggle").setExecutor(new StaffChat());
		this.getCommand("token").setExecutor(new TokenCommand());
		this.getCommand("settings").setExecutor(new SettingsCommand());
	}

	public void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new ChatManager(), this);
		pm.registerEvents(new GrantCommandListener(), this);
		pm.registerEvents(new SignColours(), this);
		pm.registerEvents(new PlayerJoinQuitListener(), this);
		pm.registerEvents(new PlayerTPListener(), this);
		pm.registerEvents(new SettingsListener(), this);
	}

	@Override
	public void onDisable() {
		this.saveConfig();
		this.savePlayerData();
		try {
			connection.close();
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultCore disconnected from Database");
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "VaultCore could not disconnect to Database");
		}
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