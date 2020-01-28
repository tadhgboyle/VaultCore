package net.vaultmc.vaultcore;

import net.vaultmc.vaultcore.commands.AFKCommand;
import net.vaultmc.vaultcore.commands.BackCommand;
import net.vaultmc.vaultcore.commands.DiscordCommand;
import net.vaultmc.vaultcore.commands.HelpCommand;
import net.vaultmc.vaultcore.commands.ListCommand;
import net.vaultmc.vaultcore.commands.PingCommand;
import net.vaultmc.vaultcore.commands.PlayTime;
import net.vaultmc.vaultcore.commands.RanksCommand;
import net.vaultmc.vaultcore.commands.ReloadCommand;
import net.vaultmc.vaultcore.commands.SeenCommand;
import net.vaultmc.vaultcore.commands.StatsCommand;
import net.vaultmc.vaultcore.commands.TokenCommand;
import net.vaultmc.vaultcore.commands.WarpCommand;
import net.vaultmc.vaultcore.commands.WildTeleport;
import net.vaultmc.vaultcore.commands.economy.EconomyCommand;
import net.vaultmc.vaultcore.commands.economy.MoneyCommand;
import net.vaultmc.vaultcore.commands.economy.TransferCommand;
import net.vaultmc.vaultcore.commands.msg.MsgCommand;
import net.vaultmc.vaultcore.commands.msg.ReplyCommand;
import net.vaultmc.vaultcore.commands.msg.SocialSpyCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsCommand;
import net.vaultmc.vaultcore.commands.settings.SettingsListener;
import net.vaultmc.vaultcore.commands.staff.BrandCommand;
import net.vaultmc.vaultcore.commands.staff.CheckCommand;
import net.vaultmc.vaultcore.commands.staff.ClearChatCommand;
import net.vaultmc.vaultcore.commands.staff.ConsoleSay;
import net.vaultmc.vaultcore.commands.staff.FeedCommand;
import net.vaultmc.vaultcore.commands.staff.FlyCommand;
import net.vaultmc.vaultcore.commands.staff.HasPermCommand;
import net.vaultmc.vaultcore.commands.staff.HealCommand;
import net.vaultmc.vaultcore.commands.staff.InvseeCommand;
import net.vaultmc.vaultcore.commands.staff.ModMode;
import net.vaultmc.vaultcore.commands.staff.MuteChatCommand;
import net.vaultmc.vaultcore.commands.staff.StaffChatCommand;
import net.vaultmc.vaultcore.commands.staff.TagCommand;
import net.vaultmc.vaultcore.commands.staff.TimeCommand;
import net.vaultmc.vaultcore.commands.staff.VanishCommand;
import net.vaultmc.vaultcore.commands.staff.WeatherCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMCreativeCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMSpectatorCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GMSurvivalCommand;
import net.vaultmc.vaultcore.commands.staff.gamemode.GameModeCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommand;
import net.vaultmc.vaultcore.commands.staff.grant.GrantCommandListener;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.BanCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.BannedListener;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.IpBanCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.IpTempBanCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.TempBanCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.ban.UnbanCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.kick.KickCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.IpMuteCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.IpTempMuteCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.MuteCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.MutedListener;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.TempMuteCommand;
import net.vaultmc.vaultcore.commands.staff.punishments.mute.UnmuteCommand;
import net.vaultmc.vaultcore.commands.teleport.TPACommand;
import net.vaultmc.vaultcore.commands.teleport.TPAHereCommand;
import net.vaultmc.vaultcore.commands.teleport.TPAcceptCommand;
import net.vaultmc.vaultcore.commands.teleport.TPCommand;
import net.vaultmc.vaultcore.commands.teleport.TPDenyCommand;
import net.vaultmc.vaultcore.commands.teleport.TPHereCommand;
import net.vaultmc.vaultcore.commands.worldtp.CRCommand;
import net.vaultmc.vaultcore.commands.worldtp.SVCommand;
import net.vaultmc.vaultcore.listeners.ChatUtils;
import net.vaultmc.vaultcore.listeners.CycleListener;
import net.vaultmc.vaultcore.listeners.GameModeListeners;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultcore.listeners.ShutDownListener;
import net.vaultmc.vaultcore.listeners.SignColours;
import net.vaultmc.vaultcore.listeners.VanishListeners;
import net.vaultmc.vaultcore.ported.inventory.InventoryStorageListeners;
import net.vaultmc.vaultcore.ported.report.ReportCommand;
import net.vaultmc.vaultcore.ported.report.ReportsCommand;

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
		new GameModeCommand();
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
		new TPCommand();
		new ReloadCommand();
		new HasPermCommand();
		new TagCommand();
		new StatsCommand();
		new ListCommand();
		new SocialSpyCommand();
		
        new ModMode();
        new ReportCommand();
        new ReportsCommand();
        new AFKCommand();
        new ChatUtils();
        new EconomyCommand();
        new MoneyCommand();
        
        new TimeCommand();
        new TransferCommand();
        new VanishCommand();
        new VanishListeners();
        new InventoryStorageListeners();
        new GameModeListeners();
        new WeatherCommand();
        new HelpCommand();
        
        new BrandCommand();
        
        new KickCommand();
        new BanCommand();
        
        new MuteCommand();
        new UnbanCommand();
        new IpBanCommand();
        
        new IpTempBanCommand();
        new IpMuteCommand();
        new IpTempMuteCommand();
        
        new UnmuteCommand();
        new TempBanCommand();
        new TempMuteCommand();
	}

	public static void registerListeners() {
		vault.registerEvents(new GrantCommandListener());
		vault.registerEvents(new SignColours());
		vault.registerEvents(new PlayerJoinQuitListener());
		vault.registerEvents(new PlayerTPListener());
		vault.registerEvents(new SettingsListener());
		vault.registerEvents(new CycleListener());
		vault.registerEvents(new ShutDownListener());
        vault.registerEvents(new BannedListener());
        vault.registerEvents(new MutedListener());
	}
}