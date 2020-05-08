package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.cache.SkinCache;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.npcs.api.state.NPCSlot;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.servers.ServerManager;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class NPCBuilder extends NPCRegistry {

	private NPCConfig npcConfig = new NPCConfig();
	private SkinCache skinCache = new SkinCache();
	private ServerManager servers = new ServerManager();

	private List<String> npcList = null;
	
	private JavaPlugin plugin;

	public NPCBuilder(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void create(int skinId, String name, Location location, Player player) {
		Utils.npcToConfig(location, npcConfig.getConfig(), "npcs." + name, skinId);
	}

	// test
	public void buildNPC(String name) {
		// Shouldn't need this because this should never happen
		// if (npcConfig.getSection("npcs") == null) { return; }
		
		final ConfigurationSection section = npcConfig.getSection("npcs." + name);
		
		int skinId = section.getInt("skin");
		String server = section.getString("server.server-name");
		
		Bukkit.getScheduler().runTask(plugin, () -> {
			NPC npc = Main.getNPCLib().createNPC(Arrays.asList("Loading.."));
			applyItems(npc, name);
			
			npc.setSkin(skinCache.getCachedSkin(skinId));
			npc.setLocation(npcConfig.getLocation(name));
			npc.setText(Utils.replaceText(section.getStringList("holotext"), "0"));
			npc.create();
			
			// addToRegistry(npc, name); 
			new LobbyNPC(npc, name, server);
		});
		
	}

	public void buildNPC() {
		// Shouldn't need this because this should never happen
		// if (npcConfig.getSection("npcs") == null) { return; }
		
		for (final String name : npcConfig.get("npcs")) {
			final ConfigurationSection section = npcConfig.getSection("npcs." + name);
			int skinId = section.getInt("skin");
			String server = section.getString("server.server-name");
			
			Bukkit.getScheduler().runTask(plugin, () -> {
				NPC npc = Main.getNPCLib().createNPC(Utils.replaceText(section.getStringList("holotext"), "0"));
				applyItems(npc, name);
				
				npc.setSkin(skinCache.getCachedSkin(skinId));
				npc.setLocation(npcConfig.getLocation(name));
				npc.create();
				
				// addToRegistry(npc, name); 
				new LobbyNPC(npc, name, server);
			});
		}
	}

	public void applyItems(NPC npc, String name) {
		for (String items : npcConfig.getConfig().getStringList("npcs." + name + ".equipment")) {
			
			if (!items.contains(":true")) {
				items = items + ":false";
			}
			
			String[] parts = items.split(":");
			
			npc.setItem(NPCSlot.getSlot(parts[0]
					.toUpperCase()), new ItemBuilder(Material
					.getMaterial(parts[1]))
					.setEnchanted(parts[2].equals("true")) // will throw error, if true is not present
					.toItemStack());
		}
	}

//	public void reloadNPC(Player player, String npcName, boolean msg) {
//		destroy(npcName);
//		npcConfig.reload();
//		buildNPC(npcName);
//		servers.loadNPCAssosiation(npcName);
//		
//		Bukkit.getScheduler().runTaskLater(plugin, () -> {
//			showAll(player);
//			if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
//		}, 3);
//	}
	
	public void _reloadNPC(Player player, String npcName, boolean msg) {
		npcConfig.reload();
		this.destroy(npcName);
		this.buildNPC(npcName);
		servers.loadNPCAssosiation(npcName);
		
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			showAll(player);
			if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
		}, 3);
	}
		
		public void _reloadNPC(Player player, boolean msg) {
			npcConfig.reload();
			this.destroyAll();
			this.buildNPC();
			servers.loadServers();
			
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				showAll(player);
				if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
			}, 3);
	}

//	public void reloadNPCs(Player player, boolean msg) {
//		destroyAll();
//		clearRegistry();
//		npcConfig.reload();
//		servers.loadServers();
//		buildNPC();
//		
//		Bukkit.getScheduler().runTaskLater(plugin, () -> {
//			showAll(player);
//			if (msg) { Messages.send("&f[&6NPC&f] Reload complete!", player); }
//		}, 3);
//	}

	public void setSkin(LobbyNPC lobbyNPC, int skinId, Player player) {
		NPC npc = lobbyNPC.getNPC();
		Skin newSkin = skinCache.getCachedSkin(skinId);
		
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			npc.updateSkin(newSkin);
			player.sendMessage("Changed skin for NPC " + lobbyNPC.getName() + ".");
		}, 5);
	}

	public void move(Location location, String name, Player player) {
		// todo
		Messages.send("Moved NPC " + name, player);
	}
	
	public void loadNPCsFor(Player player) {
		for (NPC npc : NPCManager.getAllNPCs()) {
			if (!npc.isShown(player)) {
				npc.show(player);
			}
		}
	}

	public void destroy(String name) {
		LobbyNPC lobbyNPC = LobbyNPC.getByName(name);
		NPC npc = lobbyNPC.getNPC();
		
		npc.destroy();
		lobbyNPC.remove(lobbyNPC);
	}

	public void destroyAll() {
		for (NPC npc : NPCManager.getAllNPCs()) {
			LobbyNPC lobbyNPC = LobbyNPC.getByID(npc.getId());
			
			lobbyNPC.remove(lobbyNPC);
			npc.destroy();
		}
	}
	
	public List<String> listNPCs() {
		npcList = new ArrayList<String>();
		for (NPC npc : NPCManager.getAllNPCs()) {
			npcList.add(LobbyNPC.getByID(npc.getId()).getName());
		}
		return npcList;
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
}
