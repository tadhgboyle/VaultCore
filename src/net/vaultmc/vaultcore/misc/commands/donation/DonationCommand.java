package net.vaultmc.vaultcore.misc.commands.donation;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RootCommand(literal = "donation", description = "Check or modify how much a player has donated.")
@Permission(Permissions.DonationCommand)
public class DonationCommand extends CommandExecutor {

    public DonationCommand() {
        register("checkDonationSelf", Collections.emptyList());
        register("checkDonationOther", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));

        register("addDonation", Arrays.asList(Arguments.createLiteral("add"), Arguments.createArgument("amount", Arguments.floatArgument())));
        register("setDonation", Arrays.asList(Arguments.createLiteral("set"), Arguments.createArgument("amount", Arguments.floatArgument(0))));
        register("removeDonation", Arrays.asList(Arguments.createLiteral("remove"), Arguments.createArgument("amount", Arguments.floatArgument())));

    }

    DecimalFormat df = new DecimalFormat("#.##");

    @SubCommand("checkDonationSelf")
    @PlayerOnly
    public void checkDonationSelf(VLPlayer sender) {
        SQLPlayerData data = sender.getPlayerData();
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.get_self"), df.format(data.getDouble("donation"))));
    }

    @SubCommand("checkDonationOther")
    @Permission(Permissions.DonationCommandAdmin)
    public void checkDonationOther(VLCommandSender sender, VLOfflinePlayer target) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        if (!(sender instanceof ConsoleCommandSender) && (sender == target)) checkDonationSelf((VLPlayer) sender);
        else {
            SQLPlayerData data = target.getPlayerData();
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.get_other"), target.getFormattedName(), df.format(data.getDouble("donation"))));
        }
    }

    @SubCommand("addDonation")
    @Permission(Permissions.DonationCommandAdmin)
    public void addDonation(VLCommandSender sender, VLOfflinePlayer target, float donation) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        SQLPlayerData data = target.getPlayerData();
        double currentDonation = data.getDouble("donation");
        currentDonation += donation;
        data.set("donation", currentDonation);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.add_donation"), target.getFormattedName(), df.format(currentDonation)));
        updateDonationRank(sender, target, currentDonation);
    }

    @SubCommand("removeDonation")
    @Permission(Permissions.DonationCommandAdmin)
    public void removeDonation(VLCommandSender sender, VLOfflinePlayer target, float donation) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        SQLPlayerData data = target.getPlayerData();
        double currentDonation = data.getDouble("donation");
        if (donation > currentDonation) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.remove_less_total"), df.format(currentDonation)));
            return;
        }
        currentDonation -= donation;
        data.set("donation", currentDonation);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.remove_donation"), target.getFormattedName(), df.format(currentDonation)));
        updateDonationRank(sender, target, currentDonation);
    }

    @SubCommand("setDonation")
    @Permission(Permissions.DonationCommandAdmin)
    public void setDonation(VLCommandSender sender, VLOfflinePlayer target, float donation) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        SQLPlayerData data = target.getPlayerData();
        data.set("donation", donation);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.set_donation"), target.getFormattedName(), df.format(donation)));
        updateDonationRank(sender, target, donation);
    }

    List<String> donorRanks = Arrays.asList(DonationRanks.DONOR.getLuckPermsRole(), DonationRanks.DONOR_PLUS.getLuckPermsRole(), DonationRanks.DONOR_PLUS_PLUS.getLuckPermsRole());

    // TODO: This is really shitty, make it better soon :D
    private void updateDonationRank(VLCommandSender sender, VLOfflinePlayer target, double donation) {
        String name;
        String luckPermsGroup;
        if (donation >= DonationRanks.DONOR.getDonationRequirement() && donation <= DonationRanks.DONOR_PLUS.getDonationRequirement()) {
            name = DonationRanks.DONOR.getName();
            luckPermsGroup = DonationRanks.DONOR.getLuckPermsRole();
        } else if (donation >= DonationRanks.DONOR_PLUS.getDonationRequirement() && donation <= DonationRanks.DONOR_PLUS_PLUS.getDonationRequirement()) {
            name = DonationRanks.DONOR_PLUS.getName();
            luckPermsGroup = DonationRanks.DONOR_PLUS.getLuckPermsRole();
        } else if (donation > DonationRanks.DONOR_PLUS_PLUS.getDonationRequirement()) {
            name = DonationRanks.DONOR_PLUS_PLUS.getName();
            luckPermsGroup = DonationRanks.DONOR_PLUS_PLUS.getLuckPermsRole();
        } else {
            removeRanks(target, true, "");
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.no_avail_rank"), target.getFormattedName()));
            return;
        }
        // Remove all donor ranks which are not equal to their new one
        removeRanks(target, false, luckPermsGroup);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent add " + luckPermsGroup);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.updated_rank_sender"), target.getFormattedName(), name));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.donation.updated_rank_target"), sender.getFormattedName(), name));
    }

    private void removeRanks(VLOfflinePlayer target, boolean removeAll, String keepRank) {
        for (String rank : donorRanks) {
            if (!removeAll && keepRank.equalsIgnoreCase(keepRank)) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent remove " + rank);
        }
    }
}
