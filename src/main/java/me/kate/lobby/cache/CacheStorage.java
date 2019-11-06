package me.kate.lobby.cache;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class CacheStorage {
	
	private static File skinfile;
	private static FileConfiguration skinstorage;
	
	public static FileConfiguration getSkinStorage() {
		return skinstorage;
	}

	public static void create() {
		skinfile = new File(Main.getInstance().getDataFolder() + "/skins/", "skincache.yml");
		if (!skinfile.exists()) {
			skinfile.getParentFile().mkdirs();
			try {
				skinfile.createNewFile();
				Main.getInstance().saveResource("skincache.yml", false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		skinstorage = new YamlConfiguration();
		try {
			skinstorage.load(skinfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	public static void save() {
		try {
			skinstorage.save(skinfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		try {
			skinstorage.load(skinfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void forceLoad() {
		skinfile = new File(Main.getInstance().getDataFolder() + "/skins/", "skincache.yml");
		skinstorage = new YamlConfiguration();
		try {
			skinstorage.load(skinfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
