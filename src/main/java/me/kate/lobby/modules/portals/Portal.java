package me.kate.lobby.modules.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;

public class Portal extends Selections {

	private final IUtils utils = new Utils();
	
	private Config portalConfig = new PortalsConfig();
	
	public void load(boolean reload) {
		if (!Main.getInstance().getPortals().isEmpty() && reload) { Main.getInstance().getPortals().clear(); }
		if (portalConfig.getConfig().getConfigurationSection("portals") != null) {
			for (String key : portalConfig.getConfig().getConfigurationSection("portals").getKeys(false)) {
				final ConfigurationSection section = portalConfig.getConfig().getConfigurationSection("portals." + key);
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
				Main.getInstance().getPortals().put(key, new Cuboid(loc1, loc2));
			}
		}
	}
	
	public void reloadAll() {
		if (!Main.getInstance().getPortals().isEmpty()) {
			Main.getInstance().getPortals().clear();
		}
		for (String key : portalConfig.getConfig().getConfigurationSection("portals").getKeys(false)) {
			final ConfigurationSection section = portalConfig.getConfig().getConfigurationSection("portals." + key);
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
				Main.getInstance().getPortals().put(key, new Cuboid(loc1, loc2));
			}
		}
	
	public void create(Location pos1, Location pos2, String name, String world, String server, Player player) {
		utils.toConfig(pos1, portalConfig.getConfig(), "portals." + name + ".loc-1");
		utils.toConfig(pos1, portalConfig.getConfig(), "portals." + name + ".loc-2");
		
		portalConfig.getConfig().set("portals." + name + ".world", world);
		portalConfig.getConfig().set("portals." + name + ".server", server);
		portalConfig.save();
		portalConfig.reload();
		load(true);
		clearSelection(player);
	}
	
	public void delete(String name) {
		portalConfig.getConfig().set("portals." + name, null);
		portalConfig.save();
		portalConfig.reload();
		this.reloadAll();
	}
}
