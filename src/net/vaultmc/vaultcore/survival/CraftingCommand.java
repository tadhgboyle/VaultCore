package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.survival.item.Item;
import net.vaultmc.vaultcore.survival.item.recipe.ShapedRecipe;
import net.vaultmc.vaultcore.survival.item.recipe.ShapelessRecipe;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@RootCommand(
        literal = "crafting",
        description = "Check custom crafting recipes."
)
@PlayerOnly
public class CraftingCommand extends CommandExecutor implements Listener {
    private static final ItemStack previous = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.YELLOW + "Previous")
            .build();
    private static final ItemStack next = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.YELLOW + "Next")
            .build();

    public CraftingCommand() {
        register("crafting", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("crafting")
    public void crafting(VLPlayer sender) {
        if (!sender.getWorld().getName().toLowerCase().contains("survival")) {
            return;
        }
        craftingPaged(sender, 0);
    }

    private static final ItemStack arrow = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.YELLOW + "Crafts Into")
            .build();
    private static final ItemStack close = new ItemStackBuilder(Material.BARRIER)
            .name(ChatColor.RED + "Close")
            .build();

    public void recipe(VLPlayer sender, Item item) {
        Inventory inv = Bukkit.createInventory(null, 27, "Recipe");
        if (item.getRecipe() instanceof ShapedRecipe) {
            ItemStack[] recipe = ((ShapedRecipe) item.getRecipe()).getRecipe();
            inv.setItem(1, recipe[0]);
            inv.setItem(2, recipe[1]);
            inv.setItem(3, recipe[2]);
            inv.setItem(10, recipe[3]);
            inv.setItem(11, recipe[4]);
            inv.setItem(12, recipe[5]);
            inv.setItem(19, recipe[6]);
            inv.setItem(20, recipe[7]);
            inv.setItem(21, recipe[8]);
        } else if (item.getRecipe() instanceof ShapelessRecipe) {
            ItemStack[] recipe = ((ShapelessRecipe) item.getRecipe()).getIngredients();
            for (int i = 0; i < 9; i++) {
                if (i > recipe.length - 1) break;
                if (i <= 2) {
                    inv.setItem(1 + i, recipe[i]);
                } else if (i <= 5) {
                    inv.setItem(7 + i, recipe[i]);
                } else {
                    inv.setItem(13 + i, recipe[i]);
                }
            }
        }
        inv.setItem(14, arrow);
        inv.setItem(16, item.getItem());
        inv.setItem(53, close);
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() instanceof CraftingInventory && e.getView().getTitle().equals("Recipe")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> crafting(VLPlayer.getPlayer((Player) e.getPlayer())));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith(ChatColor.RESET + "Crafting Recipes") && !(e.getClickedInventory() instanceof PlayerInventory)) {
            e.setCancelled(true);
            int page = Integer.parseInt(e.getView().getTitle().split("\\(")[1].split("/")[0]);
            if (e.getCurrentItem() == null) return;
            if (e.getSlot() == 47 && e.getInventory().getItem(47) != null) {
                e.getWhoClicked().closeInventory();
                craftingPaged(VLPlayer.getPlayer((Player) e.getWhoClicked()), page - 2);
                return;
            } else if (e.getSlot() == 51 && e.getInventory().getItem(51) != null) {
                e.getWhoClicked().closeInventory();
                craftingPaged(VLPlayer.getPlayer((Player) e.getWhoClicked()), page);
                return;
            }
            Item item = Item.getBy(e.getCurrentItem());
            if (item != null) {
                e.getWhoClicked().closeInventory();
                recipe(VLPlayer.getPlayer((Player) e.getWhoClicked()), item);
            }
        } else if (e.getView().getTitle().startsWith(ChatColor.RESET + "Recipe")) {
            e.setCancelled(true);
            if (e.getSlot() == 53) {
                e.getWhoClicked().closeInventory();
                crafting(VLPlayer.getPlayer((Player) e.getWhoClicked()));
            }
        }
    }

    public void craftingPaged(VLPlayer sender, int page) {
        int pages = (int) Math.round(Math.ceil(Item.getItems().size() / 36D));
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RESET + "Crafting Recipes (" + (page + 1) + "/" + pages + ")");
        Iterator<Map.Entry<String, Item>> it = Item.getItems().entrySet().stream().filter(item -> item.getValue().getRecipe() != null).collect(Collectors.toList()).iterator();
        for (int i = page * 36; i < page * 36 + 35; i++) {
            if (!it.hasNext()) break;
            Item item = it.next().getValue();
            inv.addItem(item.getItem());
        }

        if (page != 0) {
            inv.setItem(47, previous);
        }

        if (page != pages - 1) {
            inv.setItem(51, next);
        }
        sender.openInventory(inv);
    }
}
