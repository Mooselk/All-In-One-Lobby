package me.kate.lobby.events.world;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;

public class TouchVoidEvent implements Listener {

	private FileConfiguration config = Main.getInstance().getConfig();
	private int timer;

	@EventHandler
	public void onVoidTouch(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.VOID) {
				e.setCancelled(true);
				p.teleport(spawn());
			}
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (p.getLocation().getBlockY() < 1) {
			p.teleport(spawn());
			timer = 0;
			new BukkitRunnable() {
				@Override
				public void run() {
					counter();
					if (timer == 5) {
						cancel();
					}
					p.getWorld().spigot().playEffect(p.getLocation(), Effect.CLOUD, 0, 0, 1.0f, 2.0f, 1.0f, 0.0f, 6, 2);
				}
			}.runTaskTimer(Main.getInstance(), 1, 1);
		}
	}

	private void counter() {
		timer++;
	}

	private Location spawn() {
		double x = config.getDouble("spawn.x");
		double y = config.getDouble("spawn.y");
		double z = config.getDouble("spawn.z");
		int yaw = config.getInt("spawn.yaw");
		int pitch = config.getInt("spawn.pitch");
		String world = config.getString("spawn.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
}
