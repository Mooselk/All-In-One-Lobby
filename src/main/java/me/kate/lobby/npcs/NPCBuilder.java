package me.kate.lobby.npcs;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;
import me.kate.lobby.cache.SkinCache;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Messages;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class NPCBuilder {

	private final IUtils utils = new Utils();
	private final Messages msgs = new Messages();
	private final NPCRegistry registry = Main.getRegistry();
	private final FileConfiguration config = NPCConfig.getNPCConfig();
	private SkinCache cache = new SkinCache();
	
	public NPCBuilder() {
	}

	public void create(int skinId, String name, Location location, Player player) {
		utils.npcToConfig(location, NPCConfig.getNPCConfig(), "npcs." + name, name, skinId);
	}

	public void load(Player player) {
		for (Map.Entry<String, NPC> npcs : registry.getNPCObjects().entrySet()) {
			NPC npc = npcs.getValue();
			Logger.debug("Showing NPC " + npc);
			if (!npc.isShown(player)) {
				npc.show(player);
			}
		}
	}

	public void build() {
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
				if (!cache.isCached(skinId)) {
					MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
						Logger.debug("Caching new skin '" + skinId + "'");
						cache.toConfig(skin, skinId);
						npc.setSkin(skin);
					});
				} else {
					npc.setSkin(cache.getSkin(skinId));
					Logger.debug("Loaded NPC '" + name + "' from skin cache");
				}
				npc.create();
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> registry.addToRegistry(npc, name));
			}
		}
	}

	public void applyItems() {
		
	}
	
	public void reloadNPCs(Player player, NPCBuilder builder, boolean msg) {
		destroyAll(player);
		registry.clearRegistry();
		NPCConfig.reload();
		this.build();
		new BukkitRunnable() {
			@Override
			public void run() {
				builder.showAll(true, player);
				if (msg) {
					msgs.send("&f[&6NPC&f] Reload complete!", player);
				}
			}

		}.runTaskLater(Main.getInstance(), 20);
	}

	public void move(NPC npc) {
		// TO-DO
		// npc.destroy();
		// Update location in npcs.yml
		// Rebuild NPC
		// this.build(player);
	}

	public void destroy(String name) {
		if (Main.getRegistry().getNPCInfo().containsValue(name)) {
			NPC npcs = getNPCById(getValue(registry.getNPCInfo(), name));
			npcs.destroy();
		}
	}

	public String getValue(Map<String, String> map, String value) {
		for (Map.Entry<String, String> s : map.entrySet()) {
			if (s.getValue().equalsIgnoreCase(value)) {
				String key = s.getKey();
				return key;
			}
		}
		return null;
	}

	public NPC getNPCById(String id) {
		for (NPC npc : NPCManager.getAllNPCs()) {
			if (npc.getId().equals(id)) {
				return npc;
			}
		}
		return null;
	}

	public void destroyAll(Player player) {
		for (Map.Entry<String, NPC> name : registry.getNPCObjects().entrySet()) {
			NPC npc = name.getValue();
			npc.destroy();
		}
		Main.getRegistry().clearRegistry();
	}

	public void showAll(boolean update, Player player) {
		if (update) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				for (NPC npc : NPCManager.getAllNPCs()) {
					if (!npc.isShown(online)) {
						npc.show(online);
					}
				}
			}
		}
		if (!update && player != null) {
			for (NPC npc : NPCManager.getAllNPCs()) {
				if (!npc.isShown(player)) {
					npc.show(player);
				}
			}
		}
	}
}
