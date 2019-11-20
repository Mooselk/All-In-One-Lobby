package me.kate.lobby.listeners.world;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobSpawnEvent implements Listener {
	
	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER 
				&& event.getSpawnReason() == SpawnReason.NATURAL) {
			event.setCancelled(true);
		}
	}
}
