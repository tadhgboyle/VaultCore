/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.chat;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.afk.AFKCommand;
import net.vaultmc.vaultcore.chat.groups.ChatGroup;
import net.vaultmc.vaultcore.chat.groups.ChatGroupsCommand;
import net.vaultmc.vaultcore.chat.staff.AdminChatCommand;
import net.vaultmc.vaultcore.chat.staff.StaffChatCommand;
import net.vaultmc.vaultcore.settings.ChatContext;
import net.vaultmc.vaultcore.settings.PlayerCustomKeys;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatUtils extends ConstructorRegisterListener {
    private static final Map<String, String> colors = new HashMap<>();

    static {
        colors.put("aliceblue", "f0f8ff");
        colors.put("antiquewhite", "faebd7");
        colors.put("aqua", "00ffff");
        colors.put("aquamarine", "7fffd4");
        colors.put("azure", "f0ffff");
        colors.put("beige", "f5f5dc");
        colors.put("bisque", "ffe4c4");
        colors.put("black", "000000");
        colors.put("blanchedalmond", "ffebcd");
        colors.put("blue", "0000ff");
        colors.put("blueviolet", "8a2be2");
        colors.put("brown", "a52a2a");
        colors.put("burlywood", "deb887");
        colors.put("cadetblue", "5f9ea0");
        colors.put("chartreuse", "7fff00");
        colors.put("chocolate", "d2691e");
        colors.put("coral", "ff7f50");
        colors.put("cornflowerblue", "6495ed");
        colors.put("cornsilk", "fff8dc");
        colors.put("crimson", "dc143c");
        colors.put("cyan", "00ffff");
        colors.put("darkblue", "00008b");
        colors.put("darkcyan", "008b8b");
        colors.put("darkgoldenrod", "b8860b");
        colors.put("gray", "a9a9a9");
        colors.put("darkgreen", "006400");
        colors.put("grey", "a9a9a9");
        colors.put("darkkhaki", "bdb76b");
        colors.put("darkmagenta", "8b008b");
        colors.put("darkolivegreen", "556b2f");
        colors.put("darkorange", "ff8c00");
        colors.put("darkorchid", "9932cc");
        colors.put("darkred", "8b0000");
        colors.put("darksalmon", "e9967a");
        colors.put("darkseagreen", "8fbc8f");
        colors.put("darkslateblue", "483d8b");
        colors.put("darkslategray", "2f4f4f");
        colors.put("darkslategrey", "2f4f4f");
        colors.put("darkturquoise", "00ced1");
        colors.put("darkviolet", "9400d3");
        colors.put("deeppink", "ff1493");
        colors.put("deepskyblue", "00bfff");
        colors.put("dimgray", "696969");
        colors.put("dodgerblue", "1e90ff");
        colors.put("firebrick", "b22222");
        colors.put("floralwhite", "fffaf0");
        colors.put("forestgreen", "228b22");
        colors.put("fuchsia", "ff00ff");
        colors.put("gainsboro", "dcdcdc");
        colors.put("ghostwhite", "f8f8ff");
        colors.put("gold", "ffd700");
        colors.put("goldenrod", "daa520");
        colors.put("darkgray", "808080");
        colors.put("green", "008000");
        colors.put("greenyellow", "adff2f");
        colors.put("grey", "808080");
        colors.put("honeydew", "f0fff0");
        colors.put("hotpink", "ff69b4");
        colors.put("indianred", "cd5c5c");
        colors.put("indigo", "4b0082");
        colors.put("ivory", "fffff0");
        colors.put("khaki", "f0e68c");
        colors.put("lavender", "e6e6fa");
        colors.put("lavenderblush", "fff0f5");
        colors.put("lawngreen", "7cfc00");
        colors.put("lemonchiffon", "fffacd");
        colors.put("lightblue", "add8e6");
        colors.put("lightcoral", "f08080");
        colors.put("lightcyan", "e0ffff");
        colors.put("lightgoldenrodyellow", "fafad2");
        colors.put("lightgray", "d3d3d3");
        colors.put("lightgreen", "90ee90");
        colors.put("lightgrey", "d3d3d3");
        colors.put("lightpink", "ffb6c1");
        colors.put("lightsalmon", "ffa07a");
        colors.put("lightseagreen", "20b2aa");
        colors.put("lightskyblue", "87cefa");
        colors.put("lightslategray", "778899");
        colors.put("lightslategrey", "778899");
        colors.put("lightsteelblue", "b0c4de");
        colors.put("lightyellow", "ffffe0");
        colors.put("lime", "00ff00");
        colors.put("limegreen", "32cd32");
        colors.put("linen", "faf0e6");
        colors.put("magenta", "ff00ff");
        colors.put("maroon", "800000");
        colors.put("mediumaquamarine", "66cdaa");
        colors.put("mediumblue", "0000cd");
        colors.put("mediumorchid", "ba55d3");
        colors.put("mediumpurple", "9370db");
        colors.put("mediumseagreen", "3cb371");
        colors.put("mediumslateblue", "7b68ee");
        colors.put("mediumspringgreen", "00fa9a");
        colors.put("mediumturquoise", "48d1cc");
        colors.put("mediumvioletred", "c71585");
        colors.put("midnightblue", "191970");
        colors.put("mintcream", "f5fffa");
        colors.put("mistyrose", "ffe4e1");
        colors.put("moccasin", "ffe4b5");
        colors.put("navajowhite", "ffdead");
        colors.put("navy", "000080");
        colors.put("oldlace", "fdf5e6");
        colors.put("olive", "808000");
        colors.put("olivedrab", "6b8e23");
        colors.put("orange", "ffa500");
        colors.put("orangered", "ff4500");
        colors.put("orchid", "da70d6");
        colors.put("palegoldenrod", "eee8aa");
        colors.put("palegreen", "98fb98");
        colors.put("paleturquoise", "afeeee");
        colors.put("palevioletred", "db7093");
        colors.put("papayawhip", "ffefd5");
        colors.put("peachpuff", "ffdab9");
        colors.put("peru", "cd853f");
        colors.put("pink", "ffc0cb");
        colors.put("plum", "dda0dd");
        colors.put("powderblue", "b0e0e6");
        colors.put("purple", "800080");
        colors.put("rebeccapurple", "663399");
        colors.put("red", "ff0000");
        colors.put("rosybrown", "bc8f8f");
        colors.put("royalblue", "4169e1");
        colors.put("saddlebrown", "8b4513");
        colors.put("salmon", "fa8072");
        colors.put("sandybrown", "f4a460");
        colors.put("seagreen", "2e8b57");
        colors.put("seashell", "fff5ee");
        colors.put("sienna", "a0522d");
        colors.put("silver", "c0c0c0");
        colors.put("skyblue", "87ceeb");
        colors.put("slateblue", "6a5acd");
        colors.put("slategray", "708090");
        colors.put("slategrey", "708090");
        colors.put("snow", "fffafa");
        colors.put("springgreen", "00ff7f");
        colors.put("steelblue", "4682b4");
        colors.put("tan", "d2b48c");
        colors.put("teal", "008080");
        colors.put("thistle", "d8bfd8");
        colors.put("tomato", "ff6347");
        colors.put("turquoise", "40e0d0");
        colors.put("violet", "ee82ee");
        colors.put("wheat", "f5deb3");
        colors.put("white", "ffffff");
        colors.put("whitesmoke", "f5f5f5");
        colors.put("yellow", "ffff00");
        colors.put("yellowgreen", "9acd32");
    }

    public static String translateRGBCodes(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length; i++) {
            try {
                if (b[i] == '&' && b[i + 1] == '#') {
                    String rgb = new String(new char[]{b[i + 2], b[i + 3], b[i + 4], b[i + 5], b[i + 6], b[i + 7]});
                    ChatColor color = ChatColor.of("#" + rgb);
                    textToTranslate = textToTranslate.replace("&#" + rgb, color.toString());
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return textToTranslate;
    }

    public static String translateColorNames(String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length; i++) {
            try {
                if (b[i] == '&' && b[i + 1] == '!') {
                    int x = 2;
                    while (';' != b[x]) {
                        x++;
                        if (x > 25) {
                            break;
                        }
                    }
                    String name = text.substring(i + 2, x).toLowerCase();
                    if (colors.containsKey(name)) {
                        text = text.replace("&!" + name + ";", ChatColor.of("#" + colors.get(name)).toString());
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return text;
    }

    public static void formatChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());

        if (!player.hasPermission(Permissions.RGBImprovesPerformance)) {
            e.setMessage(e.getMessage().replace("&x", ChatColor.COLOR_CHAR + "rx"));
        }

        if (player.hasPermission(Permissions.RGBImprovesPerformance)) {
            e.setMessage(translateColorNames(e.getMessage()));
            e.setMessage(translateRGBCodes(e.getMessage()));
        }

        if (player.hasPermission(Permissions.ChatColor)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        // Staff + Admin chat
        if ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.STAFF_CHAT)) ||
                StaffChatCommand.toggled.contains(player.getUniqueId())) && player.hasPermission(Permissions.StaffChatCommand)) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.STAFF_CHAT), "");
            if (message.length() > 0) {
                StaffChatCommand.chat(player, message);
                Bukkit.getLogger().info("[SC] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }
        if ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.ADMIN_CHAT)) ||
                AdminChatCommand.getToggled().contains(player.getUniqueId())) && player.hasPermission(Permissions.AdminChatCommand)) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.ADMIN_CHAT), "");
            if (message.length() > 0) {
                AdminChatCommand.chat(player, message);
                Bukkit.getLogger().info("[AC] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }
        // Chat Groups
        if (ChatGroup.getChatGroup(player) != null && ((e.getMessage().startsWith(PlayerCustomKeys.getCustomKey(player, ChatContext.CHAT_GROUP)) ||
                ChatGroupsCommand.getToggled().contains(player.getUniqueId())))) {
            String message = e.getMessage().replaceFirst(PlayerCustomKeys.getCustomKey(player, ChatContext.CHAT_GROUP), "");
            if (message.length() > 0) {
                ChatGroup.sendMessage(ChatGroup.getChatGroup(player), player, PlayerSettings.getSetting(player, "settings.grammarly") ? Utilities.grammarly(message) : message);
                Bukkit.getLogger().info("[CG] " + player.getFormattedName() + ": " + e.getMessage());
                e.setCancelled(true);
                return;
            }
        }

        // MuteChat
        if (MuteChatCommand.chatMuted && !player.hasPermission(Permissions.MuteChatCommandOverride)) {
            player.sendMessage(VaultLoader.getMessage("chat.muted"));
            e.setCancelled(true);
            return;
        }

        // Grammarly
        if (PlayerSettings.getSetting(VLPlayer.getPlayer(e.getPlayer()), "settings.grammarly")) {
            e.setMessage(Utilities.grammarly(e.getMessage()));
        }

        e.setFormat(player.getExtraFormattedName() + ChatColor.DARK_GRAY + ":" + ChatColor.RESET + " %2$s");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatShouldFormat(AsyncPlayerChatEvent e) {
        // Let clans handle itself
        if (e.getPlayer().getWorld().getName().contains("clans")) return;
        formatChat(e);
        e.getRecipients().removeIf(player -> IgnoreCommand.isIgnoring(VLPlayer.getPlayer(player), VLPlayer.getPlayer(e.getPlayer())));
        e.getRecipients().removeIf(p -> Tour.getTouringPlayers().contains(p.getUniqueId()));
    }

    // Handle @mentions in chat
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        String[] words = e.getMessage().split(" ");

        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("@") && !words[i].equals("@")) {
                Player referred = Bukkit.getPlayer(words[i].replace("@", ""));
                if (referred == null) {
                    words[i] = ChatColor.WHITE + "@" + words[i].replace("@", "") + ChatColor.RESET;
                    e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-offline"));
                } else {
                    words[i] = ChatColor.YELLOW + "@" + referred.getName() + ChatColor.RESET;
                    if (AFKCommand.getAfk().containsKey(referred)) {
                        e.getPlayer().sendMessage(VaultLoader.getMessage("chat.mention-afk"));
                    }
                    if (PlayerSettings.getSetting(VLPlayer.getPlayer(referred), "settings.mention_notifications"))
                        referred.playSound(referred.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 100, (float) Math.pow(2F, (-6F / 12F)) /* High C */);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : words) {
            sb.append(s).append(" ");
        }
        e.setMessage(sb.toString().trim());
    }
}
