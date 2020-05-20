package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.utils.LocationUtils;

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
	private Set<String> empty = new HashSet<String>();
	
	public Set<String> get(String path) {
		
		if (getConfig().getConfigurationSection(path) == null) {
			return empty;
		}
		
		return getConfig().getConfigurationSection(path).getKeys(false);
	}
	
	public ConfigurationSection getSection(String path) {
		return getConfig().getConfigurationSection(path);
	}
	
	public List<String> getEquipment(String path) {
		return getConfig().getStringList("npcs." + path + ".equipment");
	}
	
	public List<String> getText(String path) {
		return getConfig().getStringList("npcs." + path + ".holotext");
	}
	
	public List<String> getMessages(String path) {
		return getConfig().getStringList("npcs." + path + ".messages");
	}
	
	public Location getLocation(String name) {
		final ConfigurationSection section = this.getSection("npcs." + name);
		return LocationUtils.fromString(section.getString("location"));
	}
	
	public void refresh() {
		this.save();
		this.reload();
	}
	
	private final boolean live = false;
	private final List<String> defaultHoloText = Arrays.asList("Edit this text in NPC config!", "Players: %players%");
	private final List<String> defaultMessages = Arrays.asList("&3Default message!", "&9Second line!");
	private final List<String> equipmentExample = Arrays.asList("helmet:IRON_HELMET:true", "hand:STONE_SWORD");
	
	public void toConfig(int skinId, Location location, String npcName) {
		this.getConfig().set(npcName + ".skin", skinId);
		this.getConfig().set(npcName + ".holotext", defaultHoloText);
		this.getConfig().set(npcName + ".messages", defaultMessages);
		this.getConfig().set(npcName + ".equipment", equipmentExample);
		this.getConfig().set(npcName + ".server.live-player-count", live);
		this.getConfig().set(npcName + ".server.server-name", "example");
		this.getConfig().set(npcName + ".location", LocationUtils.toString(location));
		this.refresh();
	}
}
