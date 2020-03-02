package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SignHandler implements Listener {
    private static final ItemStack sign = new ItemStack(Material.OAK_SIGN);

    @EventHandler
    public void signChange(SignChangeEvent e) {
        if (!e.getPlayer().hasPermission(Permissions.ChatColor)) return;
        String[] lines = e.getLines();
        for (int n = 0; n <= 3; n++)
            e.setLine(n, ChatColor.translateAlternateColorCodes('&', lines[n]));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void editSign(PlayerInteractEvent e) {
        if (e.isCancelled()) return;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = e.getClickedBlock();
        if (block.getType().toString().endsWith("_SIGN")) {
            BlockData data = block.getBlockData().clone();
            BlockBreakEvent break_ = new BlockBreakEvent(block, e.getPlayer());
            Bukkit.getPluginManager().callEvent(break_);
            if (break_.isCancelled()) return;
            BlockPlaceEvent place_ = new BlockPlaceEvent(block, block.getState(), block.getRelative(0, -1, 0),
                    sign, e.getPlayer(), true, EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(place_);
            if (place_.isCancelled()) return;

            block.setBlockData(data, true);
            e.getPlayer().openSign((Sign) block.getState());
        }
    }
}