package me.kate.lobby.items.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsFile;

public class Portal {

	public Portal() {
	}
	
	public void load() {
		if (!Main.PORTALS.isEmpty()) {
			Main.PORTALS.clear();
		}
		for (String key : PortalsFile.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = PortalsFile.getPortalConfig().getConfigurationSection("portals." + key);
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
	
	public void reload() {
		for (String key : PortalsFile.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = PortalsFile.getPortalConfig().getConfigurationSection("portals." + key);
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
		for (String key : PortalsFile.getPortalConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = PortalsFile.getPortalConfig().getConfigurationSection("portals." + key);
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
		PortalsFile.portalconf.set("portals." + name + ".world", world);
		PortalsFile.portalconf.set("portals." + name + ".server", server);
		PortalsFile.portalconf.set("portals." + name + ".loc-1.x", pos1.getBlockX());
		PortalsFile.portalconf.set("portals." + name + ".loc-1.y", pos1.getBlockY());
		PortalsFile.portalconf.set("portals." + name + ".loc-1.z", pos1.getBlockZ());
		PortalsFile.portalconf.set("portals." + name + ".loc-2.x", pos2.getBlockX());
		PortalsFile.portalconf.set("portals." + name + ".loc-2.y", pos2.getBlockY());
		PortalsFile.portalconf.set("portals." + name + ".loc-2.z", pos2.getBlockZ());
		PortalsFile.save();
		PortalsFile.reload();
		this.reload();
		if (!Main.SELECTIONS.isEmpty()) {
			Main.SELECTIONS.clear();
		}
	}
	
	public void delete(String name) {
		PortalsFile.portalconf.set("portals." + name, null);
		PortalsFile.save();
		PortalsFile.reload();
		this.reloadAll();
	}
}
