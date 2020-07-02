package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.modules.portals.select.Selection;
import me.kate.lobby.modules.portals.select.SelectionVisualizer;
import me.kate.lobby.utils.Utils;

public class PlayerQuitListener implements Listener {
	
	private PluginConfig config;
	private Selection selection;
	private SelectionVisualizer visualizer;
	
	public PlayerQuitListener(JavaPlugin plugin) {
		this.config = new PluginConfig();
		this.selection = new Selection();
		this.visualizer = new SelectionVisualizer();
	}
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		
		selection.getSelections().remove(player.getUniqueId());
		visualizer.getIntances().remove(player.getUniqueId());
		
		event.setQuitMessage(Utils.replacePlayer(config.getLeaveMessage(), player));
	}
}
