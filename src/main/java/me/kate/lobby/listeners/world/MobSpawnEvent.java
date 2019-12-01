package me.kate.lobby.listeners.world;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSpawnEvent implements Listener {
	
	private JavaPlugin plugin;

	public MobSpawnEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER && event.getSpawnReason() == SpawnReason.NATURAL) {
			if (plugin.getConfig().getBoolean("options.disable-mobSpawning")) {
				event.setCancelled(true);
			}
		}
	}
}
