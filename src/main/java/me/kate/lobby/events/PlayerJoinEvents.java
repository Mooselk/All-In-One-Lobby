package me.kate.lobby.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.Spawn;
import me.kate.lobby.items.hideplayers.HidePlayers;
import me.kate.lobby.items.hideplayers.interfaces.Hideable;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class PlayerJoinEvents implements Listener {

	private ISelectorSettings sf = new SelectorFile();
	private IHidePlayerSettings hf = new HidePlayersFile();
	private Hideable hp = new HidePlayers();
	private FileConfiguration cs = sf.getSelectorFile();
	private FileConfiguration hc = hf.getHideSettings();
	private IPlayerSettings ps = new PlayerSettingsFile();
	private final IUtils utils = new Utils();

	private NPCBuilder npcb = new NPCBuilder();

	private ConfigurationSection hideSection = hc.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hc.getConfigurationSection("item.unhide");
	private boolean npc;

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();
		if (!Main.getInstance().getConfig().getString("options.custom-joinmsg").equals("none")) {
			e.setJoinMessage(utils.replacePlayer(Main.getInstance().getConfig().getString("options.custom-joinmsg"), p));
		}
		if (!npc) {
			npcb.build(p);
			npc = true;
		}
		npcb.showAll(false, p);
		if (!ps.sectionExists(p.getUniqueId().toString())) {
			ps.createSection(p.getUniqueId().toString());
			ps.getPlayerSettings().getConfigurationSection(p.getUniqueId().toString()).set("hidden", false);
			ps.save();
		}
		p.teleport(Spawn.toSpawn());
	}

	@EventHandler
	public void hideOnJoin(final PlayerJoinEvent e) {
		final Player login = (Player) e.getPlayer();
		ConfigurationSection hSection;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			hSection = ps.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
			if (hSection.getBoolean("hidden")) {
				player.hidePlayer(login);
			}
		}
	}

	private ItemStack hide = new ItemBuilder(Material.getMaterial(hideSection.getString("material")), 1)
			.setName(ChatColor.translateAlternateColorCodes('&', hideSection.getString("name")))
			.toItemStack();
	
	private ItemStack unhide = new ItemBuilder(Material.getMaterial(unhideSection.getString("material")))
			.setName(ChatColor.translateAlternateColorCodes('&', unhideSection.getString("name")))
			.toItemStack();
	
	private ItemStack selector = new ItemBuilder(
			Material.getMaterial(cs.getConfigurationSection("selector.options").getString("material")))
					.setName(ChatColor.translateAlternateColorCodes('&', cs.getConfigurationSection("selector.options").getString("item-name")))
					.setLore(utils.colorParser(cs.getConfigurationSection("selector.options").getStringList("lore")))
					.toItemStack();

	@EventHandler
	public void giveItemsOnJoin(final PlayerJoinEvent e) {
		final Player p = (Player) e.getPlayer();
		p.getInventory().clear();
		ConfigurationSection hSection = ps.getPlayerSettings().getConfigurationSection(p.getUniqueId().toString());
		if (!hSection.getBoolean("hidden")) {
			p.getInventory().setItem(2, hide);
		}
		if (hSection.getBoolean("hidden")) {
			p.getInventory().setItem(2, unhide);
			hp.setHidden(true, p);
		}
		if (cs.getConfigurationSection("selector.options").getBoolean("enabled")) {
			ConfigurationSection section = cs.getConfigurationSection("selector.options");
			if (!p.getInventory().contains(Material.getMaterial(section.getString("material")), 1)) {
				p.getInventory().setItem(section.getInt("slot"), selector);
			}
		}
	}
	
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		ItemStack i = e.getCurrentItem();
		if (i == null) {
			return;
		}
		if (i.equals(selector)) {
			e.setCancelled(true);
		}
		if (i.equals(hide)) {
			e.setCancelled(true);
		}
		if (i.equals(unhide)) {
			e.setCancelled(true);
		}
	}
}
