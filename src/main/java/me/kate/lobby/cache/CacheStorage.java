package me.kate.lobby.cache;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class CacheStorage extends Config {
	
	private static File skinfile;
	private static FileConfiguration skinstorage;
	
	private Main plugin;
	
	public CacheStorage(Main plugin) {
		this.plugin = plugin;
	}
	
	public static FileConfiguration getSkinStorage() {
		return skinstorage;
	}

	@Override
	public void create() {
		skinfile = new File(plugin.getDataFolder() + "/skins/", "skincache.yml");
		if (!skinfile.exists()) {
			skinfile.getParentFile().mkdirs();
			try {
				skinfile.createNewFile();
				plugin.saveResource("skincache.yml", false);
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
	
	@Override
	public void save() {
		try {
			skinstorage.save(skinfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		try {
			skinstorage.load(skinfile);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return skinstorage;
	}
	
	@Override
	public String getName() {
		return "SkinCache";
	}
	
	@Override
	public Set<String> get(String key) {
		return getConfig().getConfigurationSection(key).getKeys(false);
	}

	@Override
	public ConfigurationSection getSection(String section) {
		return getConfig().getConfigurationSection(section);
	}
	
	public String getValue(int skinId) {
		return getConfig().getString(skinId + ".value");
	}
	
	public String getSignature(int skinId) {
		return getConfig().getString(skinId + ".signature");
	}
}
