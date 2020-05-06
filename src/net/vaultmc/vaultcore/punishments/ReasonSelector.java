package net.vaultmc.vaultcore.punishments;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReasonSelector implements Listener {
    private static final Inventory punish = Bukkit.createInventory(null, 54, ChatColor.RESET + "Ban / Mute / Kick");
    private static final Map<UUID, CallbackData> callbacks = new HashMap<>();

    static {
        punish.addItem(new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.YELLOW + "Political Discussion")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (3):",
                        ChatColor.YELLOW + " Political discussion of any form,",
                        ChatColor.YELLOW + " even indirectly is strictly forbidden.",
                        "",
                        ChatColor.RED + "Ban: 2 months"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.RAIL)
                .name(ChatColor.YELLOW + "Exploiting server/client bugs")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (4):",
                        ChatColor.YELLOW + " If you found a bug on the server,",
                        ChatColor.YELLOW + " do not exploit the bug.",
                        ChatColor.YELLOW + "Also apply: spreading the method",
                        ChatColor.YELLOW + "to exploit a bug, except when using buggy.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.RAIL)
                .name(ChatColor.YELLOW + "Exploiting server/client bugs")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (4):",
                        ChatColor.YELLOW + " If you found a bug on the server,",
                        ChatColor.YELLOW + " do not exploit the bug.",
                        ChatColor.YELLOW + "Also apply: spreading the method",
                        ChatColor.YELLOW + "to exploit a bug, except when using buggy.",
                        "",
                        ChatColor.GREEN + "2nd Offense",
                        ChatColor.RED + "Ban: " + ChatColor.RED + "1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.RAIL)
                .name(ChatColor.YELLOW + "Exploiting server/client bugs")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (4):",
                        ChatColor.YELLOW + " If you found a bug on the server,",
                        ChatColor.YELLOW + " do not exploit the bug.",
                        ChatColor.YELLOW + "Also apply: spreading the method",
                        ChatColor.YELLOW + "to exploit a bug, except when using buggy.",
                        "",
                        ChatColor.GREEN + "3rd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Doxing / Invading Privacy")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (5):",
                        ChatColor.YELLOW + " Every player has the right",
                        ChatColor.YELLOW + " not to have their privacy ",
                        ChatColor.YELLOW + " invaded by others, even indirectly.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.YELLOW + "Mute: " + ChatColor.RED + "1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Doxing / Invading Privacy")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (5):",
                        ChatColor.YELLOW + " Every player has the right",
                        ChatColor.YELLOW + " not to have their privacy ",
                        ChatColor.YELLOW + " invaded by others, even indirectly.",
                        "",
                        ChatColor.YELLOW + "2nd Offense",
                        ChatColor.RED + "Ban: 1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Doxing / Invading Privacy")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (5):",
                        ChatColor.YELLOW + " Every player has the right",
                        ChatColor.YELLOW + " not to have their privacy ",
                        ChatColor.YELLOW + " invaded by others, even indirectly.",
                        "",
                        ChatColor.RED + "3rd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.YELLOW + "Client side unfair modifications (Hacking)")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (6):",
                        ChatColor.YELLOW + " Every player has the right to ",
                        ChatColor.YELLOW + " play in a fair and friendly environment.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.RED + "Ban: 1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.YELLOW + "Client side unfair modifications (Hacking)")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (6):",
                        ChatColor.YELLOW + " Every player has the right to ",
                        ChatColor.YELLOW + " play in a fair and friendly environment.",
                        "",
                        ChatColor.YELLOW + "2nd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Spamming / Disrespectful")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (6):",
                        ChatColor.YELLOW + " Every player has the right to ",
                        ChatColor.YELLOW + " play in a fair and friendly environment.",
                        "",
                        ChatColor.YELLOW + "Also apply: Swearing disrespectfully, ",
                        ChatColor.YELLOW + "eXcEsSiVe capitalization.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.YELLOW + "Mute: 7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Spamming / Disrespectful")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (6):",
                        ChatColor.YELLOW + " Every player has the right to ",
                        ChatColor.YELLOW + " play in a fair and friendly environment.",
                        "",
                        ChatColor.YELLOW + "Also apply: Swearing disrespectfully, ",
                        ChatColor.YELLOW + "eXcEsSiVe capitalization.",
                        "",
                        ChatColor.YELLOW + "2nd Offense",
                        ChatColor.YELLOW + "Mute: 1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Spamming / Disrespectful")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (6):",
                        ChatColor.YELLOW + " Every player has the right to ",
                        ChatColor.YELLOW + " play in a fair and friendly environment.",
                        "",
                        ChatColor.YELLOW + "Also apply: Swearing disrespectfully, ",
                        ChatColor.YELLOW + "eXcEsSiVe capitalization.",
                        "",
                        ChatColor.RED + "3rd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PLAYER_HEAD)
                .name(ChatColor.YELLOW + "Ban Evading")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (7):",
                        ChatColor.YELLOW + " (...) However, ban evading using",
                        ChatColor.YELLOW + " an alt is disallowed.",
                        "",
                        ChatColor.YELLOW + "Applies for both main and alt account.",
                        ChatColor.YELLOW + "Main account needs to be unbanned first.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.POTION)
                .name(ChatColor.YELLOW + "Slowing the server down")
                .potion(new PotionData(PotionType.SLOWNESS, false, false), Color.GRAY)
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (8):",
                        ChatColor.YELLOW + " Do not slow the server down",
                        ChatColor.YELLOW + " in any way.",
                        "",
                        ChatColor.YELLOW + "Machine / clock or the like should be",
                        ChatColor.YELLOW + "removed.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.YELLOW + "Kick"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.POTION)
                .name(ChatColor.YELLOW + "Slowing the server down")
                .potion(new PotionData(PotionType.SLOWNESS, false, false), Color.GRAY)
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (8):",
                        ChatColor.YELLOW + " Do not slow the server down",
                        ChatColor.YELLOW + " in any way.",
                        "",
                        ChatColor.YELLOW + "Machine / clock or the like should be",
                        ChatColor.YELLOW + "removed.",
                        "",
                        ChatColor.YELLOW + "2nd Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.POTION)
                .name(ChatColor.YELLOW + "Slowing the server down")
                .potion(new PotionData(PotionType.SLOWNESS, false, false), Color.GRAY)
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (8):",
                        ChatColor.YELLOW + " Do not slow the server down",
                        ChatColor.YELLOW + " in any way.",
                        "",
                        ChatColor.YELLOW + "Machine / clock or the like should be",
                        ChatColor.YELLOW + "removed.",
                        "",
                        ChatColor.RED + "3rd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.TNT)
                .name(ChatColor.YELLOW + "Griefing")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (9):",
                        ChatColor.YELLOW + " Griefing is strictly forbidden.",
                        "",
                        ChatColor.YELLOW + "Loss should be rolled back.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.TNT)
                .name(ChatColor.YELLOW + "Griefing")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (9):",
                        ChatColor.YELLOW + " Griefing is strictly forbidden.",
                        "",
                        ChatColor.YELLOW + "Loss should be rolled back.",
                        "",
                        ChatColor.YELLOW + "2nd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PLAYER_HEAD)
                .name(ChatColor.YELLOW + "Talking in non-English")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (11):",
                        ChatColor.YELLOW + " Please refrain from talking",
                        ChatColor.YELLOW + " in languages other than English",
                        ChatColor.YELLOW + " in public chat.",
                        "",
                        ChatColor.YELLOW + "Kick"
                ))
                .skullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RlN2I4YmFmNzg1ODdiNzQ0MjdlYjBjNzMxMzc0N2Y1OGM0MTgzMDVhMTQ3Mjc4Yjc3MzE1YTljOTdmZDkifX19")
                .build());
        punish.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS)
                .name(ChatColor.YELLOW + "NSFW / Shocking Content")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (12):",
                        ChatColor.YELLOW + " NSFW and shocking contents ",
                        ChatColor.YELLOW + " (even if referred indirectly)",
                        ChatColor.YELLOW + " are strictly forbidden.",
                        "",
                        ChatColor.YELLOW + "Includes NSFW buildings.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"  // Not going to change this punishment
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.HOPPER)
                .name(ChatColor.YELLOW + "Advertising on/for VaultMC")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (13):",
                        ChatColor.YELLOW + " Advertising on VaultMC is disallowed.",
                        ChatColor.YELLOW + " Unless explicitly allowed by an ",
                        ChatColor.YELLOW + " administrator, advertising for VaultMC to",
                        ChatColor.YELLOW + " a large group of people (35+) is disallowed.",
                        "",
                        ChatColor.YELLOW + "This does not apply when you're asking",
                        ChatColor.YELLOW + "your friends to join.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.HOPPER)
                .name(ChatColor.YELLOW + "Advertising on/for VaultMC")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (13):",
                        ChatColor.YELLOW + " Advertising on VaultMC is disallowed.",
                        ChatColor.YELLOW + " Unless explicitly allowed by an ",
                        ChatColor.YELLOW + " administrator, advertising for VaultMC to",
                        ChatColor.YELLOW + " a large group of people (35+) is disallowed.",
                        "",
                        ChatColor.YELLOW + "This does not apply when you're asking",
                        ChatColor.YELLOW + "your friends to join.",
                        "",
                        ChatColor.YELLOW + "2nd Offense (or more)",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WOODEN_AXE)
                .name(ChatColor.YELLOW + "Creating Inappropriate Builds")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (14):",
                        ChatColor.YELLOW + " Do not create inappropriate builds.",
                        "",
                        ChatColor.YELLOW + "If the build is NSFW, it should be punished",
                        ChatColor.YELLOW + "the same as spreading NSFW content.",
                        "",
                        ChatColor.GREEN + "1st Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WOODEN_AXE)
                .name(ChatColor.YELLOW + "Creating Inappropriate Builds")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (14):",
                        ChatColor.YELLOW + " Do not create inappropriate builds.",
                        "",
                        ChatColor.YELLOW + "If the build is NSFW, it should be punished",
                        ChatColor.YELLOW + "the same as spreading NSFW content.",
                        "",
                        ChatColor.YELLOW + "2nd Offense",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "15 days"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.WOODEN_AXE)
                .name(ChatColor.YELLOW + "Creating Inappropriate Builds")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (14):",
                        ChatColor.YELLOW + " Do not create inappropriate builds.",
                        "",
                        ChatColor.YELLOW + "If the build is NSFW, it should be punished",
                        ChatColor.YELLOW + "the same as spreading NSFW content.",
                        "",
                        ChatColor.RED + "3rd Offense (or more)",
                        ChatColor.RED + "Ban: 1 month"
                ))
                .build());
        punish.addItem(new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Wasting others' time")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "Rules (16):",
                        ChatColor.YELLOW + " Do not waste anybody's time.",
                        "",
                        ChatColor.YELLOW + "This includes creating false reports.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.GREEN + "12 hours"
                ))
                .build());
        punish.setItem(27, new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.YELLOW + "Other Ban")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are banned.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.GREEN + "1 day"
                ))
                .build());
        punish.setItem(28, new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.YELLOW + "Other Ban")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are banned.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.setItem(29, new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.YELLOW + "Other Ban")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are banned.",
                        "",
                        ChatColor.RED + "Ban: 1 month"
                ))
                .build());
        punish.setItem(30, new ItemStackBuilder(Material.DIAMOND_AXE)
                .name(ChatColor.YELLOW + "Other Ban")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are banned.",
                        "",
                        ChatColor.RED + "Ban: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.setItem(36, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Other Mute")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are muted.",
                        "",
                        ChatColor.YELLOW + "Mute: " + ChatColor.GREEN + "1 day"
                ))
                .build());
        punish.setItem(37, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Other Mute")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are muted.",
                        "",
                        ChatColor.YELLOW + "Mute: " + ChatColor.YELLOW + "7 days"
                ))
                .build());
        punish.setItem(38, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Other Mute")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are muted.",
                        "",
                        ChatColor.YELLOW + "Mute: " + ChatColor.RED + "1 month"
                ))
                .build());
        punish.setItem(39, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(ChatColor.YELLOW + "Other Mute")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are muted.",
                        "",
                        ChatColor.YELLOW + "Mute: " + ChatColor.DARK_RED + "Permanent"
                ))
                .build());
        punish.setItem(45, new ItemStackBuilder(Material.SPRUCE_DOOR)
                .name(ChatColor.YELLOW + "Other Kick")
                .lore(Arrays.asList(
                        ChatColor.YELLOW + "You are required to inform the player",
                        ChatColor.YELLOW + "of why they are kicked.",
                        "",
                        ChatColor.YELLOW + "Kick"
                ))
                .build());
    }

    public static void start(VLPlayer from, VLOfflinePlayer to, TriConsumer<String, Long, Type> callback) {
        callbacks.put(from.getUniqueId(), new CallbackData(from.getUniqueId(), to.getUniqueId(), callback));
        from.openInventory(punish);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Ban / Mute / Kick")) {
            CallbackData data = callbacks.remove(e.getWhoClicked().getUniqueId());
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            if (data == null) {
                return;
            }
            switch (e.getSlot()) {
                case 0:
                    data.callback.accept("Political Discussion", 5184000L, Type.BAN);
                    break;
                case 1:
                    data.callback.accept("Exploiting server/client bugs (1st Offense)", 604800L, Type.BAN);
                    break;
                case 2:
                    data.callback.accept("Exploiting server/client bugs (2nd Offense)", 2592000L, Type.BAN);
                    break;
                case 3:
                    data.callback.accept("Exploiting server/client bugs (3rd Offense)", -1L, Type.BAN);
                    break;
                case 4:
                    data.callback.accept("Doxing / Invading Privacy (1st Offense)", 2592000L, Type.MUTE);
                    break;
                case 5:
                    data.callback.accept("Doxing / Invading Privacy (2nd Offense)", 2592000L, Type.BAN);
                    break;
                case 6:
                    data.callback.accept("Doxing / Invading Privacy (3rd Offense)", -1L, Type.BAN);
                    break;
                case 7:
                    data.callback.accept("Client side unfair modifications (Hacking) (1st Offense)", 2592000L, Type.BAN);
                    break;
                case 8:
                    data.callback.accept("Client side unfair modifications (Hacking) (2nd Offense)", -1L, Type.BAN);
                    break;
                case 9:
                    data.callback.accept("Spamming / Disrespectful (1st Offense)", 604800L, Type.MUTE);
                    break;
                case 10:
                    data.callback.accept("Spamming / Disrespectful (2nd Offense)", 2592000L, Type.MUTE);
                    break;
                case 11:
                    data.callback.accept("Spamming / Disrespectful (3rd Offense)", 604800L, Type.BAN);
                    break;
                case 12:
                    data.callback.accept("Ban Evading", -1L, Type.BAN);
                    break;
                case 13:
                    data.callback.accept("Slowing the server down (1st Offense)", -1L, Type.KICK);
                    break;
                case 14:
                    data.callback.accept("Slowing the server down (2nd Offense)", 604800L, Type.BAN);
                    break;
                case 15:
                    data.callback.accept("Slowing the server down (3rd Offense)", 2592000L, Type.BAN);
                    break;
                case 16:
                    data.callback.accept("Griefing (1st Offense)", 2592000L, Type.BAN);
                    break;
                case 17:
                    data.callback.accept("Griefing (2nd Offense)", -1L, Type.BAN);
                    break;
                case 18:
                    data.callback.accept("Talking in non-English", -1L, Type.KICK);
                    break;
                case 19:
                    data.callback.accept("NSFW / Shocking Content", -1L, Type.BAN);
                    break;
                case 20:
                    data.callback.accept("Advertising on/for VaultMC (1st Offense)", 604800L, Type.BAN);
                    break;
                case 21:
                    data.callback.accept("Advertising on/for VaultMC (2nd Offense)", 2592000L, Type.BAN);
                    break;
                case 22:
                    data.callback.accept("Creating Inappropriate Builds (1st Offense)", 604800L, Type.BAN);
                    break;
                case 23:
                    data.callback.accept("Creating Inappropriate Builds (2nd Offense)", 1296000L, Type.BAN);
                    break;
                case 24:
                    data.callback.accept("Creating Inappropriate Builds (3rd Offense)", 2592000L, Type.BAN);
                    break;
                case 25:
                    data.callback.accept("Wasting Others' Time", 43200L, Type.BAN);
                    break;
                case 27:
                    data.callback.accept("Other Ban (You should be informed of the reason)", 86400L, Type.BAN);
                    break;
                case 28:
                    data.callback.accept("Other Ban (You should be informed of the reason)", 604800L, Type.BAN);
                    break;
                case 29:
                    data.callback.accept("Other Ban (You should be informed of the reason)", 2592000L, Type.BAN);
                    break;
                case 30:
                    data.callback.accept("Other Ban (You should be informed of the reason)", -1L, Type.BAN);
                    break;
                case 36:
                    data.callback.accept("Other Mute (You should be informed of the reason)", 86400L, Type.MUTE);
                    break;
                case 37:
                    data.callback.accept("Other Mute (You should be informed of the reason)", 604800L, Type.MUTE);
                    break;
                case 38:
                    data.callback.accept("Other Mute (You should be informed of the reason)", 2592000L, Type.MUTE);
                    break;
                case 39:
                    data.callback.accept("Other Mute (You should be informed of the reason)", -1L, Type.MUTE);
                    break;
                case 45:
                    data.callback.accept("Other Kick (You should be informed of the reason)", -1L, Type.KICK);
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getUniqueId().toString().equals("f78a4d8d-d51b-4b39-98a3-230f2de0c670")) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You couldn't join. You are the console and is being used in the server.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        callbacks.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Ban / Mute / Kick")) {
            callbacks.remove(e.getPlayer().getUniqueId());
        }
    }

    public enum Type {
        BAN,
        MUTE,
        KICK
    }

    @AllArgsConstructor
    @Data
    public static class Reason {
        private String reason;
        private long length;
        private Type type;
    }

    @AllArgsConstructor
    @Data
    public static class CallbackData {
        private UUID from;
        private UUID to;
        private TriConsumer<String, Long, Type> callback;
    }
}
