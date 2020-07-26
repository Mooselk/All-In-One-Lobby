package me.kate.lobby.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;

public class Utils {
	
	/**
	 * 
	 * @param string to replace
	 * @param player
	 * @return string with playername in place of %player%
	 */
	public static String replacePlayer(String in, Player player) {
		String out = in.replaceAll("%player%", player.getName());
		out = color(out);
		return out;
	}

	public static List<String> replaceLore(List<String> lore, String online) {
		ArrayList<String> mlore = null; mlore = new ArrayList<String>();
		for (String l : lore) {
			String out = l;
			if (l.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
			mlore.add(color(out));
		}
		return mlore;
	}

	public static String replace(String in, String online) {
		String out = in;
		if (Integer.valueOf(online) != 0) {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
		} else {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", "0");
			}
		}
		return color(out);
	}
	
	public static String color(String color) {
		return ChatColor.translateAlternateColorCodes('&', color);
	}
	
	/**
	 * 
	 * @param List<String> list
	 * @return list
	 */
	public static ArrayList<String> colorParser(List<String> list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(color(line));
		}
		return newList;
	}
	
	/**
	 * 
	 * @param String[] list
	 * @return list
	 */
	public static ArrayList<String> colorParser(String[] list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(color(line));
		}
		return newList;
	}
	
	/**
	 * Replace player count placeholder with player count
	 * 
	 * @param list
	 * @param players
	 * @return list
	 */
	
	public static List<String> replace(List<String> list, String players) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(color(in.replace("%players%", players)));
		}
		return text;
	}
	
	
	/**
	 * Replace player count placeholder with player name
	 * 
	 * @param list
	 * @param players
	 * @return list
	 */
	public static String replaceTab(List<String> lines, Player player) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : lines) {
			text.add(in.replace("%player%", player.getName()));
		}
		String out = String.join("\n&r", text);	
		return color(out);
	}
	
	private static final String ERROR = color("&4&lERROR");
	
	public static String replaceStatus(String in, String status) {
		if (status == null) { return ERROR; }
		if (in.contains("%status%") && status != null) {
			return in.replace("%status%", status);
		}
		return " ";
	}
	
	
	/**
	 * 
	 * @param player
	 * @param server
	 * 
	 * Sends player to specfied server
	 */
	public static void send(Player player, String server) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (IOException | NullPointerException e) {
			Bukkit.getLogger().severe("Error sending player to server!");
			e.printStackTrace();
		}
		Bukkit.getPlayer(player.getName()).sendPluginMessage(Main.getInstance(), "BungeeCord", byteStream.toByteArray());
	}
	
	/**
	 * Empty a map without knowing it's types
	 * @param map
	 */
	public static void empty(Map<?,?> map) {
		if (!map.isEmpty())
			map.clear();
	}
}
