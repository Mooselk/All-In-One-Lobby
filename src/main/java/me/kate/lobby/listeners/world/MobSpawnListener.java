package me.kate.lobby.listeners.world;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSpawnListener implements Listener {

	private JavaPlugin plugin;

	public MobSpawnListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER) {
			if (event.getSpawnReason() != SpawnReason.CUSTOM || event.getSpawnReason() == SpawnReason.DEFAULT) {
				if (plugin.getConfig().getBoolean("options.disable-mobSpawning")) {
					event.setCancelled(true);
				}
			}
		}
	}
}
