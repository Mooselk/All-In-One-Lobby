package me.kate.lobby.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kate.lobby.Main;

public class PlayerLeaveEvents implements Listener {
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		if (!Main.getInstance().getConfig().getString("options.custom-leavemsg").equals("none")) {
			e.setQuitMessage(this.replace(Main.getInstance().getConfig().getString("options.custom-leavemsg"), p));
		}
	}
	
	private String replace(String in, Player p) {
		String out = in.replaceAll("%player%", p.getName());
		out = ChatColor.translateAlternateColorCodes('&', out);
		return out;
	}
}
