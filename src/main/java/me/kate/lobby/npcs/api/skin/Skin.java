package me.kate.lobby.npcs.api.skin;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.google.common.collect.ImmutableMap;

@SerializableAs("Skin")
public class Skin implements ConfigurationSerializable {
	
	private static final String VALUE = "value";
	private static final String SIGNATURE = "signature";
	
	private final String value, signature;
	
    public Skin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }
    
    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }

	@Override
	public Map<String, Object> serialize() {
		return ImmutableMap.<String, Object>of(
	            VALUE, this.getValue(),
	            SIGNATURE, this.getSignature()
	        );
	}
}
