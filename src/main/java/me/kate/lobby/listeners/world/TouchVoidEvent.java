package me.kate.lobby.listeners.world;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.modules.Spawn;

public class TouchVoidEvent implements Listener {

	private int timer;

	private JavaPlugin plugin;

	public TouchVoidEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVoidTouch(final EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (event.getCause() == DamageCause.VOID) {
				event.setCancelled(true);
				player.teleport(Spawn.toSpawn());
			}
			if (event.getCause() == DamageCause.FALL) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		if (player.getLocation().getBlockY() < 1) {
			player.teleport(Spawn.toSpawn());
			timer = 0;
			new BukkitRunnable() {
				@Override
				public void run() {
					counter();
					if (timer == 5) {
						cancel();
					}
					player.getWorld().spigot().playEffect(player.getLocation(), Effect.CLOUD, 0, 0, 1.0f, 2.0f, 1.0f, 0.0f, 6, 2);
				}
			}.runTaskTimer(plugin, 1, 1);
		}
	}

	private void counter() {
		timer++;
	}
}
