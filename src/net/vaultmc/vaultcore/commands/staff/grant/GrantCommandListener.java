package net.vaultmc.vaultcore.commands.staff.grant;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.VaultCoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GrantCommandListener implements Listener {

	private static final String INVTITLE = ChatColor.DARK_GRAY + "Grant Rank to " + ChatColor.WHITE + ""
			+ ChatColor.ITALIC;

	@EventHandler
	public void onPlayerClick(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

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
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this rank.");
                        return;
                    }
                    Player target = Bukkit.getServer().getPlayer(e.getView().getTitle().substring(INVTITLE.length()));
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "lp user " + target.getName() + " parent set " + rank);

                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + "Successfully updated " + ChatColor.GOLD
                            + e.getView().getTitle().substring(INVTITLE.length()) + ChatColor.YELLOW + "'s rank to "
                            + ChatColor.GOLD + rank + ChatColor.YELLOW + ".");
                    target.sendMessage(ChatColor.YELLOW + "Your rank has been updated to " + ChatColor.GOLD + rank
                            + ChatColor.YELLOW + " by " + VaultCoreAPI.getName(player) + ChatColor.YELLOW + ".");
                }
			}
		}
	}
}