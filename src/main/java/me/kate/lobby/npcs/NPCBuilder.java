package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
	private NPCConfig npcConfig = new NPCConfig();
	private SkinCache skinCache = new SkinCache();
	private ServerManager servers = new ServerManager();

	private List<String> npcList = null;
	
	private JavaPlugin plugin;

	public NPCBuilder(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void create(int skinId, String name, Location location, Player player) {
		utils.npcToConfig(location, npcConfig.getConfig(), "npcs." + name, name, skinId);
	}

	// test
	public void buildNPC(String name) {
		if (npcConfig.getSection("npcs") == null) { return; }
		final ConfigurationSection section = npcConfig.getSection("npcs." + name);
		int skinId = section.getInt("skin");
		NPC npc = Main.getNPCLib().createNPC(utils.replaceHoloText(section.getStringList("holotext"), "0"));
		if (!skinCache.isCached(skinId)) {
			MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> { 																		Logger.debug("Caching new skin '" + skinId + "'");
				skinCache.toConfig(skin, skinId);
				npc.setSkin(skin);
			});
		} else npc.setSkin(skinCache.getSkin(skinId)); 																					Logger.debug("Loaded NPC '" + name + "' from skin cache");
		applyItems(npc, name);
		npc.setLocation(getNPCLocation(name));
		npc.create();
		Bukkit.getScheduler().runTask(plugin, () -> addToRegistry(npc, name));
	}

	public void buildNPC() {
		if (npcConfig.getSection("npcs") == null) { return; }
		for (final String name : npcConfig.get("npcs")) {
			final ConfigurationSection section = npcConfig.getSection("npcs." + name);
			int skinId = section.getInt("skin");
			NPC npc = Main.getNPCLib().createNPC(utils.replaceHoloText(section.getStringList("holotext"), "0"));
			if (!skinCache.isCached(skinId)) {
				MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> { 																	Logger.debug("Caching new skin '" + skinId + "'");
					skinCache.toConfig(skin, skinId);
					npc.setSkin(skin);
				});
			} else npc.setSkin(skinCache.getSkin(skinId));																				Logger.debug("Loaded NPC '" + name + "' from skin cache");
			applyItems(npc, name);
			npc.setLocation(getNPCLocation(name));
			npc.create();
			Bukkit.getScheduler().runTask(plugin, () -> addToRegistry(npc, name));
		}
	}

	public void applyItems(NPC npc, String name) {
		for (String items : npcConfig.getConfig().getStringList("npcs." + name + ".equipment")) {
			String[] parts = items.split(":");
			if (parts.length > 2 && parts[2].equals("true"))
				npc.setItem(NPCSlot.getSlot(parts[0]
						.toUpperCase()), new ItemBuilder(Material
						.getMaterial(parts[1]))
						.addEnchant(Enchantment.DURABILITY, 1)
						.toItemStack());
			else 
				npc.setItem(NPCSlot.getSlot(parts[0]
						.toUpperCase()), new ItemBuilder(Material
						.getMaterial(parts[1]))
						.toItemStack());
		}
	}

	// test
	public void reloadNPC(Player player, String npc, boolean msg) {
		destroy(npc);
		npcConfig.reload();
		buildNPC(npc);
		servers.loadNPCAssosiation(npc);
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			showAll(player);
			if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
		}, 3);
	}

	public void reloadNPCs(Player player, boolean msg) {
		destroyAll();
		clearRegistry();
		npcConfig.reload();
		servers.loadServers();
		buildNPC();
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			showAll(player);
			if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
		}, 3);
	}

	public void setSkin(NPC npc, int skinId, String name, Player player) {
		final ConfigurationSection section = npcConfig.getSection("npcs." + name);
		if (!skinCache.isCached(skinId)) {
			MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {																			Logger.debug("Caching new skin '" + skinId + "'");
				skinCache.toConfig(skin, skinId);
				npc.setSkin(skin);
				reloadNPC(player, name, false);
			});
		} else {
			npc.setSkin(skinCache.getSkin(skinId)); 																						Logger.debug("Loaded NPC '" + name + "' from skin cache");
			reloadNPC(player, name, false);
		}
		section.set(".skin", skinId);
		npcConfig.save();
	}

	public void move(Location location, String name, Player player) {
		npcConfig.getConfig().set("npcs." + name + ".location", null);
		utils.toConfig(location, npcConfig.getConfig(), "npcs." + name + ".location");
		npcConfig.save();
		npcConfig.reload();
		reloadNPC(player, name, false);
		Messages.send("Moved NPC " + name, player);
	}
	
	public void loadNPCsFor(Player player) {
		for (Map.Entry<String, NPC> npcs : getNPCObjects().entrySet()) {
			NPC npc = npcs.getValue();																										Logger.debug("Showing NPC " + npc);
			if (!npc.isShown(player)) {
				npc.show(player);
			}
		}
	}

	public void destroy(String name) {
		if (Main.getRegistry().getNPCInfo().containsValue(name)) {
			NPC npcs = getNPCById(getValue(getNPCInfo(), name));
			remove(name);
			npcs.destroy();
		}
	}

	public void destroyAll() {
		for (Map.Entry<String, NPC> name : getNPCObjects().entrySet()) {
			NPC npc = name.getValue();
			npc.destroy();
		}
		clearRegistry();
	}
	
	public List<String> listNPCs() {
		npcList = new ArrayList<String>();
		for (Map.Entry<String, NPC> npc : getNPCObjects().entrySet()) {
			npcList.add(npc.getKey());
		}
		return npcList;
	}

	public NPC getNPCByName(String name) {
		return getNPCObjects().get(name);
	}

	public void showAll(Player player) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			for (NPC npc : NPCManager.getAllNPCs()) {
				if (!npc.isShown(online)) {
					npc.show(online);
				}
			}
		}
	}

	public Location getNPCLocation(String npcname) {
		final ConfigurationSection section = npcConfig.getSection("npcs." + npcname);
		Location location = new Location(Bukkit.getWorld("world"), 
				section.getDouble("location.x"),
				section.getDouble("location.y"), 
				section.getDouble("location.z"));
		location.setPitch(section.getInt("location.pitch"));
		location.setYaw(section.getInt("location.yaw"));
		return location;
	}
}
