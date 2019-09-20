package me.kate.lobby.items.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;

public class Portal {

	public Portal() {
		
	}
	
	public void load() {
		for (String key : Main.getInstance().getConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("portals." + key);
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
