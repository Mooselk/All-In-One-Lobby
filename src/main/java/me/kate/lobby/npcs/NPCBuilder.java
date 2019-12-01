package me.kate.lobby.npcs;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.cache.SkinCache;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;
import me.kate.lobby.npcs.api.state.NPCSlot;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.servers.ServerManager;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class NPCBuilder extends NPCRegistry {

	private final IUtils utils = new Utils();
	private final Messages msgs = new Messages();
	
	private FileConfiguration config = NPCConfig.getNPCConfig();
	private SkinCache skinCache = new SkinCache();
	private ServerManager servers = new ServerManager();

	private JavaPlugin plugin;
	
	public NPCBuilder(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void create(int skinId, String name, Location location, Player player) {
		utils.npcToConfig(location, NPCConfig.getNPCConfig(), "npcs." + name, name, skinId);
	}

	public void loadNPCsFor(Player player) {
		for (Map.Entry<String, NPC> npcs : getNPCObjects().entrySet()) {
			NPC npc = npcs.getValue();
			Logger.debug("Showing NPC " + npc);
			if (!npc.isShown(player)) { npc.show(player); }
		}
	}

	public void buildNPC() {
		if (config.getConfigurationSection("npcs") != null) {
			for (final String name : config.getConfigurationSection("npcs").getKeys(false)) {
				final ConfigurationSection section = config.getConfigurationSection("npcs." + name);
				int skinId = section.getInt("skin");
				NPC npc = Main.getInstance().getNPCLib()
						.createNPC(utils.replaceHoloText(section.getStringList("holotext"), "0"));
				Location loc = new Location(Bukkit.getWorld("world"),
						section.getDouble("location.x"),
						section.getDouble("location.y"),
						section.getDouble("location.z"));
				loc.setPitch(section.getInt("location.pitch"));
				loc.setYaw(section.getInt("location.yaw"));
				npc.setLocation(loc);
				if (!skinCache.isCached(skinId)) {
					MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
						Logger.debug("Caching new skin '" + skinId + "'");
						skinCache.toConfig(skin, skinId);
						npc.setSkin(skin);
					});
				} else {
					npc.setSkin(skinCache.getSkin(skinId));
					Logger.debug("Loaded NPC '" + name + "' from skin cache");
				}
				applyItems(npc, name);
				npc.create();
				Bukkit.getScheduler().runTask(plugin, () -> addToRegistry(npc, name));
			}
		}
	}

	public void applyItems(NPC npc, String name) {
		for (String items : config.getStringList("npcs." + name + ".equipment")) {
			if (items.startsWith("helmet:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("helmet:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.HELMET, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("helmet:", "");
					npc.setItem(NPCSlot.HELMET, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
			if (items.startsWith("chestplate:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("chestplate:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.CHESTPLATE, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("chestplate:", "");
					npc.setItem(NPCSlot.CHESTPLATE, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
			if (items.startsWith("leggings:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("leggings:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.LEGGINGS, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("leggings:", "");
					npc.setItem(NPCSlot.LEGGINGS, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
			if (items.startsWith("boots:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("boots:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.BOOTS, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("boots:", "");
					npc.setItem(NPCSlot.BOOTS, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
			if (items.startsWith("hand:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("hand:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.MAINHAND, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("hand:", "");
					npc.setItem(NPCSlot.MAINHAND, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
			if (items.startsWith("offhand:")) {
				if (items.endsWith(":true")) {
					String material = items.replace("offhand:", "");
					material = material.replace(":true", "");
					npc.setItem(NPCSlot.OFFHAND, new ItemBuilder(
							Material.getMaterial(material))
							.addEnchant(Enchantment.DURABILITY, 1).toItemStack());
				} else {
					String material = items.replace("offhand:", "");
					npc.setItem(NPCSlot.OFFHAND, new ItemBuilder(
							Material.getMaterial(material)).toItemStack());
				}
			}
		}
	}
	
	public void reloadNPCs(Player player, boolean msg) {
		destroyAll(player);
		clearRegistry();
		NPCConfig.reload();
		servers.loadServers();
		buildNPC();
		new BukkitRunnable() {
			@Override
			public void run() {
				showAll(true, player);
				if (msg) { msgs.send("&f[&6NPC&f] Reload complete!", player); }
			}
		}.runTaskLater(plugin, 3);
	}
	
	public void setSkin() {
		
	}

	public void move(Location location, String name, Player player) {
		config.set("npcs." + name + ".location", null);
		utils.toConfig(location, config, "npcs." + name + ".location");
		NPCConfig.save();
		NPCConfig.reload();
		reloadNPCs(player, false);
		msgs.send("Moved NPC " + name, player);
	}

	public void destroy(String name) {
		if (Main.getInstance().getRegistry().getNPCInfo().containsValue(name)) {
			NPC npcs = getNPCById(getValue(getNPCInfo(), name));
			npcs.destroy();
		}
	}

	public void destroyAll(Player player) {
		for (Map.Entry<String, NPC> name : getNPCObjects().entrySet()) {
			NPC npc = name.getValue();
			npc.destroy();
		}
		clearRegistry();
	}

	public void showAll(boolean update, Player player) {
		if (update) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				for (NPC npc : NPCManager.getAllNPCs()) {
					if (!npc.isShown(online)) { npc.show(online); }
				}
			}
		}
		if (!update && player != null) {
			for (NPC npc : NPCManager.getAllNPCs()) {
				if (!npc.isShown(player)) { npc.show(player); }
			}
		}
	}
}
