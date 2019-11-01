package me.kate.lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kate.lobby.Main;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class PlayerLeaveEvents implements Listener {
	
	private final IUtils utils = new Utils();
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (!Main.getInstance().getConfig().getString("options.custom-leavemsg").equals("none")) {
			event.setQuitMessage(utils.replacePlayer(Main.getInstance().getConfig().getString("options.custom-leavemsg"), player));
		}
	}
}
