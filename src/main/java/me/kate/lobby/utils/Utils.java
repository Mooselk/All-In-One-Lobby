package me.kate.lobby.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;

public class Utils {

	private static final Config npcConfig = new NPCConfig();
	
	
	public static String replacePlayer(String in, Player player) {
		String out = in.replaceAll("%player%", player.getName());
		out = ChatColor.translateAlternateColorCodes('&', out);
		return out;
	}

	public static List<String> replaceLore(List<String> lore, int online) {
		ArrayList<String> mlore = null; mlore = new ArrayList<String>();
		for (String l : lore) {
			String out = l;
			if (l.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
			mlore.add(ChatColor.translateAlternateColorCodes('&', out));
		}
		return mlore;
	}

	public static String replace(String in, int online) {
		String out = in;
		if (online != 0) {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
		} else {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", "0");
			}
		}
		return ChatColor.translateAlternateColorCodes('&', out);
	}
	
	public static String color(String color) {
		return ChatColor.translateAlternateColorCodes('&', color);
	}
	
	
	public static ArrayList<String> colorParser(List<String> list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return newList;
	}
	
	// Test first
	
	public static void toConfig(Location location, FileConfiguration config, String path) {
		config.set(path + ".x", location.getBlockX());
		config.set(path + ".y", location.getBlockY());
		config.set(path + ".z", location.getBlockZ());
		config.set(path + ".pitch", location.getPitch());
		config.set(path + ".yaw", location.getYaw());
	}
	
	private static final boolean live = false;
	private static final List<String> defaultHoloText = Arrays.asList("Edit this text in NPC config!", "Players: %players%");
	private static final List<String> defaultMessages = Arrays.asList("&3Default message!", "&9Second line!");
	private static final List<String> equipmentExample = Arrays.asList("helmet:IRON_HELMET:true", "hand:STONE_SWORD");
	
	public static void npcToConfig(Location location, FileConfiguration config, String npcName, int skinId) {
		config.set(npcName + ".id", npcName);
		config.set(npcName + ".skin", skinId);
		config.set(npcName + ".holotext", defaultHoloText);
		config.set(npcName + ".messages", defaultMessages);
		config.set(npcName + ".equipment", equipmentExample);
		config.set(npcName + ".server.live-player-count", live);
		config.set(npcName + ".server.server-name", "example");
		config.set(npcName + ".location.x", location.getBlockX());
		config.set(npcName + ".location.y", location.getBlockY());
		config.set(npcName + ".location.z", location.getBlockZ());
		config.set(npcName + ".location.pitch", location.getPitch());
		config.set(npcName + ".location.yaw", location.getYaw());
		npcConfig.save();
		npcConfig.reload();
	}
	
	
	public static List<String> replaceText(List<String> list, String players) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}
	
	
	public static String replaceTab(List<String> lines, Player player) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : lines) {
			text.add(in.replace("%player%", player.getName()));
		}
		String out = String.join("\n&r", text);	
		return ChatColor.translateAlternateColorCodes('&', out);
	}
	
	public static String getValue(Map<String, String> map, String value) {
		for (Map.Entry<String, String> s : map.entrySet()) {
			if (s.getValue().equalsIgnoreCase(value)) {
				String key = s.getKey();
				return key;
			}
		}
		return null;
	}
	
	public static void reloadConfig(Config config, Player player) {
		if (config.reload()) {
			Messages.send("&f[&6Lobby&f] Successfully reloaded &6" + config.getName() + "&f!", player);
		} else {
			Messages.send("&f[&6Lobby&f] Failed to reload &6" + config.getName() + "&f!", player);
		}
	}
	
	private static final String ERROR = color("&4&lERROR");
	
	public static String replaceStatus(String in, String status) {
		if (status == null) { return ERROR; }
		if (in.contains("%status%") && status != null) {
			return in.replace("%status%", status);
		}
		return " ";
	}

	public static List<String> replaceHoloText(List<String> list, String players) {
		List<String> text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}
}
