package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "heal",
        description = "Heal a player."
)
@Permission(Permissions.HealCommand)
public class HealCommand extends CommandExecutor {
    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
    public HealCommand() {
        register("healSelf", Collections.emptyList(), "vaultcore");
        register("healOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())), "vaultcore");
    }

    @SubCommand("healSelf")
    @PlayerOnly
    public void healSelf(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have been " + variable1 + "healed"));
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    @SubCommand("healOthers")
    @Permission(Permissions.HealCommandOther)
    public void healOthers(CommandSender sender, Player target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have healed " + variable1 + VaultCoreAPI.getName(target)));
        target.setFoodLevel(20);
        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        target.setSaturation(20);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "You have been healed by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                        ChatColor.BOLD + "CONSOLE" + ChatColor.RESET)));
    }
}