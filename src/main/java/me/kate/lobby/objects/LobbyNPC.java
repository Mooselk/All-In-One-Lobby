package me.kate.lobby.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;

import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;

public class LobbyNPC {

	private NPC npc;
	private String server;
	private String name;
	
	private List<String> text;
	
	private static final Map<String, LobbyNPC> NPC_BY_ID = new HashMap<>();
	private static final Map<String, LobbyNPC> NPC_BY_NAME = new HashMap<>();
	private static final Map<String, LobbyNPC> NPC_BY_SERVER = new HashMap<>();
	
	public LobbyNPC(NPC npc, String name, String server) {
		
		this.npc = npc;
		this.name = name;
		this.server = server;
		this.text = new NPCConfig().getText(name);
		
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
		NPC_BY_SERVER.remove(lobbyNPC.getServer());
	}
	
	public void remove() {
		NPC_BY_ID.remove(getID());
		NPC_BY_NAME.remove(getName());
		NPC_BY_SERVER.remove(getServer());
	}
	
	public void clear() {
		NPC_BY_ID.clear();
		NPC_BY_NAME.clear();
		NPC_BY_SERVER.clear();
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
		return server;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getHolotext() {
		return text;
	}
}
