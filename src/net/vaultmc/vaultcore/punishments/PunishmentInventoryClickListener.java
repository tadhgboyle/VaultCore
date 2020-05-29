package net.vaultmc.vaultcore.punishments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

public class PunishmentInventoryClickListener implements Listener {

    public static HashMap<Player, String> reasons = new HashMap<Player, String>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        Player target = PunishCommand.punishes.get(p);

        int slot = e.getSlot();


        if(e.getView().getTitle().equals("Punishments > Main")) {

            e.setCancelled(true);

            if(slot == 4) {
                Bukkit.dispatchCommand(p, "history " + target.getName());
                p.closeInventory();
                return;

            }

            if(slot == 17) {
                p.closeInventory();
                return;

            }

            if(slot == 14) {
                p.closeInventory();
                PunishmentInventories.openPunishBanInventory(target, p);

            }

            if(slot == 10) {
                p.closeInventory();
                PunishmentInventories.openPunishWarnInventory(target, p);

            }

            if(slot == 12) {
                p.closeInventory();
                PunishmentInventories.openPunishMuteInventory(target, p);

            }

            if(slot == 14) {

                return;
            }
        }

        if(e.getView().getTitle().equals("Punishments > Bans > Reasons")) {

            e.setCancelled(true);

            if(slot == 8) {
                PunishmentInventories.openPunishMainInventory(target, p);

            }

            if(slot == 0) {
                reasons.put(p, "Hacking Offenses");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 1) {
                reasons.put(p, "Staff/Player Disrespect");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 2) {
                reasons.put(p, "Unwanted in-game activity");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 3) {
                reasons.put(p, "Spamming");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 4) {
                reasons.put(p, "Chat Abuse");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 5) {
                reasons.put(p, "Bug Abuse");
                PunishmentInventories.openPunishBanDurationInventory(target, p);

            }

            if(slot == 6) {

                PunishmentAPI.punishType.put(p, "BAN");

                PunishmentAPI.setPlayerTypeReason(target, p);
                return;
            }

        }

        if(e.getView().getTitle().equals("Punishments > Mutes > Reasons")) {

            e.setCancelled(true);

            if(slot == 8) {
                PunishmentInventories.openPunishMainInventory(target, p);

            }

            if(slot == 0) {
                reasons.put(p, "Hacking Offenses");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 1) {
                reasons.put(p, "Staff/Player Disrespect");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 2) {
                reasons.put(p, "Unwanted in-game activity");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 3) {
                reasons.put(p, "Spamming");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 4) {
                reasons.put(p, "Chat Abuse");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 5) {
                reasons.put(p, "Bug Abuse");
                PunishmentInventories.openPunishMutesDurationInventory(target, p);

            }

            if(slot == 6) {

                PunishmentAPI.punishType.put(p, "MUTE");

                PunishmentAPI.setPlayerTypeReason(target, p);
                return;

            }

        }

        if(e.getView().getTitle().equals("Punishments > Warns > Reasons")) {

            e.setCancelled(true);

            if(slot == 8) {
                PunishmentInventories.openPunishMainInventory(target, p);

            }

            if(slot == 0) {
                reasons.put(p, "Hacking Offenses");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));

                reasons.put(p, "");

            }

            if(slot == 1) {
                reasons.put(p, "Staff/Player Disrespect");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

            }

            if(slot == 2) {
                reasons.put(p, "Unwanted in-game activity");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

            }

            if(slot == 3) {
                reasons.put(p, "Spamming");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

            }

            if(slot == 4) {
                reasons.put(p, "Chat Abuse");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

            }

            if(slot == 5) {
                reasons.put(p, "Bug Abuse");
                Bukkit.dispatchCommand(p, "warn -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

            }

            if(slot == 6) {

                PunishmentAPI.punishType.put(p, "WARN");

                PunishmentAPI.setPlayerTypeReason(target, p);
                return;
            }

        }

        if(e.getView().getTitle().equals("Punishments > Mutes > Durations")) {

            e.setCancelled(true);

            if(slot == 0) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 1h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 1) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 2h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 2) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 12h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 3) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 48h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 4) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 2mo " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 5) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " 6mo " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 6) {
                Bukkit.dispatchCommand(p, "mute -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 8) {
                PunishmentInventories.openPunishMainInventory(target, p);


            }

        }

        if(e.getView().getTitle().equals("Punishments > Bans > Durations")) {

            e.setCancelled(true);

            if(slot == 0) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 1h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 1) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 2h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 2) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 12h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 3) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 48h " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 4) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 2mo " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 5) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " 6mo " + reasons.get(p));
                reasons.put(p, "");
                p.closeInventory();
            }

            if(slot == 6) {
                Bukkit.dispatchCommand(p, "ban -s " + target.getName() + " " + reasons.get(p));
                reasons.put(p, "");

                p.closeInventory();

            }

            if(slot == 8) {
                PunishmentInventories.openPunishMainInventory(target, p);

            }
        }

        if(e.getView().getTitle().equals("Players")) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();

            SkullMeta meta = (SkullMeta) item.getItemMeta();

            Player clicked_player = Bukkit.getPlayer(meta.getOwner());

            p.closeInventory();

            PunishCommand.punishes.put(p, clicked_player);

            PunishmentInventories.openPunishMainInventory(clicked_player, p);

        }


    }

}
