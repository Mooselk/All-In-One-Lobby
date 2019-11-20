package me.kate.lobby.listeners.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import me.kate.lobby.Main;

public class PlantGrowthEvent implements Listener {

	@EventHandler
	public void onGrow(final BlockGrowEvent event) {
		if (Main.getInstance().getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onVineGrow(final BlockSpreadEvent event) {
		if (Main.getInstance().getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			event.setCancelled(true);
		}
	}
}
