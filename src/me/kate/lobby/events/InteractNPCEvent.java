package me.kate.lobby.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;

public class InteractNPCEvent implements Listener {
	
	private FileConfiguration c = NPCFile.getNPCConfig();
	
	@EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
		Player p = event.getWhoClicked().getPlayer();
        String id = Main.NPCS.get(event.getNPC().getId());
        
        for (String npc : c.getConfigurationSection("npcs").getKeys(false)) {
        	if (id.equalsIgnoreCase(npc)) {
            	ConfigurationSection section = c.getConfigurationSection("npcs." + npc);
            	String msg = section.getString("message");
            	p.sendMessage(msg);
            }
        }
    }
}
