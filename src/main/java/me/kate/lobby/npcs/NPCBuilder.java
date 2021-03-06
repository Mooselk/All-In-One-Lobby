package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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

public class NPCBuilder {

	private NPCConfig npcConfig;
	private SkinCache skinCache;
	private ServerManager servers;
	private Main plugin;
	private Messages messages;
	private LobbyNPC lobbyNPC;
	private List<String> npcList = null;

	public NPCBuilder(Main plugin) {
		this.plugin = plugin;
		this.npcConfig = new NPCConfig(plugin);
		this.skinCache = new SkinCache(plugin);
		this.servers = new ServerManager(plugin);
		this.messages = new Messages();
		this.lobbyNPC = new LobbyNPC();
	}

	public void create(String skinId, String name, Location location) {
		npcConfig.toConfig(Integer.valueOf(skinId), location, "npcs." + name);
	}

	public void buildNPC(String name) {
		final ConfigurationSection section = npcConfig.getSection("npcs." + name);
		int skinId = section.getInt("skin");
		String server = section.getString("server.server-name");

		Bukkit.getScheduler().runTask(plugin, () -> {
			NPC npc = plugin.getNPCLib().createNPC(Utils.replace(section.getStringList("holotext"), "0"));
			applyItems(npc, name);

			npc.setSkin(skinCache.getCachedSkin(skinId));
			npc.setLocation(npcConfig.getLocation(name));
			npc.setText(Utils.replace(section.getStringList("holotext"), "Loading.."));
			npc.create();

			new LobbyNPC(npc, name, server, plugin);
		});

	}

	public void buildNPC() {
		
		if (npcConfig.get("npcs").isEmpty()) return;
		
		npcConfig.get("npcs").forEach(name -> {
			ConfigurationSection section = npcConfig.getSection("npcs." + name);
			int skinId = section.getInt("skin");
			String server = section.getString("server.server-name");

			Bukkit.getScheduler().runTask(plugin, () -> {
				NPC npc = plugin.getNPCLib().createNPC(Utils.replace(section.getStringList("holotext"), "0"));
				applyItems(npc, name);

				npc.setSkin(skinCache.getCachedSkin(skinId));
				npc.setLocation(npcConfig.getLocation(name));
				npc.create();

				new LobbyNPC(npc, name, server, plugin);
			});
			
		});
	}

	public void applyItems(NPC npc, String name) {
		npcConfig.getEquipment(name).forEach(items -> {
			
			if (!items.contains(":true"))
				items = items.concat(":false");
			
			String[] parts = items.split(":");
			
			npc.setItem(NPCSlot.getSlot(parts[0].toUpperCase()),
					new ItemBuilder(Material
					.getMaterial(parts[1]))
					.setEnchanted(parts[2]
					.equals("true"))
					.toItemStack());
			
		});
	}

	public void reloadNPC(Player player, String npcName, boolean msg) {
		
		npcConfig.reload();
		this.destroy(npcName);
		this.buildNPC(npcName);

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			
			showAll();
			if (msg) messages.send("&f[&6NPC&f] Reload complete!", player);
			
		}, 3);
	}

	public void reloadNPC(CommandSender sender, boolean msg) {
		
		npcConfig.reload();
		this.destroyAll();
		this.buildNPC();
		servers.loadServers();

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			
			showAll();
			if (msg) messages.send("&f[&6NPC&f] Reload complete!", sender);
		
		}, 3);
	}

	public void setSkin(LobbyNPC lobbyNPC, String skinId, Player player) {
		
		NPC npc = lobbyNPC.getNPC();
		Skin newSkin = skinCache.getCachedSkin(Integer.valueOf(skinId));

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
		
			npc.updateSkin(newSkin);
			player.sendMessage("Changed skin for NPC " + lobbyNPC.getName() + ".");
			
		}, 5);
	}

	public void move(Location location, String name, Player player) {
		// todo
		messages.send("Moved NPC " + name, player);
	}

	public void loadNPCsFor(Player player) {
		NPCManager.getAllNPCs().forEach(npc -> {
			if (!npc.isShown(player))
				npc.show(player);
		});
	}

	public void destroy(String name) {
		LobbyNPC lobbyNPC = LobbyNPC.getLobbyNPC(name, LobbyNPC.Get.NAME);
		NPC npc = lobbyNPC.getNPC();

		npc.destroy();
		lobbyNPC.remove(lobbyNPC);
	}

	public void destroyAll() {
		
		lobbyNPC.getObjects().values().forEach(lobbyNPC -> {
			lobbyNPC.destroy();
		});
		
	}

	public List<String> listNPCs() {
		npcList = new ArrayList<String>();
		NPCManager.getAllNPCs().forEach(npc -> npcList.add(LobbyNPC.getLobbyNPC(npc.getId(), LobbyNPC.Get.ID).getName()));
		return npcList;
	}

	public void showAll() {
		Bukkit.getOnlinePlayers().forEach(online -> {
			NPCManager.getAllNPCs().forEach(npc -> {
				if (!npc.isShown(online))
					npc.show(online);
			});
		});
	}
}
