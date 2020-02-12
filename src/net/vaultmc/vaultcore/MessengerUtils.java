package net.vaultmc.vaultcore;

import lombok.SneakyThrows;
import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MessengerUtils implements PluginMessageListener {
    private static final Set<Consumer<List<String>>> getServersCallbacks = new HashSet<>();
    private static final Set<Consumer<List<String>>> getPlayersCallbacks = new HashSet<>();
    private static final Set<Consumer<String>> getThisServerCallbacks = new HashSet<>();

    @SneakyThrows
    public static void getThisServer(Consumer<String> callback) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("GetServer");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        getThisServerCallbacks.add(callback);
    }

    @SneakyThrows
    public static void getServers(Consumer<List<String>> callback) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("GetServers");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        getServersCallbacks.add(callback);
    }

    @SneakyThrows
    public static void getPlayers(Consumer<List<String>> callback) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("PlayerList");
        dos.writeUTF("ALL");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        getPlayersCallbacks.add(callback);
    }

    @SneakyThrows
    public static void sendForwarding(String channel, String server, byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.writeUTF("Forward");
        stream.writeUTF(server);
        stream.writeShort(data.length);
        stream.write(data);
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        stream.close();

        if (server.equals("ALL")) {
            getThisServer(th -> sendForwarding(channel, th, data));
        }
    }

    @Override
    @SneakyThrows
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        System.out.println("new String(message) = " + new String(message));
        if (channel.contains("BungeeCord")) {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            String s = stream.readUTF();
            if (s.contains(",")) {
                if (s.contains("vaultmc")) {
                    for (Consumer<List<String>> callback : getServersCallbacks) {
                        callback.accept(Arrays.asList(s.split(", ")));
                    }
                    getServersCallbacks.clear();
                } else {
                    for (Consumer<List<String>> callback : getPlayersCallbacks) {
                        callback.accept(Arrays.asList(s.split(", ")));
                    }
                    getPlayersCallbacks.clear();
                }
            } else {
                for (Consumer<String> callback : getThisServerCallbacks) {
                    callback.accept(s);
                }
                getThisServerCallbacks.clear();
            }
            stream.close();
        }
    }
}
