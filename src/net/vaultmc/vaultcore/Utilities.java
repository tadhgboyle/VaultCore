package net.vaultmc.vaultcore;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import lombok.SneakyThrows;
import net.vaultmc.vaultloader.utils.DBConnection;

public class Utilities {
	
	static DBConnection database = VaultCore.getDatabase();

	public static String string = ChatColor.translateAlternateColorCodes('&', "&e");
	public static String variable1 = ChatColor.translateAlternateColorCodes('&', "&6");
	public static String variable2 = ChatColor.translateAlternateColorCodes('&', "&2");

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

	public static String millisToDate(long millis) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date epoch = new Date(millis);
		return format.format(epoch);
	}
	
	@SneakyThrows
	public static void cleanDatabase() {
		database.executeUpdateStatement("DELETE FROM sessions WHERE duration IS NULL");
		
		ResultSet deleted = database.executeQueryStatement("SELECT ROW_COUNT() as deletedRows;");
		
		Bukkit.getConsoleSender().sendMessage("Cleaned " + deleted.getString("deletedRows") + " rows from the Database");
	}
}
