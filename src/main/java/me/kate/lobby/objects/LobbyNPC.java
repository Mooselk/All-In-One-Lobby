package me.kate.lobby.objects;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;

public class LobbyNPC {

	private NPC npc;
	private String server;
	private String name;
	private List<String> text;
	
	private static final Map<String, LobbyNPC> LOBBY_NPC_OBJECTS = new ConcurrentHashMap<>();
	
	public LobbyNPC() { }
	
	public LobbyNPC(NPC npc, String name, String server) {
		
		this.npc = npc;
		this.name = name;
		this.server = server;
		this.text = new NPCConfig(Main.getInstance()).getText(name);
		
		LOBBY_NPC_OBJECTS.put(npc.getId(), this);
	}
	
	public Map<String, LobbyNPC> getObjects() {
		return LOBBY_NPC_OBJECTS;
	}
	
	
	public static LobbyNPC getLobbyNPC(String get, Get type) {
		Iterator<LobbyNPC> iter = LOBBY_NPC_OBJECTS.values().iterator();
		
		while (iter.hasNext()) {
			LobbyNPC lobbyNpc = iter.next();
			
			switch(type) {
			
				case ID : {
					if (lobbyNpc.getID().equals(get)) {
						return lobbyNpc;
					}
					break;
				}
				
				case NAME : {
					if (lobbyNpc.getID().equals(get)) {
						return lobbyNpc;
					}
					break;
				}
				
				case SERVER : {
					if (lobbyNpc.getID().equals(get)) {
						return lobbyNpc;
					}
					break;
				}
				
			}
		}
		return null;
	}
	
	
//	public static LobbyNPC getById(String npcId) {
//		Iterator<LobbyNPC> iter = LOBBY_NPC_OBJECTS.values().iterator();
//		
//		while (iter.hasNext()) {
//			LobbyNPC lobbyNpc = iter.next();
//			if (lobbyNpc.getID().equals(npcId)) {
//				return lobbyNpc;
//			}
//		}
//		return null;
//	}
//	
//	public static LobbyNPC getByName(String npcName) {
//		Iterator<LobbyNPC> iter = LOBBY_NPC_OBJECTS.values().iterator();
//		
//		while (iter.hasNext()) {
//			LobbyNPC lobbyNpc = iter.next();
//			if (lobbyNpc.getName().equals(npcName)) {
//				return lobbyNpc;
//			}
//		}
//		return null;
//	}
//	
//	public static LobbyNPC getByServer(String npcServer) {
//		Iterator<LobbyNPC> iter = LOBBY_NPC_OBJECTS.values().iterator();
//		
//		while (iter.hasNext()) {
//			LobbyNPC lobbyNpc = iter.next();
//			if (lobbyNpc.getServer().equals(npcServer)) {
//				return lobbyNpc;
//			}
//		}
//		return null;
//	}
	
	public void remove(String id) {
		LOBBY_NPC_OBJECTS.remove(id);
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
	
	public void destroy() {
		npc.destroy();
		remove(this);
	}
	
	public enum Get {
		
		ID,
		NAME,
		SERVER;
		
	}
}
