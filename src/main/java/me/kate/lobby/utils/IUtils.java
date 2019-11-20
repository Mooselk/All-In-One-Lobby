package me.kate.lobby.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IUtils {
	
	List<String> replaceLore(List<String> lore, int max, int online, int ping, boolean noPing);

	String replace(String in, ConfigurationSection section, int max, int online, int ping);

	ItemStack itemStackBuilder(String mat, ItemStack item, String displayName, List<String> lore, boolean enchanted, int type);
	
	ArrayList<String> colorParser(List<String> list);
	
	String replacePlayer(String in, Player player);
	
	List<String> replaceHoloText(List<String> list, String players);
	
	void toConfig(Location location, FileConfiguration config, String path);
	
	void npcToConfig(Location location, FileConfiguration config, String path, String npcName, int skinId);
	
	String replaceTab(List<String> lines, Player player);
	
}
