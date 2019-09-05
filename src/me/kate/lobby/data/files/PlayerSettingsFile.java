package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class PlayerSettingsFile {

	private File playerSettings;
	private FileConfiguration playerSettingsConf;

	public PlayerSettingsFile() {

	}

	public void create() {
		playerSettings = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		if (!playerSettings.exists()) {
			playerSettings.getParentFile().mkdirs();
			try {
				playerSettings.createNewFile();
				Bukkit.getLogger().info("Creating playersettings...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("Failed to create playersettings.yml!");
				e.printStackTrace();
			}
		}
		playerSettingsConf = new YamlConfiguration();
		try {
			playerSettingsConf.load(playerSettings);
			Bukkit.getLogger().info("[NorthernStars] Loading playerdata file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[NorthernStars] Failed to load playerdata.yml!");
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			playerSettingsConf.save(playerSettings);
		} catch (IOException e) {
			Bukkit.getLogger().severe("Failed to save playersettings.yml!");
			e.printStackTrace();
		}
	}
	
	public void reload() {
		try {
			playerSettingsConf.load(playerSettings);
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("Error updating playersettings.yml!");
			e.printStackTrace();
		}
	}
}
