package me.kate.lobby.listeners;

import java.util.Map;

import com.google.common.collect.Maps;

public enum SpawnMode {
	
	OFF("off"),
	NATURAL("natural"), 
	ALL("all");
	
	private final static Map<String, SpawnMode> BY_NAME = Maps.newHashMap();
	
	private String name;
	
	private SpawnMode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	static {
        for (SpawnMode mode : values()) {
            BY_NAME.put(mode.name(), mode);
        }
    }
    
    public static SpawnMode getSpawnMode(String name) {
    	return BY_NAME.get(name);
    }
}