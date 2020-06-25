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

package net.vaultmc.vaultcore.logs;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class LogsHandler implements Runnable {

    private static final HashMap<Integer, String> lineMatches = new HashMap<>();
    private static final HashMap<Integer, String> lineFiles = new HashMap<>();
    private final VLCommandSender sender;
    private final String search;

    public LogsHandler(VLCommandSender sender, String search) {
        this.sender = sender;
        this.search = search;
    }

    public void run() {
        LogsCommand.setSearching(true);
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.searching"));
        File logsDir = new File("/srv/" + VaultCore.getInstance().getConfig().getString("server") + "/logs/");
        for (File file : logsDir.listFiles()) {
            try {
                Scanner scanner = new Scanner(file);
                int lineID = 0;
                Pattern regex = Pattern.compile(search);
                Matcher matcher;
                String line;

                if (file.getName().endsWith(".gz")) {
                    BufferedReader inputReader = new BufferedReader(
                            new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
                    while (inputReader.readLine() != null) {
                        line = inputReader.readLine();
                        lineID++;
                        matcher = regex.matcher(line);
                        if (matcher.find()) {
                            if (line.endsWith(sender.getName() + " issued server command: /logs " + search)) continue;
                            if (line.startsWith("[VaultLoader]")) continue;
                            lineMatches.put(lineID, line);
                            lineFiles.put(lineID, file.getName());
                        }
                    }
                    inputReader.close();
                } else {
                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        lineID++;
                        matcher = regex.matcher(line);
                        if (matcher.find()) {
                            if (line.endsWith(sender.getName() + " issued server command: /logs " + search)) continue;
                            if (line.startsWith("[VaultLoader]")) continue;
                            lineMatches.put(lineID, line);
                            lineFiles.put(lineID, file.getName());
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (!lineMatches.isEmpty()) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.header"));
            // TODO pagination/date selection
            for (int lineNumber : lineMatches.keySet()) {
                String fileName;
                try {
                    fileName = lineFiles.get(lineNumber).substring(0, lineFiles.get(lineNumber).lastIndexOf("-"));
                } catch (ArrayIndexOutOfBoundsException e) {
                    fileName = lineFiles.get(lineNumber);
                }
                String line = lineMatches.get(lineNumber);
                String substring = line.substring(line.lastIndexOf("/INFO]:") + 7);
                sender.sendMessage(ChatColor.YELLOW + fileName + ChatColor.RESET + substring);
            }
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.logs.total_count"), lineMatches.size()));
            lineFiles.clear();
            lineMatches.clear();
        } else sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.no_matches"));
        LogsCommand.setSearching(false);
    }
}
