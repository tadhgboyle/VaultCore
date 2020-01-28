package net.vaultmc.vaultcore;

import net.vaultmc.vaultloader.VaultLoader;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utilities {

	public static String string = VaultLoader.getMessage("colours.string");
	public static String variable1 = VaultLoader.getMessage("colours.variable1");
	public static String variable2 = VaultLoader.getMessage("colours.variable2");

	/**
	 * @param message - Message from
	 *                {@link net.vaultmc.vaultloader.VaultLoader#getMessage(String)}
	 * @param objects - The variables you wish to insert to the message.
	 * @return Compiled message
	 * @author Aberdeener
	 */
	public static String formatMessage(String message, Object... objects) {
		int num = 0;
		StringBuilder sb = new StringBuilder();
		for (String s : message.split(" ")) {
			if (s.matches(".*?\\{.*?}.*")) {
				String before = StringUtils.substringBefore(s, "{");
				String after = s.substring(s.lastIndexOf("}") + 1);
				s = objects[num].toString();
				sb.append(before).append(s).append(after).append(" ");
				num++;
			} else {
				sb.append(s).append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * @author Aberdeener
	 * @param message - Message to capitalize.
	 * @return Capitalized message.
	 */
	public static String capitalizeMessage(String message) {
		return message.substring(0, 1).toUpperCase() + message.substring(1);
	}

	private static Map<String, Long> timeparts = new LinkedHashMap<String, Long>();

	/**
	 * @author Aberdeener
	 * @param millis - Time in milliseconds you wish to turn into a duration.
	 * @return Duration from milliseconds
	 */
	public static String millisToTime(long millis) {

		long millisInSecond = 1000L;
		long millisInMinute = 60000L;
		long millisInHour = 3600000L;
		long millisInDay = 86400000;
		long millisInMonth = 2592000000L;
		long millisInYear = 31536000000L;

		long years = (long) Math.floor(millis / millisInYear);
		long monthMillis = millis % millisInYear;
		long months = (long) Math.floor(monthMillis / millisInMonth);
		long dayMillis = millis % millisInMonth;
		long days = (long) Math.floor(dayMillis / millisInDay);
		long hourMillis = millis % millisInDay;
		long hours = (long) Math.floor(hourMillis / millisInHour);
		long minuteMillis = millis % millisInHour;
		long minutes = (long) Math.floor(minuteMillis / millisInMinute);
		long secondMillis = millis % millisInMinute;
		long seconds = (long) Math.floor(secondMillis / millisInSecond);

		timeparts.put("year", years);
		timeparts.put("month", months);
		timeparts.put("day", days);
		timeparts.put("hour", hours);
		timeparts.put("minute", minutes);
		timeparts.put("second", seconds);

		StringBuilder sb = new StringBuilder();

		for (String section : timeparts.keySet()) {
			long duration = timeparts.get(section);

			if (duration > 0) {
				int position = new ArrayList<String>(timeparts.keySet()).indexOf(section);
				String ending = (duration == 1 ? ", " : "s, ");
				// if the second last entry is == 1, we can assume we dont need a break
				if (position == 4) {
					ending = (duration == 1 ? " and " : "s \nand ");
				}
				// if it is the last entry then add a period
				if (position == 5) {
					ending = (duration == 1 ? ". " : "s.");
				}
				sb.append(ChatColor.DARK_GREEN + "" + duration + ChatColor.YELLOW + " " + section + ending);
			}
		}
		return sb.toString();
	}

	/**
	 * @author Aberdeener
	 * @param millis - Time in milliseconds you wish to turn into a date.
	 * @return Date from milliseconds
	 */
	public static String millisToDate(long millis) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date epoch = new Date(millis);
		return format.format(epoch);
	}
}
