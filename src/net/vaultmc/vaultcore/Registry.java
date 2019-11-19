package net.vaultmc.vaultcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import net.vaultmc.vaultcore.commands.BackCommand;
import net.vaultmc.vaultcore.commands.DiscordCommand;
import net.vaultmc.vaultcore.commands.ECCommand;
import net.vaultmc.vaultcore.commands.HelpCommand;
import net.vaultmc.vaultcore.commands.MsgCommand;
import net.vaultmc.vaultcore.commands.PingCommand;
import net.vaultmc.vaultcore.commands.PlayTime;
import net.vaultmc.vaultcore.commands.RanksCommand;
import net.vaultmc.vaultcore.commands.SeenCommand;
import net.vaultmc.vaultcore.commands.TPACommand;
import net.vaultmc.vaultcore.commands.TokenCommand;
import net.vaultmc.vaultcore.commands.WarpCommand;
import net.vaultmc.vaultcore.commands.WildTeleport;
import net.vaultmc.vaultcore.commands.WorldTPCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsListener;
import net.vaultmc.vaultcore.commands.staff.CheckCommand;
import net.vaultmc.vaultcore.commands.staff.ClearChatCommand;
import net.vaultmc.vaultcore.commands.staff.ConsoleSay;
import net.vaultmc.vaultcore.commands.staff.FeedCommand;
import net.vaultmc.vaultcore.commands.staff.FlyCommand;
import net.vaultmc.vaultcore.commands.staff.GamemodeCommand;
import net.vaultmc.vaultcore.commands.staff.HealCommand;
import net.vaultmc.vaultcore.commands.staff.InvseeCommand;
import net.vaultmc.vaultcore.commands.staff.MuteChatCommand;
import net.vaultmc.vaultcore.commands.staff.StaffChat;
import net.vaultmc.vaultcore.commands.staff.TeleportCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandListener;
import net.vaultmc.vaultcore.listeners.ChatManager;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultcore.listeners.SignColours;
import net.vaultmc.vaultcore.tabcompletion.TabCompletion;

public class Registry {
	
	static VaultCore vault = VaultCore.getInstance();

	public static void registerCommands() {
		vault.getCommand("help").setExecutor(new HelpCommand());
		vault.getCommand("help").setTabCompleter(new TabCompletion());
		vault.getCommand("clearchat").setExecutor(new ClearChatCommand());
		vault.getCommand("cc").setExecutor(new ClearChatCommand());
		vault.getCommand("playtime").setExecutor(new PlayTime());
		vault.getCommand("pt").setExecutor(new PlayTime());
		vault.getCommand("staffchat").setExecutor(new StaffChat());
		vault.getCommand("sc").setExecutor(new StaffChat());
		vault.getCommand("wild").setExecutor(new WildTeleport());
		vault.getCommand("ping").setExecutor(new PingCommand());
		vault.getCommand("mutechat").setExecutor(new MuteChatCommand());
		vault.getCommand("check").setExecutor(new CheckCommand());
		vault.getCommand("say").setExecutor(new ConsoleSay());
		vault.getCommand("chat").setExecutor(new ConsoleSay());
		vault.getCommand("grant").setExecutor(new GrantCommand());
		vault.getCommand("gamemode").setExecutor(new GamemodeCommand());
		vault.getCommand("gamemode").setTabCompleter(new TabCompletion());
		vault.getCommand("gmc").setExecutor(new GamemodeCommand());
		vault.getCommand("gms").setExecutor(new GamemodeCommand());
		vault.getCommand("gmsp").setExecutor(new GamemodeCommand());
		vault.getCommand("teleport").setExecutor(new TeleportCommand());
		vault.getCommand("tp").setExecutor(new TeleportCommand());
		vault.getCommand("tphere").setExecutor(new TeleportCommand());
		vault.getCommand("tpa").setExecutor(new TPACommand());
		vault.getCommand("tpahere").setExecutor(new TPACommand());
		vault.getCommand("tpaccept").setExecutor(new TPACommand());
		vault.getCommand("tpdeny").setExecutor(new TPACommand());
		vault.getCommand("invsee").setExecutor(new InvseeCommand());
		vault.getCommand("fly").setExecutor(new FlyCommand());
		vault.getCommand("enderchest").setExecutor(new ECCommand());
		vault.getCommand("msg").setExecutor(new MsgCommand());
		vault.getCommand("r").setExecutor(new MsgCommand());
		vault.getCommand("feed").setExecutor(new FeedCommand());
		vault.getCommand("heal").setExecutor(new HealCommand());
		vault.getCommand("ranks").setExecutor(new RanksCommand());
		vault.getCommand("back").setExecutor(new BackCommand());
		vault.getCommand("discord").setExecutor(new DiscordCommand());
		vault.getCommand("sv").setExecutor(new WorldTPCommand());
		vault.getCommand("cr").setExecutor(new WorldTPCommand());
		vault.getCommand("warp").setExecutor(new WarpCommand());
		vault.getCommand("setwarp").setExecutor(new WarpCommand());
		vault.getCommand("delwarp").setExecutor(new WarpCommand());
		vault.getCommand("sctoggle").setExecutor(new StaffChat());
		vault.getCommand("token").setExecutor(new TokenCommand());
		vault.getCommand("seen").setExecutor(new SeenCommand());
	}

	public static void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(VaultCore.getInstance(), VaultCore.getInstance());
		pm.registerEvents(new ChatManager(), VaultCore.getInstance());
		pm.registerEvents(new GrantCommandListener(), VaultCore.getInstance());
		pm.registerEvents(new SignColours(), VaultCore.getInstance());
		pm.registerEvents(new PlayerJoinQuitListener(), VaultCore.getInstance());
		pm.registerEvents(new PlayerTPListener(), VaultCore.getInstance());
		pm.registerEvents(new SettingsListener(), VaultCore.getInstance());
	}
}