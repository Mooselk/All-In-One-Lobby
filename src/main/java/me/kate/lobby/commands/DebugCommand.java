package me.kate.lobby.commands;

import java.util.Map;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.select.SelectionVisualizer;
import me.kate.lobby.utils.Logger;

public class DebugCommand  implements CommandExecutor{

	private Portal portal;
	private SelectionVisualizer vis;
	
	public DebugCommand(Main plugin) {
		this.portal = new Portal(plugin);
		this.vis = new SelectionVisualizer();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		
		if (sender instanceof Player) {
			sender.sendMessage("[Debug] See console for output.");
		}
		
		if (args.length == 0 ) {
			sender.sendMessage("/debug <portal, selector, npcs> dump");
			return true;
		}
		
		switch (args[0]) {
		
			case "portals": {
				
				if (args[1].equalsIgnoreCase("dump")) {
					
					Logger.info("Portals: ");
					printMap(portal.getPortals());
					
					Logger.info("Visualizer: ");
					printSet(vis.getVisualizing());
					
				}
				
				return true;
			}
			
		}
		
		return true;
	}

	private void printMap(Map<?,?> map) {
		map.forEach((key, value) -> {
			System.out.print(key);
			System.out.print(value);
		});
	}
	
	private void printSet(Set<?> set) {
		set.forEach(uuid -> {
			System.out.print(uuid);
		});
	}
	
}
