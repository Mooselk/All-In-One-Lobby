package me.kate.lobby.items.pads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumpPad {
	
	private double height;
	private double power;
	private String topBlock;
	private String bottomBlock;
	private String sound;
	
	public JumpPad(double power, double height, String top, String bottom, String sound) {
		this.height = height;
		this.power = power;
		this.topBlock = top;
		this.bottomBlock = bottom;
		this.sound = sound;
	}

	public void launchIdiot(final Player player) {
		Vector vector = player.getLocation().getDirection().multiply(this.power).setY(this.height);
		Location location = player.getLocation();
		player.teleport(location.add(0, 0.3, 0));
		player.setVelocity(vector);
	}

	public boolean validJumpPad(final Location location) {
		final Block block = location.subtract(0, 1, 0).getBlock();
		if (block.getType().equals(Material.getMaterial(this.getBottomBlock()))) {
			return true;
		}
		return false;
	}
	
	public String getTopBlock() {
		return topBlock;
	}
	
	public String getBottomBlock() {
		return bottomBlock;
	}
	
	public String getSound() {
		return sound;
	}
}
