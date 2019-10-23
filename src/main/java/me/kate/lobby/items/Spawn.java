package me.kate.lobby.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.utils.Messages;

public class Spawn {

	private static FileConfiguration c = Main.getInstance().getConfig();
	
	private static final Messages MSGS = new Messages();
	
	public static Location toSpawn() {
		double x = c.getDouble("spawn.x");
		double y = c.getDouble("spawn.y");
		double z = c.getDouble("spawn.z");
		int yaw = c.getInt("spawn.yaw");
		int pitch = c.getInt("spawn.pitch");
		String world = c.getString("options.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
	
	public static void setSpawn(Player player, Location loc) {
		c.set("options.world", player.getWorld().getName());
		c.set("spawn.x", loc.getBlockX());
		c.set("spawn.y", loc.getBlockY());
		c.set("spawn.z", loc.getBlockZ());
		c.set("spawn.yaw", loc.getYaw());
		c.set("spawn.pitch", loc.getPitch());
		MSGS.send("&f[&6Lobby&f] Set spawn in world '&6" 
		+ player.getWorld().getName() 
		+ "&f' at X: &6" + loc.getBlockX() 
		+ "&f Y: &6" + loc.getBlockY() 
		+ "&f Z: &6" + loc.getBlockZ(), player);
		Main.getInstance().saveConfig();
		Main.getInstance().reloadConfig();
	}
}
