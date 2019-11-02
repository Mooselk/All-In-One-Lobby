package me.kate.lobby.items.pads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.files.JumpPadConfig;

public class JumpPadInteractEvent implements Listener {

	private String top = JumpPadConfig.getPadConfig().getString("settings.top-block");
	private String bottom = JumpPadConfig.getPadConfig().getString("settings.bottom-block");
	private String sound = JumpPadConfig.getPadConfig().getString("settings.sound");
	private boolean enable_sound = JumpPadConfig.getPadConfig().getBoolean("jumppad.sound");
	private double power = JumpPadConfig.getPadConfig().getDouble("settings.power");
	private double height = JumpPadConfig.getPadConfig().getDouble("settings.height");

	private JumpPad pad = new JumpPad(power, height, top, bottom, sound);

	@EventHandler
	public void onTrigger(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			final Player player = event.getPlayer();
			final Block block = event.getClickedBlock();
			final Location location = block.getLocation();
			final Material mat = block.getType();
			if (mat.equals(Material.getMaterial(pad.getTopBlock())) 
					&& pad.validJumpPad(location)) {
				if (enable_sound) {
					player.playSound(player.getLocation(), 
							Sound.valueOf(pad.getSound()), 1.0f, 1.0f);
				}
				pad.launchIdiot(player);
			}
		}
	}
}
