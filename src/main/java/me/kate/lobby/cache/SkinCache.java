package me.kate.lobby.cache;

import me.kate.lobby.data.Config;
import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.utils.Logger;

public class SkinCache {

	private Config cache = new CacheStorage();

	public void toConfig(Skin skin, int skinId) {
		if (cache.getSection(String.valueOf(skinId)) == null) {
			cache.getConfig().set(String.valueOf(skinId), skin.serialize());
			cache.save();
			cache.reload();
		}
	}
	
	public boolean isCached(int skinId) {
		return cache.getConfig().contains(String.valueOf(skinId));
	}

	public Skin getSkin(int skinId) {
		if (cache.getSection(String.valueOf(skinId)) != null) {
			String value = cache.getConfig().getString(skinId + ".value");
			String signature = cache.getConfig().getString(skinId + ".signature");
			return new Skin(value, signature);
		} else {
			Logger.severe("Couldn't load skin '" + skinId + "'");
			Logger.severe("	- Skin was not found or failed to cache!");
		}
		return null;
	}
}
