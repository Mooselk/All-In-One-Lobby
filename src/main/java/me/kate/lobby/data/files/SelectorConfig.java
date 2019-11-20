package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;

public class SelectorConfig implements ISelectorSettings {
	
	private static File selector;
	private static FileConfiguration selectorConf;
	private boolean success;
	
	
	public SelectorConfig() {
	}
	
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
		selectorConf = new YamlConfiguration();
		try {
			selectorConf.load(selector);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
		selector = null;
		selectorConf = null;
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		selectorConf = new YamlConfiguration();
		try {
			selectorConf.load(selector);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		try {
			selectorConf.save(selector);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save selector.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		selector = null;
		selectorConf = null;
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		selectorConf = new YamlConfiguration();
		try {
			selectorConf.load(selector);
			success = true;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	@Override
	public boolean reloadFailed() {
		return success;
	}

	@Override
	public FileConfiguration getSelectorFile() {
		this.load();
		return selectorConf;
	}
}
