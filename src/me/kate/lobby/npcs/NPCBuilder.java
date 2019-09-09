package me.kate.lobby.npcs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;

public class NPCBuilder {

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
				NPC npc = Main.getInstance().getNPCLib().createNPC(section.getStringList("holotext"));
				Location loc = new Location(Bukkit.getWorld("world"), 
						section.getDouble("location.x"), 
						section.getDouble("location.y"), 
						section.getDouble("location.z"));
				loc.setPitch(section.getInt("location.pitch"));
				loc.setYaw(section.getInt("location.yaw"));
				npc.setLocation(loc);
				npc.setSkin(skin);
				Main.NPCS.put(npc.getId(), name);
				npc.create();
				// The SkinFetcher fetches the skin async, you can only show the NPC to the
				// player sync.
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> npc.show(p));
			});
			p.sendMessage("NPCS:" + Main.NPCS);
			p.sendMessage("Done!");
		}
	}

	public void move(NPC npc) {

	}

	public void destroy(NPC npc) {

	}
}
