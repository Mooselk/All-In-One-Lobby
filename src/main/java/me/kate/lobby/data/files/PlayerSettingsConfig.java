package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class PlayerSettingsConfig extends Config {

	public static File playerSettings;
	public static FileConfiguration playerSettingsConf;

	@Override
	public void create() {
		playerSettings = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		if (!playerSettings.exists()) {
			playerSettings.getParentFile().mkdirs();
			try {
				playerSettings.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating playersettings.yml...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create playersettings.yml!");
				e.printStackTrace();
			}
		}
		playerSettingsConf = new YamlConfiguration();
		try {
			playerSettingsConf.load(playerSettings);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load playersettings.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			playerSettingsConf.save(playerSettings);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save playersettings.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		playerSettings = null;
		playerSettingsConf = null;
		playerSettings = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		playerSettingsConf = new YamlConfiguration();
		try {
			playerSettingsConf.load(playerSettings);
			return true;
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("[Lobby] Error updating playersettings.yml!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return playerSettingsConf;
	}
	
	@Override
	public Set<String> get(String key) {
		return getConfig().getConfigurationSection(key).getKeys(false);
	}

	@Override
	public ConfigurationSection getSection(String section) {
		return getConfig().getConfigurationSection(section);
	}

	public boolean sectionExists(String section) {
		if (playerSettingsConf.getConfigurationSection(section) != null) {
			return true;
		}
		return false;
	}
	
	public void createSection(String section) {
		playerSettingsConf.createSection(section);
		playerSettingsConf.getConfigurationSection(section).set("hidden", false);
	}
}
