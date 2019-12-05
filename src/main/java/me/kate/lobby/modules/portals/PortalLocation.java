package me.kate.lobby.modules.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PortalLocation extends Selections {
	
	private Location location;
	private Position position;
	
	public PortalLocation(Location location, Position position) {
		this.location = location;
		this.position = position;
	}
	
	public Location toLocation(String string) {
		if (string == null || string.trim() == "") {
	        return null;
	    }
	    final String[] parts = string.split(":");
	    if (parts.length == 4) {
	        final World w = Bukkit.getServer().getWorld(parts[0]);
	        final int x = (int) Double.parseDouble(parts[1]);
	        final int y = (int) Double.parseDouble(parts[2]);
	        final int z = (int) Double.parseDouble(parts[3]);
	        return new Location(w, x, y, z);
	    }
	    return null;
	}
	
	public String toPortal() {
		final World world = this.location.getWorld();
		final double x = this.location.getX();
		final double y = this.location.getY();
		final double z = this.location.getZ();
		return this.position 
				+ ":" + world.getName()
				+ ":" + String.valueOf(x) 
				+ ":" + String.valueOf(y) 
				+ ":" + String.valueOf(z);
	}
	
	public Location fromPortal(String portalString, Player player, String position) {
		if (portalString.startsWith(position + ":")) {
			String trimmedPortal = portalString.replace(position + ":", "");
			return toLocation(trimmedPortal);
		}
		return null;
	}
}
