package net.vaultmc.vaultcore.misc.commands.staff.logs;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class LogsHandler implements Runnable {

    private static HashMap<Integer, String> lineMatches = new HashMap<>();
    private static HashMap<Integer, String> lineFiles = new HashMap<>();
    private VLCommandSender sender;
    private String search;

    public LogsHandler(VLCommandSender sender, String search) {
        this.sender = sender;
        this.search = search;
    }

    public void run() {
        LogsCommand.setSearching(true);
        File logsDir = new File("/srv/" + VaultCore.getInstance().getConfig().getString("server") + "/logs/");
        for (File file : logsDir.listFiles()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                int lineID = 0;
                Pattern regex = Pattern.compile(search);
                Matcher matcher = null;
                String line = "";
                if (file.getName().endsWith(".gz")) {
                    BufferedReader inputReader = new BufferedReader(
                            new InputStreamReader(new GZIPInputStream(fileInputStream)));
                    readLine(file, inputReader, lineID, regex);
                    inputReader.close();
                } else {
                    readLine(file, bufferedReader, lineID, regex);
                    bufferedReader.close();
                }
                lineID++;
            } catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.error_reading"));
            }
        }
        if (!lineMatches.isEmpty()) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.header"));
            for (int lineNumber : lineMatches.keySet()) {
                String fileName = lineFiles.get(lineNumber);
                String line = lineMatches.get(lineNumber);
                String substring = line.substring(line.indexOf("/INFO]:") + 7);

                sender.sendMessage(fileName + " - " + substring);
            }
            lineFiles.clear();
            lineMatches.clear();
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.no_matches"));
        }
        // allow others to search now that we have our results
        LogsCommand.setSearching(false);
    }

    private void readLine(File file, BufferedReader bufferedReader, int lineID, Pattern regex) {
        try {
            String line;
            Matcher matcher;
            while (bufferedReader.readLine() != null) {
                line = bufferedReader.readLine();
                if (line.endsWith(sender.getName() + " issued server command: /logs " + search)) continue;
                matcher = regex.matcher(line);
                if (matcher.find()) {
                    lineMatches.put(lineID, line);
                    lineFiles.put(lineID, file.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.error_reading"));
        }
    }
}
