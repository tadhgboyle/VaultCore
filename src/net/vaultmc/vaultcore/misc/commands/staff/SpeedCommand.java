package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "speed", description = "Change the speed of yourself or another player.")
@Permission(Permissions.SpeedCommand)
@Aliases("lsd")
public class SpeedCommand extends CommandExecutor {

    public SpeedCommand() {
        register("speedFind", Collections.emptyList());
        register("speedSelf", Arrays.asList(Arguments.createArgument("speed", Arguments.integerArgument(1, 10)),
                Arguments.createArgument("movement", Arguments.word())));
        register("speedOther",
                Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()),
                        Arguments.createArgument("speed", Arguments.integerArgument(1, 10)),
                        Arguments.createArgument("movement", Arguments.word())));
    }

    @SubCommand("speedFind")
    @PlayerOnly
    public static void speedFind(VLPlayer sender) {
        if (sender.isFlying()) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.find_self"), "flying", Math.round(Bukkit.getPlayer(sender.getUniqueId()).getFlySpeed() * 10)));
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.find_self"), "walking", Math.round(Bukkit.getPlayer(sender.getUniqueId()).getWalkSpeed() * 10)));
        }
    }

    @SubCommand("speedSelf")
    @PlayerOnly
    public static void speedSelf(VLPlayer sender, int speed, String movement) {
        setSpeed(sender, sender, speed, movement);
    }

    @SubCommand("speedOther")
    @Permission(Permissions.SpeedCommandOther)
    public static void speedOther(VLPlayer sender, VLPlayer target, int speed, String movement) {
        setSpeed(sender, target, speed, movement);
    }

    private static void setSpeed(VLPlayer sender, VLPlayer target, int speed, String movement) {
        float newSpeed;
        if (speed / 10 > 1.0 || speed / 10 < -1.0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.speed.invalid_speed"));
            return;
        } else {
            newSpeed = (float) speed / 10;
        }
        switch (movement.toLowerCase()) {
            case "walk":
                if (sender == target) {
                    Bukkit.getPlayer(sender.getUniqueId()).setWalkSpeed(newSpeed);
                    sender.sendMessage(Utilities.formatMessage(
                            VaultLoader.getMessage("vaultcore.commands.speed.sender_set_to"), "walk", speed));
                } else {
                    Bukkit.getPlayer(target.getUniqueId()).setWalkSpeed(speed);
                    sender.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.sender_target"),
                                    target.getFormattedName(), "walk", speed));
                    target.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.target_set_to"),
                                    "walk", speed, sender.getFormattedName()));
                }
                break;
            case "fly":
                if (sender == target) {
                    Bukkit.getPlayer(sender.getUniqueId()).setFlySpeed(newSpeed);
                    sender.sendMessage(Utilities.formatMessage(
                            VaultLoader.getMessage("vaultcore.commands.speed.sender_set_to"), "fly", speed));
                } else {
                    Bukkit.getPlayer(target.getUniqueId()).setWalkSpeed(newSpeed);
                    sender.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.sender_target"),
                                    target.getFormattedName(), "fly", speed));
                    target.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.speed.target_set_to"),
                                    "fly", speed, sender.getFormattedName()));
                }
                break;
            default:
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.speed.invalid_movement"));
                break;
        }
    }
}
