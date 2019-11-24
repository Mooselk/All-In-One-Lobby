package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.utils.Logger;

public class NPCRegistry {
	
	private static final Map<String, String> NPCINFO = new HashMap<>();
	private static final Map<String, NPC> NPCS_OBJECT = new HashMap<>();
	private static final Map<String, String> SERVER_ASSOCIATION = new HashMap<>();
	private static final Map<NPC, ArrayList<String>> HOLOTEXT = new HashMap<>();
	
	public void addToRegistry(NPC npc, String name) {
		Logger.debug("Adding NPC: " + npc + " with name " + name + " to NPCRegistry");
		NPCS_OBJECT.put(name, npc); 
		NPCINFO.put(npc.getId(), name);
	}
	
	public void clearRegistry() {
		if (!SERVER_ASSOCIATION.isEmpty()) { SERVER_ASSOCIATION.clear(); }
		if (!NPCS_OBJECT.isEmpty()) { NPCS_OBJECT.clear(); }
		if (!HOLOTEXT.isEmpty()) { HOLOTEXT.clear(); }
		if (!NPCINFO.isEmpty()) { NPCINFO.clear(); }
	}
	
	public Map<String, NPC> getNPCObjects() {
		return NPCS_OBJECT;
	}
	
	public Map<String, String> getNPCInfo() {
		return NPCINFO;
	}
	
	public Map<String, String> getAssociation() {
		return SERVER_ASSOCIATION;
	}
	
	public Map<NPC, ArrayList<String>> getNPCHoloText() {
		return HOLOTEXT;
	}
	
	public void remove(String name) {
		if (NPCS_OBJECT.containsKey(name)) {
			NPCS_OBJECT.remove(name);
		}
		
		if (NPCINFO.containsValue(name)) {
			NPCINFO.remove(getValue(NPCINFO, name));
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
		for (NPC npc : NPCManager.getAllNPCs()) {
			if (npc.getId().equals(id)) { return npc; }
		}
		return null;
	}
}
