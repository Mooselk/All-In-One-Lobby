package me.kate.lobby.events;

import java.util.ArrayList;
import java.util.Arrays;
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
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.utils.ItemBuilder;

public class JoinEvent implements Listener {

	private ISelectorSettings sf = new SelectorFile();
	private IHidePlayerSettings hf = new HidePlayersFile();
	private FileConfiguration cs = sf.getSelectorFile();
	private FileConfiguration hc = hf.getHideSettings();
	private FileConfiguration c = Main.getInstance().getConfig();
	private IPlayerSettings ps = new PlayerSettingsFile();
	
	
	private ConfigurationSection hideSection = hc.getConfigurationSection("item.hide");
//	private ConfigurationSection unhideSection = hc.getConfigurationSection("item.unhide");

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();
		
		
		
		if (!ps.sectionExists(p.getUniqueId().toString())) {
			ps.createSection(p.getUniqueId().toString());
			ps.getPlayerSettings().getConfigurationSection(p.getUniqueId().toString()).addDefault("hidden", false);
			ps.save();
		}
		
		p.teleport(spawn());
	}

	private ItemStack hide = new ItemBuilder(Material.getMaterial(hideSection.getString("material")))
			.name(ChatColor.translateAlternateColorCodes('&', hideSection.getString("name")))
			.lores(hideSection.getStringList("lore"))
			.make();
//	private ItemStack unhide = new ItemBuilder(Material.getMaterial(unhideSection.getString("material")))
//			.name(ChatColor.translateAlternateColorCodes('&', unhideSection.getString("name")))
//			.lores(colorLore(unhideSection.getStringList("lore")))
//			.make();

	@EventHandler
	public void giveItemsOnJoin(PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();

		if (!p.getInventory().contains(hide)) {
			p.getInventory().setItem(2, hide);
		}

		if (cs.getConfigurationSection("selector.options").getBoolean("enabled")) {
			ConfigurationSection section = cs.getConfigurationSection("selector.options");
			if (!p.getInventory().contains(Material.getMaterial(section.getString("material")), 1)) {
				p.getInventory().setItem(section.getInt("slot"),
						giveSelector(section.getString("item-name"), section.getStringList("lore")));
			}
		}
	}

	private ItemStack giveSelector(String displayName, List<String> lore) {
		ItemStack item = new ItemStack(
				Material.getMaterial(cs.getConfigurationSection("selector.options").getString("material")));
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
	
	public void genNPC(Player p) {
		NPC npc = Main.getInstance().getNPCLib().createNPC(Arrays.asList(ChatColor.WHITE + "Hi there (#2)", ChatColor.YELLOW + "Click on me!"));
        npc.setLocation(new Location(Bukkit.getWorld("world"), -13.5, 61, 0.5));
        Main.IDS.add(npc.getId());
        Bukkit.getLogger().info("IDS: " +  Main.IDS);
        npc.create();
        npc.show(p);
	}
	
	@EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        String id = Main.NPCS.get(event.getNPC().getId());
        if (id.equals("Factions")) {
        	event.getWhoClicked().sendMessage(ChatColor.GREEN + "niqqa ill beat yo ass in factions");
        }
        if (id.equals("SkyBlock")) {
        	event.getWhoClicked().sendMessage(ChatColor.GREEN + "im an intellegtual i lige skyblock");
        }
    }
}
