package net.vaultmc.vaultcore.punishments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PunishmentAPI {

    public static HashMap<Player, Boolean> isChatReason = new HashMap<>();

    public static HashMap<Player, String> punishType = new HashMap<>();

    public static void setPlayerTypeReason(Player target, Player executor) {

        executor.closeInventory();
        isChatReason.put(executor, true);

        executor.sendMessage(ChatColor.YELLOW + "Please type a custom reason in chat.");

    }

    public static void setPlayerOutOutTypeReason(Player target, Player executor, String reason, String type) {

        executor.closeInventory();

        if(type.equalsIgnoreCase("BAN")) {
            isChatReason.remove(executor);
            PunishmentInventoryClickListener.reasons.put(executor, reason);
            executor.sendMessage(ChatColor.YELLOW + "Your custom reason is now: " + ChatColor.DARK_GREEN + PlayerPunishmentReasonChatEvent.custom_reason);
            PunishmentInventories.openPunishBanDurationInventory(target, executor);

        }

        if(type.equalsIgnoreCase("WARN")) {
            isChatReason.remove(executor);
            PunishmentInventoryClickListener.reasons.put(executor, reason);
            executor.sendMessage(ChatColor.YELLOW + "Your custom reason is now: " + ChatColor.DARK_GREEN + PlayerPunishmentReasonChatEvent.custom_reason);
            executor.chat("/warn " + target.getName() + " " + PlayerPunishmentReasonChatEvent.custom_reason);

        }

        if(type.equalsIgnoreCase("MUTE")) {
            isChatReason.remove(executor);
            PunishmentInventoryClickListener.reasons.put(executor, reason);
            executor.sendMessage(ChatColor.YELLOW + "Your custom reason is now: " + ChatColor.DARK_GREEN + PlayerPunishmentReasonChatEvent.custom_reason);
            PunishmentInventories.openPunishMutesDurationInventory(target, executor);

        }

    }

}
