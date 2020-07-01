package me.kate.lobby.listeners.world;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.listeners.SpawnMode;

public class MobSpawnListener implements Listener {

	private PluginConfig config;
	
	public MobSpawnListener(JavaPlugin plugin) {
		this.config = new PluginConfig();
	}

	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		
		if (event.getEntity().getType() != EntityType.PLAYER) {
			return;
		}
		
		if (!config.disableMobSpawning()) {
			return;
		}
		
		final SpawnMode mode = config.getSpawnMode();
		
		switch (mode) {
		
		case NATURAL : {
			if (event.getSpawnReason() == SpawnReason.NATURAL 
					|| event.getSpawnReason() == SpawnReason.DEFAULT) {
				event.setCancelled(true);
			}
			break;
		}
		
		case ALL : {
			if (event.getSpawnReason() == SpawnReason.NATURAL 
					|| event.getSpawnReason() == SpawnReason.DEFAULT 
					|| event.getSpawnReason() == SpawnReason.CUSTOM) {
				event.setCancelled(true);
			}
			break;
		}
		
		case OFF : {
			break;
		}
		
		}
	}
}
