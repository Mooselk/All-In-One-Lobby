package me.kate.lobby.cache;

import me.kate.lobby.data.Config;
import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.utils.Logger;

public class SkinCache {

	private Config cacheStorage = new CacheStorage();

	public void toConfig(Skin skin, int skinId) {
		if (cacheStorage.getConfig().getConfigurationSection(String.valueOf(skinId)) == null) {
			cacheStorage.getConfig().set(String.valueOf(skinId), skin.serialize());
			cacheStorage.save();
			cacheStorage.reload();
		}
	}
	
	public boolean isCached(int skinId) {
		return cacheStorage.getConfig().contains(String.valueOf(skinId));
	}

	public Skin getSkin(int skinId) {
		String value;
		String signature;
		if (cacheStorage.getConfig().getConfigurationSection(String.valueOf(skinId)) != null) {
			value = cacheStorage.getConfig().getString(skinId + ".value");
			signature = cacheStorage.getConfig().getString(skinId + ".signature");
			return new Skin(value, signature);
		} else {
			Logger.severe("Couldn't load skin '" + skinId + "'");
			Logger.severe("	- Skin was not found or failed to cache!");
		}
		return null;
	}
}
