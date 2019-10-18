package me.kate.lobby.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;

public class Spawn {

	private static FileConfiguration c = Main.getInstance().getConfig();
	
	public static Location toSpawn() {
		double x = c.getDouble("spawn.x");
		double y = c.getDouble("spawn.y");
		double z = c.getDouble("spawn.z");
		int yaw = c.getInt("spawn.yaw");
		int pitch = c.getInt("spawn.pitch");
		String world = c.getString("spawn.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
}
