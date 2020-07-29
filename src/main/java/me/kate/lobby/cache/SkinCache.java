package me.kate.lobby.cache;

import me.kate.lobby.Main;
import me.kate.lobby.npcs.api.skin.MineSkinFetcher;
import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.utils.Logger;

public class SkinCache {

	private CacheStorage cache;
	
	public SkinCache(Main plugin) {
		this.cache = new CacheStorage(plugin);
	}

	public void cache(Skin skin, int skinId) {
		if (cache.getSection(String.valueOf(skinId)) == null) {
			cache.getConfig().set(String.valueOf(skinId), skin.serialize());
			this.refresh();
		}
	}
	
	public Skin getCachedSkin(int skinId) {
	    if (isCached(skinId)) { 
	    	return getSkin(skinId);
	    } else {
	    	MineSkinFetcher.fetchSkinFromIdAsync(skinId, skinCallback -> {
				cache(skinCallback, skinId);
			});
	    }
		return getSkin(skinId);
	}
	
	public boolean isCached(int skinId) {
		return cache.getConfig().contains(String.valueOf(skinId));
	}

	public Skin getSkin(int skinId) {
		if (cache.getSection(String.valueOf(skinId)) != null) {
			return new Skin(
					cache.getValue(skinId), 
					cache.getSignature(skinId)
				);	
		} else {
			Logger.severe("Couldn't load skin '" + skinId + "'");
			Logger.severe("	- Skin was not found or failed to cache!");
		}
		return null;
	}
	
	private void refresh() {
		cache.save();
		cache.reload();
	}
}
