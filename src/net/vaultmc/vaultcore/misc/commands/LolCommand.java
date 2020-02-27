package net.vaultmc.vaultcore.misc.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(literal = "lol", description = "The chat based soundboard.")
@Permission(Permissions.LolCommand)
@Aliases({"lmao", "lul"})
public class LolCommand extends CommandExecutor {

    public static File lolFile = new File(VaultCore.getInstance().getDataFolder() + "/lols.json");
    private static JsonArray lolsList;
    private static FileWriter updatedLols;
    private static long LAST_LOL = 0;
    private static long COOLDOWN = 15000;
    private static int PAGE_SIZE = 7;

    JsonParser parser = new JsonParser();
    Object obj;

    {
        try {
            obj = parser.parse(new FileReader(lolFile));
            JsonObject object = (JsonObject) obj;
            lolsList = object.getAsJsonArray("lols");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LolCommand() {
        register("lol", Collections.emptyList());
        register("lolId", Collections.singletonList(Arguments.createArgument("id", Arguments.integerArgument())));
        register("lolList", Collections.singletonList(Arguments.createLiteral("list")));
        register("lolListPage",
                Arrays.asList(Arguments.createLiteral("list"), Arguments.createArgument("page", Arguments.integerArgument(1))));
        register("lolAdd",
                Arrays.asList(Arguments.createLiteral("add"), Arguments.createArgument("text", Arguments.greedyString())));
        register("lolDelete",
                Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("id", Arguments.integerArgument())));
    }

    @SubCommand("lol")
    public static void lol(VLCommandSender sender) {
        if (System.currentTimeMillis() - LAST_LOL > COOLDOWN || LAST_LOL == 0) {
            int id = ThreadLocalRandom.current().nextInt(0, lolsList.size());
            String victim = ((VLPlayer) VLPlayer.getOnlinePlayers().toArray()[new Random().nextInt(VLPlayer.getOnlinePlayers().size())]).getName();
            for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
                players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.format"), sender.getFormattedName(), ChatColor.translateAlternateColorCodes('&', String.valueOf(lolsList.get(id)))).replace("{PLAYER}", victim));
            }
            LAST_LOL = System.currentTimeMillis();
        } else {
            int secs = (14 - (int) Math.ceil((System.currentTimeMillis() - LAST_LOL) / 1000D));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.cooldown"), secs));
        }
    }

    @SubCommand("lolId")
    @Permission(Permissions.LolCommandId)
    public static void lolId(VLCommandSender sender, int id) {
        if (System.currentTimeMillis() - LAST_LOL > COOLDOWN || LAST_LOL == 0) {
            try {
                String victim = ((VLPlayer) VLPlayer.getOnlinePlayers().toArray()[new Random().nextInt(VLPlayer.getOnlinePlayers().size())]).getName();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.format"), sender.getFormattedName(), ChatColor.translateAlternateColorCodes('&', String.valueOf(lolsList.get(id)))).replace("{PLAYER}", victim));
                }
                LAST_LOL = System.currentTimeMillis();
                // if they typed too high a number
            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.max"), id, lolsList.size() - 1));
            }
        } else {
            int secs = (14 - (int) Math.ceil((System.currentTimeMillis() - LAST_LOL) / 1000D));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.cooldown"), secs));
        }
    }

    @SubCommand("lolList")
    @Permission(Permissions.LolCommandId)
    public static void lolList(VLCommandSender sender) {
        lolListPages(sender, 1);
    }

    @SubCommand("lolListPage")
    @Permission(Permissions.LolCommandId)
    public static void lolListPage(VLCommandSender sender, int page) {
        lolListPages(sender, page);
    }

    public static void lolListPages(VLCommandSender sender, int page) {
        page = page - 1;
        int MAX_PAGES = (int) Math.floor(lolsList.size() / PAGE_SIZE);

        if (page + 1 > MAX_PAGES + 1) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.max"), page + 1, MAX_PAGES + 1));
            return;
        }
        /* check if page will be empty */
        // TODO fix this.
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.lol.list.header"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.list.page_number"), page + 1, MAX_PAGES + 1));
        int displayed = 0;
        for (int i = page * PAGE_SIZE; displayed < PAGE_SIZE; ++i, ++displayed) {
            try {
                String lol = String.valueOf(lolsList.get(i));
                String victim = ((VLPlayer) VLPlayer.getOnlinePlayers().toArray()[new Random().nextInt(VLPlayer.getOnlinePlayers().size())]).getName();
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.list.format"), i, ChatColor.translateAlternateColorCodes('&', lol)).replace("{PLAYER}", victim));
            } catch (IndexOutOfBoundsException ignored) {
                // if last page only has 3 entries etc
            }
        }
    }

    @SubCommand("lolAdd")
    @Permission(Permissions.LolCommandEdit)
    public static void lolAdd(VLCommandSender sender, String text) {
        if (!lolsList.contains(new JsonPrimitive(text))) {
            lolsList.add(text);
            if (saveLols()) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.add.added"), lolsList.size() - 1));
            } else {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.add.not_added"), lolsList.size() - 1));
            }
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.add.already_exists")));
        }
    }

    @SubCommand("lolDelete")
    @Permission(Permissions.LolCommandEdit)
    public static void lolDelete(VLPlayer sender, int id) {
        try {
            lolsList.remove(id);
            if (saveLols()) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.delete.deleted"), id));
            } else {
                // if we have an exception
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.delete.not_deleted"), id));
            }
            // if they typed too high a number
        } catch (IndexOutOfBoundsException e) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.delete.not_found"), id, lolsList.size() - 1));
        }
    }

    public static boolean saveLols() {
        try {
            JsonObject lolsEdited = new JsonObject();
            lolsEdited.add("lols", lolsList);
            updatedLols = new FileWriter(lolFile);
            updatedLols.write(lolsEdited.toString());
            updatedLols.flush();
            updatedLols.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
