package me.kate.lobby.npcs.api.state;

import java.util.Map;

import com.google.common.collect.Maps;

public enum NPCSlot {

    HELMET(4),
    CHESTPLATE(3),
    LEGGINGS(2),
    BOOTS(1),
    HAND(0),
    OFFHAND(5);

    int slot;

    private final static Map<String, NPCSlot> BY_NAME = Maps.newHashMap();
    
    static {
        for (NPCSlot slot : values()) {
            BY_NAME.put(slot.name(), slot);
        }
    }
    
    public static NPCSlot getSlot(final String name) {
    	return BY_NAME.get(name);
    }
    
    NPCSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}