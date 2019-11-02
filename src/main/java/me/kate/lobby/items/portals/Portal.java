package me.kate.lobby.items.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.items.portals.utils.Cuboid;

public class Portal {

	public Portal() {
	}
	
	public void load() {
		if (!Main.PORTALS.isEmpty()) {
			Main.PORTALS.clear();
		}
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
	
	public void create(Location pos1, Location pos2, String name, String world, String server) {
		PortalsConfig.portalconf.set("portals." + name + ".world", world);
		PortalsConfig.portalconf.set("portals." + name + ".server", server);
		PortalsConfig.portalconf.set("portals." + name + ".loc-1.x", pos1.getBlockX());
		PortalsConfig.portalconf.set("portals." + name + ".loc-1.y", pos1.getBlockY());
		PortalsConfig.portalconf.set("portals." + name + ".loc-1.z", pos1.getBlockZ());
		PortalsConfig.portalconf.set("portals." + name + ".loc-2.x", pos2.getBlockX());
		PortalsConfig.portalconf.set("portals." + name + ".loc-2.y", pos2.getBlockY());
		PortalsConfig.portalconf.set("portals." + name + ".loc-2.z", pos2.getBlockZ());
		PortalsConfig.save();
		PortalsConfig.reload();
		this.reload();
		if (!Main.SELECTIONS.isEmpty()) {
			Main.SELECTIONS.clear();
		}
	}
	
	public void delete(String name) {
		PortalsConfig.portalconf.set("portals." + name, null);
		PortalsConfig.save();
		PortalsConfig.reload();
		this.reloadAll();
	}
}
