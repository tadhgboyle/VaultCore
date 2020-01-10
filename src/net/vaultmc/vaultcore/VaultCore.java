package net.vaultmc.vaultcore;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandInv;
import net.vaultmc.vaultcore.runnables.RankPromotions;
import net.vaultmc.vaultcore.runnables.Statistics;
import net.vaultmc.vaultloader.components.Component;
import net.vaultmc.vaultloader.components.annotations.ComponentInfo;
import net.vaultmc.vaultloader.components.annotations.Version;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.configuration.Configuration;
import net.vaultmc.vaultloader.utils.configuration.ConfigurationManager;

@ComponentInfo(name = "VaultCore", description = "The suite of tools created for the VaultMC server.", authors = {
		"Aberdeener", "yangyang200", "2xjtn" })
@Version(major = 3, minor = 0, revision = 4)
public class VaultCore extends Component implements Listener {
	@Getter
	public static VaultCore instance;
	private static Chat chat = null;
	private static Permission perms = null;
	private Configuration playerData;
	private Configuration config;
	private Configuration locations;
	@Getter
	private static DBConnection database;

	private static String getServerName() {
		String name = "CraftBukkit";

		try {
			Class.forName("org.spigotmc.event.entity.EntityDismountEvent");
			name = "Spigot";
		} catch (ClassNotFoundException ex) {
		}

		try {
			Class.forName("com.destroystokyo.paper.NamespacedTag");
			name = "Paper";
		} catch (ClassNotFoundException ex) {
		}

		return name;
	}

	@Override
	public void onEnable() {
		instance = this;

		config = ConfigurationManager.loadConfiguration("config.yml", this);
		playerData = ConfigurationManager.loadConfiguration("data.yml", this);

		database = new DBConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
				getConfig().getString("mysql.database"), getConfig().getString("mysql.user"),
				getConfig().getString("mysql.password"));

		setupChat();
		setupPermissions();
		Registry.registerCommands();
		Registry.registerListeners();
		GrantCommandInv.initAdmin();
		GrantCommandInv.initMod();
		int minute = (int) 1200L;
		new BukkitRunnable() {
			public void run() {
				RankPromotions.memberPromotion();
				RankPromotions.patreonPromotion();
				try {
					Statistics.statistics();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimerAsynchronously(this.getBukkitPlugin(), 0, minute * 2);

		Bukkit.getServer().getConsoleSender().sendMessage(new String[] {
				ChatColor.YELLOW + "                   _ _     " + ChatColor.GOLD + "___               ",
				ChatColor.YELLOW + " /\\   /\\__ _ _   _| | |_  " + ChatColor.GOLD + "/ __\\___  _ __ ___ ",
				ChatColor.YELLOW + " \\ \\ / / _` | | | | | __|" + ChatColor.GOLD + "/ /  / _ \\| '__/ _ \\",
				ChatColor.YELLOW + "  \\ V / (_| | |_| | | |_" + ChatColor.GOLD + "/ /__| (_) | | |  __/",
				ChatColor.YELLOW + "   \\_/ \\__,_|\\__,_|_|\\__" + ChatColor.GOLD + "\\____/\\___/|_|  \\___|", "",
				ChatColor.GREEN + "Successfully enabled. Maintained by " + ChatColor.YELLOW + "Aberdeener"
						+ ChatColor.GREEN + ", " + "running on " + ChatColor.YELLOW + "Bukkit - " + getServerName()
						+ ChatColor.GREEN + "." });
	}

	public FileConfiguration getPlayerData() {
		return this.playerData.getConfig();
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

	@SneakyThrows
	public void savePlayerData() {
		playerData.save();
	}

	@SneakyThrows
	public void saveLocations() {
		locations.save();
	}

	@SneakyThrows
	public void saveConfig() {
		config.save();
	}

	@SneakyThrows
	public void reloadConfig() {
		config.reload();
	}

	private void setupChat() {
		RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public static Chat getChat() {
		return chat;
	}

	public static Permission getPermissions() {
		return perms;
	}

	@Override
	public void onDisable() {
		this.savePlayerData();
	}
}