package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.modules.portals.select.Selection;
import me.kate.lobby.modules.portals.select.SelectionVisualizer;
import me.kate.lobby.utils.Utils;

public class PlayerQuitListener implements Listener {
	
	private JavaPlugin plugin;
	private Selection selection;
	private SelectionVisualizer visualizer;
	
	public PlayerQuitListener(JavaPlugin plugin) {
		this.plugin = plugin;
		this.selection = new Selection();
		this.visualizer = new SelectionVisualizer();
	}
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		
		selection.getSelections().remove(player.getUniqueId());
		visualizer.getIntances().remove(player.getUniqueId());
		
		if (!plugin.getConfig().getString("options.custom-leavemsg").equals("none")) {
			event.setQuitMessage(Utils.replacePlayer(plugin.getConfig().getString("options.custom-leavemsg"), player));
		} else {
			event.setQuitMessage(null);
		}
	}
}
