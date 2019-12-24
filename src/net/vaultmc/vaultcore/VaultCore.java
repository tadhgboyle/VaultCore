package net.vaultmc.vaultcore;

import net.milkbowl.vault.chat.Chat;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandInv;
import net.vaultmc.vaultcore.runnables.RankPromotions;
import net.vaultmc.vaultcore.utils.ConnectionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class VaultCore extends JavaPlugin implements Listener {
    public static VaultCore instance;
    private static Chat chat = null;
    // data file setup
    private File playerDataFile;
    private FileConfiguration playerData;
    // mysql info
    public ConnectionHandler connection;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        instance = this;
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                connection = new ConnectionHandler();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultCore connected to Database");
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
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            RankPromotions.memberPromotion();
            RankPromotions.patreonPromotion();
        }, 0L, minute * 5);
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

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
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
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultCore disconnected from the database");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "VaultCore could not disconnect from the database");
        }
    }
}