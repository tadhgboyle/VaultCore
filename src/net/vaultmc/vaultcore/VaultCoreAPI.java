package net.vaultmc.vaultcore;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VaultCoreAPI {

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

	public static Boolean PWCCheck(Player player) {
		Boolean pwc = VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.pwc");
		return pwc;
	}
	
	public static long[] formatDuration(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		return new long[] { days, hours, minutes, seconds };
	}
}
