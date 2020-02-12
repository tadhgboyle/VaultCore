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
import java.util.List;
import java.util.function.Supplier;

public class MessengerUtils implements PluginMessageListener {
    private static String lastOp;
    private static String lastGetServerResponse;
    private static String lastGetServersResponse;

    @SneakyThrows
    public static Supplier<String> getThisServer() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("GetServer");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        lastOp = "getServer";
        return () -> {
            if (lastGetServerResponse != null) {
                lastOp = null;
                String s = lastGetServerResponse;
                lastGetServerResponse = null;
                return s;
            } else {
                return null;
            }
        };
    }

    @SneakyThrows
    public static Supplier<List<String>> getServers() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("GetServers");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        lastOp = "getServers";
        return () -> {
            if (lastGetServersResponse != null) {
                lastOp = null;
                String s = lastGetServersResponse;
                lastGetServersResponse = null;
                return Arrays.asList(s.split(", "));
            } else {
                return null;
            }
        };
    }

    public static String getThisServerBlocking() {
        Supplier<String> resp = getThisServer();
        String supplied;
        while (true) {
            supplied = resp.get();
            if (supplied != null) {
                return supplied;
            }
        }
    }

    public static List<String> getServersBlocking() {
        Supplier<List<String>> resp = getServers();
        List<String> supplied;
        while (true) {
            supplied = resp.get();
            if (supplied != null) {
                return supplied;
            }
        }
    }

    private static String lastPlayerListResponse;

    @SneakyThrows
    public static Supplier<List<String>> getPlayers() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("PlayerList");
        dos.writeUTF("ALL");
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
        lastOp = "playerList";
        return () -> {
            if (lastPlayerListResponse != null) {
                lastOp = null;
                String s = lastPlayerListResponse;
                lastPlayerListResponse = null;
                return Arrays.asList(s.split(", "));
            } else {
                return null;
            }
        };
    }

    public static List<String> getPlayersBlocking() {
        Supplier<List<String>> resp = getPlayers();
        List<String> supplied;
        while (true) {
            supplied = resp.get();
            if (supplied != null) {
                return supplied;
            }
        }
    }

    @Override
    @SneakyThrows
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.contains("Return")) {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            if ("getServer".equals(lastOp)) {
                lastGetServerResponse = stream.readUTF();
            } else if ("getServers".equals(lastOp)) {
                lastGetServersResponse = stream.readUTF();
            } else if ("playerList".equals(lastOp)) {
                lastPlayerListResponse = stream.readUTF();
            }
            stream.close();
        }
    }
}
