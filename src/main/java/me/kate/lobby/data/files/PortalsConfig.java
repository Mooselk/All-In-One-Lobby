package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
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

public class PortalsConfig extends Config {
	
	private Main plugin;
	public static File portalfile;
	public static FileConfiguration portalconf;

	public PortalsConfig(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void create() {
		portalfile = new File(plugin.getDataFolder(), "portals.yml");
		if (!portalfile.exists()) {
			portalfile.getParentFile().mkdirs();
			plugin.saveResource("portals.yml", false);
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
	
	public ConfigurationSection getPortal(String key) {
		return getSection("portals." + key);
	}
	
	public String getServer(String key) {
		return getConfig().getString("portals." + key + ".server");
	}
	
	public void setServer(String key, String server) {
		getConfig().set("portals." + key + ".server", server);
		refresh();
	}
	
	public boolean delete(String name) {
		if (getPortal(name) != null) {
			getConfig().set("portals." + name, null);
			refresh();
			return true;
		}
		return false;
	}
	
	public void setPositions(String key, String[] loc) {
		getConfig().set("portals." + key + ".positions", loc);
	}
	
	public void deletePositions(String key) {
		getConfig().set("portals." + key + ".positions", null);
	}
	
	public String[] getPositions(String key) {
		List<String> list = getConfig().getStringList("portals." + key + ".positions");
		return (String[]) list.toArray();
	}
	
	public void toConfig(String name, Location pos1, Location pos2) {
		getConfig().set("portals." + name + ".loc-1", LocationUtils.toString(pos1));
		getConfig().set("portals." + name + ".loc-2", LocationUtils.toString(pos2));
		getConfig().set("portals." + name + ".world", pos1.getWorld().getName());
		refresh();
	}
	
	public void refresh() {
		this.save();
		this.reload();
	}
}
