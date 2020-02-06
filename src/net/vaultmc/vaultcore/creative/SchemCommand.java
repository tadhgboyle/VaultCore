package net.vaultmc.vaultcore.creative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.session.SessionOwner;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "schem", description = "Save or Load a schematic.")
@Permission(Permissions.SchemCommand)
@PlayerOnly
public class SchemCommand extends CommandExecutor {

	// TODO custom schem saving, listing and loading. no need for /delete right now.
	public SchemCommand() {
		unregisterExisting();
		register("loadSchem",
				Arrays.asList(Arguments.createLiteral("load"), Arguments.createArgument("filename", Arguments.word())));
		register("saveSchem",
				Arrays.asList(Arguments.createLiteral("save"), Arguments.createArgument("filename", Arguments.word())));
	}

	@SubCommand("loadSchem")
	public void loadSchem(VLPlayer sender, String filename) {
					
		Player player = sender.getPlayer();
		
		Clipboard clipboard;
		try {
			clipboard = WorldEdit.getInstance().getSessionManager().get((SessionOwner) player).getClipboard().getClipboard();
		} catch (EmptyClipboardException e1) {
			e1.printStackTrace();
		}

		File schem = new File(VaultCore.getInstance().getDataFolder() + "/schems/" + sender.getName() + "/" + filename);

		ClipboardFormat format = ClipboardFormats.findByFile(schem);
		try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
			clipboard = reader.read();
		} catch (FileNotFoundException e) {
			sender.sendMessage("that schem is not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SubCommand("saveSchem")
	public void saveSchem(VLPlayer sender, String filename) {
				
		Player player = sender.getPlayer();
				
		Clipboard clipboard = null;
		try {
			clipboard = WorldEdit.getInstance().getSessionManager().get((SessionOwner) BukkitAdapter.adapt(player)).getClipboard().getClipboard();
		} catch (EmptyClipboardException e1) {
			sender.sendMessage("your clipbaord is empty. try //copy first");
			e1.printStackTrace();
		}
		File schem = new File(VaultCore.getInstance().getDataFolder() + "/schems/" + sender.getName() + "/" + filename);

		try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schem))) {
			writer.write(clipboard);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}