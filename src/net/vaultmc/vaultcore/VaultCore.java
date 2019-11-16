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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.chat.Chat;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandInv;
import net.vaultmc.vaultcore.runnables.RankPromotions;

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
		r.runTaskAsynchronously(this);
		setupChat();
		Registry.registerCommands();
		Registry.registerListeners();
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