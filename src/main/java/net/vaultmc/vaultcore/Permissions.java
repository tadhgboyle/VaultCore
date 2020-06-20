/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore;

public final class Permissions {
    public static final String DisguiseCommand = "vaultcore.disguise";
    public static final String DisguiseAsPlayer = "vaultcore.disguise.player";
    public static final String DisguiseRandomPlayer = "vaultcore.disguise.player.random";
    public static final String BuggyCommand = "vaultcore.buggy";
    public static final String BuggyAdmin = "vaultcore.buggy.admin";
    public static final String BackCommand = "vaultcore.back";
    public static final String CooldownBypass = "vaultcore.cooldown";
    public static final String DiscordCommand = "vaultcore.discord";
    public static final String MsgCommand = "vaultcore.msg";
    public static final String PingCommand = "vaultcore.ping";
    public static final String PingCommandOther = "vaultcore.ping.other";
    public static final String PlayTime = "vaultcore.playtime";
    public static final String PlayTimeOther = "vaultcore.playtime.other";
    public static final String RanksCommand = "vaultcore.ranks";
    public static final String SeenCommand = "vaultcore.seen";
    public static final String TokenCommand = "vaultcore.token";
    public static final String TPACommand = "vaultcore.tpa";
    public static final String TPAHereCommand = "vaultcore.tpahere";
    public static final String WarpCommand = "vaultcore.warp";
    public static final String WarpCommandSet = "vaultcore.warp.set";
    public static final String WarpCommandDelete = "vaultcore.warp.delete";
    public static final String WildTeleport = "vaultcore.wildteleport";
    public static final String CreativeCommand = "vaultcore.worlds.creative";
    public static final String SurvivalCommand = "vaultcore.worlds.survival";
    public static final String SettingsCommand = "vaultcore.settings";
    public static final String InvSeeAdmin = "vaultcore.invsee.admin";
    public static final String CheckCommand = "vaultcore.check";
    public static final String ClearChatCommand = "vaultcore.clearchat";
    public static final String FeedCommand = "vaultcore.feed";
    public static final String FeedCommandOther = "vaultcore.feed.other";
    public static final String FlyCommand = "vaultcore.fly";
    public static final String FlyCommandOther = "vaultcore.fly.other";
    public static final String FlyLobbyOnlyBypass = "vaultcore.fly.lobbybypass";
    public static final String HasPermCommand = "vaultcore.hasperm";
    public static final String HasPermCommandOther = "vaultcore.hasperm.other";
    public static final String GameModeCommand = "vaultcore.gamemode";
    public static final String GameModeCommandOther = "vaultcore.gamemode.other";
    public static final String HealCommand = "vaultcore.heal";
    public static final String HealCommandOther = "vaultcore.heal.other";
    public static final String InvseeCommand = "vaultcore.invsee";
    public static final String InvseeExempt = "vaultcore.invsee.exempt";
    public static final String MuteChatCommand = "vaultcore.mutechat";
    public static final String MuteChatCommandOverride = "vaultcore.mutechat.override";
    public static final String StaffChatCommand = "vaultcore.staffchat";
    public static final String TeleportCommand = "vaultcore.teleport";
    public static final String TeleportCommandHere = "vaultcore.teleport.here";
    public static final String GrantCommand = "vaultcore.grant";
    public static final String ReloadCommand = "vaultcore.reload";
    public static final String TagCommand = "vaultcore.tag";
    public static final String TagCommandDelete = "vaultcore.tag.delete";
    public static final String ListCommand = "vaultcore.list";
    public static final String SocialSpyCommand = "vaultcore.socialspy";
    public static final String ModMode = "vaultcore.modmode";
    public static final String ReportCommand = "vaultcore.report";
    public static final String ReportsCommand = "vaultcore.report.view";
    public static final String AFKCommand = "vaultcore.afk";
    public static final String AFKCommandOther = "vaultcore.afk.other";
    public static final String EconomyCommand = "vaultcore.economy";
    public static final String BalanceCommand = "vaultcore.economy.balance";
    public static final String TransferCommand = "vaultcore.economy.transfer";
    public static final String TimeCommand = "vaultcore.time";
    public static final String VanishCommand = "vaultcore.vanish";
    public static final String WeatherCommand = "vaultcore.weather";
    public static final String HelpCommand = "vaultcore.help";
    public static final String BrandCommand = "vaultcore.brand";
    public static final String BanCommand = "vaultcore.punishments.ban";
    public static final String KickCommand = "vaultcore.punishments.kick";
    public static final String MuteCommand = "vaultcore.punishments.mute";
    public static final String WarnCommand = "vaultcore.punishments.warn";
    public static final String PunishmentNotify = "vaultcore.punishments.notify";
    public static final String PunishmentSilentOverride = "vaultcore.punishments.silent";
    public static final String ChatColor = "vaultcore.chat.color";
    public static final String Tour = "vaultcore.tour";
    public static final String ClaimCommand = "vaultcore.claim";
    public static final String SchemCommand = "vaultcore.schem";
    public static final String SchemCommandDelete = "vaultcore.schem.delete.other";
    public static final String SpeedCommand = "vaultcore.speed";
    public static final String SpeedCommandOther = "vaultcore.speed.other";
    public static final String LolCommand = "vaultcore.lol";
    public static final String LolCommandId = "vaultcore.lol.id";
    public static final String LolCommandEdit = "vaultcore.lol.edit";
    public static final String LagCommand = "vaultcore.lag";
    public static final String ClearCommand = "vaultcore.clear";
    public static final String ClearCommandOther = "vaultcore.clear.other";
    public static final String AdminChatCommand = "vaultcore.staffchat.adminchat";
    public static final String LogsCommand = "vaultcore.logs";
    public static final String Home = "vaultcore.home";
    public static final String SetHomeBase = "vaultcore.sethome.";
    public static final String Crash = "vaultcore.crash";
    public static final String VaultMCBotManage = "vaultcore.bot.manage";
    public static final String SpawnCommand = "vaultcore.spawn";
    public static final String ForceFieldCommand = "vaultcore.forcefield";
    public static final String ForceFieldExempt = "vaultcore.forcefield.exempt";
    public static final String NearCommand = "vaultcore.near";
    public static final String IgnoreCommand = "vaultcore.ignore";
    public static final String SudoCommand = "vaultcore.sudo";
    public static final String SudoCommandExempt = "vaultcore.sudo.exempt";
    public static final String SuicideCommand = "vaultcore.suicide";
    public static final String ChatGroupsCommand = "vaultcore.chatgroups";
    public static final String NicknameCommand = "vaultcore.nickname";
    public static final String NicknameLimitBypass = "vaultcore.nickname.bypass";
    public static final String NicknameCommandOther = "vaultcore.nickname.other";
    public static final String NightVisionCommand = "vaultcore.nightvision";
    public static final String SkullCommand = "vaultcore.skull";
    public static final String MailCommand = "vaultcore.mail";
    public static final String StatsCommand = "vaultcore.pvp.stats";
    public static final String StatsCommandOther = "vaultcore.pvp.stats.other";
    public static final String PvPAdmin = "vaultcore.pvp.admin";
    public static final String KitGuiCommand = "vaultcore.pvp.kits";
    public static final String EntityLimitOverride = "vaultcore.plots.overrideentitylimit";
    public static final String PunishCommand = "vaultcore.punishments.gui";
    public static final String BuilderAccess = "vaultcore.builderaccess";
    public static final String PlayerVault = "vaultcore.playervault";
    public static final String LinkRedditBot = "vaultcore.reddit.link";
    public static final String CosmeticsCommand = "vaultcore.cosmetics";
    public static final String DisguiseAsBypass = "vaultcore.disguise.bypass";
    public static final String Punchable = "vaultcore.punchable";
    public static final String CanPunch = "vaultcore.punch";
    public static final String ShieldCommand = "vaultcore.shield";
}
