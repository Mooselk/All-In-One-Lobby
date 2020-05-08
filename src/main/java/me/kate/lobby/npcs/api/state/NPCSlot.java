package me.kate.lobby.npcs.api.state;

import java.util.Map;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;

public enum NPCSlot {

    HELMET(4, "HEAD"),
    CHESTPLATE(3, "CHEST"),
    LEGGINGS(2, "LEGS"),
    BOOTS(1, "FEET"),
    HAND(0, "HAND"),
    OFFHAND(5, "OFFHAND");

    private final int slot;
    private final String nmsName;
    
    private final static Map<String, NPCSlot> BY_NAME = Maps.newHashMap();
    
    static {
        for (NPCSlot slot : values()) {
            BY_NAME.put(slot.name(), slot);
        }
    }
    
    public static NPCSlot getSlot(String name) {
    	return BY_NAME.get(name);
    }
    
    public static void listSlots() {
    	for (Map.Entry<String, NPCSlot> name : BY_NAME.entrySet()) {
			Bukkit.getLogger().info(name.toString());
		}
    }

    NPCSlot(int slot, String nmsName) {
        this.slot = slot;
        this.nmsName = nmsName;
    }

    public int getSlot() {
        return slot;
    }

    public String getNmsName() {
        return nmsName;
    }

    public <E extends Enum<E>> E getNmsEnum(Class<E> nmsEnumClass) {
        return Enum.valueOf(nmsEnumClass, this.nmsName);
    }
}