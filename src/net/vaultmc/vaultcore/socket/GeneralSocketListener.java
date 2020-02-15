package net.vaultmc.vaultcore.socket;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class GeneralSocketListener implements Runnable {
    @Getter
    private static final Map<String, Integer> pong = new HashMap<>();
    @Getter
    private static BufferedReader reader;
    @Getter
    private static BufferedWriter writer;

    @SneakyThrows
    public GeneralSocketListener() {
        // Don't tell me this is not recommended.
        new Thread(this, "General Socket Listener").start();
        if (reader == null) reader = new BufferedReader(new InputStreamReader(VaultCore.getSocket().getInputStream()));
        if (writer == null)
            writer = new BufferedWriter(new OutputStreamWriter(VaultCore.getSocket().getOutputStream()));
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            String response;
            while ((response = reader.readLine()) != null) {
                if (response.startsWith("Ping")) {
                    VaultCore.getInstance().getLogger().info("Received " + response + " from global server");
                    writer.write("Pong" + VaultCore.SEPARATOR + response.split(VaultCore.SEPARATOR)[1] +
                            VaultCore.SEPARATOR + VaultCore.getInstance().getConfig().getString("server") + " kindly servicing you.\n");
                    VaultCore.getInstance().getLogger().info("Sending back pong response to global server");
                    writer.flush();
                } else if (response.startsWith("Pong")) {
                    VaultCore.getInstance().getLogger().info("Received " + response + " from global server");
                    int i = pong.getOrDefault(response.split(VaultCore.SEPARATOR)[1], 0);
                    i++;
                    pong.put(response.split(VaultCore.SEPARATOR)[1], i);
                }
            }
        }
    }
}
