/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utilities {
    private static final Map<String, Long> timeparts = new LinkedHashMap<>();
    static List<String> apostrophe = Arrays.asList("lets", "doesnt", "hasnt", "hes", "shes", "dont", "wont", "cant", "wouldnt", "shouldnt", "its", "hows", "isnt", "im", "thats");
    static List<String> punctuation = Arrays.asList(".", "!", "?");

    public static Location deserializeLocation(String s) {
        if (s == null) return null;
        String[] parts = s.split(VaultCore.SEPARATOR);
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + VaultCore.SEPARATOR + location.getX() + VaultCore.SEPARATOR + location.getY() + VaultCore.SEPARATOR +
                location.getZ();
    }

    /**
     * @param message      Message to format.
     * @param replacements The variables you wish to insert to the message.
     * @return Compiled message
     * @author Aberdeener
     * @deprecated Use {@link net.vaultmc.vaultloader.utils.player.VLPlayer#sendMessageByKey} instead.
     */
    @Deprecated
    public static String formatMessage(String message, Object... replacements) {
        int num = 0;
        StringBuilder sb = new StringBuilder();
        try {
            for (String s : message.split(" ")) {
                if (s.matches(".*?\\{.*?}.*")) {
                    String before = StringUtils.substringBefore(s, "{");
                    String after = s.substring(s.lastIndexOf("}") + 1);
                    s = replacements[num].toString();
                    sb.append(before).append(s).append(after).append(" ");
                    num++;
                } else {
                    sb.append(s).append(" ");
                }
            }
            return sb.toString().trim();
        } catch (NullPointerException e) {
            return ChatColor.DARK_RED + "There was an error formatting a message. Usually this means invalid message path, or a variable is null. Please report this.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return ChatColor.DARK_RED + "There was an error formatting a message. This usually there are too many variables in the message string. Please report this.";
        }
    }

    public static String grammarly(String message) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String word : message.split(" ")) {
            if (word.equals("i")) word = word.toUpperCase();
            else if (word.equalsIgnoreCase("u")) word = "you";
            else if (word.equalsIgnoreCase("ur")) word = "you're";
            else if (word.equalsIgnoreCase("ok")) word = "okay";
            else if (word.equalsIgnoreCase("dont")) word = "don't";
            // Add apostrophe
            if (apostrophe.contains(word)) {
                word = word.substring(0, word.length() - 1) + "'" + word.substring(word.length() - 1);
            }
            // Uppercase if first word
            if (first)
                // Check if first char is a custom key. Not a fantastic solution, but sorta works.
                if (!Character.isLetterOrDigit(word.charAt(0)) && String.valueOf(word.charAt(0)).equals(word)) {
                    sb.append(word).append(" ");
                    first = false;
                    continue;
                } else if (!Character.isLetterOrDigit(word.charAt(0)))
                    word = word.charAt(0) + capitalizeMessage(word.substring(1));
                else word = capitalizeMessage(word);
            first = false;
            sb.append(word).append(" ");
        }
        message = sb.toString().trim();
        // Punctuation for last word
        if (!punctuation.contains(Character.toString(message.charAt(message.length() - 1)))) {
            message = message + ".";
        }
        return message;
    }

    /**
     * @param message Message to capitalize.
     * @return Capitalized message.
     * @author Aberdeener
     */
    public static String capitalizeMessage(String message) {
        return message.substring(0, 1).toUpperCase() + message.substring(1);
    }

    /**
     * @param millis  Milliseconds of time to format
     * @param newline Whether we should add a newline
     * @param period  Whether we should add a period
     * @return Prettified human-readable message of the duration of milliseconds
     */
    public static String millisToTime(long millis, boolean newline, boolean period) {
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
        timeparts.put("second", seconds != 0 ? seconds : Integer.MIN_VALUE);

        StringBuilder sb = new StringBuilder();

        for (String section : timeparts.keySet()) {
            long duration = timeparts.get(section);

            if (duration > 0) {
                int position = new ArrayList<>(timeparts.keySet()).indexOf(section);
                String ending = (duration == 1 ? ", " : "s, ");

                if (position == 4 && timeparts.get("second") == Integer.MIN_VALUE) {
                    sb.append(ChatColor.YELLOW).append(".");
                    continue;
                }
                // If the second last entry is == 1, we can assume we dont need a break.
                else if (position == 4) {
                    if (newline) ending = (duration == 1 ? "," : "s, \n ");
                    else ending = (duration != 1 ? "s, " : ",");
                }
                // If it is the last entry then add a period.
                else if (position == 5) {
                    if (period) ending = (duration == 1 ? ". " : "s.");
                    else ending = (duration == 1 ? " " : "s");
                }
                sb.append(ChatColor.DARK_GREEN).append(duration).append(ChatColor.YELLOW).append(" ").append(section).append(ending);
            }
        }
        return sb.toString();
    }

    /**
     * @param millis Time in milliseconds to turn into a date.
     * @return Date from milliseconds
     */
    public static String millisToDate(long millis) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date epoch = new Date(millis);
        return format.format(epoch);
    }

    /**
     * @param millis Time in milliseconds to turn into a date.
     * @return Date from milliseconds with hours/min/seconds
     */
    public static String millisToLongDate(long millis) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date epoch = new Date(millis);
        return format.format(epoch);
    }

    /**
     * @param list       String List to turn into comma seperated String.
     * @param chatColour Whether to use ChatColors on commas.
     * @return Comma separated List<String>
     * @author Aberdeener
     */
    public static String listToString(Collection<String> list, boolean chatColour) {

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String word : list) {
            if (first) {
                sb.append(word);
                first = false;
                continue;
            }
            if (chatColour) sb.append(ChatColor.YELLOW).append(", ").append(ChatColor.RESET).append(word);
            else sb.append(", ").append(word);
        }
        return sb.toString();
    }

    /**
     * @param bytes Storage in bytes to convert to human readable kb/mb/gb etc
     * @return Human readable storage
     */
    public static String bytesToReadable(long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " b"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f Kb", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f Mb", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f Gb", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f Tb", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.1f Pb", (bytes >> 10) / 0x1p40)
                : String.format("%.1f Eb", (bytes >> 20) / 0x1p40);
    }

    public static long currentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String humanReadableTime(long seconds) {
        if (seconds == 0) {
            return "None";
        }
        if (seconds < 60) {
            return seconds + "s";
        }
        long minutes = seconds / 60;
        long s = 60 * minutes;
        long secondsLeft = seconds - s;
        if (minutes < 60) {
            if (secondsLeft > 0) {
                return minutes + "m " + secondsLeft + "s";
            }
            return minutes + "m";
        }
        if (minutes < 1440) {
            String time;
            long hours = minutes / 60;
            time = hours + "h";
            long inMins = 60 * hours;
            long leftOver = minutes - inMins;
            if (leftOver >= 1) {
                time = time + " " + leftOver + "m";
            }
            if (secondsLeft > 0) {
                time = time + " " + secondsLeft + "s";
            }
            return time;
        }
        String time;
        long days = minutes / 1440;
        time = days + "d";
        long inMins = 1440 * days;
        long leftOver = minutes - inMins;
        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            } else {
                long hours = leftOver / 60;
                time = time + " " + hours + "h";
                long hoursInMins = 60 * hours;
                long minsLeft = leftOver - hoursInMins;
                time = time + " " + minsLeft + "m";
            }
        }
        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + "s";
        }
        return time;
    }
}
