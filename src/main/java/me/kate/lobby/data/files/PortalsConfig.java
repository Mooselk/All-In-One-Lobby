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

public class PortalsConfig extends Config {
	
	public static File portalfile;
	public static FileConfiguration portalconf;

	@Override
	public void create() {
		portalfile = new File(Main.getInstance().getDataFolder(), "portals.yml");
		if (!portalfile.exists()) {
			portalfile.getParentFile().mkdirs();
			Main.getInstance().saveResource("portals.yml", false);
			Bukkit.getLogger().info("[Lobby] Creating portals.yml...");
		}
		portalconf = new YamlConfiguration();
		try {
			portalconf.load(portalfile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to create portals.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		try {
			portalconf.load(portalfile);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to reload portals.yml!");
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public void save() {
		try {
			portalconf.save(portalfile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save portals.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public Set<String> get(String key) {
		return getConfig().getConfigurationSection(key).getKeys(false);
	}

	@Override
	public ConfigurationSection getSection(String section) {
		return getConfig().getConfigurationSection(section);
	}

	@Override
	public FileConfiguration getConfig() {
		return portalconf;
	}
	
	@Override
	public String getName() {
		return "Portals";
	}
	
	public void refresh() {
		this.save();
		this.reload();
	}
}
