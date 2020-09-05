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

import me.kate.lobby.Main;
import me.kate.lobby.data.files.JumpPadConfig;

public class JumpPadInteractListener implements Listener {
		
	private JumpPadConfig jumpPadConfig;
	private JumpPad jPad;

	public JumpPadInteractListener(Main plugin) {
		this.jumpPadConfig = new JumpPadConfig(plugin);
		this.jPad = new JumpPad(
				jumpPadConfig.getPower(), 
				jumpPadConfig.getHeight(), 
				jumpPadConfig.getTopBlock(), 
				jumpPadConfig.getBottomBlock(), 
				jumpPadConfig.getSound(), 
				jumpPadConfig.getPitchOne(), 
				jumpPadConfig.getPitchTwo());
	}
	
	@EventHandler
	public void onTrigger(final PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			final Player player = event.getPlayer();
			final Block block = event.getClickedBlock();
			final Location location = block.getLocation();
			final Material material = block.getType();
			if (material.equals(Material.getMaterial(jPad.getTopBlock())) && jPad.validJumpPad(location)) {
				if (jumpPadConfig.isSoundEnabled()) {
					player.playSound(
					player.getLocation(), 
					Sound.valueOf(jPad.getSound()), 
					Float.valueOf(jPad.getPitch1()), 
					Float.valueOf(jPad.getPitch2())); 
				}
				jPad.launch(player);
			}
		}
	}
}
