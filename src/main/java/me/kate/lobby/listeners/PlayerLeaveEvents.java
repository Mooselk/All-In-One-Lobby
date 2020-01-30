package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;

public class PlayerLeaveEvents implements Listener {
	
	private IUtils utils = new Utils();
	
	private JavaPlugin plugin;
	
	public PlayerLeaveEvents(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (!plugin.getConfig().getString("options.custom-leavemsg").equals("none")) {
			event.setQuitMessage(utils.replacePlayer(plugin.getConfig().getString("options.custom-leavemsg"), player));
		}
	}
}
