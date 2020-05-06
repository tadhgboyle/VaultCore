package net.vaultmc.vaultcore;

import lombok.Getter;
import net.vaultmc.vaultcore.brand.BrandCommand;
import net.vaultmc.vaultcore.buggy.BuggyCommand;
import net.vaultmc.vaultcore.chat.*;
import net.vaultmc.vaultcore.chat.groups.ChatGroupsCommand;
import net.vaultmc.vaultcore.chat.msg.MsgCommand;
import net.vaultmc.vaultcore.chat.msg.ReplyCommand;
import net.vaultmc.vaultcore.chat.msg.SocialSpyCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.combat.CombatLog;
import net.vaultmc.vaultcore.connections.DiscordCommand;
import net.vaultmc.vaultcore.connections.TokenCommand;
import net.vaultmc.vaultcore.cosmetics.CosmeticsCommand;
import net.vaultmc.vaultcore.creative.SchemCommand;
import net.vaultmc.vaultcore.discordbot.ManageBotCommand;
import net.vaultmc.vaultcore.economy.EconomyCommand;
import net.vaultmc.vaultcore.economy.EconomyListener;
import net.vaultmc.vaultcore.economy.MoneyCommand;
import net.vaultmc.vaultcore.economy.TransferCommand;
import net.vaultmc.vaultcore.gamemode.GMCreativeCommand;
import net.vaultmc.vaultcore.gamemode.GMSpectatorCommand;
import net.vaultmc.vaultcore.gamemode.GMSurvivalCommand;
import net.vaultmc.vaultcore.gamemode.GameModeCommand;
import net.vaultmc.vaultcore.misc.commands.*;
import net.vaultmc.vaultcore.misc.commands.donation.DonationCommand;
import net.vaultmc.vaultcore.misc.commands.mail.MailCommand;
import net.vaultmc.vaultcore.misc.commands.staff.*;
import net.vaultmc.vaultcore.misc.commands.staff.grant.GrantCommand;
import net.vaultmc.vaultcore.misc.commands.staff.logs.LogsCommand;
import net.vaultmc.vaultcore.punishments.ban.BanCommand;
import net.vaultmc.vaultcore.punishments.ban.IpBanCommand;
import net.vaultmc.vaultcore.punishments.ban.UnbanCommand;
import net.vaultmc.vaultcore.punishments.mute.UnmuteCommand;
import net.vaultmc.vaultcore.report.ReportCommand;
import net.vaultmc.vaultcore.report.ReportsCommand;
import net.vaultmc.vaultcore.settings.PlayerCustomKeys;
import net.vaultmc.vaultcore.settings.SettingsCommand;
import net.vaultmc.vaultcore.stats.CheckCommand;
import net.vaultmc.vaultcore.stats.PlayTimeCommand;
import net.vaultmc.vaultcore.stats.SeenCommand;
import net.vaultmc.vaultcore.survival.CraftingCommand;
import net.vaultmc.vaultcore.survival.TheEndReset;
import net.vaultmc.vaultcore.survival.claim.ClaimCommand;
import net.vaultmc.vaultcore.survival.claim.UnclaimCommand;
import net.vaultmc.vaultcore.survival.home.DelHomeCommand;
import net.vaultmc.vaultcore.survival.home.HomeCommand;
import net.vaultmc.vaultcore.survival.home.SetHomeCommand;
import net.vaultmc.vaultcore.teleport.*;
import net.vaultmc.vaultcore.teleport.tpa.TPACommand;
import net.vaultmc.vaultcore.teleport.tpa.TPAHereCommand;
import net.vaultmc.vaultcore.teleport.tpa.TPAcceptCommand;
import net.vaultmc.vaultcore.teleport.tpa.TPDenyCommand;
import net.vaultmc.vaultcore.teleport.worldtp.CRCommand;
import net.vaultmc.vaultcore.teleport.worldtp.SVCommand;
import net.vaultmc.vaultcore.tour.TourCommand;
import net.vaultmc.vaultcore.tour.TourMusic;
import net.vaultmc.vaultcore.tour.TourStageCommand;
import net.vaultmc.vaultcore.vanish.VanishCommand;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.RootCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Registry {

    // Store all our command classes
    // TODO: Organize these
    private static final Set<Class<?>> classSet = new HashSet<Class<?>>() {{
        add(CheckCommand.class);
        add(MsgCommand.class);
        add(ReplyCommand.class);
        add(TPACommand.class);
        add(TPAcceptCommand.class);
        add(TPAHereCommand.class);
        add(TPHereCommand.class);
        add(TPDenyCommand.class);
        add(DiscordCommand.class);
        add(PingCommand.class);
        add(PlayTimeCommand.class);
        add(RanksCommand.class);
        add(SpeedCommand.class);
        add(SeenCommand.class);
        add(TokenCommand.class);
        add(GrantCommand.class);
        add(GameModeCommand.class);
        add(GMCreativeCommand.class);
        add(GMSurvivalCommand.class);
        add(GMSpectatorCommand.class);
        add(ClearChatCommand.class);
        add(ConsoleSay.class);
        add(FeedCommand.class);
        add(FlyCommand.class);
        add(HealCommand.class);
        add(InvseeCommand.class);
        add(MuteChatCommand.class);
        add(StaffChatCommand.class);
        add(TPCommand.class);
        add(ReloadCommand.class);
        add(HasPermCommand.class);
        add(TagCommand.class);
        add(CrashCommand.class);
        add(ListCommand.class);
        add(SocialSpyCommand.class);
        add(ModMode.class);
        add(DonationCommand.class);
        add(TimeCommand.class);
        add(VanishCommand.class);
        add(WeatherCommand.class);
        add(HelpCommand.class);
        add(BrandCommand.class);
        add(BanCommand.class);
        add(UnbanCommand.class);
        add(IpBanCommand.class);
        add(UnmuteCommand.class);
        add(LagCommand.class);
        add(HubCommand.class);
        add(AdminChatCommand.class);
        add(ClearCommand.class);
        add(HomeCommand.class);
        add(SetHomeCommand.class);
        add(DelHomeCommand.class);
        add(EconomyListener.class);
        add(SpawnCommand.class);
        add(ForceFieldCommand.class);
        add(SudoCommand.class);
        add(PlayerCustomKeys.class);
        add(SuicideCommand.class);
        add(IgnoreCommand.class);
        add(UnignoreCommand.class);
        add(SecLogCommand.class);
        add(LogsCommand.class);
        add(CombatLog.class);
        add(SettingsCommand.class);
        add(EconomyCommand.class);
        add(MoneyCommand.class);
        add(TransferCommand.class);
        add(SkullCommand.class);
        add(NicknameCommand.class);
        add(CraftingCommand.class);
        add(CosmeticsCommand.class);
        add(NightvisionCommand.class);
        add(TPAllCommand.class);
        add(MailCommand.class);
        add(BuggyCommand.class);
        add(ManageBotCommand.class);
        add(ChatGroupsCommand.class);
        add(CRCommand.class);
        add(SVCommand.class);
        add(WildTeleportCommand.class);
        add(TourCommand.class);
        add(TourStageCommand.class);
        add(TourMusic.class);
        add(ClaimCommand.class);
        add(UnclaimCommand.class);
        add(SchemCommand.class);
        add(BackCommand.class);
        add(LolCommand.class);
        add(AFKCommand.class);
        add(WarpCommand.class);
        add(TheEndReset.class);
        add(ReportCommand.class);
        add(NearCommand.class);
        add(ReportsCommand.class);
    }};
    @Getter
    private static final HashMap<String, Class<?>> commandClasses = new HashMap<>();

    // Register the above classes, and add to commandClasses with the root command and the class name
    public static void registerCommands() {
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(RootCommand.class)) {
                commandClasses.put(clazz.getAnnotation(RootCommand.class).literal(), clazz);
            }
            if (clazz.isAnnotationPresent(Aliases.class)) {
                for (String aliases : clazz.getAnnotation(Aliases.class).value())
                    commandClasses.put(aliases, clazz);
            }
            try {
                clazz.getConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
            }
        }
    }
}
