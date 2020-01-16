package net.vaultmc.vaultcore.commands.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RootCommand(
        literal = "teleport",
        description = "Teleport to a player."
)
@Permission(Permissions.TeleportCommand)
@Aliases("tp")
public class TeleportCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    public TeleportCommand() {
        unregisterExisting();
        register("teleportLocation", Collections.singletonList(Arguments.createArgument("location", Arguments.location3DArgument())));
        register("teleportLocationWorld", Arrays.asList(
                Arguments.createArgument("location", Arguments.location3DArgument()),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("teleportToEntity", Collections.singletonList(Arguments.createArgument("target", Arguments.entityArgument())));
        register("teleportEntityTo", Arrays.asList(
                Arguments.createArgument("target", Arguments.entitiesArgument()),
                Arguments.createArgument("location", Arguments.location3DArgument())
        ));
        register("teleportEntityToWorld", Arrays.asList(
                Arguments.createArgument("target", Arguments.entitiesArgument()),
                Arguments.createArgument("location", Arguments.location3DArgument()),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("teleportEntityToEntity", Arrays.asList(
                Arguments.createArgument("target", Arguments.entitiesArgument()),
                Arguments.createArgument("to", Arguments.entityArgument())
        ));
    }

    private static String readLocation(Location location) {
        return location.getWorld().getName() + ", " + Math.round(location.getX() * 100.0) / 100.0 + ", " +
                Math.round(location.getY() * 100.0) / 100.0 + ", " + Math.round(location.getZ() * 100.0) / 100.0;
    }

    @SubCommand("teleportLocation")
    @PlayerOnly
    public void teleportLocation(VLPlayer sender, Location location) {
        sender.teleport(location);
        sender.sendMessage(string + "Teleported you to " + variable1 + readLocation(location) + string + ".");
    }

    @SubCommand("teleportLocationWorld")
    @PlayerOnly
    public void teleportLocationWorld(VLPlayer sender, Location location, World w) {
        location.setWorld(w);
        sender.teleport(location);
        sender.sendMessage(string + "Teleported you to " + variable1 + readLocation(location) + string + ".");
    }

    @SubCommand("teleportToEntity")
    @PlayerOnly
    @Permission(Permissions.TeleportCommandOther)
    public void teleportToEntity(VLPlayer sender, Entity target) {
        sender.teleport(target);
        sender.sendMessage(string + "Teleported you to " + variable1 + target.getName() + string + ".");
    }

    @SubCommand("teleportEntityTo")
    @Permission(Permissions.TeleportCommandOther)
    public void teleportEntityTo(VLCommandSender sender, Collection<Entity> entities, Location location) {
        for (Entity entity : entities) {
            if (location.getWorld() == null) location.setWorld(entity.getWorld());
            entity.teleport(location);
            sender.sendMessage(string + "Teleported " + variable1 + entity.getName() + string + " to " + variable1 + readLocation(location) + string + ".");
        }
    }

    @SubCommand("teleportEntityToWorld")
    @Permission(Permissions.TeleportCommandOther)
    public void teleportEntityToWorld(VLCommandSender sender, Collection<Entity> entities, Location location, World w) {
        location.setWorld(w);
        teleportEntityTo(sender, entities, location);
    }

    @SubCommand("teleportEntityToEntity")
    @Permission(Permissions.TeleportCommandOther)
    public void teleportEntityToEntity(VLCommandSender sender, Collection<Entity> entities, Entity to) {
        for (Entity entity : entities) {
            entity.teleport(to);
            sender.sendMessage(string + "Teleported " + variable1 + entity.getName() + string + " to " + variable1 + to.getName() + string + ".");
        }
    }
}
