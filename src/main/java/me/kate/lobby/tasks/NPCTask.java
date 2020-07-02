package me.kate.lobby.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.npcs.hologram.HoloTextHandler;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.objects.LobbyNPC;

public class NPCTask implements Task {

	private static final UUID taskID = UUID.fromString("820c06aa-0e8a-11ea-8d71-362b9e155667");
	private static final int DELAY = 3;

	private HoloTextHandler holotext;
	private JavaPlugin plugin;

	public NPCTask(JavaPlugin plugin) {
		this.plugin = plugin;
		this.holotext = new HoloTextHandler();
	}

	@Override
	public void start() {
		Task.getTasks().put(taskID, Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			
			NPCManager.getAllNPCs().forEach(npc -> {
				LobbyNPC lobbyNPC = LobbyNPC.getById(npc.getId());
				holotext.updateText(lobbyNPC);
			});
			
		}, DELAY * 20, DELAY * 20));
	}

	@Override
	public void stop() {
		BukkitTask refreshTimer = Task.getTasks().remove(taskID);
		if (refreshTimer != null) {
			refreshTimer.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return Task.getTasks().containsKey(taskID);
	}

	@Override
	public UUID getTaskID() {
		return taskID;
	}
}
