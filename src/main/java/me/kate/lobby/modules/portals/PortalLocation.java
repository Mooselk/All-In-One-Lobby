package me.kate.lobby.modules.portals;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PortalLocation {
	
	private Location location;
	private Position position;
	
	public PortalLocation(Location location, Position position) {
		this.location = location;
		this.position = position;
	}
	
//	public void test() {
//		Logger.debug(this.toPortal());
//	}
	
	public Location toLocation(String string) {
		if (string == null || string.trim() == "") {
	        return null;
	    }
	    final String[] parts = string.split(":");
	    if (parts.length == 4) {
	        final World w = Bukkit.getServer().getWorld(parts[0]);
	        final int x = Integer.parseInt(parts[1]);
	        final int y = Integer.parseInt(parts[2]);
	        final int z = Integer.parseInt(parts[3]);
	        return new Location(w, x, y, z);
	    }
	    return null;
	}
	
	public String toPortal() {
		World world = this.location.getWorld();
		double x = this.location.getX();
		double y = this.location.getY();
		double z = this.location.getZ();
		return this.position 
				+ ":" + String.valueOf(x) 
				+ ":" + String.valueOf(y) 
				+ ":" + String.valueOf(z) 
				+ ":" + world;
	}
	
	public Location fromPortal(Map <UUID, String> map, Player player, String position) {
		String portalString = map.get(player.getUniqueId());
		if (portalString.startsWith(Position.valueOf(position) + ":")) {
			portalString.replace(position + ":", "");
			return this.toLocation(portalString);
		}
		return null;
	}
}
