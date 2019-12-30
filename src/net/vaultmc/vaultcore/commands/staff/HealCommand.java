package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.Arguments;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "heal", description = "Heal a player.")
@Permission(Permissions.HealCommand)
public class HealCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public HealCommand() {
		register("healSelf", Collections.emptyList());
		register("healOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("healSelf")
	@PlayerOnly
	public void healSelf(CommandSender sender) {
		Player player = (Player) sender;
		sender.sendMessage(
				ChatColor.translateAlternateColorCodes('&', string + "You have been " + variable1 + "healed"));
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
				string + "You have been healed by " + variable1
						+ (sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
								: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE" + ChatColor.RESET)));
	}
}