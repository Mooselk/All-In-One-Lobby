package me.kate.lobby.modules.portals.select;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.collect.Maps;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;

public class Selection {

	private UUID uuid;
	private Location pos1;
	private Location pos2;
	private PortalsConfig config;
	
	private static final Map<UUID, Selection> SELECTIONS = Maps.newHashMap();
	
	public Selection(UUID uuid, Main plugin) {
		this.uuid = uuid;
		this.config = new PortalsConfig(plugin);
		SELECTIONS.put(uuid, this);
	}
	
	public Selection() {
	}
	
	public UUID getPlayerUUID() {
		return uuid;
	}
	
	public void setPos1(Location pos1) {
		this.pos1 = pos1;
	}
	
	public Location getPos1() {
		return pos1;
	}
	
	public void setPos2(Location pos2) {
		this.pos2 = pos2;
	}
	
	public Location getPos2() {
		return pos2;
	}
	
	public Map<UUID, Selection> getSelections() {
		return SELECTIONS;
	}
	
	public Selection getSelection(UUID uuid) {
		return SELECTIONS.get(uuid);
	}
	
	public void clear() {
		SELECTIONS.remove(uuid);
	}
	
	public void clear(UUID uuid) {
		SELECTIONS.remove(uuid);
	}
	
	public enum Position {
		
		POS1,
		POS2;
		
	}
	
	public boolean saveToConfig(String key) {
		if (pos1 != null && pos2 != null) {
			
			String[] locations = {
				this.toString(pos1, Position.POS1),
				this.toString(pos2, Position.POS2)
			};
			
			config.setPositions(key, locations);	
			return true;
		} 
		return false;
	}
	
	public String toString(Location location, Position pos) {
		final World world = location.getWorld();
		final double x = location.getX();
		final double y = location.getY();
		final double z = location.getZ();
		return pos 
				+ ":" + world.getName()
				+ ":" + String.valueOf(x) 
				+ ":" + String.valueOf(y) 
				+ ":" + String.valueOf(z);
	}
	
	public Location toLocation(String string) {
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
}
