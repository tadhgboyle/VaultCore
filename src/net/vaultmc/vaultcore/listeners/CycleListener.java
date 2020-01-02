package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.VaultCore;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CycleListener implements Listener {

	@EventHandler
	public void onInventoryCycle(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();

		if ((VaultCore.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".settings.cycle"))
				&& player.getGameMode().equals(GameMode.CREATIVE)
				&& player.getWorld().getName().equalsIgnoreCase("Creative") && !player.isSneaking()) {

			int prev_slot = event.getPreviousSlot();
			int new_slot = event.getNewSlot();

			if (prev_slot == 0 && new_slot == 8) {
				shift(player, false);
			} else if (prev_slot == 8 && new_slot == 0) {
				shift(player, true);
			}
		} else {
			return;
		}
	}

	private void shift(Player player, boolean down) {

		Inventory inv = player.getInventory();
		ItemStack[] items = inv.getStorageContents();

		int shift = down ? -9 : 9;
		shift = (shift + items.length) % items.length;

		for (int i = 0; i < 4; i++) {
			items = join(subset(items, shift, items.length), subset(items, 0, shift));

			ItemStack[] hotbar = subset(items, 0, 9);
			boolean found = false;

			for (ItemStack item : hotbar)
				if (item != null) {
					found = true;
					break;
				}

			if (found)
				break;
		}
		inv.setStorageContents(items);
	}

	private ItemStack[] subset(ItemStack[] items, int start, int end) {
		ItemStack[] result = new ItemStack[end - start];

		for (int i = start; i < end; i++) {
			result[i - start] = items[i];
		}
		return result;
	}

	private ItemStack[] join(ItemStack[] items1, ItemStack[] items2) {
		ItemStack[] result = new ItemStack[items1.length + items2.length];

		for (int i = 0; i < items1.length; i++)
			result[i] = items1[i];
		int offset = items1.length;
		for (int i = 0; i < items2.length; i++)
			result[i + offset] = items2[i];

		return result;
	}
}