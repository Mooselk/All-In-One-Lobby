package me.kate.lobby.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.kate.lobby.npcs.api.NPC;

public class LobbyNPC {

	private NPC npc;
	private String server;
	private String name;
	
	private static final Map<String, LobbyNPC> NPC_BY_ID = new HashMap<>();
	private static final Map<String, LobbyNPC> NPC_BY_NAME = new HashMap<>();
	private static final Map<String, LobbyNPC> NPC_BY_SERVER = new HashMap<>();
	
	public LobbyNPC(NPC npc, String name, String server) {
		
		this.npc = npc;
		this.name = name;
		this.server = server;
		
		NPC_BY_ID.put(npc.getId(), this);
		NPC_BY_NAME.put(name, this);
		NPC_BY_SERVER.put(server, this);
	}
	
	public static LobbyNPC getByID(String npcID) {
		return NPC_BY_ID.get(npcID);
	}
	
	public static LobbyNPC getByName(String npcName) {
		return NPC_BY_NAME.get(npcName);
	}
	
	public void remove(LobbyNPC lobbyNPC) {
		NPC_BY_ID.remove(lobbyNPC.getID());
		NPC_BY_NAME.remove(lobbyNPC.getName());
	}
	
	public void clear() {
		NPC_BY_ID.clear();
		NPC_BY_NAME.clear();
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	public Location getLocation() {
		return npc.getLocation();
	}
	
	public String getID() {
		return npc.getId();
	}
	
	public UUID getUUID() {
		return npc.getUniqueId();
	}
	
	public String getServer() {
		return this.server;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getHolotext() {
		return npc.getText();
	}
	
	public void test() {
		for (Map.Entry<String, LobbyNPC> map : NPC_BY_ID.entrySet()) {
			Bukkit.getLogger().info("ID: " + map.getKey() + " >> NPC:" + map.getValue() );
		}
	}
}
