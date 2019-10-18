package me.kate.lobby.events.world;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import me.kate.lobby.Main;

public class PlantGrowthEvent implements Listener {

	@EventHandler
	public void onGrow(final BlockGrowEvent e) {
		if (Main.getInstance().getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			Bukkit.getLogger().info("Stopped plant growth");
			e.setCancelled(true);
		}
	}

	public void onVineGrow(final BlockSpreadEvent e) {
		if (Main.getInstance().getConfig().getConfigurationSection("options").getBoolean("disable-plantGrowth")) {
			Bukkit.getLogger().info("Stopped plant spread");
			e.setCancelled(true);
		}
	}
}
