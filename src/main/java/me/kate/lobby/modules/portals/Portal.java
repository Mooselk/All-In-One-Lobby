package me.kate.lobby.modules.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;

public class Portal extends Selections {

	private final IUtils utils = new Utils();
	private FileConfiguration config = PortalsConfig.portalconf;
	
	public void load() {
		if (!Main.PORTALS.isEmpty()) { Main.PORTALS.clear(); }
		if (PortalsConfig.getPortalConfig().getConfigurationSection("portals") != null) {
			for (String key : PortalsConfig.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
				final ConfigurationSection section = PortalsConfig.getPortalConfig().getConfigurationSection("portals." + key);
				final Location loc1 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-1.x"),
						section.getInt("loc-1.y"),
						section.getInt("loc-1.z"));
				final Location loc2 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-2.x"),
						section.getInt("loc-2.y"),
						section.getInt("loc-2.z"));
				Main.PORTALS.put(key, new Cuboid(loc1, loc2));
			}
		}
	}
	
	public void reload() {
		for (String key : PortalsConfig.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = PortalsConfig.getPortalConfig().getConfigurationSection("portals." + key);
			if (!Main.PORTALS.containsKey(key)) {
				final Location loc1 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-1.x"),
						section.getInt("loc-1.y"),
						section.getInt("loc-1.z"));
				final Location loc2 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-2.x"),
						section.getInt("loc-2.y"),
						section.getInt("loc-2.z"));
				Main.PORTALS.put(key, new Cuboid(loc1, loc2));
			}
		}
	}
	
	public void reloadAll() {
		if (!Main.PORTALS.isEmpty()) {
			Main.PORTALS.clear();
		}
		for (String key : PortalsConfig.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = PortalsConfig.getPortalConfig().getConfigurationSection("portals." + key);
				final Location loc1 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-1.x"),
						section.getInt("loc-1.y"),
						section.getInt("loc-1.z"));
				final Location loc2 = new Location(Bukkit.getWorld(
						section.getString("world")),
						section.getInt("loc-2.x"),
						section.getInt("loc-2.y"),
						section.getInt("loc-2.z"));
				Main.PORTALS.put(key, new Cuboid(loc1, loc2));
			}
		}
	
	public void create(Location pos1, Location pos2, String name, String world, String server, Player player) {
		utils.toConfig(pos1, PortalsConfig.getPortalConfig(), "portals." + name + ".loc-1");
		utils.toConfig(pos1, PortalsConfig.getPortalConfig(), "portals." + name + ".loc-2");
		
		PortalsConfig.getPortalConfig().set("portals." + name + ".world", world);
		PortalsConfig.getPortalConfig().set("portals." + name + ".server", server);
		PortalsConfig.save();
		PortalsConfig.reload();
		this.reload();
		super.clearSelection(player);
	}
	
	public void delete(String name) {
		config.set("portals." + name, null);
		PortalsConfig.save();
		PortalsConfig.reload();
		this.reloadAll();
	}
}
