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
import net.vaultmc.vaultcore.commands.staff.ClearChat;
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

	public static void registerCommands() {
		VaultCore.getInstance().getCommand("help").setExecutor(new HelpCommand());
		VaultCore.getInstance().getCommand("help").setTabCompleter(new TabCompletion());
		VaultCore.getInstance().getCommand("clearchat").setExecutor(new ClearChat());
		VaultCore.getInstance().getCommand("cc").setExecutor(new ClearChat());
		VaultCore.getInstance().getCommand("playtime").setExecutor(new PlayTime());
		VaultCore.getInstance().getCommand("pt").setExecutor(new PlayTime());
		VaultCore.getInstance().getCommand("staffchat").setExecutor(new StaffChat());
		VaultCore.getInstance().getCommand("sc").setExecutor(new StaffChat());
		VaultCore.getInstance().getCommand("wild").setExecutor(new WildTeleport());
		VaultCore.getInstance().getCommand("ping").setExecutor(new PingCommand());
		VaultCore.getInstance().getCommand("mutechat").setExecutor(new MuteChatCommand());
		VaultCore.getInstance().getCommand("check").setExecutor(new CheckCommand());
		VaultCore.getInstance().getCommand("say").setExecutor(new ConsoleSay());
		VaultCore.getInstance().getCommand("chat").setExecutor(new ConsoleSay());
		VaultCore.getInstance().getCommand("grant").setExecutor(new GrantCommand());
		VaultCore.getInstance().getCommand("gamemode").setExecutor(new GamemodeCommand());
		VaultCore.getInstance().getCommand("gamemode").setTabCompleter(new TabCompletion());
		VaultCore.getInstance().getCommand("gmc").setExecutor(new GamemodeCommand());
		VaultCore.getInstance().getCommand("gms").setExecutor(new GamemodeCommand());
		VaultCore.getInstance().getCommand("gmsp").setExecutor(new GamemodeCommand());
		VaultCore.getInstance().getCommand("teleport").setExecutor(new TeleportCommand());
		VaultCore.getInstance().getCommand("tp").setExecutor(new TeleportCommand());
		VaultCore.getInstance().getCommand("tphere").setExecutor(new TeleportCommand());
		VaultCore.getInstance().getCommand("tpa").setExecutor(new TPACommand());
		VaultCore.getInstance().getCommand("tpahere").setExecutor(new TPACommand());
		VaultCore.getInstance().getCommand("tpaccept").setExecutor(new TPACommand());
		VaultCore.getInstance().getCommand("tpdeny").setExecutor(new TPACommand());
		VaultCore.getInstance().getCommand("invsee").setExecutor(new InvseeCommand());
		VaultCore.getInstance().getCommand("fly").setExecutor(new FlyCommand());
		VaultCore.getInstance().getCommand("enderchest").setExecutor(new ECCommand());
		VaultCore.getInstance().getCommand("msg").setExecutor(new MsgCommand());
		VaultCore.getInstance().getCommand("r").setExecutor(new MsgCommand());
		VaultCore.getInstance().getCommand("feed").setExecutor(new FeedCommand());
		VaultCore.getInstance().getCommand("heal").setExecutor(new HealCommand());
		VaultCore.getInstance().getCommand("ranks").setExecutor(new RanksCommand());
		VaultCore.getInstance().getCommand("back").setExecutor(new BackCommand());
		VaultCore.getInstance().getCommand("discord").setExecutor(new DiscordCommand());
		VaultCore.getInstance().getCommand("sv").setExecutor(new WorldTPCommand());
		VaultCore.getInstance().getCommand("cr").setExecutor(new WorldTPCommand());
		VaultCore.getInstance().getCommand("warp").setExecutor(new WarpCommand());
		VaultCore.getInstance().getCommand("setwarp").setExecutor(new WarpCommand());
		VaultCore.getInstance().getCommand("delwarp").setExecutor(new WarpCommand());
		VaultCore.getInstance().getCommand("sctoggle").setExecutor(new StaffChat());
		VaultCore.getInstance().getCommand("token").setExecutor(new TokenCommand());
		VaultCore.getInstance().getCommand("seen").setExecutor(new SeenCommand());
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