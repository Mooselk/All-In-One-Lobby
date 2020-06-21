package me.kate.lobby.modules.portals;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.LocationUtils;

public class Portal {
	
	private PortalsConfig portalConfig;
	
	public Portal() {
		this.portalConfig = new PortalsConfig();
	}
	
	public void load(boolean reload) {
		if (!Main.getInstance().getPortals().isEmpty() && reload) 
			Main.getInstance().getPortals().clear();
		
		if (portalConfig.getSection("portals") == null)
			return;
		for (String key : portalConfig.get("portals")) {
			ConfigurationSection section = portalConfig.getSection("portals." + key);
			Location loc1 = LocationUtils.fromString(section.getString("loc-1"));
			Location loc2 = LocationUtils.fromString(section.getString("loc-2"));
			Main.getInstance().getPortals().put(key, new Cuboid(loc1, loc2));
		}
	}
	
	public void reloadAll() {
		if (!Main.getInstance().getPortals().isEmpty())
			Main.getInstance().getPortals().clear();
		for (String key : portalConfig.get("portals")) {
			ConfigurationSection section = portalConfig.getSection("portals." + key);
			Location loc1 = LocationUtils.fromString(section.getString("loc-1"));
			Location loc2 = LocationUtils.fromString(section.getString("loc-2"));
			Main.getInstance().getPortals().put(key, new Cuboid(loc1, loc2));
		}
	}
	
	public void create(Location pos1, Location pos2, String name, String world, String server, Player player) {		
		portalConfig.getConfig().set("portals." + name + ".loc-1", LocationUtils.toString(pos1));
		portalConfig.getConfig().set("portals." + name + ".loc-2", LocationUtils.toString(pos2));
		portalConfig.getConfig().set("portals." + name + ".world", world);
		
		if (server == null) {
			server = "none";
		}
		
		portalConfig.getConfig().set("portals." + name + ".server", server);
		portalConfig.refresh();
		load(true);
		// clearSelection(player);
	}
	
	public void delete(String name) {
		portalConfig.getConfig().set("portals." + name, null);
		portalConfig.refresh();
		this.reloadAll();
	}
}
