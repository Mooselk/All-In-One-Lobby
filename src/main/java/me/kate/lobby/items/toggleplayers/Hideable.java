package me.kate.lobby.items.toggleplayers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface Hideable {
	
	boolean isHidden(Player p);
	
	void hide(Player p, ConfigurationSection section);
	
	void unhide(Player p, ConfigurationSection section);
	
	void setHidden(boolean hide, Player p);
}
