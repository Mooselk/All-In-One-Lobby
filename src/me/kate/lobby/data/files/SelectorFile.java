package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;

public class SelectorFile implements ISelectorSettings {
	
	private File selector;
	private FileConfiguration selectorConf;
	private boolean failed;
	
	
	public SelectorFile() {
	}
	
	@Override
	public void create() {
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		if (!selector.exists()) {
			selector.getParentFile().mkdirs();
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
			Bukkit.getLogger().info("[Lobby] Loading selector file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
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
		this.load();
		try {
			selectorConf.save(selector);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save selector.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void reload() {
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		selectorConf = new YamlConfiguration();
		try {
			selectorConf.load(selector);
			failed = false;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			failed = true;
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reloadFailed() {
		return failed;
	}

	@Override
	public FileConfiguration getSelectorFile() {
		this.load();
		return selectorConf;
	}
}
