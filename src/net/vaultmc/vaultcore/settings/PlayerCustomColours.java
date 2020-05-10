package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerCustomColours {

    public static HashMap<String, String> defaultColours = new HashMap<String, String>() {{
        put("s", "e");
        put("v1", "6");
        put("v2", "2");
    }};
    static HashMap<VLPlayer, HashMap<String, String>> playerColours = new HashMap<>();
    static List<String> contexts = Arrays.asList("s", "v1", "v2");
    static List<String> colours = Arrays.asList("4", "c", "6", "e", "2", "a", "b", "3", "1", "9", "d", "5", "f", "7", "8", "0");

    public static String getColour(VLPlayer player, String context) {
        if (contexts.contains(context)) {
            HashMap<String, String> currentColours = playerColours.get(player);
            String colour = currentColours.get(context);
            if (colour == null) {
                currentColours.put(context, defaultColours.get(context));
                playerColours.put(player, currentColours);
                colour = defaultColours.get(context);
            }
            return colour;
        } else return null;
    }

    public static HashMap<String, String> getColours(VLPlayer player) {
        HashMap<String, String> currentColours = playerColours.get(player);
        if (currentColours == null) {
            playerColours.put(player, defaultColours);
            currentColours = defaultColours;
        }
        return currentColours;
    }

    public static void setColours(VLPlayer player, HashMap<String, String> customColours) {
        playerColours.put(player, customColours);
    }

    public static HashMap<String, String> getColoursFromFile(VLPlayer player) {
        Object hashmap = VaultCore.getInstance().getData().get("colours", player.getUniqueId().toString());
        return (HashMap<String, String>) hashmap;
    }

    public static void saveColoursToFile(VLPlayer player) {
        VaultCore.getInstance().getData().set("colours." + player.getUniqueId().toString(), getColours(player));
    }

    public void setColour(VLPlayer player, String context, String colour) {
        HashMap<String, String> currentColours = playerColours.get(player);
        if (contexts.contains(context) && colours.contains(colour)) {
            currentColours.put(context, colour);
            playerColours.put(player, currentColours);
        }
    }
}
