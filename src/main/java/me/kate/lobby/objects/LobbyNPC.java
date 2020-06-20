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
	
	private static final Map<String, LobbyNPC> LOBBY_NPC_OBJECTS = new HashMap<>();
	
	public LobbyNPC(NPC npc, String name, String server) {
		
		this.npc = npc;
		this.name = name;
		this.server = server;
		this.text = new NPCConfig().getText(name);
		
		LOBBY_NPC_OBJECTS.put(npc.getId(), this);
	}
	
	
	public static LobbyNPC getById(String npcId) {
		for (LobbyNPC lobbyNpc : LOBBY_NPC_OBJECTS.values()) {
			if (lobbyNpc.getID().equals(npcId))
				return lobbyNpc;
		}
		return null;
	}
	
	public static LobbyNPC getByName(String npcName) {
		for (LobbyNPC lobbyNpc : LOBBY_NPC_OBJECTS.values()) {
			if (lobbyNpc.getName().equals(npcName))
				return lobbyNpc;
		}
		return null;
	}
	
	public static LobbyNPC getByServer(String npcServer) {
		for (LobbyNPC lobbyNpc : LOBBY_NPC_OBJECTS.values()) {
			if (lobbyNpc.getServer().equals(npcServer))
				return lobbyNpc;
		}
		return null;
	}
	
	public void remove(LobbyNPC lobbyNPC) {
		LOBBY_NPC_OBJECTS.remove(lobbyNPC.getID());
	}
	
	public void remove() {
		LOBBY_NPC_OBJECTS.remove(this.getID());
	}
	
	public void clear() {
		LOBBY_NPC_OBJECTS.clear();
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
