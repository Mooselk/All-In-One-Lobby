package me.kate.lobby.listeners.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.modules.Spawn;

public class TouchVoidListener implements Listener {

	public TouchVoidListener(JavaPlugin plugin) { }
	
	@EventHandler
	public void onVoidTouch(final EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			
			final Player player = (Player) event.getEntity();
			
			if (event.getCause() == DamageCause.VOID) {
				event.setCancelled(true);
				player.teleport(Spawn.getSpawn());
			}
			
			if (event.getCause() == DamageCause.FALL) {
				event.setCancelled(true);
			}
		}
	}
}
