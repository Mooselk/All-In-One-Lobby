package me.kate.lobby.modules.portals;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.LocationUtils;
import me.kate.lobby.utils.Utils;

public class Portal {
	
	private static final Map<String, Cuboid> PORTALS = Maps.newHashMap(); 
	
	private PortalsConfig portalConfig;
	
	public Portal() {
		this.portalConfig = new PortalsConfig();
	}
	
	public Map<String, Cuboid> getPortals() {
		return PORTALS;
	}
	
	public void load(boolean reload) {
		Utils.empty(PORTALS);
		if (portalConfig.getSection("portals") == null)
			return;
		
		for (String key : portalConfig.get("portals")) {
			ConfigurationSection section = portalConfig.getSection("portals." + key);
			Location loc1 = LocationUtils.fromString(section.getString("loc-1"));
			Location loc2 = LocationUtils.fromString(section.getString("loc-2"));
			PORTALS.put(key, new Cuboid(loc1, loc2));
		}
	}
	
	public void reloadAll() {
		Utils.empty(PORTALS);
		for (String key : portalConfig.get("portals")) {
			ConfigurationSection section = portalConfig.getSection("portals." + key);
			Location loc1 = LocationUtils.fromString(section.getString("loc-1"));
			Location loc2 = LocationUtils.fromString(section.getString("loc-2"));
			PORTALS.put(key, new Cuboid(loc1, loc2));
		}
	}
	
	public void create(Location pos1, Location pos2, String name, String world, String server, Player player) {		
		portalConfig.toConfig(name, pos2, pos2);
		
		if (server == null)
			server = "none";
		
		portalConfig.setServer(name, server);
		load(true);
	}
}
