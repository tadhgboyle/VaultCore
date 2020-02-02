package net.vaultmc.vaultcore.grant;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GrantCommandListener implements Listener {
    private static final String INVTITLE = ChatColor.DARK_GRAY + "Grant Rank to ";

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());

        if (e.getView().getTitle().startsWith(INVTITLE)) {
            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
            } else {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String rank = "Patreon";
                    String itemName = e.getCurrentItem().getItemMeta().getDisplayName();

                    if (itemName.equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Member"))
                        rank = "Member";
                    else if (itemName.equals(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon"))
                        rank = "Patreon";
                    else if (itemName.equals(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted"))
                        rank = "Trusted";
                    else if (itemName.equals(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Moderator"))
                        rank = "Moderator";
                    else if (itemName.equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Admin"))
                        rank = "Admin";
                    else if (itemName.equals(ChatColor.RED + "No Permission")) {
                        player.closeInventory();
                        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.grant.no_permission"));
                        return;
                    }
                    Player target = Bukkit.getPlayer(e.getView().getTitle().substring(INVTITLE.length()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "lp user " + target.getName() + " parent set " + rank);

                    player.closeInventory();

                    player.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.grant.updated_sender"),
                                    target.getDisplayName(), rank));
                    target.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.grant.updated_player"),
                                    rank, player.getFormattedName()));
                }
            }
        }
    }
}