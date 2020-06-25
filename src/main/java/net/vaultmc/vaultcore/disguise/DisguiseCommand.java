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

package net.vaultmc.vaultcore.disguise;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.nametags.Nametags;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(
        literal = "disguise",
        description = "Disguise yourself. This is an interesting command."
)
@Permission(Permissions.DisguiseCommand)
@PlayerOnly
public class DisguiseCommand extends CommandExecutor {
    @Getter
    private static final Map<UUID, GameProfile> disguisedPlayers = new HashMap<>();
    @Getter
    private static final Map<UUID, String> disguisedDN = new HashMap<>();

    @Getter
    private static final Map<UUID, GameProfile> actualGameProfile = new HashMap<>();
    @Getter
    private static final Map<UUID, String> actualDN = new HashMap<>();

    private static final Multimap<UUID, Property> skinCache = ArrayListMultimap.create();
    private static final List<UUID> randomPlayers = new ArrayList<>();
    private static final Map<String, String> randomPlayerToName = new HashMap<>();

    static {
        try (FileInputStream stream = new FileInputStream(new File(VaultCore.getInstance().getDataFolder(), "names.json").getAbsoluteFile())) {
            Scanner s = new Scanner(stream).useDelimiter("\\A");
            if (s.hasNext()) {
                JsonArray array = new JsonParser().parse(s.next()).getAsJsonArray();
                for (JsonElement element : array) {
                    JsonObject object = element.getAsJsonObject();
                    randomPlayers.add(UUID.fromString(object.get("uuid").getAsString()));
                    randomPlayerToName.put(object.get("uuid").getAsString(), object.get("name").getAsString());
                }
            }
        } catch (Exception ex) {
            VaultLoader.logger().error("An error occurred while attempting to load names.json");
            ex.printStackTrace();
        }
    }

    public DisguiseCommand() {
        register("disguiseAsPlayer", Arrays.asList(
                Arguments.createLiteral("player"),
                Arguments.createArgument("target", Arguments.offlinePlayerArgument())
        ));
        register("disguiseAsRandomPlayer", Collections.singletonList(
                Arguments.createLiteral("random")
        ));
    }

    @EventHandler
    @SneakyThrows
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (disguisedPlayers.containsKey(e.getPlayer().getUniqueId())) {
            ((CraftPlayer) e.getPlayer()).getHandle().setProfile(disguisedPlayers.get(e.getPlayer().getUniqueId()));
            e.getPlayer().setDisplayName(disguisedDN.get(e.getPlayer().getUniqueId()));
            ClientboundPlayerInfoPacket tabRemove = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,
                    ((CraftPlayer) e.getPlayer()).getHandle());
            ClientboundPlayerInfoPacket tabAdd = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                    ((CraftPlayer) e.getPlayer()).getHandle());
            ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(e.getPlayer().getEntityId());
            ClientboundAddPlayerPacket add = new ClientboundAddPlayerPacket(((CraftPlayer) e.getPlayer()).getHandle());

            SynchedEntityData dataWatcher = ((CraftPlayer) e.getPlayer()).getHandle().getDataWatcher();
            Field field = net.minecraft.world.entity.player.Player.class.getDeclaredField("bq");
            field.setAccessible(true);
            EntityDataAccessor<Byte> object = (EntityDataAccessor<Byte>) field.get(((CraftPlayer) e.getPlayer()).getHandle());
            dataWatcher.set(object, (byte) (dataWatcher.get(object) | 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40 | 0x80));
            ClientboundSetEntityDataPacket data = new ClientboundSetEntityDataPacket(e.getPlayer().getEntityId(), dataWatcher, true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId().toString().equals(e.getPlayer().getUniqueId().toString())) {
                    continue;
                }
                ((CraftPlayer) p).getHandle().connection.send(tabRemove);
                ((CraftPlayer) p).getHandle().connection.send(tabAdd);
                ((CraftPlayer) p).getHandle().connection.send(remove);
                ((CraftPlayer) p).getHandle().connection.send(add);
                ((CraftPlayer) p).getHandle().connection.send(data);
            }
        }
    }

    @SubCommand("disguiseAsPlayer")
    @Permission(Permissions.DisguiseAsPlayer)
    @SneakyThrows
    public void disguiseAsPlayer(VLPlayer sender, VLOfflinePlayer player) {
        if (player.hasPermission(Permissions.DisguiseAsBypass)) {
            sender.sendMessageByKey("vaultcore.commands.disguise.bypass");
            return;
        }

        if (disguisedPlayers.containsKey(sender.getUniqueId())) {
            UndisguiseCommand.undisguise(sender);
        }

        actualDN.put(sender.getUniqueId(), sender.getDisplayName());
        GameProfile oldProfile = new GameProfile(sender.getUniqueId(), sender.getName());
        for (Map.Entry<String, Collection<Property>> entry : ((GameProfile) net.minecraft.world.entity.player.Player.class.getDeclaredMethod("getProfile").invoke(
                ((CraftPlayer) sender.getPlayer()).getHandle())).getProperties().asMap().entrySet()) {
            oldProfile.getProperties().putAll(entry.getKey(), entry.getValue());
        }
        actualGameProfile.put(sender.getUniqueId(), oldProfile);

        String name = player.getName();
        if (player.getName() == null) {
            // "We have not seen a name for the player yet"
            name = randomPlayerToName.get(player.getUniqueId().toString());
        }

        if (skinCache.containsKey(player.getUniqueId())) {
            sender.sendMessageByKey("vaultcore.commands.disguise.cache");
            GameProfile newProfile = new GameProfile(sender.getUniqueId(), name);
            newProfile.getProperties().clear();
            for (Property property : skinCache.get(player.getUniqueId())) {
                newProfile.getProperties().put(property.getName(), property);
            }
            ((CraftPlayer) sender.getPlayer()).getHandle().setProfile(newProfile);
            ClientboundPlayerInfoPacket tabRemove = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,
                    ((CraftPlayer) sender.getPlayer()).getHandle());
            ClientboundPlayerInfoPacket tabAdd = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                    ((CraftPlayer) sender.getPlayer()).getHandle());  // Our PacketListener will change the GameProfile for us
            ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(sender.getPlayer().getEntityId());
            ClientboundAddPlayerPacket add = new ClientboundAddPlayerPacket(((CraftPlayer) sender.getPlayer()).getHandle());
            SynchedEntityData dataWatcher = ((CraftPlayer) sender.getPlayer()).getHandle().getDataWatcher();
            Field field = net.minecraft.world.entity.player.Player.class.getDeclaredField("bq");
            field.setAccessible(true);
            EntityDataAccessor<Byte> object = (EntityDataAccessor<Byte>) field.get(((CraftPlayer) sender.getPlayer()).getHandle());
            dataWatcher.set(object, (byte) (dataWatcher.get(object) | 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40 | 0x80));
            ClientboundSetEntityDataPacket data = new ClientboundSetEntityDataPacket(sender.getPlayer().getEntityId(), dataWatcher, true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId().toString().equals(sender.getUniqueId().toString())) {
                    continue;
                }
                ((CraftPlayer) p).getHandle().connection.send(tabRemove);
                ((CraftPlayer) p).getHandle().connection.send(tabAdd);
                ((CraftPlayer) p).getHandle().connection.send(remove);
                ((CraftPlayer) p).getHandle().connection.send(add);
                ((CraftPlayer) p).getHandle().connection.send(data);
            }
            disguisedPlayers.put(sender.getUniqueId(), newProfile);
            disguisedDN.put(sender.getUniqueId(), player.getDisplayName());
            sender.getPlayer().setDisplayName(player.getDisplayName());
            Nametags.forceUpdateAll();
            sender.sendMessageByKey("vaultcore.commands.disguise.disguised", "player", ChatColor.GOLD + name);
        } else {
            sender.sendMessageByKey("vaultcore.commands.disguise.fetch");
            String finalName = name;
            String finalName1 = name;
            Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                            player.getUniqueId().toString().replace("-", "") + "?unsigned=false").openConnection();
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.addRequestProperty("User-Agent", "VaultMC Disguise/VaultCore Control");
                    conn.connect();
                    Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        JsonObject json = new JsonParser().parse(scanner.next()).getAsJsonObject();
                        JsonArray properties = json.getAsJsonArray("properties");
                        GameProfile newProfile = new GameProfile(sender.getUniqueId(), finalName);
                        newProfile.getProperties().clear();
                        for (JsonElement element : properties) {
                            JsonObject property = element.getAsJsonObject();
                            Property prop = new Property(property.get("name").getAsString(), property.get("value").getAsString(),
                                    property.has("signature") ? property.get("signature").getAsString() : null);
                            skinCache.put(player.getUniqueId(), prop);
                            newProfile.getProperties().put(prop.getName(), prop);
                        }
                        ((CraftPlayer) sender.getPlayer()).getHandle().setProfile(newProfile);
                        ClientboundPlayerInfoPacket tabRemove = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,
                                ((CraftPlayer) sender.getPlayer()).getHandle());
                        ClientboundPlayerInfoPacket tabAdd = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                                ((CraftPlayer) sender.getPlayer()).getHandle());  // Our PacketListener will change the GameProfile for us
                        ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(sender.getPlayer().getEntityId());
                        ClientboundAddPlayerPacket add = new ClientboundAddPlayerPacket(((CraftPlayer) sender.getPlayer()).getHandle());
                        SynchedEntityData dataWatcher = ((CraftPlayer) sender.getPlayer()).getHandle().getDataWatcher();
                        Field field = net.minecraft.world.entity.player.Player.class.getDeclaredField("bq");
                        field.setAccessible(true);
                        EntityDataAccessor<Byte> object = (EntityDataAccessor<Byte>) field.get(((CraftPlayer) sender.getPlayer()).getHandle());
                        dataWatcher.set(object, (byte) (dataWatcher.get(object) | 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40 | 0x80));
                        ClientboundSetEntityDataPacket data = new ClientboundSetEntityDataPacket(sender.getPlayer().getEntityId(), dataWatcher, true);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.getUniqueId().toString().equals(sender.getUniqueId().toString())) {
                                continue;
                            }
                            ((CraftPlayer) p).getHandle().connection.send(tabRemove);
                            ((CraftPlayer) p).getHandle().connection.send(tabAdd);
                            ((CraftPlayer) p).getHandle().connection.send(remove);
                            ((CraftPlayer) p).getHandle().connection.send(add);
                            ((CraftPlayer) p).getHandle().connection.send(data);
                        }
                        disguisedPlayers.put(sender.getUniqueId(), newProfile);
                        disguisedDN.put(sender.getUniqueId(), player.getDisplayName());
                        sender.getPlayer().setDisplayName(player.getDisplayName());
                        Nametags.forceUpdateAll();
                        sender.sendMessageByKey("vaultcore.commands.disguise.disguised", "player", ChatColor.GOLD + finalName1);
                    } else {
                        sender.sendMessageByKey("vaultcore.commands.disguise.error");
                        throw new RuntimeException("No response from Mojang API");
                    }
                    conn.disconnect();
                } catch (Exception ex) {
                    sender.sendMessageByKey("vaultcore.commands.disguise.error");
                    ex.printStackTrace();
                }
            });
        }
    }

    @SubCommand("disguiseAsRandomPlayer")
    @Permission(Permissions.DisguiseRandomPlayer)
    public void disguiseAsRandomPlayer(VLPlayer sender) {
        VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(randomPlayers.get(ThreadLocalRandom.current().nextInt(randomPlayers.size())));
        if (player == null || player.hasPermission(Permissions.DisguiseAsBypass)) {
            sender.sendMessageByKey("vaultcore.commands.disguise.run-again");
            return;
        }
        disguiseAsPlayer(sender, player);
    }
}
