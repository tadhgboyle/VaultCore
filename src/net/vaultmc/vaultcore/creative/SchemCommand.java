package net.vaultmc.vaultcore.creative;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RootCommand(literal = "vcschem", description = "Save or Load a schematic.")
@Permission(Permissions.SchemCommand)
@PlayerOnly
public class SchemCommand extends CommandExecutor {
    private static final List<String> mySchems = new ArrayList<>();

	public SchemCommand() {
		unregisterExisting();
		register("loadSchem",
				Arrays.asList(Arguments.createLiteral("load"), Arguments.createArgument("filename", Arguments.word())));
		register("saveSchem",
				Arrays.asList(Arguments.createLiteral("save"), Arguments.createArgument("filename", Arguments.word())));
        register("listSchemSelf", Collections.singletonList(Arguments.createLiteral("list")));
		register("listSchemOther",
				Arrays.asList(Arguments.createLiteral("list"), Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
		register("delSchemSelf",
				Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("schematic", Arguments.word())));

	}

	@SubCommand("loadSchem")
	public void loadSchem(VLPlayer sender, String filename) {

		Player player = sender.getPlayer();
				
		@SuppressWarnings("unused")
		Clipboard clipboard;
		try {
			clipboard = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player))
					.getClipboard().getClipboard();
		} catch (EmptyClipboardException e1) {
			// TODO @Aberdeener - Not make them make a selection before pasting, or is this a WE API issue?
			sender.sendMessage(ChatColor.RED + "You need to //copy something first. This will be fixed soon.");
		}

		File schem = new File(
				VaultCore.getInstance().getDataFolder() + "/schems/" + sender.getName() + "/" + filename + ".schem");

		ClipboardFormat format = ClipboardFormats.findByFile(schem);
		try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
			clipboard = reader.read();
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.loaded"), filename));
		} catch (FileNotFoundException e) {
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.not_found"), filename));
		} catch (IOException e) {
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.error"), "IOException"));
			e.printStackTrace();
		}
	}

	@SubCommand("saveSchem")
	public void saveSchem(VLPlayer sender, String filename) {
		Player player = sender.getPlayer();

		Clipboard clipboard = null;
		try {
			clipboard = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player))
					.getClipboard().getClipboard();
		} catch (EmptyClipboardException e1) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.empty_clipboard"));
			return;
		}
		File schem = new File(
				VaultCore.getInstance().getDataFolder() + "/schems/" + sender.getName() + "/" + filename + ".schem");
		try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schem))) {
			writer.write(clipboard);
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.saved"), filename));
		} catch (FileNotFoundException e) {
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.error"), "Unknown"));
			e.printStackTrace();
		} catch (IOException e) {
			sender.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.error"), "IOException"));
			e.printStackTrace();
		}
	}
	
	@SubCommand("listSchemSelf")
	public void listSchemSelf(VLPlayer sender) {

        File schemDir = new File(VaultCore.getInstance().getDataFolder() + "/schems/" + sender.getName() + "/");
        
        int schems = 0;
        
        for (String schem : schemDir.list()) {
        	mySchems.add(schem);
        	schems++;
        }
        // TODO pagination... 7 per page?
        if (schems > 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.header"));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.total"), schems));
            for (String schem : mySchems) {
                sender.sendMessage(ChatColor.GOLD + schem);
            }
            mySchems.clear();
            schems = 0;
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.no_schems"));
        }
	}
	
	@SubCommand("listSchemOther")
	public void listSchemOther(VLPlayer sender, VLOfflinePlayer target) {
		
	}
	
	@SubCommand("delSchemSelf")
	public void delSchemSelf(VLPlayer sender, String filename) {
		
	}
	
	@SubCommand("delSchemOther")
	@Permission(Permissions.SchemCommandDelete)
	public void delSchemOther(VLPlayer sender, VLOfflinePlayer target, String filename) {
		
	}
}