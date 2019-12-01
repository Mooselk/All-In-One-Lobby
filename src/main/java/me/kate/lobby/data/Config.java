package me.kate.lobby.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class Config {

	private static File conf;
	private static FileConfiguration config;
	private static boolean success;

	public static void create() {
		conf = new File(Main.getInstance().getDataFolder(), "config.yml");
		if (!conf.exists()) {
			conf.getParentFile().mkdirs();
			Main.getInstance().saveResource("config.yml", false);
		}
		config = new YamlConfiguration();
		try {
			config.load(conf);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean reload() {
		conf = null;
		config = null;
		conf = new File(Main.getInstance().getDataFolder(), "config.yml");
		config = new YamlConfiguration();
		try {
			config.load(conf);
			success = true;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
}