package net.vaultmc.vaultcore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import net.vaultmc.vaultloader.VaultLoader;

public class Utilities {
	
	public static String string = ChatColor.translateAlternateColorCodes('&', VaultLoader.getMessage("colours.string"));
	public static String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultLoader.getMessage("colours.variable1"));
	public static String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultLoader.getMessage("colours.variable2"));
	
	/**
	 * @author Aberdeener
	 * @param message Message from
	 *                {@link net.vaultmc.vaultloader.VaultLoader#getMessage()}
	 * @param objects The variables you wish to insert to the message.
	 * @return Compiled message
	 */
	public static String formatMessage(String message, String... objects) {
		int num = 0;
		StringBuilder sb = new StringBuilder();
		for (String s : message.split(" ")) {
			if (s.matches(".*?\\{.*?}.*")) {
				String before = StringUtils.substringBefore(s, "{");
				String after = s.substring(s.lastIndexOf("}") + 1);
				s = objects[num];
				sb.append(before + s + after + " ");
				num++;
			} else {
				sb.append(s + " ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author Aberdeener
	 * @param message Message to capitalize.
	 * @return Capitalized message.
	 */
	public static String capitalizeMessage(String message) {
		return message.substring(0, 1).toUpperCase() + message.substring(1);
	}

	/**
	 * @author Aberdeener
	 * @param millis Time in milliseconds you wish to turn into a duration.
	 * @return Duration from milliseconds
	 */
	public static long[] millisToTime(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		return new long[] { days, hours, minutes, seconds };
	}

	/**
	 * @author Aberdeener
	 * @param millis Time in milliseconds you wish to turn into a date.
	 * @return Date from milliseconds
	 */
	public static String millisToDate(long millis) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date epoch = new Date(millis);
		return format.format(epoch);
	}
}
