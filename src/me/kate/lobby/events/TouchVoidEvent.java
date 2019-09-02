package me.kate.lobby.events;

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

	private FileConfiguration c = Main.getInstance().getConfig();
	private int timer;

	@EventHandler
	public void onVoidTouch(EntityDamageEvent e) {
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
	public void onMove(PlayerMoveEvent e) {
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

	public void counter() {
		timer++;
	}

	private Location spawn() {
		double x = c.getDouble("spawn.x");
		double y = c.getDouble("spawn.y");
		double z = c.getDouble("spawn.z");
		int yaw = c.getInt("spawn.yaw");
		int pitch = c.getInt("spawn.pitch");
		String world = c.getString("spawn.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
}
