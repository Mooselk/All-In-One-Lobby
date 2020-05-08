package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class NPCConfig extends Config {
	
	private static File npcFile;
	private static FileConfiguration npcConfiguration;

	@Override
	public void create() {
		npcFile = new File(Main.getInstance().getDataFolder(), "npcs.yml");
		if (!npcFile.exists()) {
			npcFile.getParentFile().mkdirs();
			Main.getInstance().saveResource("npcs.yml", false);
			Bukkit.getLogger().info("[Lobby] Creating npcs.yml...");
		}
		npcConfiguration = new YamlConfiguration();
		try {
			npcConfiguration.load(npcFile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to create npcs.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		try {
			npcConfiguration.save(npcFile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save npcs.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		try {
			npcConfiguration.load(npcFile);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to reload npcs.yml!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return npcConfiguration;
	}
	
	@Override
	public String getName() {
		return "NPCs";
	}
	
	/**
	 * @param path
	 * @return Set<String>
	 * Get configuration keys
	 */
	public Set<String> get(String path) {
		return npcConfiguration.getConfigurationSection(path).getKeys(false);
	}
	
	public ConfigurationSection getSection(String path) {
		return npcConfiguration.getConfigurationSection(path);
	}
	
	public String getServer(String name) {
		return null;
	}
	
	public Location getLocation(String name) {
		final ConfigurationSection section = this.getSection("npcs." + name);
		Location location = new Location(Bukkit.getWorld("world"), 
				section.getDouble("location.x"),
				section.getDouble("location.y"), 
				section.getDouble("location.z"));
		location.setPitch(section.getInt("location.pitch"));
		location.setYaw(section.getInt("location.yaw"));
		return location;
	}
}
