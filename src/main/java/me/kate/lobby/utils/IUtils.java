package me.kate.lobby.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public interface IUtils {
	
	List<String> replaceLore(List<String> lore, int max, int online);

	String replace(String in, int max, int online, int ping);
	
	String color(String color);
	
	ArrayList<String> colorParser(List<String> list);
	
	String replacePlayer(String in, Player player);
	
	List<String> replaceHoloText(List<String> list, String players);
	
	void toConfig(Location location, FileConfiguration config, String path);
	
	void npcToConfig(Location location, FileConfiguration config, String path, String npcName, int skinId);
	
	String replaceTab(List<String> lines, Player player);
	
}
