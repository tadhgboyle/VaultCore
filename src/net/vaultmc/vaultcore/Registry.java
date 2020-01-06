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
import net.vaultmc.vaultcore.commands.teleport.*;
import net.vaultmc.vaultcore.commands.warp.DelWarpCommand;
import net.vaultmc.vaultcore.commands.warp.SetWarpCommand;
import net.vaultmc.vaultcore.commands.warp.WarpCommand;
import net.vaultmc.vaultcore.commands.worldtp.CRCommand;
import net.vaultmc.vaultcore.commands.worldtp.SVCommand;
import net.vaultmc.vaultcore.listeners.*;

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
		vault.registerEvents(new GrantCommandListener());
		vault.registerEvents(new SignColours());
		vault.registerEvents(new PlayerJoinQuitListener());
		vault.registerEvents(new PlayerTPListener());
		vault.registerEvents(new SettingsListener());
		vault.registerEvents(new CycleListener());
		vault.registerEvents(new ShutDownListener());
	}
}