package me.kate.lobby.modules.portals.select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

import me.kate.lobby.Main;
import me.kate.lobby.tasks.Task;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.particles.ParticleEffect;

public class SelectionVisualizer implements Task {
	
	private static final Map<UUID, SelectionVisualizer> INSTANCES = Maps.newHashMap();
	private static final Set<UUID> VISUALIZING = new HashSet<>();
	
	private Main plugin;
	private Player player;
	private Location cornerA;
	private Location cornerB;
	private UUID uuid;
	
	public SelectionVisualizer() {
	}
	
	public SelectionVisualizer(Main plugin) {
		this.plugin = plugin;
	}
	
	public SelectionVisualizer(Main plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		this.uuid = player.getUniqueId();
		INSTANCES.put(uuid, this);
	}
	
	public SelectionVisualizer(Main plugin, Player player, Location cornerA, Location cornerB) {
		this.plugin = plugin;
		this.player = player;
		this.uuid = player.getUniqueId();
		this.cornerA = cornerA;
		this.cornerB = cornerB;
		INSTANCES.put(uuid, this);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public UUID getUUID() {
		return player.getUniqueId();
	}
	
	public void setCornerA(Location cornerA) {
		this.cornerA = cornerA;
	}
	
	public Location getCornerA() {
		return cornerA;
	}
	
	public void setCornerB(Location cornerB) {
		this.cornerB = cornerB;
	}
	
	public Location getCornerB() {
		return cornerB;
	}
	
	public Map<UUID, SelectionVisualizer> getIntances() {
		return INSTANCES;
	}
	
	public SelectionVisualizer getIntance(UUID uuid) {
		return INSTANCES.get(uuid);
	}
	
	public Set<UUID> getVisualizing() {
		return VISUALIZING;
	}
	
	public boolean isVisualizing() {
		return VISUALIZING.contains(uuid);
	}
	
	public boolean isVisualizing(UUID uuid) {
		return VISUALIZING.contains(uuid);
	}
	
	public void setVisualizing() {
		if (!VISUALIZING.contains(uuid)) {
			VISUALIZING.add(uuid);
		} else {
			VISUALIZING.remove(uuid);
		}
	}
	
	public void setVisualizing(UUID uuid) {
		if (!VISUALIZING.contains(uuid)) {
			VISUALIZING.add(uuid);
		} else {
			VISUALIZING.remove(uuid);
		}
	}
	
	public List<Location> gatherPoints(Location cornerA, Location cornerB, double particleDistance) {
        
		if (cornerA == null && cornerB != null) {
			cornerB = cornerA;
		}
		
		if (cornerB == null && cornerA != null) {
			cornerA = cornerB;
		}
		
		if (cornerB == null && cornerA == null) {
			return null;
		}
		
		List<Location> result = new ArrayList<Location>();
        
        World world = cornerA.getWorld();
        double minX = Math.min(cornerA.getX(), cornerB.getX());
        double minY = Math.min(cornerA.getY(), cornerB.getY());
        double minZ = Math.min(cornerA.getZ(), cornerB.getZ());
        double maxX = Math.max(cornerA.getX() + 1, cornerB.getX() + 1);
        double maxY = Math.max(cornerA.getY() + 1, cornerB.getY() + 1);
        double maxZ = Math.max(cornerA.getZ() + 1, cornerB.getZ() + 1);
     
        for (double x = minX; x <= maxX; x+=particleDistance) {
            for (double y = minY; y <= maxY; y+=particleDistance) {
                for (double z = minZ; z <= maxZ; z+=particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
     
        return result;
    }
	
	public void spawnParticles() {
		List<Location> points = gatherPoints(cornerA, cornerB, 0.25);
			
		if (points == null) {
			return;
		}
		
		for (int i = 0; i < points.size(); i++) {
			ParticleEffect.REDSTONE.display(points.get(i), 0f, 0f, 0f, 0f, 1, null);
		}
	}
	
	public void update() {
		stop(); 
		start();
	}
	
	@Override
	public void start() {
		BukkitTask task = Task.getTasks().get(uuid);
		if (task == null) {
			task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
				
				this.spawnParticles();
				
			}, 3, 3);
			Logger.info("running " + task.getTaskId());
			Task.getTasks().put(uuid, task);
		} else {
			stop(); 
			start();
		}
	}

	@Override
	public void stop() {
		BukkitTask task = Task.getTasks().remove(uuid);
		if (task != null) {
			task.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return Task.getTasks().containsKey(uuid);
	}

	@Override
	public UUID getTaskID() {
		return uuid;
	}
}
