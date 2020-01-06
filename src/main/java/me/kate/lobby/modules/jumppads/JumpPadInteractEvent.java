package me.kate.lobby.modules.jumppads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.JumpPadConfig;

public class JumpPadInteractEvent implements Listener {
	
	private Config jumpPadConfig = new JumpPadConfig();

	private String top = jumpPadConfig.getConfig().getString("settings.top-block");
	private String bottom = jumpPadConfig.getConfig().getString("settings.bottom-block");
	private String sound = jumpPadConfig.getConfig().getString("settings.sound");
	private boolean enable_sound = jumpPadConfig.getConfig().getBoolean("jumppad.sound");
	private double power = jumpPadConfig.getConfig().getDouble("settings.power");
	private double height = jumpPadConfig.getConfig().getDouble("settings.height");
	
	private String pitch1 = jumpPadConfig.getConfig().getString("settings.pitch1");
	private String pitch2 = jumpPadConfig.getConfig().getString("settings.pitch2");

	private JumpPad pad = new JumpPad(power, height, top, bottom, sound, pitch1, pitch2);

	@EventHandler
	public void onTrigger(final PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			final Player player = event.getPlayer();
			final Block block = event.getClickedBlock();
			final Location location = block.getLocation();
			final Material mat = block.getType();
			if (mat.equals(Material.getMaterial(pad.getTopBlock())) && pad.validJumpPad(location)) {
				if (enable_sound) {
					player.playSound(player.getLocation(), Sound.valueOf(pad.getSound()), Float.valueOf(pad.getPitch1()), Float.valueOf(pad.getPitch2()));
				}
				pad.launchIdiot(player);
			}
		}
	}
}
