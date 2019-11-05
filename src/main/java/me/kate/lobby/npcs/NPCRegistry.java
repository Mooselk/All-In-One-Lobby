package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.utils.Logger;

public class NPCRegistry {
	
	private final Map<String, String> NPCINFO = new HashMap<>();
	private final Map<String, NPC> NPCS_OBJECT = new HashMap<>();
	private final Map<NPC, ArrayList<String>> HOLOTEXT = new HashMap<>();
	
	public void addToRegistry(NPC npc, String name) {
		Logger.debug("Adding NPC: " + npc + " with name " + name + " to NPCRegistry");
		NPCS_OBJECT.put(name, npc); 
		NPCINFO.put(npc.getId(), name);
	}
	
	public void clearRegistry() {
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
	
	public Map<NPC, ArrayList<String>> getNPCHoloText() {
		return HOLOTEXT;
	}
}
