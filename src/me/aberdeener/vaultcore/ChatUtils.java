package me.aberdeener.vaultcore;

import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtils {

	static String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	static String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));

	public static TextComponent hoverMaker(String name, String hover, String command) {
		TextComponent hoverText = new TextComponent(variable1 + name);
		hoverText.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(string + hover).create()));
		hoverText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return hoverText;
	}

	public static TextComponent messageMakerString(String text) {
		TextComponent stringText = new TextComponent(string + text);
		return stringText;
	}
	public static TextComponent messageMakerVariable1(String text) {
		TextComponent variable1Text = new TextComponent(variable1 + text);
		return variable1Text;
	}
}
