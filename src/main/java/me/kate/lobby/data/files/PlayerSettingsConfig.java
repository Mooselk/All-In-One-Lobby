package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;

public class PlayerSettingsConfig implements IPlayerSettings {

	public static File playerSettings;
	public static FileConfiguration playerSettingsConf;

	public PlayerSettingsConfig() {
	}

	@Override
	public void create() {
		playerSettings = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		if (!playerSettings.exists()) {
			playerSettings.getParentFile().mkdirs();
			try {
				playerSettings.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating playersettings...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create playersettings.yml!");
				e.printStackTrace();
			}
		}
		playerSettingsConf = new YamlConfiguration();
		try {
			playerSettingsConf.load(playerSettings);
			Bukkit.getLogger().info("[Lobby] Loading playerdata file...");
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
	public void reload() {
		playerSettings = null;
		playerSettingsConf = null;
		playerSettings = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		playerSettingsConf = new YamlConfiguration();
		try {
			playerSettingsConf.load(playerSettings);
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("[Lobby] Error updating playersettings.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public FileConfiguration getPlayerSettings() {
		return playerSettingsConf;
	}

	@Override
	public boolean sectionExists(String section) {
		if (playerSettingsConf.getConfigurationSection(section) != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void createSection(String section) {
		playerSettingsConf.createSection(section);
		playerSettingsConf.getConfigurationSection(section).set("hidden", false);
	}
}