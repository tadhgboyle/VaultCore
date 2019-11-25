package net.vaultmc.vaultcore.commands;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.utils.commands.experimental.ArgumentProvider;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "help", description = "Show the help menu.")
@Permission(Permissions.BackCommand)
@PlayerOnly

public class HelpCommand extends CommandExecutor {

	public HelpCommand() {
		this.register("help", Collections.emptyList(), "VaultCore");
		this.register("generalHelp", Arrays.asList(Commands.literal("general")), "VaultCore");
		this.register("creativeHelp", Arrays.asList(Commands.literal("creative")), "VaultCore");
		this.register("survivalHelp", Arrays.asList(Commands.literal("survival")), "VaultCore");
		this.register("clansHelp", Arrays.asList(Commands.literal("clans")), "VaultCore");
		this.register("skyblockHelp", Arrays.asList(Commands.literal("skyblock")), "VaultCore");
	}

	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	String variable2 = Utilities.variable2;

	@SubCommand("help")
	public void rootHelp(CommandSender sender, ArgumentProvider args) {

		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "Usage: " + ChatColor.RED + "/help <option> [page]");
		sender.sendMessage(variable1 + "Available Options:");
		sender.spigot().sendMessage(
				Utilities.hoverMaker("General", "Helpful commands to be used universally!", "/help general"),
				Utilities.messageMakerString(", "),
				Utilities.hoverMaker("Creative", "Commands to use in the Creative world!", "/help creative"),
				Utilities.messageMakerString(", "),
				Utilities.hoverMaker("Survival", "Commands to use in the Survival world!", "/help survival"),
				Utilities.messageMakerString(", "),
				Utilities.hoverMaker("Clans", "Commands to use in the Clans world!", "/help clans"),
				Utilities.messageMakerString(", "),
				Utilities.hoverMaker("SkyBlock", "Commands to use in the SkyBlock world!", "/help skyblock"));
	}

	@SubCommand("generalHelp")
	public void generalHelp(CommandSender sender, ArgumentProvider args) {
		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "General Commands:");
		sender.sendMessage(string + "/token - Get Your VaultMC Services token.");
		sender.sendMessage(string + "/discord - Join our Discord.");
		sender.sendMessage(string + "/msg <sender> - Send a message to a sender.");
		sender.sendMessage(string + "/tpa - Send a teleport request.");
		sender.sendMessage(string + "/tpaccept - Accept a teleport request.");
		sender.sendMessage(string + "/spawn - Return to spawn.");
		sender.sendMessage(string + "/playtime [player] - Check your PlayTime.");
	}

	@SubCommand("creativeHelp")
	public void creativeHelp(CommandSender sender, ArgumentProvider args) {
		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "Creative Commands:");
		sender.sendMessage(string + "/plot auto - Claim a plot.");
		sender.sendMessage(string + "/plot home [player] - Go to a plot.");
		sender.sendMessage(string + "/plot add <player> - Let a player build on your plot.");
		sender.sendMessage(string + "/plot remove <player> - Do not a player build on your plot.");
	}

	@SubCommand("survivalHelp")
	public void survivalHelp(CommandSender sender, ArgumentProvider args) {
		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "Survival Commands:");
		sender.sendMessage(string + "/wild - Teleport randomly in the world.");
	}

	@SubCommand("clansHelp")
	public void clansHelp(CommandSender sender, ArgumentProvider args) {
		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "Clans Commands:");
		sender.sendMessage(string + "/wild - Teleport randomly in the world.");
		sender.sendMessage(string + "/c join <clan> - Join a clan.");
		sender.sendMessage(string + "/c home - Go to your clan home.");
		sender.sendMessage(string + "/c auction - Open the clan auction GUI.");
	}

	@SubCommand("skyblockHelp")
	public void skyblockHelp(CommandSender sender, ArgumentProvider args) {
		sender.sendMessage(variable2 + "--== [Help] ==--");
		sender.sendMessage(" ");
		sender.sendMessage(variable1 + "SkyBlock Commands:");
		sender.sendMessage(string + "/island - Get your own island.");
		sender.sendMessage(string + "/is - Go to your island");
		sender.sendMessage(string + "/island top - View the top islands.");
	}
}