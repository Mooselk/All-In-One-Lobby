package me.kate.lobby.cache;

import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.utils.Logger;

public class SkinCache {

	private final FileConfiguration config = CacheStorage.getSkinStorage();

	public void toConfig(Skin skin, int skinId) {
		if (config.getConfigurationSection(String.valueOf(skinId)) == null) {
			config.set(String.valueOf(skinId), skin.serialize());
			CacheStorage.save();
			CacheStorage.reload();
		}
	}
	
	public boolean isCached(int skinId) {
		return config.contains(String.valueOf(skinId));		
	}

	public Skin getSkin(int skinId) {
		String value;
		String signature;
		if (config.getConfigurationSection(String.valueOf(skinId)) != null) {
			value = config.getString(skinId + ".value");
			signature = config.getString(skinId + ".signature");
			return new Skin(value, signature);
		} else {
			Logger.severe("Couldn't load skin '" + skinId + "'");
			Logger.severe("	- Skin was not found or failed to cache!");
		}
		return null;
	}
}
