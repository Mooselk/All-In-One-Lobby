package me.kate.lobby.listeners.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlantGrowthListener implements Listener {
	
	private JavaPlugin plugin;

	public PlantGrowthListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onGrow(final BlockGrowEvent event) {
		if (plugin.getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onVineGrow(final BlockSpreadEvent event) {
		if (plugin.getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			event.setCancelled(true);
		}
	}
}
