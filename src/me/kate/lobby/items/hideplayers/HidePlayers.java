package me.kate.lobby.items.hideplayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.kate.lobby.items.hideplayers.interfaces.Hideable;

public class HidePlayers implements Hideable {
	
	private boolean hidden;

	public HidePlayers() {
		
	}

	@Override
	public boolean isHidden(Player p) {
		hidden = false; // get players setting
		return hidden;
	}

	@Override
	public void hide(Player p) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(online);
		}
		// set player settings to true
	}

	@Override
	public void unhide(Player p) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			p.showPlayer(online);
		}
		// set player settings to false
	}

	// might be useless lol
	@Override
	public void setHidden(boolean hide, Player p) {
		if (hide) {
			
		}
		if (!hide) {
			
		}
	}
}
