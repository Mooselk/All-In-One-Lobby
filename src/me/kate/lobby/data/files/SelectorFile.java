package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class SelectorFile {
	private File selector;
	private FileConfiguration selectorConf;

	public SelectorFile() {

	}

	public void create() {
		selector = new File(Main.getInstance().getDataFolder() + "/data/", "playersettings.yml");
		if (!selector.exists()) {
			selector.getParentFile().mkdirs();
			try {
				selector.createNewFile();
				Bukkit.getLogger().info("Creating playersettings...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("Failed to create playersettings.yml!");
				e.printStackTrace();
			}
		}
		selectorConf = new YamlConfiguration();
		try {
			selectorConf.load(selector);
			Bukkit.getLogger().info("[NorthernStars] Loading playerdata file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[NorthernStars] Failed to load playerdata.yml!");
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			selectorConf.save(selector);
		} catch (IOException e) {
			Bukkit.getLogger().severe("Failed to save playersettings.yml!");
			e.printStackTrace();
		}
	}
	
	public void reload() {
		try {
			selectorConf.load(selector);
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("Error updating playersettings.yml!");
			e.printStackTrace();
		}
	}
}
