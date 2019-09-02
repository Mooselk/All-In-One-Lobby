package me.kate.lobby.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.kate.lobby.Main;

public class JoinEvent implements Listener {

	private FileConfiguration c = Main.getInstance().getConfig();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();
		p.teleport(spawn());
	}

	@EventHandler
	public void giveItemsOnJoin(PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();
		if (c.getConfigurationSection("compass.options").getBoolean("enabled")) {
			ConfigurationSection section = c.getConfigurationSection("compass.options");
			if (!p.getInventory().contains(Material.COMPASS, 1)) {
				p.getInventory().setItem(section.getInt("slot"), giveCompass(section.getString("item-name"), section.getStringList("lore")));
			}
		}
		if (c.getConfigurationSection("hideplayers.options").getBoolean("enabled")) {
			if (!p.getInventory().contains(Material.REDSTONE_TORCH_ON, 1)
					|| !p.getInventory().contains(Material.REDSTONE_TORCH_OFF, 1)) {
				
			}
		}
	}

	private ItemStack giveCompass(String displayName, List<String> lore) {
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		im.setLore(colorLore(lore));
		item.setItemMeta(im);
		return item;
	}

	private ItemStack giveHideTool(String displayName, ArrayList<String> lore) {
		ItemStack item = new ItemStack(Material.REDSTONE_TORCH_ON);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		im.setLore(colorLore(lore));
		item.setItemMeta(im);
		return item;
	}
	
	private List<String> colorLore(List<String> lore) {
		List<String> nlore = new ArrayList<String>();
		for (String l : lore) {
			nlore.add(ChatColor.translateAlternateColorCodes('&', l));
		}
		return nlore;
	}

	private Location spawn() {
		double x = c.getDouble("spawn.x");
		double y = c.getDouble("spawn.y");
		double z = c.getDouble("spawn.z");
		int yaw = c.getInt("spawn.yaw");
		int pitch = c.getInt("spawn.pitch");
		String world = c.getString("spawn.world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
}
