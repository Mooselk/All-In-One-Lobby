package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class NPCRegistry {
	
	/**
	 * Name, NPCObject
	 */
	private static final Map<String, NPC> NPCS_OBJECT = new HashMap<>();
	
	public Map<String, NPC> getNPCObjects() {
		return NPCS_OBJECT;
	}
	
	/**
	 * NPCID, NPCName
	 */
	private static final Map<String, String> NPCINFO = new HashMap<>();
	
	public Map<String, String> getNPCInfo() {
		return NPCINFO;
	}
	
	/**
	 * NPCName, ServerName
	 */
	private static final Map<String, String> SERVER_ASSOCIATION = new HashMap<>();
	
	public Map<String, String> getAssociation() {
		return SERVER_ASSOCIATION;
	}
	
	/**
	 *  NPC, NPCHolotext
	 */
	private static final Map<NPC, ArrayList<String>> HOLOTEXT = new HashMap<>();
	
	public Map<NPC, ArrayList<String>> getNPCHoloText() {
		return HOLOTEXT;
	}
	
	public ArrayList<String> getHoloTextFor(NPC npc) {
		return HOLOTEXT.get(npc);
	}
	
	public void addToRegistry(NPC npc, String name) {
		Bukkit.getLogger().info("NPCid: " + npc.getId() + " Name: " + name);
		NPCS_OBJECT.put(name, npc); 																	Logger.info("Adding NPC: " + npc + " with name " + name + " to NPCRegistry");
		NPCINFO.put(npc.getId(), name);
	}
	
	public void clearRegistry() {
		if (!SERVER_ASSOCIATION.isEmpty()) { SERVER_ASSOCIATION.clear(); }
		if (!NPCS_OBJECT.isEmpty()) { NPCS_OBJECT.clear(); }
		if (!HOLOTEXT.isEmpty()) { HOLOTEXT.clear(); }
		if (!NPCINFO.isEmpty()) { NPCINFO.clear(); }
	}
	
	public void printMaps(boolean before) {
		if (before) {
			Bukkit.getLogger().info("  - Before -  ");
		} else {
			Bukkit.getLogger().info("  - After -  ");
		}
		Bukkit.getLogger().info("SERVER_ASSOCIATION: " + SERVER_ASSOCIATION);
		Bukkit.getLogger().info("NPCS_OBJECT: " + NPCS_OBJECT);
		Bukkit.getLogger().info("HOLOTEXT: " + HOLOTEXT);
		Bukkit.getLogger().info("NPCINFO: " + NPCINFO);
	}
	
	// test this
	public void remove(String name) {
		final NPC npc = getNPCObjects().get(name);
		
		if (NPCS_OBJECT.containsKey(name))
			NPCS_OBJECT.remove(name);
		
		if (NPCINFO.containsKey(npc.getId())) {
			Bukkit.getLogger().info(" Attempting to remove NPCid: " + npc.getId() + " Name: " + name);
			NPCINFO.remove(npc.getId());
		}
		
		if (SERVER_ASSOCIATION.containsValue(name))
			SERVER_ASSOCIATION.remove(Utils.getValue(SERVER_ASSOCIATION, name));
		
		if (HOLOTEXT.containsKey(npc))
			HOLOTEXT.remove(npc);
	}

	public NPC getNPCById(String id) {
		for (NPC npc : NPCManager.getAllNPCs()) {
			if (npc.getId().equals(id)) { return npc; }
		}
		return null;
	}
}
