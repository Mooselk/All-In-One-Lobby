package me.kate.lobby.listeners.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WeatherBlockEvent implements Listener {
	
	private JavaPlugin plugin;

	public WeatherBlockEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onWeatherChange(final WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			if (plugin.getConfig().getConfigurationSection("options").getBoolean("disable-weather")) {
				event.setCancelled(true);
			}
		}
	}
}
