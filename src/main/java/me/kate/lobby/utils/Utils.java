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

public class Utils implements IUtils {

	private final Config npcConfig = new NPCConfig();
	
	@Override
	public String replacePlayer(String in, Player player) {
		String out = in.replaceAll("%player%", player.getName());
		out = ChatColor.translateAlternateColorCodes('&', out);
		return out;
	}

	@Override
	public List<String> replaceLore(List<String> lore, int max, int online) {
		ArrayList<String> mlore = null; mlore = new ArrayList<String>();
		for (String l : lore) {
			String out = l;
			if (l.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
			if (l.contains("%max%")) {
				out = out.replaceAll("%max%", String.valueOf(max));
			}
			mlore.add(ChatColor.translateAlternateColorCodes('&', out));
		}
		return mlore;
	}

	@Override
	public String replace(String in, int max, int online) {
		String out = in;
		if (max != 0 || online != 0) {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
			if (in.contains("%max%")) {
				out = out.replaceAll("%max%", String.valueOf(max));
			}
		} else {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", "0");
			}
			if (in.contains("%max%")) {
				out = out.replaceAll("%max%", "0");
			}
			if (in.contains("%ping%")) {
				out = out.replaceAll("%ping%", "Offline");
			}
		}
		return ChatColor.translateAlternateColorCodes('&', out);
	}
	
	@Override
	public String color(String color) {
		return ChatColor.translateAlternateColorCodes('&', color);
	}
	
	@Override
	public ArrayList<String> colorParser(List<String> list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return newList;
	}
	
	// Test first
	@Override
	public void toConfig(Location location, FileConfiguration config, String path) {
		config.set(path + ".x", location.getBlockX());
		config.set(path + ".y", location.getBlockY());
		config.set(path + ".z", location.getBlockZ());
		config.set(path + ".pitch", location.getPitch());
		config.set(path + ".yaw", location.getYaw());
	}
	
	private final boolean live = false;
	private final List<String> defaultHoloText = Arrays.asList("Edit this text in NPC config!", "Players: %players%");
	private final List<String> defaultMessages = Arrays.asList("&3Default message!", "&9Second line!");
	private final List<String> equipmentExample = Arrays.asList("helmet:IRON_HELMET:true", "hand:STONE_SWORD");
	
	@Override
	public void npcToConfig(Location location, FileConfiguration config, String path, String npcName, int skinId) {
		config.set(path + ".id", npcName);
		config.set(path + ".skin", skinId);
		config.set(path + ".holotext", defaultHoloText);
		config.set(path + ".messages", defaultMessages);
		config.set(path + ".equipment", equipmentExample);
		config.set(path + ".server.live-player-count", live);
		config.set(path + ".server.server-name", "example");
		config.set(path + ".location.x", location.getBlockX());
		config.set(path + ".location.y", location.getBlockY());
		config.set(path + ".location.z", location.getBlockZ());
		config.set(path + ".location.pitch", location.getPitch());
		config.set(path + ".location.yaw", location.getYaw());
		npcConfig.save();
		npcConfig.reload();
	}
	
	@Override
	public List<String> replaceHoloText(List<String> list, String players) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}
	
	@Override
	public String replaceTab(List<String> lines, Player player) {
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
}
