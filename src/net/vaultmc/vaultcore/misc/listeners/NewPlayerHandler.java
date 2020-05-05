package net.vaultmc.vaultcore.misc.listeners;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;

public class NewPlayerHandler {

    private static final ItemStack book = VaultCore.getInstance().getData().getItemStack("book");

    @SneakyThrows
    public static String count() {
        String total_players = null;
        ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT COUNT(uuid) FROM players");
        while (rs.next()) {
            total_players = rs.getString(1);
        }
        return total_players;
    }

    public void newPlayer(VLPlayer player) {
        // Settings
        player.getPlayerData().set("settings.msg", true);
        player.getPlayerData().set("settings.tpa", true);
        player.getPlayerData().set("settings.autotpa", false);
        player.getPlayerData().set("settings.cycle", false);
        player.getPlayerData().set("settings.minimal_messages", false);
        player.getPlayerData().set("settings.minimal_caps", false);
        player.getPlayerData().set("settings.item_drops", true);
        player.getPlayerData().set("settings.grammarly", false);
        player.getPlayerData().set("settings.notifications", true);
        player.getPlayerData().set("settings.mention_notifications", true);
        player.getPlayerData().set("settings.check_mail_join", true);
        // Misc ("0, 0" = empty string)
        player.getPlayerData().set("donation", "0.00");
        player.getPlayerData().set("ignored", "0, 0");
        player.getPlayerData().set("refferals", 0);
        player.getPlayerData().set("refferal_used", false);
        player.getPlayerData().set("nickname", "0, 0");
        player.saveData();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(VLPlayer.getPlayer(players), "settings.minimal_messages")) continue;
            players.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.new_player"),
                            player.getFormattedName(), count()));
        }
        Bukkit.getPlayer(player.getUniqueId()).openBook(book);

    }
}
