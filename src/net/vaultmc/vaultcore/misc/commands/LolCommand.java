package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(literal = "lol", description = "The chat based soundboard.")
@Permission(Permissions.LolCommand)
@Aliases({"lmao", "lul"})
public class LolCommand extends CommandExecutor {

    public static File lolFile = new File(VaultCore.getInstance().getDataFolder() + "/lols.json");
    private static JSONArray lolsList;
    private static FileWriter updatedLols;
    private static long LAST_LOL = 0;
    private static long COOLDOWN = 15000;
    JSONParser parser = new JSONParser();
    Object obj;

    {
        try {
            obj = parser.parse(new FileReader(lolFile));
            JSONObject jsonObject = (JSONObject) obj;
            lolsList = (JSONArray) jsonObject.get("lols");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LolCommand() {
        register("lol", Collections.emptyList());
        register("lolList",
                Collections.singletonList(Arguments.createLiteral("list")));
        register("lolAdd",
                Arrays.asList(Arguments.createLiteral("add"), Arguments.createArgument("text", Arguments.greedyString())));
        register("lolDelete",
                Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("id", Arguments.integerArgument())));
    }

    @SubCommand("lol")
    public static void lol(VLPlayer sender) {
        if (System.currentTimeMillis() - LAST_LOL > COOLDOWN || LAST_LOL == 0) {
            int id = ThreadLocalRandom.current().nextInt(0, lolsList.size());
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.format"), sender.getFormattedName(), lolsList.get(id)));
            }
            LAST_LOL = System.currentTimeMillis();
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.cooldown"), Utilities.millisToTime(System.currentTimeMillis() - LAST_LOL)));
        }
    }

    @SubCommand("lolList")
    public static void lolList(VLPlayer sender) {

    }

    @SubCommand("lolAdd")
    @Permission(Permissions.LolCommandEdit)
    public static void lolAdd(VLPlayer sender, String text) {
        if (!lolsList.contains(text)) {
            lolsList.add(text);
            if (saveLols()) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.added"), lolsList.size()));
            } else {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.not_added"), lolsList.size() + 1));
            }
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.already_exists"), lolsList.indexOf(text)));
        }
    }

    @SubCommand("lolDelete")
    @Permission(Permissions.LolCommandEdit)
    public static void lolDelete(VLPlayer sender, int id) {
        try {
            lolsList.remove(id - 1);
            if (saveLols()) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.deleted"), id));
            } else {
                // if we have an exception
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.not_deleted"), id));
            }
            // if they typed too high a number
        } catch (IndexOutOfBoundsException e) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.lol.not_found"), id, lolsList.size()));
        }
    }

    public static boolean saveLols() {
        try {
            JSONObject lolsEdited = new JSONObject();
            lolsEdited.put("lols", lolsList);
            updatedLols = new FileWriter(lolFile);
            updatedLols.write(String.valueOf(lolsEdited));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                updatedLols.flush();
                updatedLols.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
