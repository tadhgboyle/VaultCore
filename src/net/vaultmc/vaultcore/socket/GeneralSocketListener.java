package net.vaultmc.vaultcore.socket;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class GeneralSocketListener extends BukkitRunnable {
    @Getter
    private static final Map<String, Integer> pong = new HashMap<>();
    @Getter
    private static BufferedReader reader;
    @Getter
    private static BufferedWriter writer;

    @SneakyThrows
    public GeneralSocketListener() {
        runTaskTimerAsynchronously(VaultLoader.getInstance(), 5, 5);
        if (reader == null) reader = new BufferedReader(new InputStreamReader(VaultCore.getSocket().getInputStream()));
        if (writer == null)
            writer = new BufferedWriter(new OutputStreamWriter(VaultCore.getSocket().getOutputStream()));
    }

    @Override
    @SneakyThrows
    public void run() {
        String response = reader.readLine();
        if (response != null) {
            if (response.startsWith("Ping")) {
                writer.write("Pong" + VaultCore.SEPARATOR + response.split(VaultCore.SEPARATOR)[1] +
                        VaultCore.SEPARATOR + VaultCore.getInstance().getConfig().getString("server") + " kindly servicing you.");
                writer.flush();
            } else if (response.startsWith("Pong")) {
                int i = pong.getOrDefault(response.split(VaultCore.SEPARATOR)[1], 0);
                i++;
                pong.put(response.split(VaultCore.SEPARATOR)[1], i);
            }
        }
    }
}
