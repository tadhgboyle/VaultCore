package net.vaultmc.vaultcore;

import net.vaultmc.vaultcore.commands.BackCommand;
import net.vaultmc.vaultcore.commands.DiscordCommand;
import net.vaultmc.vaultcore.commands.PingCommand;
import net.vaultmc.vaultcore.commands.PlayTime;
import net.vaultmc.vaultcore.commands.RanksCommand;
import net.vaultmc.vaultcore.commands.ReloadCommand;
import net.vaultmc.vaultcore.commands.SeenCommand;
import net.vaultmc.vaultcore.commands.TokenCommand;
import net.vaultmc.vaultcore.commands.WildTeleport;
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
import net.vaultmc.vaultcore.commands.teleport.TPACommand;
import net.vaultmc.vaultcore.commands.teleport.TPAHereCommand;
import net.vaultmc.vaultcore.commands.teleport.TPAcceptCommand;
import net.vaultmc.vaultcore.commands.teleport.TPDenyCommand;
import net.vaultmc.vaultcore.commands.teleport.TPHereCommand;
import net.vaultmc.vaultcore.commands.teleport.TeleportCommand;
import net.vaultmc.vaultcore.commands.warp.DelWarpCommand;
import net.vaultmc.vaultcore.commands.warp.SetWarpCommand;
import net.vaultmc.vaultcore.commands.warp.WarpCommand;
import net.vaultmc.vaultcore.commands.worldtp.CRCommand;
import net.vaultmc.vaultcore.commands.worldtp.SVCommand;
import net.vaultmc.vaultcore.listeners.CycleListener;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultcore.listeners.ShutDownListener;
import net.vaultmc.vaultcore.listeners.SignColours;

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
		new TagCommand();
	}

	public static void registerListeners() {
		vault.registerEvents(new GrantCommandListener());
		vault.registerEvents(new SignColours());
		vault.registerEvents(new PlayerJoinQuitListener());
		vault.registerEvents(new PlayerTPListener());
		vault.registerEvents(new SettingsListener());
		vault.registerEvents(new CycleListener());
		vault.registerEvents(new ShutDownListener());
	}
}