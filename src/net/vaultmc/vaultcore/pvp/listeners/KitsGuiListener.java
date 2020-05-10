package net.vaultmc.vaultcore.pvp.listeners;

import net.vaultmc.vaultcore.pvp.utils.KitGuis;
import net.vaultmc.vaultcore.pvp.utils.KitItems;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitsGuiListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().getWorld().getName().equalsIgnoreCase("PvP")) {
            return;
        }

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Kit Selector")) {
            VLPlayer p = VLPlayer.getPlayer((Player) e.getWhoClicked());
            int slot = e.getSlot();
            double coins = p.getBalance(Bukkit.getWorld("pvp"));
            e.setCancelled(true);
            if (slot == 0) {
                if (!KitGuis.hasKit.get(p).contains("swordsman")) {
                    p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                    KitGuis.hasKit.put(p, "swordsman");
                    p.closeInventory();
                    KitGuis.openKitGui(p);
                    return;
                } else {
                    KitItems.getKitSwordsman(p);
                }
            }

            if (slot == 1) {
                if (!KitGuis.hasKit.get(p).contains("axeman")) {
                    if (coins >= 50) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 50);
                        KitGuis.hasKit.put(p, "axeman");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitAxeman(p);
                }
            }

            if (slot == 2) {
                if (!KitGuis.hasKit.get(p).contains("mage")) {
                    if (coins >= 85) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 85);
                        KitGuis.hasKit.put(p, "mage");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitMage(p);
                }
            }

            if (slot == 3) {
                if (!KitGuis.hasKit.get(p).contains("archer")) {
                    if (coins >= 120) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 120);
                        KitGuis.hasKit.put(p, "archer");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitArcher(p);
                }
            }

            if (slot == 4) {
                if (!KitGuis.hasKit.get(p).contains("healer")) {
                    if (coins >= 200) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 200);
                        KitGuis.hasKit.put(p, "healer");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitHealer(p);
                }

            }

            if (slot == 5) {
                if (!KitGuis.hasKit.get(p).contains("armorer")) {
                    if (coins >= 250) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 250);
                        KitGuis.hasKit.put(p, "armorer");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitArmorer(p);
                }
            }

            if (slot == 6) {
                if (!KitGuis.hasKit.get(p).contains("warlord")) {
                    if (coins >= 400) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 400);
                        KitGuis.hasKit.put(p, "warlord");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitWarlord(p);
                }
            }

            if (slot == 7) {
                if (!KitGuis.hasKit.get(p).contains("overlord")) {
                    if (coins >= 550) {
                        p.sendMessage(ChatColor.GREEN + "Successfully purchased kit!");
                        p.withdraw(Bukkit.getWorld("pvp"), 550);
                        KitGuis.hasKit.put(p, "overlord");
                        p.closeInventory();
                        KitGuis.openKitGui(p);

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have enough coins!");
                    }
                    return;
                } else {
                    KitItems.getKitOverlord(p);
                }
            }

            if (slot == 8) {
                p.closeInventory();
            }
        }
    }

}
