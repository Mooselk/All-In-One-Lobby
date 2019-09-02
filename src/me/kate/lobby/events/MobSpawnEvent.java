package me.kate.lobby.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobSpawnEvent implements Listener {
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e) {
		if (e.getEntity().getType() != EntityType.PLAYER && e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}
}
