package net.vaultmc.vaultcore;

import net.vaultmc.vaultcore.commands.*;
import net.vaultmc.vaultcore.commands.msg.MsgCommand;
import net.vaultmc.vaultcore.commands.msg.ReplyCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsListener;
import net.vaultmc.vaultcore.commands.staff.*;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMCreativeCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMSpectatorCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMSurvivalCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandListener;
import net.vaultmc.vaultcore.commands.tpa.TPACommand;
import net.vaultmc.vaultcore.commands.tpa.TPAHereCommand;
import net.vaultmc.vaultcore.commands.tpa.TPAcceptCommand;
import net.vaultmc.vaultcore.commands.tpa.TPDenyCommand;
import net.vaultmc.vaultcore.commands.tpa.TPHereCommand;
import net.vaultmc.vaultcore.commands.warp.DelWarpCommand;
import net.vaultmc.vaultcore.commands.warp.SetWarpCommand;
import net.vaultmc.vaultcore.commands.warp.WarpCommand;
import net.vaultmc.vaultcore.commands.worldtp.CRCommand;
import net.vaultmc.vaultcore.commands.worldtp.SVCommand;
import net.vaultmc.vaultcore.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Registry {
	private static final VaultCore vault = VaultCore.getInstance();

	public static void registerCommands() {
		new MsgCommand();
		new ReplyCommand();
		new SettingsCommand();
		new TPACommand();
		new TPAcceptCommand();
		new TPAHereCommand();
		new TPHereCommand();
		new TPDenyCommand();
		new WarpCommand();
		new SetWarpCommand();
		new DelWarpCommand();
		new CRCommand();
		new SVCommand();
		new BackCommand();
		new DiscordCommand();
		new PingCommand();
		new PlayTime();
		new RanksCommand();
		new SeenCommand();
		new TokenCommand();
		new WildTeleport();
		new GrantCommand();
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
		new TeleportCommand();
		new ReloadCommand();
		new HasPermCommand();
	}

	public static void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(vault, vault);
		pm.registerEvents(new GrantCommandListener(), vault);
		pm.registerEvents(new SignColours(), vault);
		pm.registerEvents(new PlayerJoinQuitListener(), vault);
		pm.registerEvents(new PlayerTPListener(), vault);
		pm.registerEvents(new SettingsListener(), vault);
		pm.registerEvents(new CycleListener(), vault);
		pm.registerEvents(new ShutDownListener(), vault);
	}
}