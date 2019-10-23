package me.kate.lobby.events.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.kate.lobby.Main;

public class WeatherBlockEvent implements Listener {

	@EventHandler
	public void onWeatherChange(final WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			if (Main.getInstance().getConfig().getConfigurationSection("options").getBoolean("disable-weather")) {
				e.setCancelled(true);
			}
		}
	}
}
