package me.kate.lobby.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {
	
	
	/**
	 * @implNote String format "world:x:y:z:pitch:yaw"
	 * @param locString
	 * @return Location
	 */
	
	public static Location fromString(String locString) {
		
		String[] splitLoc = locString.split(":");

		World world = Bukkit.getWorld(splitLoc[0]);
		double x = Double.parseDouble(splitLoc[1]);
		double y = Double.parseDouble(splitLoc[2]);
		double z = Double.parseDouble(splitLoc[3]);
		
		float pitch = Float.parseFloat(splitLoc[4]);
		float yaw = Float.parseFloat(splitLoc[5]);
		
		return new Location(world, x, y, z, pitch, yaw);
	}
	
	/**
	 * @implNote String format "world:x:y:z:pitch:yaw"
	 * @param locString
	 * @return String
	 */
	
	public static String toString(Location location) {
		return location.getWorld().getName() + ":" + 
				location.getX() + ":" + 
				location.getY() + ":" +
				location.getZ() + ":" + 
				location.getPitch() + ":" + 
				location.getYaw();
	}

}
