package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class PortalsFile {
	
	public static File portalfile;
	public static FileConfiguration portalconf;
	
	public static FileConfiguration getPortalConfig() {
		forceLoad();
		return portalconf;
	}

	public static void create() {
		portalfile = new File(Main.getInstance().getDataFolder(), "portals.yml");
		if (!portalfile.exists()) {
			portalfile.getParentFile().mkdirs();
			Main.getInstance().saveResource("portals.yml", false);
			Bukkit.getLogger().info("[Lobby] Creating portals...");
		}
		portalconf = new YamlConfiguration();
		try {
			portalconf.load(portalfile);
			Bukkit.getLogger().info("[Lobby] Loading portals file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to create portals.yml!");
			e.printStackTrace();
		}
	}
	public static void save() {
		try {
			portalconf.save(portalfile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save portals.yml!");
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		try {
			portalconf.load(portalfile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to reload portals.yml!");
			e.printStackTrace();
		}
	}
	
	public static void forceLoad() {
		portalfile = new File(Main.getInstance().getDataFolder(), "portals.yml");
		portalconf = new YamlConfiguration();
		try {
			portalconf.load(portalfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
