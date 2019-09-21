package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;
import me.kate.lobby.npcs.hologram.Hologram;

public class NPCBuilder {

	private Hologram hologram;

	private final List<String> defaultLore = Arrays.asList("Edit this text", "In the npc config!");

	public NPCBuilder() {
	}

	public void create(int skinId, String name, Location loc, Player p) {
		NPCFile.getNPCConfig().createSection("npcs." + name);
		NPCFile.getNPCConfig().set("npcs." + name + ".id", name);
		NPCFile.getNPCConfig().set("npcs." + name + ".skin", skinId);
		NPCFile.getNPCConfig().set("npcs." + name + ".holotext", defaultLore);
		NPCFile.getNPCConfig().set("npcs." + name + ".message", "Default message!");
		NPCFile.getNPCConfig().set("npcs." + name + ".command", "none");
		NPCFile.getNPCConfig().createSection("npcs." + name + ".location");
		NPCFile.getNPCConfig().set("npcs." + name + ".location.x", loc.getBlockX());
		NPCFile.getNPCConfig().set("npcs." + name + ".location.y", loc.getBlockY());
		NPCFile.getNPCConfig().set("npcs." + name + ".location.z", loc.getBlockZ());
		NPCFile.getNPCConfig().set("npcs." + name + ".location.yaw", loc.getYaw());
		NPCFile.getNPCConfig().set("npcs." + name + ".location.ptich", loc.getPitch());
		NPCFile.save();
		NPCFile.reload();
		this.destroyAll(p);
		this.build(p);
	}

	public void build(Player p) {
		for (String name : NPCFile.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
			Bukkit.getLogger().info(name);
			ConfigurationSection section = NPCFile.getNPCConfig().getConfigurationSection("npcs." + name);
			int skinId = section.getInt("skin");
			Bukkit.getLogger().info("x: " + section.getDouble("location.x"));
			Bukkit.getLogger().info("y: " + section.getDouble("location.y"));
			Bukkit.getLogger().info("z: " + section.getDouble("location.z"));
			MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
				NPC npc = Main.getInstance().getNPCLib().createNPC(colorParser(section.getStringList("holotext")));
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
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> npc.show(p));
			});
		}
	}

	public void move(NPC npc) {

	}

	public void destroy(String name) {
		if (Main.NPCINFO.containsValue(name)) {
			NPC npcs = getNPCById(getValue(Main.NPCINFO, name));
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
			Bukkit.getLogger().info("NPC2: " + Main.NPCS.get(i));
			if (match.getId().equals(id)) {
				return match;
			}
		}
		return null;
	}

	public void reloadNPCs(Player p, NPCBuilder npcb, boolean msg) {
		this.destroyAll(p);
		NPCFile.reload();
		this.build(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				npcb.showAll(true, null);
				if (msg) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6NPC&f] Reload complete!"));
				}
			}

		}.runTaskLater(Main.getInstance(), 20);
	}

	public void destroyAll(Player p) {
		for (int i = 0; i < Main.NPCS.size(); i++) {
			NPC npc = Main.NPCS.get(i);
			npc.destroy();
			if (hologram != null) {
				hologram.destroy(p);
			}
		}
		Main.NPCS.clear();
		Main.NPCINFO.clear();
	}

	public void showAll(boolean update, Player p) {
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
		if (!update && p != null) {
			for (int i = 0; i < Main.NPCS.size(); i++) {
				NPC npc = Main.NPCS.get(i);
				if (!npc.isShown(p)) {
					npc.show(p);
				}
			}
		}
	}

	public ArrayList<String> colorParser(List<String> list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return newList;
	}
}
