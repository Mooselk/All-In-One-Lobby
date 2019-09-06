package me.kate.lobby.items.hideplayers.interfaces;

import org.bukkit.entity.Player;

public interface Hideable {
	
	boolean isHidden(Player p);
	
	void hide(Player p);
	
	void unhide(Player p);
	
	void setHidden(boolean hide, Player p);
}
