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

package net.vaultmc.vaultcore.creative;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "vcschem", description = "Save or Load a schematic.")
@Permission(Permissions.SchemCommand)
@PlayerOnly
public class SchemCommand extends CommandExecutor {
    public SchemCommand() {
        unregisterExisting();
        register("loadSchem",
                Arrays.asList(Arguments.createLiteral("load"), Arguments.createArgument("filename", Arguments.word())));
        register("saveSchem",
                Arrays.asList(Arguments.createLiteral("save"), Arguments.createArgument("filename", Arguments.word())));
        register("listSchemSelf", Collections.singletonList(Arguments.createLiteral("list")));
        register("listSchemOther", Arrays.asList(Arguments.createLiteral("list"),
                Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("delSchemSelf", Arrays.asList(Arguments.createLiteral("delete"),
                Arguments.createArgument("schematic", Arguments.word())));
    }

    @SubCommand("loadSchem")
    @SneakyThrows
    public void loadSchem(VLPlayer sender, String filename) {
        Player sessionOwner = BukkitAdapter.adapt(sender.getPlayer());
        if (filename.contains("..") || filename.contains("/")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.bad-path"));
            return;
        }
        File file = new File(VaultCore.getInstance().getDataFolder(),
                "/schems/" + sender.getUniqueId().toString() + "/" + filename + ".schem").getAbsoluteFile();
        if (!file.exists()) {
            file = new File(VaultCore.getInstance().getDataFolder(),
                    "/schems/" + sender.getUniqueId().toString() + "/" + filename + ".schematic").getAbsoluteFile();
            if (!file.exists()) {
                file = new File(VaultCore.getInstance().getDataFolder(), "/schems/" + sender.getName() + "/" + filename)
                        .getAbsoluteFile();
                if (!file.exists()) {
                    sender.sendMessage(
                            VaultLoader.getMessage("vaultcore.commands.schem.not_found").replace("{SCHEM}", filename));
                    return;
                }
            }
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.invalid-format"));
            return;
        }
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            WorldEdit.getInstance().getSessionManager().get(sessionOwner)
                    .setClipboard(new ClipboardHolder(reader.read()));
            sender.sendMessage(
                    VaultLoader.getMessage("vaultcore.commands.schem.loaded").replace("{SCHEM}", file.getName()));
        }
    }

    @SubCommand("saveSchem")
    @SneakyThrows
    public void saveSchem(VLPlayer sender, String filename) {
        Player sessionOwner = BukkitAdapter.adapt(sender.getPlayer());
        if (filename.contains("..") || filename.contains("/")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.bad-path"));
            return;
        }
        File file;
        if (filename.endsWith(".schem") || filename.endsWith(".schematic")) {
            file = new File(VaultCore.getInstance().getDataFolder(), "/schems/" + sender.getUniqueId().toString() + "/" + filename)
                    .getAbsoluteFile();
        } else {
            file = new File(VaultCore.getInstance().getDataFolder(),
                    "/schems/" + sender.getUniqueId().toString() + "/" + filename + ".schem").getAbsoluteFile();
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Unspecified error creating schematic");
            }
        }

        Clipboard clipboard;
        try {
            clipboard = WorldEdit.getInstance().getSessionManager().get(sessionOwner).getClipboard().getClipboard();
            if (clipboard == null) {
                // Shouldn't be possible, but for safety --
                throw new EmptyClipboardException();
            }
        } catch (EmptyClipboardException ex) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.empty-clipboard"));
            return;
        }

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
            sender.sendMessage(
                    VaultLoader.getMessage("vaultcore.commands.schem.saved").replace("{SCHEM}", file.getName()));
        }
    }

    @SubCommand("listSchemSelf")
    public void listSchemSelf(VLPlayer sender) {
        listSchem(sender, null);
    }

    @SubCommand("listSchemOther")
    public void listSchemOther(VLPlayer sender, VLOfflinePlayer target) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        listSchem(sender, target);
    }

    @SubCommand("delSchemSelf")
    public void delSchemSelf(VLPlayer sender, String filename) {
        delSchem(sender, null, filename);
    }

    @SubCommand("delSchemOther")
    @Permission(Permissions.SchemCommandDelete)
    public void delSchemOther(VLPlayer sender, VLOfflinePlayer target, String filename) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        delSchem(sender, target, filename);
    }

    public void listSchem(VLPlayer sender, VLOfflinePlayer target) {
        String subFolder;

        if (target != null) {
            if (target.getFirstPlayed() == 0L) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
                return;
            }
            subFolder = target.getUniqueId().toString();
        } else {
            subFolder = sender.getUniqueId().toString();
        }
        File schemDir = new File(VaultCore.getInstance().getDataFolder(), "/schems/" + subFolder + "/").getAbsoluteFile();
        if (schemDir.exists() && schemDir.list().length > 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.header"));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.total"),
                    subFolder, schemDir.list().length, schemDir.list().length > 1 ? "schematic" : "schematics"));
            for (String schem : schemDir.list()) {
                sender.sendMessage(ChatColor.GOLD + schem);
            }
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.list.no_schems"), target != null ? target.getFormattedName() : sender.getFormattedName()));
        }
    }

    @SneakyThrows
    public void delSchem(VLPlayer sender, VLOfflinePlayer target, String filename) {
        String subFolder;

        if (target != null) {
            subFolder = target.getUniqueId().toString();
        } else {
            subFolder = sender.getUniqueId().toString();
        }
        File file = new File(VaultCore.getInstance().getDataFolder(),
                "/schems/" + subFolder + "/" + filename + ".schem").getAbsoluteFile();
        if (!file.exists()) {
            file = new File(VaultCore.getInstance().getDataFolder(), "/schems/" + subFolder + "/" + filename + ".schematic").getAbsoluteFile();
            if (!file.exists()) {
                file = new File(VaultCore.getInstance().getDataFolder(), "/schems/" + subFolder + "/" + filename)
                        .getAbsoluteFile();
                if (!file.exists()) {
                    sender.sendMessage(Utilities
                            .formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.not_found"), filename));
                }
            }
        } else {
            if (file.delete()) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.not_deleted"),
                        filename));
            } else {
                sender.sendMessage(
                        Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.deleted"), filename));
                if (target != null && target.isOnline()) {
                    target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.schem.deleted_target"), sender.getFormattedName(), filename));
                }
            }
        }
    }
}