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

	public void cache(Skin skin, String skinId) {
		// If skin isn't present in cache, serialize and add
		if (cache.getSection(skinId) == null) {
			cache.getConfig().set(skinId, skin.serialize());
			this.refresh();
		}
	}
	
	/*
	 * If skin is already cached, return skin from cache.
	 * If not, cache skin from MineSkinFetcher and return skin added to cache.
	 */
	
	/**
	 * @param skinId
	 * @return Skin
	 */
	public Skin getCachedSkin(int skinId) {
	    if (isCached(skinId)) { 
	    	return getSkin(skinId);
	    } else {
	    	MineSkinFetcher.fetchSkinFromIdAsync(skinId, skinCallback -> {
				cache(skinCallback, String.valueOf(skinId));
			});
	    }
		return getSkin(skinId);
	}
	
	/*
	 * Check if skin has been cached by checking it's presence in skin.yml
	 */
	public boolean isCached(int skinId) {
		return cache.getConfig().contains(String.valueOf(skinId));
	}

	
	/*
	 * Gets skin from skin.yml
	 */
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
	
	// Save and reload skins.yml with one method
	private void refresh() {
		cache.save();
		cache.reload();
	}
}
