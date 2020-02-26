package net.vaultmc.vaultcore.combat;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LegacyCombat extends ConstructorRegisterListener implements Runnable {
    private static final Map<Material, Double> axe = new HashMap<>();
    private static final Map<UUID, ItemStack> offHandItem = new HashMap<>();
    private static final List<Material> interactive = Arrays.asList(
            Material.DISPENSER,
            Material.TNT,
            Material.CHEST,
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.STONE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.BIRCH_BUTTON,
            Material.OAK_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.ACACIA_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.OAK_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.OAK_DOOR,
            Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR,
            Material.SPRUCE_DOOR,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.OAK_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.ENCHANTING_TABLE,
            Material.END_PORTAL_FRAME,
            Material.BOOKSHELF,
            Material.ENDER_CHEST,
            Material.BEACON,
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL,
            Material.TRAPPED_CHEST,
            Material.HOPPER,
            Material.DROPPER,
            Material.IRON_TRAPDOOR,
            Material.IRON_DOOR,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.ACACIA_SIGN,
            Material.BIRCH_SIGN,
            Material.OAK_SIGN,
            Material.DARK_OAK_SIGN,
            Material.JUNGLE_SIGN,
            Material.SPRUCE_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.OAK_WALL_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.LEVER,
            Material.BREWING_STAND,
            Material.LOOM,
            Material.BARREL,
            Material.SMOKER,
            Material.BLAST_FURNACE,
            Material.CARTOGRAPHY_TABLE,
            Material.FLETCHING_TABLE,
            Material.GRINDSTONE,
            Material.LECTERN,
            Material.SMITHING_TABLE,
            Material.STONECUTTER,
            Material.BELL,
            Material.BEE_NEST,
            Material.BEEHIVE,
            Material.CAMPFIRE
    );
    private static final ItemStack SHIELD = new ItemStackBuilder(Material.SHIELD)
            .unbreakable(true)
            .build();

    public LegacyCombat() {
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this, 20, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (offHandItem.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().setItemInOffHand(offHandItem.get(e.getPlayer().getUniqueId()));
            offHandItem.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getSlot() == 40) {
            e.setCancelled(true);
        }
    }

    static {
        axe.put(Material.DIAMOND_AXE, 6D);
        axe.put(Material.IRON_AXE, 5D);
        axe.put(Material.GOLDEN_AXE, 3D);
        axe.put(Material.STONE_AXE, 4D);
        axe.put(Material.WOODEN_AXE, 3D);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        if (e.getRecipe().getResult().getType() == Material.SHIELD) e.setCancelled(true);
    }

    @EventHandler
    public void legacyCooldown(PlayerJoinEvent e) {
        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().endsWith("SWORD") && (
                e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && !e.getPlayer().isBlocking()) {
            if (e.getClickedBlock() != null && interactive.contains(e.getClickedBlock().getType())) {
                return;
            }
            e.setCancelled(true);
            offHandItem.put(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getItemInOffHand());
            e.getPlayer().getInventory().setItemInOffHand(SHIELD);
            e.getPlayer().setShieldBlockingDelay(0);
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = player.getInventory();
            if (inv.getItemInOffHand().getType() == Material.SHIELD && !player.isBlocking()) {
                inv.setItemInOffHand(offHandItem.get(player.getUniqueId()));
                offHandItem.remove(player.getUniqueId());
            }

            for (int i = 0; i < inv.getContents().length; i++) {
                ItemStack stack = inv.getContents()[i];
                if (stack == null) continue;
                ItemMeta meta = stack.getItemMeta();
                if (stack.getType().toString().endsWith("AXE") && meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE) == null) {
                    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                            UUID.randomUUID(), "Attack+", axe.get(stack.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                }
                stack.setItemMeta(meta);
            }
        }
    }
}
