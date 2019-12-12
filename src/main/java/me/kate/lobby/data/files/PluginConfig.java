package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class PluginConfig extends Config {

	private static File conf;
	private static FileConfiguration configuration;
	
	@Override
	public void create() {
		conf = new File(Main.getInstance().getDataFolder(), "config.yml");
		if (!conf.exists()) {
			conf.getParentFile().mkdirs();
			Main.getInstance().saveResource("config.yml", false);
		}
		configuration = new YamlConfiguration();
		try {
			configuration.load(conf);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		Main.getInstance().saveConfig();
	}

	@Override
	public boolean reload() {
		Main.getInstance().reloadConfig();
		// TO-DO
		return true;
	}

	@Override
	public FileConfiguration getConfig() {
		return configuration;
	}

}
