package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class SelectorConfig extends Config {

	private static File selector;
	private static FileConfiguration selectorConfiguration;
	
	@Override
	public void create() {
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		if (!selector.exists()) {
			selector.getParentFile().mkdirs();
			Main.getInstance().saveResource("selector.yml", false);
			try {
				selector.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating selector.yml...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create selector.yml!");
				e.printStackTrace();
			}
		}
		selectorConfiguration = new YamlConfiguration();
		try {
			selectorConfiguration.load(selector);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			selectorConfiguration.save(selector);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean reload() {
		selector = null;
		selectorConfiguration = null;
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		selectorConfiguration = new YamlConfiguration();
		try {
			selectorConfiguration.load(selector);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public FileConfiguration getConfig() {
		return selectorConfiguration;
	}

}
