package me.kate.lobby.npcs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;
import me.kate.lobby.utils.Messages;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class NPCBuilder {

	private List<String> defaultHoloText = Arrays.asList("Edit this text", "Players: %players%");

	private final IUtils utils = new Utils();

	private final Messages msgs = new Messages();

	public NPCBuilder() {
	}

	public void create(int skinId, String name, Location loc, Player p) {
		NPCConfig.getNPCConfig().createSection("npcs." + name);
		NPCConfig.getNPCConfig().set("npcs." + name + ".id", name);
		NPCConfig.getNPCConfig().set("npcs." + name + ".skin", skinId);
		NPCConfig.getNPCConfig().set("npcs." + name + ".holotext", defaultHoloText);
		NPCConfig.getNPCConfig().set("npcs." + name + ".message", "Default message!");
		NPCConfig.getNPCConfig().set("npcs." + name + ".server", "none");
		NPCConfig.getNPCConfig().createSection("npcs." + name + ".location");
		NPCConfig.getNPCConfig().set("npcs." + name + ".location.x", loc.getBlockX());
		NPCConfig.getNPCConfig().set("npcs." + name + ".location.y", loc.getBlockY());
		NPCConfig.getNPCConfig().set("npcs." + name + ".location.z", loc.getBlockZ());
		NPCConfig.getNPCConfig().set("npcs." + name + ".location.yaw", loc.getYaw());
		NPCConfig.getNPCConfig().set("npcs." + name + ".location.ptich", loc.getPitch());
		NPCConfig.save();
		NPCConfig.reload();
		this.destroyAll(p);
		this.build(p);
	}

	public void build(Player player) {
		this.npcClear();
		if (NPCConfig.getNPCConfig().getConfigurationSection("npcs") != null) {
			for (String name : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
				final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + name);
				int skinId = section.getInt("skin");
				MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
					NPC npc = Main.getInstance().getNPCLib()
							.createNPC(utils.colorParser(section.getStringList("holotext")));
					Location loc = new Location(Bukkit.getWorld("world"), section.getDouble("location.x"),
							section.getDouble("location.y"), section.getDouble("location.z"));
					loc.setPitch(section.getInt("location.pitch"));
					loc.setYaw(section.getInt("location.yaw"));
					npc.setLocation(loc);
					npc.setSkin(skin);
					Main.NPCS.add(npc);
					Main.NPCINFO.put(npc.getId(), name);
					npc.create();
					// The SkinFetcher fetches the skin async, you can only show the NPC to the
					// player sync.
					Bukkit.getScheduler().runTask(Main.getInstance(), () -> npc.show(player));
				});
			}
		}
	}

	public void reloadNPCs(Player player, NPCBuilder builder, boolean msg) {
		this.destroyAll(player);
		NPCConfig.reload();
		this.build(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				builder.showAll(true, null);
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
		if (Main.NPCINFO.containsValue(name)) {
			NPC npcs = getNPCById(getValue(Main.NPCINFO, name));
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
		for (int i = 0; i < Main.NPCS.size(); i++) {
			NPC match = Main.NPCS.get(i);
			if (match.getId().equals(id)) {
				return match;
			}
		}
		return null;
	}

	public void destroyAll(Player player) {
		for (int i = 0; i < Main.NPCS.size(); i++) {
			NPC npc = Main.NPCS.get(i);
			npc.destroy();
//			if (hologram != null) {
//				hologram.destroy(player);
//			}
		}
		this.npcClear();
	}

	public void showAll(boolean update, Player player) {
		if (update) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < Main.NPCS.size(); i++) {
					NPC npc = Main.NPCS.get(i);
					if (!npc.isShown(online)) {
						npc.show(online);
					}
				}
			}
		}
		if (!update && player != null) {
			for (int i = 0; i < Main.NPCS.size(); i++) {
				NPC npc = Main.NPCS.get(i);
				if (!npc.isShown(player)) {
					npc.show(player);
				}
			}
		}
	}

	private void npcClear() {
		if (!Main.NPCS.isEmpty()) {
			Main.NPCS.clear();
		}
		if (!Main.NPCINFO.isEmpty()) {
			Main.NPCINFO.clear();
		}
	}
}
