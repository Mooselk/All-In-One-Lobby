package me.kate.lobby.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;

public class Spawn {

	private static FileConfiguration config = Main.getInstance().getConfig();
	
	public static Location getSpawn() {
		double x = config.getDouble("spawn.x");
		double y = config.getDouble("spawn.y");
		double z = config.getDouble("spawn.z");
		int yaw = config.getInt("spawn.yaw");
		int pitch = config.getInt("spawn.pitch");
		String world = config.getString("options.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
	
	public static void setSpawn(Player player) {
		Location loc = player.getLocation();
		config.set("options.world", player.getWorld().getName());
		config.set("spawn.x", loc.getBlockX());
		config.set("spawn.y", loc.getBlockY());
		config.set("spawn.z", loc.getBlockZ());
		config.set("spawn.yaw", loc.getYaw());
		config.set("spawn.pitch", loc.getPitch());
		Messages.get().send("&f[&6Lobby&f] Set spawn in world '&6" 
		+ player.getWorld().getName() 
		+ "&f' at X: &6" + loc.getBlockX() 
		+ "&f Y: &6" + loc.getBlockY() 
		+ "&f Z: &6" + loc.getBlockZ(), player);
		Main.getInstance().saveConfig();
		Main.getInstance().reloadConfig();
	}
	
	public static void toSpawn(Player player) {
		player.teleport(getSpawn());
		Messages.get().send("&f[&6Lobby&f] Teleporting to spawn...", player);
	}
}
