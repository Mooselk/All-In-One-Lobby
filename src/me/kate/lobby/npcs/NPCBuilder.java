package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.List;

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

	public NPCBuilder() {
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

	public void destroy(NPC npc) {

	}

	public void reloadNPCs(Player p, NPCBuilder npcb) {
		npcb.destroyAll(p);
		NPCFile.reload();
		npcb.build(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				npcb.showAll(true, null);
				p.sendMessage("NPC-info:" + Main.NPCINFO);
				p.sendMessage("Reload complete!");
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
